/*  BuGLe: an OpenGL debugging tool
 *  Copyright (C) 2004-2010  Bruce Merry
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; version 2.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

#if HAVE_CONFIG_H
# include <config.h>
#endif
#include <bugle/bool.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <assert.h>
#include <time.h>
#include <bugle/gl/glheaders.h>
#include <bugle/glwin/glwin.h>
#include <bugle/glwin/trackcontext.h>
#include <bugle/gl/glstate.h>
#include <bugle/gl/glsl.h>
#include <bugle/gl/glutils.h>
#include <bugle/gl/glbeginend.h>
#include <bugle/gl/globjects.h>
#include <bugle/gl/glextensions.h>
#include <bugle/filters.h>
#include <bugle/log.h>
#include <bugle/hashtable.h>
#include <bugle/io.h>
#include <bugle/apireflect.h>
#include <bugle/memory.h>
#include <bugle/string.h>
#include <bugle/porting.h>
#include <budgie/types.h>
#include <budgie/reflect.h>
#include <budgie/addresses.h>
#include "budgielib/defines.h"
#include "common/protocol.h"
#include "common/workqueue.h"
#include "platform/threads.h"
#include "platform/types.h"
#include "platform/io.h"
#if BUGLE_PLATFORM_MSVCRT || BUGLE_PLATFORM_MINGW
#include <Windows.h> /* Bryce - need Windows.h to get process name on MSW */
#endif

#define RESP_CALL 0xabcd0010UL /* Bryce defined type for sending state */
#define RESP_STATE_HEADER 0xabcd0011UL /* Bryce defined type for sending timestamp header */

/* types defined for use with 64 bit host to network code */
#define TYP_INIT 0
#define TYP_SMLE 1
#define TYP_BIGE 2

#define REQUEST_QUEUE_SIZE 64

typedef struct
{
    bugle_uint32_t length;
    char *data;
} gldb_binary_string;

typedef struct
{
    bugle_uint32_t code;
    bugle_uint32_t request_id;
} gldb_request_header;

typedef struct
{
    gldb_request_header header;
} gldb_request_simple;

typedef struct
{
    gldb_request_header header;
    gldb_binary_string name;
    bugle_uint32_t enable;
} gldb_request_break;

typedef struct
{
    gldb_request_header header;
    bugle_uint32_t event;
    bugle_uint32_t enable;
} gldb_request_break_event;

typedef struct
{
    gldb_request_header header;
    gldb_binary_string name;
} gldb_request_activate_filterset;

typedef struct
{
    gldb_request_header header;
    bugle_uint32_t subtype;
} gldb_request_data_header;

typedef struct
{
    gldb_request_data_header header;
    bugle_uint32_t object_id;
    bugle_uint32_t target;
    bugle_uint32_t face;
    bugle_uint32_t level;
    bugle_uint32_t format;
    bugle_uint32_t type;
} gldb_request_data_texture;

typedef struct
{
    gldb_request_data_header header;
    bugle_uint32_t object_id;
} gldb_request_data_buffer;

typedef struct
{
    gldb_request_data_header header;
    bugle_uint32_t object_id;
    bugle_uint32_t target;
    bugle_uint32_t buffer;
    bugle_uint32_t format;
    bugle_uint32_t type;
} gldb_request_data_framebuffer;

typedef struct
{
    gldb_request_data_header header;
    bugle_uint32_t object_id;
    bugle_uint32_t target;
} gldb_request_data_shader;

typedef struct
{
    gldb_request_data_header header;
    bugle_uint32_t object_id;
    bugle_uint32_t target;
} gldb_request_data_info_log;

static bugle_io_reader *in_pipe = NULL;
static bugle_io_writer *out_pipe = NULL;
static bugle_bool *break_on;
static bugle_bool break_on_event[REQ_EVENT_COUNT];
static bugle_bool break_on_next = BUGLE_FALSE;
static bugle_bool stop_in_begin_end = BUGLE_FALSE;
static bugle_bool stopped = BUGLE_TRUE;
static bugle_uint32_t start_id = 0;

static bugle_thread_id debug_thread;
static bugle_workqueue *request_queue;

static unsigned long long htonll(unsigned long long src) { 
	static int typ = TYP_INIT; 
	unsigned char c; 
	union { 
		unsigned long long ull; 
		unsigned char c[8]; 
	} x; 
	if (typ == TYP_INIT) { 
		x.ull = 0x01; 
		typ = (x.c[7] == 0x01ULL) ? TYP_BIGE : TYP_SMLE; 
	} 
	if (typ == TYP_BIGE) 
		return src; 
	x.ull = src; 
	c = x.c[0]; x.c[0] = x.c[7]; x.c[7] = c; 
	c = x.c[1]; x.c[1] = x.c[6]; x.c[6] = c; 
	c = x.c[2]; x.c[2] = x.c[5]; x.c[5] = c; 
	c = x.c[3]; x.c[3] = x.c[4]; x.c[4] = c; 
	return x.ull; 
}

#if BUGLE_PLATFORM_MSVCRT || BUGLE_PLATFORM_MINGW
/* Windows basename */
char *base_name(const char *pathname)
{
	char *lastsep = strrchr(pathname, '\\');
	return lastsep ? bugle_strdup(lastsep+1) : bugle_strdup(pathname);
}
#endif

#if BUGLE_PLATFORM_POSIX
/* POSIX basename */
char *base_name(const char *pathname)
{
	char *lastsep = strrchr(pathname, '/');
	return lastsep ? bugle_strdup(lastsep+1) : bugle_strdup(pathname);
}
#endif

static void make_indent(int indent, bugle_io_writer *writer)
{
	int i;
	for (i = 0; i < indent; i++)
		bugle_io_putc(' ', writer);
}

/* the following revised functions are defined in order to prevent dumped strings on being
 * excessively long. A dumped texture bind call, for example previously clocking in at 8mb
 * and that wasn't even in full
 */

void revised_call_parameter_dump(const generic_function_call *call, int param, bugle_io_writer *writer)
{
	int length = -1;
	const void *arg;

	arg = (param == -1) ? call->retn : call->args[param];

	budgie_dump_any_type(budgie_call_parameter_type(call, param),arg, length, writer);
}

void revised_dump_any_call(const generic_function_call *call, int indent, bugle_io_writer *writer) /* Revised version to make the dumped strings more network friendly */
{
	int i;

	make_indent(indent, writer);
	bugle_io_printf(writer, "%s(", budgie_function_name(call->id));
	for (i = 0; i < call->num_args; i++)
	{
		if (i) bugle_io_puts(", ", writer);
		revised_call_parameter_dump(call, i, writer);
	}
	bugle_io_putc(')', writer);
	if (call->retn)
	{
		bugle_io_puts(" = ", writer);
		revised_call_parameter_dump(call, -1, writer);
	}
}

static char *revised_dump_any_call_string(const function_call *call) /* Revised version to make the dumped strings more network friendly */
{
	bugle_io_writer *writer;
	char *ans;

	writer = bugle_io_writer_mem_new(256);
	revised_dump_any_call(&call->generic, 0, writer);
	ans = bugle_io_writer_mem_get(writer);
	bugle_io_writer_close(writer);
	return ans;
}

static bugle_bool stoppable(void)
{
    return stop_in_begin_end || !bugle_gl_in_begin_end();
}

static void send_timestamp(void)
{
	time_t rawTime;

	time(&rawTime);
	unsigned int timeSize;

	timeSize = sizeof(time_t);

	if(timeSize == 4)
	{
		gldb_protocol_send_code(out_pipe, timeSize);
		gldb_protocol_send_code(out_pipe, rawTime);
	}
	else if(timeSize == 8)
	{
		/* Untested code */
		time_t networkTime = htonll(rawTime);

		gldb_protocol_send_code(out_pipe, timeSize);
		bugle_io_write(&networkTime, timeSize, 1, out_pipe);
	}
	else
	{
		/* timeSize is something unexpected */
		assert(0);
	}
}

static void send_session_header(void)
{
	char *process_name;
	char *base_process_name;
#if BUGLE_PLATFORM_MSVCRT || BUGLE_PLATFORM_MINGW
	int const max_buffer_size = 256;
	process_name = malloc(max_buffer_size);
	HMODULE moduleHandle;

	moduleHandle = GetModuleHandle(NULL);
	GetModuleFileName(moduleHandle, process_name, max_buffer_size);
#endif
#if BUGLE_PLATFORM_POSIX
	process_name = getenv("_");
#endif
#if !BBUGLE_PLATFORM_MSVCRT && !BUGLE_PLATFORM_MINGW && !BUGLE_PLATFORM_POSIX
	process_name = strdup("ProcessNameNotAvailable");
	// TODO: handle not being able to query
#endif
	base_process_name = base_name(process_name);

#if BUGLE_PLATFORM_MSVCRT || BUGLE_PLATFORM_MINGW /* don't free getenv */
	free(process_name);
#endif

	send_timestamp();
	gldb_protocol_send_string(out_pipe, base_process_name);
	free(base_process_name);
}

static void send_state_header(bugle_uint32_t id)
{
	gldb_protocol_send_code(out_pipe, RESP_STATE_HEADER);
	gldb_protocol_send_code(out_pipe, id);
	send_timestamp();
}

/* Tweaked this to send the state in strings that are more in line with those decoded by gldb from raw state */

static void send_state(const glstate *state, bugle_uint32_t id)
{
    char *str;
    linked_list children;
    linked_list_node *cur;
	bugle_state_raw wrapper;

	if(!state->info)
	{
		str = NULL; /* root */
	}
	else
	{
		bugle_state_get_raw(state, &wrapper);
		if (!wrapper.data)
		{
			str =  bugle_strdup("<GL error>");
		}
		else
		{
			if (wrapper.type == TYPE_c || wrapper.type == TYPE_Kc)
			{
				str = bugle_strdup((const char *) wrapper.data);
			}
			else
			{
				bugle_io_writer *writer;

				writer = bugle_io_writer_mem_new(64);
				budgie_dump_any_type_extended(wrapper.type, wrapper.data, -1, wrapper.length, NULL, writer);
				str = bugle_io_writer_mem_get(writer);
				bugle_io_writer_close(writer);
			}
			bugle_free(wrapper.data);
		}
	}
	
	/*str = bugle_state_get_string(state);*/
    gldb_protocol_send_code(out_pipe, RESP_STATE_NODE_BEGIN);
    gldb_protocol_send_code(out_pipe, id);
    if (state->name) gldb_protocol_send_string(out_pipe, state->name);
    else gldb_protocol_send_string(out_pipe, "");
    gldb_protocol_send_code(out_pipe, state->numeric_name);
    gldb_protocol_send_code(out_pipe, state->enum_name);
    if (str) gldb_protocol_send_string(out_pipe, str);
    else gldb_protocol_send_string(out_pipe, "");
    bugle_free(str);

    bugle_state_get_children(state, &children);
    for (cur = bugle_list_head(&children); cur; cur = bugle_list_next(cur))
    {
        send_state((const glstate *) bugle_list_data(cur), id);
        bugle_state_clear((glstate *) bugle_list_data(cur));
    }
    bugle_list_clear(&children);

    gldb_protocol_send_code(out_pipe, RESP_STATE_NODE_END);
    gldb_protocol_send_code(out_pipe, id);
}

static void send_state_raw(const glstate *state, bugle_uint32_t id)
{
	linked_list children;
	linked_list_node *cur;
	bugle_state_raw wrapper = {NULL, 0, 0};

	bugle_state_get_raw(state, &wrapper);
	gldb_protocol_send_code(out_pipe, RESP_STATE_NODE_BEGIN_RAW);
	gldb_protocol_send_code(out_pipe, id);
	if (state->name) gldb_protocol_send_string(out_pipe, state->name);
	else gldb_protocol_send_string(out_pipe, "");
	gldb_protocol_send_code(out_pipe, state->numeric_name);
	gldb_protocol_send_code(out_pipe, state->enum_name);
	if (wrapper.data || !state->info)  /* root is valid but has no data */
	{
		gldb_protocol_send_string(out_pipe, budgie_type_name(wrapper.type));
		gldb_protocol_send_code(out_pipe, wrapper.length);
		gldb_protocol_send_binary_string(out_pipe, budgie_type_size(wrapper.type) * abs(wrapper.length), (const char *) wrapper.data);
	}
	else
	{
		gldb_protocol_send_string(out_pipe, "");
		gldb_protocol_send_code(out_pipe, -2); /* Magic invalid value */
		gldb_protocol_send_binary_string(out_pipe, 0, (const char *) wrapper.data);
	}
	bugle_free(wrapper.data);

	bugle_state_get_children(state, &children);
	for (cur = bugle_list_head(&children); cur; cur = bugle_list_next(cur))
	{
		send_state_raw((const glstate *) bugle_list_data(cur), id);
		bugle_state_clear((glstate *) bugle_list_data(cur));
	}
	bugle_list_clear(&children);

	gldb_protocol_send_code(out_pipe, RESP_STATE_NODE_END_RAW);
	gldb_protocol_send_code(out_pipe, id);
}

static char *dump_any_call_string(const function_call *call)
{
    bugle_io_writer *writer;
    char *ans;

    writer = bugle_io_writer_mem_new(256);
    budgie_dump_any_call(&call->generic, 0, writer);
    ans = bugle_io_writer_mem_get(writer);
    bugle_io_writer_close(writer);
    return ans;
}

static void send_call(const function_call *call)
{
	char* dumpedCall;

	if(call == NULL)
		return;

	gldb_protocol_send_code(out_pipe, RESP_CALL);
	gldb_protocol_send_code(out_pipe, -1); /* Need to send a dummy ID TODO: change this in the future */
	send_timestamp();
	dumpedCall = revised_dump_any_call_string(call);
	gldb_protocol_send_string(out_pipe, dumpedCall);
	free(dumpedCall);
}

static GLenum target_to_binding(GLenum target)
{
    switch (target)
    {
    case GL_TEXTURE_2D: return GL_TEXTURE_BINDING_2D;
#if GL_ES_VERSION_2_0 || GL_VERSION_1_3
    case GL_TEXTURE_CUBE_MAP: return GL_TEXTURE_BINDING_CUBE_MAP;
#endif
#ifdef GL_VERSION_1_2
    case GL_TEXTURE_1D: return GL_TEXTURE_BINDING_1D;
    case GL_TEXTURE_3D: return GL_TEXTURE_BINDING_3D;
    case GL_TEXTURE_RECTANGLE_ARB: return GL_TEXTURE_BINDING_RECTANGLE_ARB;
#endif
#ifdef GL_EXT_texture_array
    case GL_TEXTURE_1D_ARRAY_EXT: return GL_TEXTURE_BINDING_1D_ARRAY_EXT;
    case GL_TEXTURE_2D_ARRAY_EXT: return GL_TEXTURE_BINDING_2D_ARRAY_EXT;
#endif
#ifdef GL_EXT_texture_buffer_object
    case GL_TEXTURE_BUFFER_EXT: return GL_TEXTURE_BINDING_BUFFER_EXT;
#endif
    default:
        return GL_NONE;
    }
}

/* Utility functions and structures for image grabbers. When doing
 * grabbing from the primary context, we need to save the current pixel
 * client state so that it can be set to known values, then restored.
 * This is done in a pixel_state struct.
 */
typedef struct
{
    GLboolean swap_bytes, lsb_first;
    GLint row_length, skip_rows;
    GLint skip_pixels, alignment;
    GLint image_height, skip_images;
#ifdef GL_EXT_pixel_buffer_object
    GLint pbo;
#endif
} pixel_state;

/* Save all the old pack state to old and set default state with
 * alignment of 1.
 */
static void pixel_pack_reset(pixel_state *old)
{
    CALL(glGetIntegerv)(GL_PACK_ALIGNMENT, &old->alignment);
    CALL(glPixelStorei)(GL_PACK_ALIGNMENT, 1);
#ifdef GL_VERSION_1_1
    CALL(glGetBooleanv)(GL_PACK_SWAP_BYTES, &old->swap_bytes);
    CALL(glGetBooleanv)(GL_PACK_LSB_FIRST, &old->lsb_first);
    CALL(glGetIntegerv)(GL_PACK_ROW_LENGTH, &old->row_length);
    CALL(glGetIntegerv)(GL_PACK_SKIP_ROWS, &old->skip_rows);
    CALL(glGetIntegerv)(GL_PACK_SKIP_PIXELS, &old->skip_pixels);
    CALL(glPixelStorei)(GL_PACK_SWAP_BYTES, GL_FALSE);
    CALL(glPixelStorei)(GL_PACK_LSB_FIRST, GL_FALSE);
    CALL(glPixelStorei)(GL_PACK_ROW_LENGTH, 0);
    CALL(glPixelStorei)(GL_PACK_SKIP_ROWS, 0);
    CALL(glPixelStorei)(GL_PACK_SKIP_PIXELS, 0);
    if (BUGLE_GL_HAS_EXTENSION_GROUP(GL_EXT_texture3D))
    {
        CALL(glGetIntegerv)(GL_PACK_IMAGE_HEIGHT, &old->image_height);
        CALL(glGetIntegerv)(GL_PACK_SKIP_IMAGES, &old->skip_images);
        CALL(glPixelStorei)(GL_PACK_IMAGE_HEIGHT, 0);
        CALL(glPixelStorei)(GL_PACK_SKIP_IMAGES, 0);
    }
#endif /* GL_VERSION_1_1 */
#ifdef GL_EXT_pixel_buffer_object
    if (BUGLE_GL_HAS_EXTENSION_GROUP(GL_EXT_pixel_buffer_object))
    {
        CALL(glGetIntegerv)(GL_PIXEL_PACK_BUFFER_BINDING_EXT, &old->pbo);
        CALL(glBindBufferARB)(GL_PIXEL_PACK_BUFFER_EXT, 0);
    }
#endif
}

/* Sets the pixel pack state specified in old */
static void pixel_pack_restore(const pixel_state *old)
{
    CALL(glPixelStorei)(GL_PACK_ALIGNMENT, old->alignment);
#ifdef GL_VERSION_1_1
    CALL(glPixelStorei)(GL_PACK_SWAP_BYTES, old->swap_bytes);
    CALL(glPixelStorei)(GL_PACK_LSB_FIRST, old->lsb_first);
    CALL(glPixelStorei)(GL_PACK_ROW_LENGTH, old->row_length);
    CALL(glPixelStorei)(GL_PACK_SKIP_ROWS, old->skip_rows);
    CALL(glPixelStorei)(GL_PACK_SKIP_PIXELS, old->skip_pixels);
    if (BUGLE_GL_HAS_EXTENSION_GROUP(GL_EXT_texture3D))
    {
        CALL(glPixelStorei)(GL_PACK_IMAGE_HEIGHT, old->image_height);
        CALL(glPixelStorei)(GL_PACK_SKIP_IMAGES, old->skip_images);
    }
#endif /* GL_VERSION_1_1 */
#ifdef GL_EXT_pixel_buffer_object
    if (BUGLE_GL_HAS_EXTENSION_GROUP(GL_EXT_pixel_buffer_object))
        CALL(glBindBufferARB)(GL_PIXEL_PACK_BUFFER_EXT, old->pbo);
#endif
}

#ifdef GL_VERSION_1_1
/* Wherever possible we use the aux context. However, default textures
 * are not shared between contexts, so we sometimes have to take our
 * chances with setting up the default readback state and restoring it
 * afterwards.
 */
static bugle_bool send_data_texture(bugle_uint32_t id, GLuint texid, GLenum target,
                                    GLenum face, GLint level,
                                    GLenum format, GLenum type)
{
    char *data;
    size_t length;
    GLint width = 1, height = 1, depth = 1;
    GLint old_tex;

    glwin_display dpy = NULL;
    glwin_context aux = NULL, real = NULL;
    glwin_drawable old_read = 0, old_write = 0;
    pixel_state old_pack;

    if (!bugle_gl_begin_internal_render())
    {
        gldb_protocol_send_code(out_pipe, RESP_ERROR);
        gldb_protocol_send_code(out_pipe, id);
        gldb_protocol_send_code(out_pipe, 0);
        gldb_protocol_send_string(out_pipe, "inside glBegin/glEnd");
        return BUGLE_FALSE;
    }

    aux = bugle_get_aux_context(BUGLE_TRUE);
    if (aux && texid)
    {
        real = bugle_glwin_get_current_context();
        old_write = bugle_glwin_get_current_drawable();
        old_read = bugle_glwin_get_current_read_drawable();
        dpy = bugle_glwin_get_current_display();
        bugle_glwin_make_context_current(dpy, old_write, old_write, aux);

        CALL(glPushAttrib)(GL_ALL_ATTRIB_BITS);
        CALL(glPushClientAttrib)(GL_CLIENT_PIXEL_STORE_BIT);
        CALL(glPixelStorei)(GL_PACK_ALIGNMENT, 1);
    }
    else
    {
        CALL(glGetIntegerv)(target_to_binding(target), &old_tex);
        pixel_pack_reset(&old_pack);
    }

    CALL(glBindTexture)(target, texid);

    CALL(glGetTexLevelParameteriv)(face, level, GL_TEXTURE_WIDTH, &width);
    switch (face)
    {
    case GL_TEXTURE_1D:
#ifdef GL_EXT_texture_buffer_object
    case GL_TEXTURE_BUFFER_EXT:
#endif
        break;
#ifdef GL_EXT_texture3D
    case GL_TEXTURE_3D_EXT:
        CALL(glGetTexLevelParameteriv)(face, level, GL_TEXTURE_HEIGHT, &height);
        if (BUGLE_GL_HAS_EXTENSION_GROUP(GL_EXT_texture3D))
            CALL(glGetTexLevelParameteriv)(face, level, GL_TEXTURE_DEPTH_EXT, &depth);
        break;
#endif
#ifdef GL_EXT_texture_array
    case GL_TEXTURE_2D_ARRAY_EXT:
        CALL(glGetTexLevelParameteriv)(face, level, GL_TEXTURE_HEIGHT, &height);
        if (BUGLE_GL_HAS_EXTENSION_GROUP(GL_EXT_texture_array))
            CALL(glGetTexLevelParameteriv)(face, level, GL_TEXTURE_DEPTH_EXT, &depth);
        break;
#endif
    default: /* 2D-like: 2D, RECTANGLE and 1D_ARRAY at the moment */
        CALL(glGetTexLevelParameteriv)(face, level, GL_TEXTURE_HEIGHT, &height);
    }

    length = bugle_gl_type_to_size(type) * bugle_gl_format_to_count(format, type)
        * width * height * depth;
    data = bugle_malloc(length);

    CALL(glGetTexImage)(face, level, format, type, data);

    if (aux && texid)
    {
        GLenum error;
        while ((error = CALL(glGetError)()) != GL_NO_ERROR)
            bugle_log_printf("debugger", "protocol", BUGLE_LOG_WARNING,
                             "GL error %#04x generated by send_data_texture in aux context", error);
        CALL(glPopClientAttrib)();
        CALL(glPopAttrib)();
        bugle_glwin_make_context_current(dpy, old_write, old_read, real);
    }
    else
    {
        CALL(glBindTexture)(target, old_tex);
        pixel_pack_restore(&old_pack);
    }

    gldb_protocol_send_code(out_pipe, RESP_DATA);
    gldb_protocol_send_code(out_pipe, id);
    gldb_protocol_send_code(out_pipe, REQ_DATA_TEXTURE);
    gldb_protocol_send_binary_string(out_pipe, length, data);
    gldb_protocol_send_code(out_pipe, width);
    gldb_protocol_send_code(out_pipe, height);
    gldb_protocol_send_code(out_pipe, depth);
    bugle_gl_end_internal_render("send_data_texture", BUGLE_TRUE);
    bugle_free(data);
    return BUGLE_TRUE;
}
#endif /* GL */

/* Unfortunately, FBO cannot have a size query, because while incomplete
 * it could have different sizes for different buffers. Thus, we have to
 * do the query on the underlying attachment. The return is BUGLE_FALSE if there
 * is no attachment or a size could not be established.
 *
 * It is assumed that the appropriate framebuffer (if any) is already current.
 */
static bugle_bool get_framebuffer_size(GLuint fbo, GLenum target, GLenum attachment,
                                       GLint *width, GLint *height)
{
    /* FIXME: support GLES framebuffer objects */
#ifdef GL_EXT_framebuffer_object
    if (fbo)
    {
        GLint type, name, old_name;
        GLenum texture_target, texture_binding;
        GLint level = 0, face;

        CALL(glGetFramebufferAttachmentParameterivEXT)(target, attachment,
                                                      GL_FRAMEBUFFER_ATTACHMENT_OBJECT_TYPE_EXT, &type);
        CALL(glGetFramebufferAttachmentParameterivEXT)(target, attachment,
                                                      GL_FRAMEBUFFER_ATTACHMENT_OBJECT_NAME_EXT, &name);
        if (type == GL_RENDERBUFFER_EXT)
        {
            CALL(glGetIntegerv)(GL_RENDERBUFFER_BINDING_EXT, &old_name);
            CALL(glBindRenderbufferEXT)(GL_RENDERBUFFER_EXT, name);
            CALL(glGetRenderbufferParameterivEXT)(GL_RENDERBUFFER_EXT, GL_RENDERBUFFER_WIDTH_EXT, width);
            CALL(glGetRenderbufferParameterivEXT)(GL_RENDERBUFFER_EXT, GL_RENDERBUFFER_HEIGHT_EXT, height);
            CALL(glBindRenderbufferEXT)(GL_RENDERBUFFER_EXT, old_name);
        }
        else if (type == GL_TEXTURE)
        {
            CALL(glGetFramebufferAttachmentParameterivEXT)(target, attachment,
                                                          GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_LEVEL_EXT, &level);
            texture_target = bugle_globjects_get_target(BUGLE_GLOBJECTS_TEXTURE, name);
            texture_binding = target_to_binding(texture_target);
            if (!texture_binding) return BUGLE_FALSE;

            CALL(glGetIntegerv)(texture_binding, &old_name);
            CALL(glBindTexture)(texture_target, name);
            if (target == GL_TEXTURE_CUBE_MAP)
            {
                CALL(glGetFramebufferAttachmentParameterivEXT)(target, attachment,
                                                               GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_CUBE_MAP_FACE_EXT, &face);
            }
            else
            {
                face = texture_target;
            }
            CALL(glGetTexLevelParameteriv)(face, level, GL_TEXTURE_WIDTH, width);
            CALL(glGetTexLevelParameteriv)(face, level, GL_TEXTURE_HEIGHT, height);
        }
        else
            return BUGLE_FALSE;
    }
    else
#endif /* GL_EXT_framebuffer_object */
    {
        glwin_display dpy;
        glwin_drawable draw;

        /* Window-system framebuffer */
        dpy = bugle_glwin_get_current_display();
        draw = bugle_glwin_get_current_read_drawable();

        /* AMD's GLES emulator doesn't query the drawable correctly */
        if (draw)
        {
            *width = -1;
            *height = -1;
            bugle_glwin_get_drawable_dimensions(dpy, draw, width, height);
        }
        else
        {
            GLint viewport[4];
            CALL(glGetIntegerv)(GL_VIEWPORT, viewport);
            *width = viewport[2];
            *height = viewport[3];
        }
        return *width >= 0 && *height >= 0;
    }
    return BUGLE_TRUE;
}

/* This function is complicated by GL_EXT_framebuffer_object and
 * GL_EXT_framebuffer_blit. The latter defines separate read and draw
 * framebuffers, and also modifies the semantics of the former (even if
 * the latter is never actually used!)
 *
 * The target is currently not used. It is expected to be either 0
 * (for the window-system-defined framebuffer) or GL_FRAMEBUFFER_EXT
 * (for application-defined framebuffers). In the future it may be used
 * to allow other types of framebuffers e.g. pbuffers.
 */
static bugle_bool send_data_framebuffer(bugle_uint32_t id, GLuint fbo, GLenum target,
                                        GLenum buffer, GLenum format, GLenum type)
{
    glwin_display dpy = NULL;
    glwin_context aux = NULL, real = NULL;
    glwin_drawable old_read = 0, old_write = 0;
    pixel_state old_pack;
    GLint old_fbo = 0, old_read_buffer;
    GLint width = 0, height = 0;
    size_t length;
    GLuint fbo_target = 0, fbo_binding = 0;
    char *data;
    bugle_bool illegal = BUGLE_FALSE;

    if (!bugle_gl_begin_internal_render())
    {
        gldb_protocol_send_code(out_pipe, RESP_ERROR);
        gldb_protocol_send_code(out_pipe, id);
        gldb_protocol_send_code(out_pipe, 0);
        gldb_protocol_send_string(out_pipe, "inside glBegin/glEnd");
        return BUGLE_FALSE;
    }

#ifdef GL_EXT_framebuffer_blit
    if (BUGLE_GL_HAS_EXTENSION(GL_EXT_framebuffer_blit))
    {
        fbo_target = GL_READ_FRAMEBUFFER_EXT;
        fbo_binding = GL_READ_FRAMEBUFFER_BINDING_EXT;
    }
    else
#endif
    {
#ifdef GL_EXT_framebuffer_object
        if (BUGLE_GL_HAS_EXTENSION(GL_EXT_framebuffer_object))
        {
            fbo_target = GL_FRAMEBUFFER_EXT;
            fbo_binding = GL_FRAMEBUFFER_BINDING_EXT;
        }
        else
#endif
        {
            illegal = fbo != 0;
        }
    }

    if (illegal)
    {
        gldb_protocol_send_code(out_pipe, RESP_ERROR);
        gldb_protocol_send_code(out_pipe, id);
        gldb_protocol_send_code(out_pipe, 0);
        gldb_protocol_send_string(out_pipe, "GL_EXT_framebuffer_object is not available");
        return BUGLE_FALSE;
    }

#ifdef GL_VERSION_1_1
    if (fbo)
    {
        /* This version doesn't share renderbuffers between contexts.
         * Older versions have even buggier FBO implementations, so we
         * don't bother to work around them.
         */
        if (strcmp((char *) CALL(glGetString)(GL_VERSION), "2.0.2 NVIDIA 87.62") != 0)
            aux = bugle_get_aux_context(BUGLE_TRUE);
    }
    if (aux)
    {
        real = bugle_glwin_get_current_context();
        old_write = bugle_glwin_get_current_drawable();
        old_read = bugle_glwin_get_current_read_drawable();
        dpy = bugle_glwin_get_current_display();
        bugle_glwin_make_context_current(dpy, old_write, old_write, aux);

        CALL(glPushAttrib)(GL_ALL_ATTRIB_BITS);
        CALL(glPushClientAttrib)(GL_CLIENT_PIXEL_STORE_BIT);
        CALL(glPixelStorei)(GL_PACK_ALIGNMENT, 1);
    }
    else
#endif
    {
        pixel_pack_reset(&old_pack);
        if (fbo_binding)
            CALL(glGetIntegerv)(fbo_binding, &old_fbo);
    }

#ifdef GL_EXT_framebuffer_object
    if ((GLint) fbo != old_fbo)
        CALL(glBindFramebufferEXT)(fbo_target, fbo);
#endif

    /* Note: GL_READ_BUFFER belongs to the FBO where an application-created
     * FBO is in use. Thus, we must save the old value even if we are
     * using the aux context. We're also messing with shared object state,
     * so if anyone was insane enough to be using the FBO for readback in
     * another thread at the same time, things might go pear-shaped. The
     * workaround would be to query all the bindings of the FBO and clone
     * a brand new FBO, but that seems like a lot of work and quite
     * fragile to future changes in the way FBO's are handled.
     */
#ifdef GL_VERSION_1_1
    if (format != GL_DEPTH_COMPONENT && format != GL_STENCIL_INDEX)
    {
        CALL(glGetIntegerv)(GL_READ_BUFFER, &old_read_buffer);
        CALL(glReadBuffer)(buffer);
    }
#endif

    get_framebuffer_size(fbo, fbo_target, buffer, &width, &height);
    length = bugle_gl_type_to_size(type) * bugle_gl_format_to_count(format, type)
        * width * height;
    data = bugle_malloc(length);
    CALL(glReadPixels)(0, 0, width, height, format, type, data);

    /* Restore the old state. glPushAttrib currently does NOT save FBO
     * bindings, so we have to do that manually even for the aux context. */
#ifdef GL_VERSION_1_1
    if (format != GL_DEPTH_COMPONENT && format != GL_STENCIL_INDEX)
        CALL(glReadBuffer)(old_read_buffer);
    if (aux)
    {
        GLenum error;
        while ((error = CALL(glGetError)()) != GL_NO_ERROR)
            bugle_log_printf("debugger", "protocol", BUGLE_LOG_WARNING,
                             "GL error %#04x generated by send_data_framebuffer in aux context", error);
#ifdef GL_EXT_framebuffer_object
        if (fbo)
            CALL(glBindFramebufferEXT)(fbo_target, 0);
#endif
        CALL(glPopClientAttrib)();
        CALL(glPopAttrib)();
        bugle_glwin_make_context_current(dpy, old_write, old_read, real);
    }
    else
#endif /* GL_VERSION_1_1 */
    {
#ifdef GL_EXT_framebuffer_object
        if ((GLint) fbo != old_fbo)
            CALL(glBindFramebufferEXT)(fbo_target, old_fbo);
#endif
        pixel_pack_restore(&old_pack);
    }

    gldb_protocol_send_code(out_pipe, RESP_DATA);
    gldb_protocol_send_code(out_pipe, id);
    gldb_protocol_send_code(out_pipe, REQ_DATA_FRAMEBUFFER);
    gldb_protocol_send_binary_string(out_pipe, length, data);
    gldb_protocol_send_code(out_pipe, width);
    gldb_protocol_send_code(out_pipe, height);
    bugle_gl_end_internal_render("send_data_framebuffer", BUGLE_TRUE);
    bugle_free(data);
    return BUGLE_TRUE;
}

#if GL_ES_VERSION_2_0 || GL_VERSION_2_0
static bugle_bool send_data_shader(bugle_uint32_t id, GLuint shader_id,
                                   GLenum target)
{
    GLint length;
    char *text;

    if (!bugle_gl_begin_internal_render())
    {
        gldb_protocol_send_code(out_pipe, RESP_ERROR);
        gldb_protocol_send_code(out_pipe, id);
        gldb_protocol_send_code(out_pipe, 0);
        gldb_protocol_send_string(out_pipe, "inside glBegin/glEnd");
        return BUGLE_FALSE;
    }

    switch (target)
    {
#ifdef GL_ARB_fragment_program
    case GL_FRAGMENT_PROGRAM_ARB:
#endif
#ifdef GL_ARB_vertex_program
    case GL_VERTEX_PROGRAM_ARB:
#endif
#if defined(GL_ARB_vertex_program) || defined(GL_ARB_fragment_program)
        CALL(glPushAttrib)(GL_ALL_ATTRIB_BITS);
        CALL(glBindProgramARB)(target, shader_id);
        CALL(glGetProgramivARB)(target, GL_PROGRAM_LENGTH_ARB, &length);
        text = BUGLE_NMALLOC(length, char);
        CALL(glGetProgramStringARB)(target, GL_PROGRAM_STRING_ARB, text);
        CALL(glPopAttrib)();
        break;
#endif
    case GL_VERTEX_SHADER:
    case GL_FRAGMENT_SHADER:
#ifdef GL_EXT_geometry_shader4
    case GL_GEOMETRY_SHADER_EXT:
#endif
        bugle_glGetShaderiv(shader_id, GL_SHADER_SOURCE_LENGTH, &length);
        if (length != 0)
        {
            text = BUGLE_NMALLOC(length, char);
            bugle_glGetShaderSource(shader_id, length, NULL, text);
            length--; /* Don't count NULL terminator */
        }
        else
            text = BUGLE_NMALLOC(1, char);
        break;
    default:
        /* Should never get here */
        text = BUGLE_NMALLOC(1, char);
        length = 0;
    }

    gldb_protocol_send_code(out_pipe, RESP_DATA);
    gldb_protocol_send_code(out_pipe, id);
    gldb_protocol_send_code(out_pipe, REQ_DATA_SHADER);
    gldb_protocol_send_binary_string(out_pipe, length, text);
    bugle_free(text);

    bugle_gl_end_internal_render("send_data_shader", BUGLE_TRUE);
    return BUGLE_TRUE;
}

static bugle_bool send_data_info_log(bugle_uint32_t id, GLuint object_id,
                                     GLenum target)
{
    GLint length;
    char *text;

    if (!bugle_gl_begin_internal_render())
    {
        gldb_protocol_send_code(out_pipe, RESP_ERROR);
        gldb_protocol_send_code(out_pipe, id);
        gldb_protocol_send_code(out_pipe, 0);
        gldb_protocol_send_string(out_pipe, "inside glBegin/glEnd");
        return BUGLE_FALSE;
    }

    switch (target)
    {
#ifdef GL_ARB_fragment_program
    case GL_FRAGMENT_PROGRAM_ARB:
#endif
#ifdef GL_ARB_vertex_program
    case GL_VERTEX_PROGRAM_ARB:
#endif
#if defined(GL_ARB_vertex_program) || defined(GL_ARB_fragment_program)
        /* The info log is overwritten at the next call, so don't try to
         * fetch this.
         */
        length = 0;
        text = BUGLE_NMALLOC(1, char);
        text[0] = '\0';
        break;
#endif
    case GL_VERTEX_SHADER:
    case GL_FRAGMENT_SHADER:
#ifdef GL_EXT_geometry_shader4
    case GL_GEOMETRY_SHADER_EXT:
#endif
        bugle_glGetShaderiv(object_id, GL_INFO_LOG_LENGTH, &length);
        if (length != 0)
        {
            text = BUGLE_NMALLOC(length, char);
            bugle_glGetShaderInfoLog(object_id, length, NULL, text);
            length--; /* Don't count NULL terminator */
        }
        else
            text = BUGLE_NMALLOC(1, char);
        break;
    case 0x8B40: /* GL_PROGRAM_OBJECT_ARB - but doesn't exist in ES header files */
        bugle_glGetProgramiv(object_id, GL_INFO_LOG_LENGTH, &length);
        if (length != 0)
        {
            text = BUGLE_NMALLOC(length, char);
            bugle_glGetProgramInfoLog(object_id, length, NULL, text);
            length--; /* Don't count NULL terminator */
        }
        else
            text = BUGLE_NMALLOC(1, char);
        break;
    default:
        /* Should never get here */
        text = BUGLE_NMALLOC(1, char);
        length = 0;
    }

    gldb_protocol_send_code(out_pipe, RESP_DATA);
    gldb_protocol_send_code(out_pipe, id);
    gldb_protocol_send_code(out_pipe, REQ_DATA_INFO_LOG);
    gldb_protocol_send_binary_string(out_pipe, length, text);
    bugle_free(text);

    bugle_gl_end_internal_render("send_data_info_log", BUGLE_TRUE);
    return BUGLE_TRUE;

}
#endif /* GL_ES_VERSION_2_0 || GL_VERSION_2_0 */

#ifdef GL_VERSION_1_1
static bugle_bool send_data_buffer(bugle_uint32_t id, GLuint object_id)
{
    GLint old_binding;
    GLint size;
    void *data;

    glwin_display dpy = NULL;
    glwin_context aux = NULL, real = NULL;
    glwin_drawable old_read = 0, old_write = 0;

    if (!BUGLE_GL_HAS_EXTENSION(GL_ARB_vertex_buffer_object))
    {
        gldb_protocol_send_code(out_pipe, RESP_ERROR);
        gldb_protocol_send_code(out_pipe, id);
        gldb_protocol_send_code(out_pipe, 0);
        gldb_protocol_send_string(out_pipe, "GL_ARB_vertex_buffer_object is not available");
        return BUGLE_FALSE;
    }
    if (!bugle_gl_begin_internal_render())
    {
        gldb_protocol_send_code(out_pipe, RESP_ERROR);
        gldb_protocol_send_code(out_pipe, id);
        gldb_protocol_send_code(out_pipe, 0);
        gldb_protocol_send_string(out_pipe, "inside glBegin/glEnd");
        return BUGLE_FALSE;
    }
    if (!CALL(glIsBufferARB)(object_id))
    {
        bugle_gl_end_internal_render("send_data_buffer", BUGLE_TRUE);
        gldb_protocol_send_code(out_pipe, RESP_ERROR);
        gldb_protocol_send_code(out_pipe, id);
        gldb_protocol_send_code(out_pipe, 0);
        gldb_protocol_send_string(out_pipe, "buffer ID is invalid");
        return BUGLE_FALSE;
    }

    aux = bugle_get_aux_context(BUGLE_TRUE);
    if (aux)
    {
        real = bugle_glwin_get_current_context();
        old_write = bugle_glwin_get_current_drawable();
        old_read = bugle_glwin_get_current_read_drawable();
        dpy = bugle_glwin_get_current_display();
        bugle_glwin_make_context_current(dpy, old_write, old_write, aux);
        old_binding = 0;
    }
    else
    {
        CALL(glGetIntegerv)(GL_ARRAY_BUFFER_BINDING_ARB, &old_binding);
    }

    CALL(glBindBuffer)(GL_ARRAY_BUFFER_ARB, object_id);
    CALL(glGetBufferParameterivARB)(GL_ARRAY_BUFFER_ARB, GL_BUFFER_SIZE_ARB, &size);
    data = bugle_malloc(size);
    CALL(glGetBufferSubDataARB)(GL_ARRAY_BUFFER_ARB, 0, size, data);
    CALL(glBindBuffer)(GL_ARRAY_BUFFER_ARB, old_binding);

    if (aux)
    {
        GLenum error;
        while ((error = CALL(glGetError)()) != GL_NO_ERROR)
            bugle_log_printf("debugger", "protocol", BUGLE_LOG_WARNING,
                             "GL error %#04x generated by send_data_buffer in aux context", error);
        bugle_glwin_make_context_current(dpy, old_write, old_read, real);
    }

    gldb_protocol_send_code(out_pipe, RESP_DATA);
    gldb_protocol_send_code(out_pipe, id);
    gldb_protocol_send_code(out_pipe, REQ_DATA_BUFFER);
    gldb_protocol_send_binary_string(out_pipe, size, data);

    bugle_free(data);
    bugle_gl_end_internal_render("send_data_buffer", BUGLE_TRUE);
    return BUGLE_TRUE;
}
#endif

/* Reads a binary string into a structure. Returns true on success */
static bugle_bool read_binary_string(bugle_io_reader *in_pipe, gldb_binary_string *s)
{
    return gldb_protocol_recv_binary_string(in_pipe, &s->length, &s->data);
}

static void process_single_command(function_call *call, gldb_request_header *req)
{
    char *resp_str;

    if (req == NULL)
    {
        bugle_log_printf("debugger", "process_single_command",
                         BUGLE_LOG_WARNING,
                         "Could not read a command from the pipe, exiting");
        exit(1);
    }

    switch (req->code)
    {
    case REQ_RUN:
        gldb_protocol_send_code(out_pipe, RESP_RUNNING);
        gldb_protocol_send_code(out_pipe, req->request_id);
		send_session_header();
        /* Fall through */
    case REQ_CONT:
    case REQ_STEP:
        break_on_next = (req->code == REQ_STEP);
        stopped = BUGLE_FALSE;
        start_id = req->request_id;
        break;
    case REQ_BREAK:
        {
            gldb_request_break *req2 = (gldb_request_break *) req;
            budgie_function func = budgie_function_id(req2->name.data);
            if (func != NULL_FUNCTION)
            {
                gldb_protocol_send_code(out_pipe, RESP_ANS);
                gldb_protocol_send_code(out_pipe, req->request_id);
                gldb_protocol_send_code(out_pipe, 0);
                break_on[func] = req2->enable != 0;
            }
            else
            {
                gldb_protocol_send_code(out_pipe, RESP_ERROR);
                gldb_protocol_send_code(out_pipe, req->request_id);
                gldb_protocol_send_code(out_pipe, 0);
                resp_str = bugle_asprintf("Unknown function %s", req2->name.data);
                gldb_protocol_send_string(out_pipe, resp_str);
                bugle_free(resp_str);
            }
            bugle_free(req2->name.data);
        }
        break;
    case REQ_BREAK_EVENT:
        {
            gldb_request_break_event *req2 = (gldb_request_break_event *) req;
            if (req2->event >= REQ_EVENT_COUNT)
            {
                gldb_protocol_send_code(out_pipe, RESP_ERROR);
                gldb_protocol_send_code(out_pipe, req->request_id);
                gldb_protocol_send_code(out_pipe, 0);
                gldb_protocol_send_string(out_pipe, "Event out of range - protocol mismatch?");
            }
            else
            {
                break_on_event[req2->event] = req2->enable != 0;
                gldb_protocol_send_code(out_pipe, RESP_ANS);
                gldb_protocol_send_code(out_pipe, req->request_id);
                gldb_protocol_send_code(out_pipe, 0);
            }
        }
        break;
    case REQ_ACTIVATE_FILTERSET:
    case REQ_DEACTIVATE_FILTERSET:
        {
            gldb_request_activate_filterset *req2 = (gldb_request_activate_filterset *) req;
            bugle_bool activate = (req->code == REQ_ACTIVATE_FILTERSET);
            filter_set *f = bugle_filter_set_get_handle(req2->name.data);
            if (!f)
            {
                resp_str = bugle_asprintf("Unknown filter-set %s", req2->name.data);
                gldb_protocol_send_code(out_pipe, RESP_ERROR);
                gldb_protocol_send_code(out_pipe, req->request_id);
                gldb_protocol_send_code(out_pipe, 0);
                gldb_protocol_send_string(out_pipe, resp_str);
                bugle_free(resp_str);
            }
            else if (!strcmp(req2->name.data, "debugger"))
            {
                gldb_protocol_send_code(out_pipe, RESP_ERROR);
                gldb_protocol_send_code(out_pipe, req->request_id);
                gldb_protocol_send_code(out_pipe, 0);
                gldb_protocol_send_string(out_pipe, "Cannot activate or deactivate the debugger");
            }
            else if (!bugle_filter_set_is_loaded(f))
            {
                resp_str = bugle_asprintf("Filter-set %s is not loaded; it must be loaded at program start",
                                          req2->name.data);
                gldb_protocol_send_code(out_pipe, RESP_ERROR);
                gldb_protocol_send_code(out_pipe, req->request_id);
                gldb_protocol_send_code(out_pipe, 0);
                gldb_protocol_send_string(out_pipe, resp_str);
                bugle_free(resp_str);
            }
            else if (bugle_filter_set_is_active(f) == activate)
            {
                resp_str = bugle_asprintf("Filter-set %s is already %s",
                                          req2->name.data, activate ? "active" : "inactive");
                gldb_protocol_send_code(out_pipe, RESP_ERROR);
                gldb_protocol_send_code(out_pipe, req->request_id);
                gldb_protocol_send_code(out_pipe, 0);
                gldb_protocol_send_string(out_pipe, resp_str);
                bugle_free(resp_str);
            }
            else
            {
                if (activate) bugle_filter_set_activate_deferred(f);
                else bugle_filter_set_deactivate_deferred(f);
                if (!bugle_filter_set_is_active(bugle_filter_set_get_handle("debugger")))
                {
                    gldb_protocol_send_code(out_pipe, RESP_ERROR);
                    gldb_protocol_send_code(out_pipe, req->request_id);
                    gldb_protocol_send_code(out_pipe, 0);
                    gldb_protocol_send_string(out_pipe, "Debugger was disabled; re-enabling");
                    bugle_filter_set_activate_deferred(bugle_filter_set_get_handle("debugger"));
                }
                else
                {
                    gldb_protocol_send_code(out_pipe, RESP_ANS);
                    gldb_protocol_send_code(out_pipe, req->request_id);
                    gldb_protocol_send_code(out_pipe, 0);
                }
            }
            bugle_free(req2->name.data);
        }
        break;
    case REQ_STATE_TREE:
        if (bugle_gl_begin_internal_render())
        {
			send_state_header(req->request_id); /* Bryce code to send a header with the time */
            send_state(bugle_state_get_root(), req->request_id);
            bugle_gl_end_internal_render("send_state", BUGLE_TRUE);
        }
        else
        {
            gldb_protocol_send_code(out_pipe, RESP_ERROR);
            gldb_protocol_send_code(out_pipe, req->request_id);
            gldb_protocol_send_code(out_pipe, 0);
            gldb_protocol_send_string(out_pipe, "In glBegin/glEnd; no state available");
        }
        break;
    case REQ_STATE_TREE_RAW:
        if (bugle_gl_begin_internal_render())
        {
            send_state_raw(bugle_state_get_root(), req->request_id);
            bugle_gl_end_internal_render("send_state_raw", BUGLE_TRUE);
        }
        else
        {
            gldb_protocol_send_code(out_pipe, RESP_ERROR);
            gldb_protocol_send_code(out_pipe, req->request_id);
            gldb_protocol_send_code(out_pipe, 0);
            gldb_protocol_send_string(out_pipe, "In glBegin/glEnd; no state available");
        }
        break;
    case REQ_SCREENSHOT:
        gldb_protocol_send_code(out_pipe, RESP_ERROR);
        gldb_protocol_send_code(out_pipe, req->request_id);
        gldb_protocol_send_code(out_pipe, 0);
        gldb_protocol_send_string(out_pipe, "Screenshot interface has been removed. Please upgrade gldb.");
        break;
    case REQ_QUIT:
        exit(1);
    case REQ_DATA:
        {
            gldb_request_data_header *req_sub = (gldb_request_data_header *) req;
            /* FIXME: check for begin/end? */
            switch (req_sub->subtype)
            {
#ifdef GL_VERSION_1_1
            case REQ_DATA_TEXTURE:
                {
                    gldb_request_data_texture *req2 = (gldb_request_data_texture *) req;
                    send_data_texture(req->request_id,
                                      req2->object_id,
                                      req2->target,
                                      req2->face,
                                      req2->level,
                                      req2->format,
                                      req2->type);
                }
                break;
            case REQ_DATA_BUFFER:
                {
                    gldb_request_data_buffer *req2 = (gldb_request_data_buffer *) req;
                    send_data_buffer(req->request_id, req2->object_id);
                }
                break;
#endif
            case REQ_DATA_FRAMEBUFFER:
                {
                    gldb_request_data_framebuffer *req2 = (gldb_request_data_framebuffer *) req;
                    send_data_framebuffer(req->request_id,
                                          req2->object_id,
                                          req2->target,
                                          req2->buffer,
                                          req2->format,
                                          req2->type);
                }
                break;
#if GL_ES_VERSION_2_0 || GL_VERSION_2_0
            case REQ_DATA_SHADER:
                {
                    gldb_request_data_shader *req2 = (gldb_request_data_shader *) req;
                    send_data_shader(req->request_id, req2->object_id, req2->target);
                }
                break;
            case REQ_DATA_INFO_LOG:
                {
                    gldb_request_data_info_log *req2 = (gldb_request_data_info_log *) req;
                    send_data_info_log(req->request_id, req2->object_id, req2->target);
                }
                break;
#endif
            default:
                /* read_request should have caught this */
                assert(0);
                gldb_protocol_send_code(out_pipe, RESP_ERROR);
                gldb_protocol_send_code(out_pipe, req->request_id);
                gldb_protocol_send_code(out_pipe, 0);
                gldb_protocol_send_string(out_pipe, "unknown subtype");
                break;
            }
        }
        break;
    case REQ_ASYNC:
        if (!stopped)
        {
            if (!stoppable())
                break_on_next = BUGLE_TRUE;
            else
            {
                resp_str = revised_dump_any_call_string(call);
                stopped = BUGLE_TRUE;
                break_on_next = BUGLE_FALSE;
                gldb_protocol_send_code(out_pipe, RESP_BREAK);
                gldb_protocol_send_code(out_pipe, start_id);
                gldb_protocol_send_string(out_pipe, resp_str);
                bugle_free(resp_str);
            }
        }
        break;
    default:
        assert(0); /* read_request should have caught this case */
        bugle_log_printf("debugger", "process_single_command",
                         BUGLE_LOG_WARNING,
                         "Unknown debug command %#08lx received",
                         (unsigned long) req);
        break;
    }
    bugle_free(req);
}

/* The main initialiser calls this with call == NULL. It has essentially
 * the usual effects, but refuses to do anything that doesn't make sense
 * until the program is properly running (such as flush or query state).
 */
static void debugger_loop(function_call *call)
{
    if (call && bugle_gl_begin_internal_render())
    {
        CALL(glFinish)();
        bugle_gl_end_internal_render("debugger", BUGLE_TRUE);
    }

    do
    {
        gldb_request_header *req;

        if (!stopped && !bugle_workqueue_has_data(request_queue))
        {
            break;
        }

        req = bugle_workqueue_get_item(request_queue);

        if (req == NULL)
        {
            bugle_log_printf("debugger", "debugger_loop", BUGLE_LOG_ERROR,
                             "Error reading from debugger, continuing");
            stopped = BUGLE_FALSE;
            break;
        }
        process_single_command(call, req);
    } while (stopped);

	send_call(call);
}

static void debugger_init_thread(void)
{
    debug_thread = bugle_thread_self();
}

static bugle_thread_once_t debugger_init_thread_once = BUGLE_THREAD_ONCE_INIT;

static bugle_bool debugger_callback(function_call *call, const callback_data *data)
{
    char *resp_str;

    /* Only one thread can read and write from the pipes.
     * FIXME: still stop others threads, with events to start and stop everything
     * in sync.
     */
    bugle_thread_once(&debugger_init_thread_once, debugger_init_thread);
    if (!bugle_thread_equal(debug_thread, bugle_thread_self()))
        return BUGLE_TRUE;

    if (stoppable())
    {
        if (break_on[call->generic.id] || break_on_next)
        {
            resp_str = dump_any_call_string(call);
            stopped = BUGLE_TRUE;
            break_on_next = BUGLE_FALSE;
            gldb_protocol_send_code(out_pipe, RESP_BREAK);
            gldb_protocol_send_code(out_pipe, start_id);
            gldb_protocol_send_string(out_pipe, resp_str);
            bugle_free(resp_str);
        }
    }
    debugger_loop(call);
    return BUGLE_TRUE;
}

static bugle_bool debugger_error_callback(function_call *call, const callback_data *data)
{
    GLenum error;
    char *resp_str;
    const char *error_str = NULL; /* NULL indicates no break, if non-NULL then break */

    bugle_thread_once(&debugger_init_thread_once, debugger_init_thread);
    if (debug_thread != bugle_thread_self())
        return BUGLE_TRUE;

    error = bugle_gl_call_get_error(data->call_object);
    if (break_on_event[REQ_EVENT_GL_ERROR] && error != GL_NO_ERROR)
    {
        error_str = bugle_api_enum_name(error, BUGLE_API_EXTENSION_BLOCK_GL);
    }
#if GL_ES_VERSION_2_0 || GL_VERSION_2_0
    else if (break_on_event[REQ_EVENT_COMPILE_ERROR] && call->generic.group == BUDGIE_GROUP_ID(glCompileShader)
             && !bugle_gl_in_begin_end())
    {
        GLint status;
        bugle_glGetShaderiv(*call->glCompileShader.arg0, GL_COMPILE_STATUS, &status);
        if (status == GL_FALSE)
            error_str = "Shader compilation error";
    }
    else if (break_on_event[REQ_EVENT_LINK_ERROR] && call->generic.group == BUDGIE_GROUP_ID(glLinkProgram)
             && !bugle_gl_in_begin_end())
    {
        GLint status;
        bugle_glGetProgramiv(*call->glLinkProgram.arg0, GL_LINK_STATUS, &status);
        if (status == GL_FALSE)
            error_str = "Program link error";
    }
#endif /* GL_ES_VERSION_2_0 || GL_VERSION_2_0 */

    if (error_str != NULL)
    {
        resp_str = revised_dump_any_call_string(call);
        gldb_protocol_send_code(out_pipe, RESP_BREAK_EVENT);
        gldb_protocol_send_code(out_pipe, start_id);
        gldb_protocol_send_string(out_pipe, resp_str);
        gldb_protocol_send_string(out_pipe, error_str);
        bugle_free(resp_str);
        stopped = BUGLE_TRUE;
        debugger_loop(call);
    }
    return BUGLE_TRUE;
}

/* Reads a single request from the input pipe, and returns it (in newly
 * allocated memory). If reading fails (either due to EOF or failure),
 * returns NULL.
 */
static bugle_bool read_request(bugle_workqueue *queue, void *user_data, void **item)
{
    bugle_io_reader *in_pipe = (bugle_io_reader *) user_data;
    gldb_request_header **out = (gldb_request_header **) item;
    gldb_request_header header;

    *out = NULL;
    if (!gldb_protocol_recv_code(in_pipe, &header.code))
        return BUGLE_FALSE;
    if (!gldb_protocol_recv_code(in_pipe, &header.request_id))
        return BUGLE_FALSE;

    switch (header.code)
    {
    case REQ_RUN:
    case REQ_CONT:
    case REQ_SCREENSHOT:
    case REQ_STEP:
    case REQ_STATE_TREE:
    case REQ_STATE_TREE_RAW:
    case REQ_QUIT:
    case REQ_ASYNC:
        {
            gldb_request_simple *req = BUGLE_MALLOC(gldb_request_simple);
            req->header = header;
            *out = &req->header;
            return BUGLE_TRUE;
        }
        break;
    case REQ_BREAK:
        {
            gldb_request_break *req = BUGLE_MALLOC(gldb_request_break);
            req->header = header;
            req->name.data = NULL;
            if (!read_binary_string(in_pipe, &req->name)
                || !gldb_protocol_recv_code(in_pipe, &req->enable))
            {
                bugle_free(req->name.data);
                bugle_free(req);
                return BUGLE_FALSE;
            }
            *out = &req->header;
            return BUGLE_TRUE;
        }
        break;
    case REQ_BREAK_EVENT:
        {
            gldb_request_break_event *req = BUGLE_MALLOC(gldb_request_break_event);
            req->header = header;
            if (!gldb_protocol_recv_code(in_pipe, &req->event)
                || !gldb_protocol_recv_code(in_pipe, &req->enable))
            {
                bugle_free(req);
                return BUGLE_FALSE;
            }
            *out = &req->header;
            return BUGLE_TRUE;
        }
        break;
    case REQ_ACTIVATE_FILTERSET:
    case REQ_DEACTIVATE_FILTERSET:
        {
            gldb_request_activate_filterset *req = BUGLE_MALLOC(gldb_request_activate_filterset);
            req->header = header;
            if (!read_binary_string(in_pipe, &req->name))
            {
                bugle_free(req);
                return BUGLE_FALSE;
            }
            *out = &req->header;
            return BUGLE_TRUE;
        }
        break;
    case REQ_DATA:
        {
            bugle_uint32_t subtype;
            if (!gldb_protocol_recv_code(in_pipe, &subtype))
                return BUGLE_FALSE;

            switch (subtype)
            {
#ifdef GL_VERSION_1_1
            case REQ_DATA_TEXTURE:
                {
                    gldb_request_data_texture *req = BUGLE_MALLOC(gldb_request_data_texture);
                    req->header.header = header;
                    req->header.subtype = subtype;
                    if (!gldb_protocol_recv_code(in_pipe, &req->object_id)
                        || !gldb_protocol_recv_code(in_pipe, &req->target)
                        || !gldb_protocol_recv_code(in_pipe, &req->face)
                        || !gldb_protocol_recv_code(in_pipe, &req->level)
                        || !gldb_protocol_recv_code(in_pipe, &req->format)
                        || !gldb_protocol_recv_code(in_pipe, &req->type))
                    {
                        bugle_free(req);
                        return BUGLE_FALSE;
                    }
                    *out = &req->header.header;
                    return BUGLE_TRUE;
                }
                break;
            case REQ_DATA_BUFFER:
                {
                    gldb_request_data_buffer *req = BUGLE_MALLOC(gldb_request_data_buffer);
                    req->header.header = header;
                    req->header.subtype = subtype;
                    if (!gldb_protocol_recv_code(in_pipe, &req->object_id))
                    {
                        bugle_free(req);
                        return BUGLE_FALSE;
                    }
                    *out = &req->header.header;
                    return BUGLE_TRUE;
                }
                break;
#endif
            case REQ_DATA_FRAMEBUFFER:
                {
                    gldb_request_data_framebuffer *req = BUGLE_MALLOC(gldb_request_data_framebuffer);
                    req->header.header = header;
                    req->header.subtype = subtype;
                    if (!gldb_protocol_recv_code(in_pipe, &req->object_id)
                        || !gldb_protocol_recv_code(in_pipe, &req->target)
                        || !gldb_protocol_recv_code(in_pipe, &req->buffer)
                        || !gldb_protocol_recv_code(in_pipe, &req->format)
                        || !gldb_protocol_recv_code(in_pipe, &req->type))
                    {
                        bugle_free(req);
                        return BUGLE_FALSE;
                    }
                    *out = &req->header.header;
                    return BUGLE_TRUE;
                }
                break;
            case REQ_DATA_SHADER:
                {
                    gldb_request_data_shader *req = BUGLE_MALLOC(gldb_request_data_shader);
                    req->header.header = header;
                    req->header.subtype = subtype;
                    if (!gldb_protocol_recv_code(in_pipe, &req->object_id)
                        || !gldb_protocol_recv_code(in_pipe, &req->target))
                    {
                        bugle_free(req);
                        return BUGLE_FALSE;
                    }
                    *out = &req->header.header;
                    return BUGLE_TRUE;
                }
                break;
            case REQ_DATA_INFO_LOG:
                {
                    gldb_request_data_info_log *req = BUGLE_MALLOC(gldb_request_data_info_log);
                    req->header.header = header;
                    req->header.subtype = subtype;
                    if (!gldb_protocol_recv_code(in_pipe, &req->object_id)
                        || !gldb_protocol_recv_code(in_pipe, &req->target))
                    {
                        bugle_free(req);
                        return BUGLE_FALSE;
                    }
                    *out = &req->header.header;
                    return BUGLE_TRUE;
                }
                break;
            default:
                bugle_log_printf("debugger", "read_request",
                                 BUGLE_LOG_ERROR,
                                 "Unknown debug data subcommand %#08lx received",
                                 (unsigned long) subtype);
                return BUGLE_FALSE;
            }
        }
        break;
    default:
        bugle_log_printf("debugger", "read_request",
                         BUGLE_LOG_ERROR,
                         "Unknown debug command %#08lx received",
                         (unsigned long) header.code);
        return BUGLE_FALSE;
    }
}

static bugle_bool debugger_initialise(filter_set *handle)
{
    const char *env;
    char *last;
    filter *f;

    break_on = BUGLE_CALLOC(budgie_function_count(), bugle_bool);

    if (!getenv("BRYCE_DEBUGGER")) /* TODO: change name? - Bryce */
    {
        bugle_log("debugger", "initialise", BUGLE_LOG_ERROR,
                  "The debugger filter-set should only be used with bryce-debug");  /* TODO: change name? - Bryce */
        return BUGLE_FALSE;
    }

    if (BUGLE_FALSE)
    {
        /* Empty block to start conditionally-compiled chain of else ifs. */
    }
#if BUGLE_PLATFORM_MSVCRT || BUGLE_PLATFORM_MINGW
    else if (0 == strcmp(getenv("BRYCE_DEBUGGER"), "handle"))
    {
        HANDLE in_pipe_handle, out_pipe_handle;

        if (!getenv("BRYCE_DEBUGGER_HANDLE_IN")
            || !getenv("BRYCE_DEBUGGER_HANDLE_OUT"))
        {
            bugle_log("debugger", "initialise", BUGLE_LOG_ERROR,
                      "The debugger filter-set should only be used with gldb (missing envars)");  /* TODO: change name? - Bryce */
            return BUGLE_FALSE;
        }
        env = getenv("BRYCE_DEBUGGER_HANDLE_IN");
        in_pipe_handle = (HANDLE) _strtoui64(env, &last, 0);
        if (!*env || *last)
        {
            bugle_log_printf("debugger", "initialise", BUGLE_LOG_ERROR,
                             "Illegal BRYCE_DEBUGGER_HANDLE_IN: '%s' (bug in gldb?)",  /* TODO: change name? - Bryce */
                             env);
            return BUGLE_FALSE;
        }

        env = getenv("BRYCE_DEBUGGER_HANDLE_OUT");
        out_pipe_handle = (HANDLE) _strtoui64(env, &last, 0);
        if (!*env || *last)
        {
            bugle_log_printf("debugger", "initialise", BUGLE_LOG_ERROR,
                             "Illegal BRYCE_DEBUGGER_FD_OUT: '%s' (bug in gldb?)",  /* TODO: change name? - Bryce */
                             env);
            return BUGLE_FALSE;
        }
        in_pipe = bugle_io_reader_handle_new(in_pipe_handle, TRUE);
        out_pipe = bugle_io_writer_handle_new(out_pipe_handle, TRUE);
    }
#endif
#if BUGLE_PLATFORM_POSIX
    else if (0 == strcmp(getenv("BRYCE_DEBUGGER"), "fd"))
    {
        int in_pipe_fd, out_pipe_fd;

        if (!getenv("BRYCE_DEBUGGER_FD_IN")
            || !getenv("BRYCE_DEBUGGER_FD_OUT"))
        {
            bugle_log("debugger", "initialise", BUGLE_LOG_ERROR,
                      "The debugger filter-set should only be used with gldb");  /* TODO: change name? - Bryce */
            return BUGLE_FALSE;
        }

        env = getenv("BRYCE_DEBUGGER_FD_IN");
        in_pipe_fd = strtol(env, &last, 0);
        if (!*env || *last)
        {
            bugle_log_printf("debugger", "initialise", BUGLE_LOG_ERROR,
                             "Illegal BRYCE_DEBUGGER_FD_IN: '%s' (bug in gldb?)",  /* TODO: change name? - Bryce */
                             env);
            return BUGLE_FALSE;
        }

        env = getenv("BRYCE_DEBUGGER_FD_OUT");
        out_pipe_fd = strtol(env, &last, 0);
        if (!*env || *last)
        {
            bugle_log_printf("debugger", "initialise", BUGLE_LOG_ERROR,
                             "Illegal BRYCE_DEBUGGER_FD_OUT: '%s' (bug in gldb?)",  /* TODO: change name? - Bryce */
                             env);
            return BUGLE_FALSE;
        }
        in_pipe = bugle_io_reader_fd_new(in_pipe_fd);
        out_pipe = bugle_io_writer_fd_new(out_pipe_fd);
    }
#endif
    else if (0 == strcmp(getenv("BRYCE_DEBUGGER"), "tcp"))
    {
        const char *host;
        const char *port;
        char *error;

        port = getenv("BRYCE_DEBUGGER_PORT");
        if (!port)
        {
            bugle_log("debugger", "initialise", BUGLE_LOG_ERROR,
                      "BRYCE_DEBUGGER_PORT must be set");   /* TODO: change name? - Bryce */
            return BUGLE_FALSE;
        }

        host = getenv("BRYCE_DEBUGGER_HOST");  /* TODO: change name? - Bryce */

        error = bugle_io_socket_listen(host, port, &in_pipe, &out_pipe);
        if (error != NULL)
        {
            bugle_log("debugger", "initialise", BUGLE_LOG_ERROR, error);
            bugle_free(error);
            return BUGLE_FALSE;
        }
    }
    else
    {
        bugle_log_printf("debugger", "initialise", BUGLE_LOG_ERROR,
                         "did not recognise BRYCE_DEBUGGER value '%s'",  /* TODO: change name? - Bryce */
                         getenv("BRYCE_DEBUGGER"));
        return BUGLE_FALSE;
    }

    request_queue = bugle_workqueue_new(read_request, in_pipe);
    if (request_queue == NULL)
    {
        bugle_log_printf("debugger", "initialise", BUGLE_LOG_ERROR,
                         "failed to initialise work queue");
        bugle_io_reader_close(in_pipe);
        bugle_io_writer_close(out_pipe);
        return BUGLE_FALSE;
    }
    bugle_workqueue_start(request_queue);

    debugger_loop(NULL);

    f = bugle_filter_new(handle, "debugger_test");
    bugle_filter_catches_all(f, BUGLE_FALSE, debugger_callback);
    f = bugle_filter_new(handle, "debugger_error");
    bugle_filter_catches_all(f, BUGLE_FALSE, debugger_error_callback);
    bugle_filter_order("debugger", "invoke");
    bugle_filter_order("invoke", "debugger_error");
    bugle_filter_order("error", "debugger_error");
    bugle_filter_order("globjects", "debugger_error"); /* so we don't try to query any deleted objects */
    bugle_gl_filter_post_renders("debugger_error");
    bugle_gl_filter_set_queries_error("debugger");

    return BUGLE_TRUE;
}

static void debugger_shutdown(filter_set *handle)
{
    /* TODO: this will leak the resources associated with the reader thread,
     * but we don't have a portable way to interrupt it if it is blocked on
     * I/O from the pipe.
     */
    bugle_free(break_on);
}

void bugle_initialise_filter_library(void)
{
    unsigned int i;

    static const filter_set_info debugger_info =
    {
        "debugger_test",
        debugger_initialise,
        debugger_shutdown,
        NULL,
        NULL,
        NULL,
        NULL /* no documentation */
    };

    bugle_filter_set_new(&debugger_info);

    bugle_filter_set_depends("debugger_test", "error");
    bugle_filter_set_depends("debugger_test", "glextensions");
    bugle_filter_set_depends("debugger_test", "globjects");
    bugle_filter_set_depends("debugger_test", "glbeginend");
    bugle_gl_filter_set_renders("debugger_test");

    for (i = 0; i < REQ_EVENT_COUNT; i++)
        break_on_event[i] = BUGLE_TRUE;
}
