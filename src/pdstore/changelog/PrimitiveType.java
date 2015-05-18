package pdstore.changelog;

import java.sql.Timestamp;

import nz.ac.auckland.se.genoupe.tools.Debug;
import pdstore.Blob;
import pdstore.GUID;
import pdstore.PDStore;
import pdstore.PDStoreException;

public enum PrimitiveType {
	/**
	 * IETF RFC 4122 UUID
	 */
	GUID(0),

	/**
	 * int 64-bit 2-complement (java, C++ long)
	 */
	INTEGER(1),

	/**
	 * IEEE double (64 bit)
	 */
	DOUBLE(2),

	/**
	 * (8 byte length info, UTF-8 unicode)
	 */
	STRING(3),

	/**
	 * (8 byte length info)
	 */
	BLOB(4),

	/**
	 * (1 byte)
	 */
	BOOLEAN(5),

	/**
	 * (64 bit)
	 */
	TIMESTAMP(6),

	/**
	 * (SQL, Binary-coded decimal, 4-byte length info)
	 */
	DECIMAL(7);

	int code;

	/**
	 * Maximum size of values that can be inlined into an index. Values bigge
	 * than this will be stored in the log only in referenced.
	 */
	public static final int MAX_VALUE_INLINE_SIZE = 64;

	PrimitiveType(int code) {
		this.code = code;
	}

	public static PrimitiveType typeOf(Object o) {
		Debug.assertTrue(o != null, "Object should not be null");
		Class<?> type = o.getClass();
		if (type == GUID.class)
			return GUID;
		if (type == Integer.class || type == Long.class)
			return INTEGER;
		if (type == Float.class || type == Double.class) {
			return DOUBLE;
		}
		if (type == String.class)
			return STRING;
		if (type == Blob.class
				|| (type.isArray() && type.getComponentType() == Byte.TYPE))
			return BLOB;
		if (type == Boolean.class)
			return BOOLEAN;
		if (type == Timestamp.class)
			return TIMESTAMP;
		// TODO: find Java type for DECIMAL

		throw new PDStoreException(
				"Could not find a PDStore PrimitiveType for the Java type "
						+ type);
	}

	public static PrimitiveType typeForCode(int code) {
		if (code == 0)
			return GUID;
		if (code == 1)
			return INTEGER;
		if (code == 2)
			return DOUBLE;
		if (code == 3)
			return STRING;
		if (code == 4)
			return BLOB;
		if (code == 5)
			return BOOLEAN;
		if (code == 6)
			return TIMESTAMP;
		if (code == 7)
			return DECIMAL;

		throw new PDStoreException(
				"Could not find a PDStore PrimitiveType for the code number "
						+ code);
	}

	public static Object convertStringToPrimitive(String s) {
		// TODO: Create cases for all pdstore primitive types
		try {
			int value = Integer.parseInt(s);
			return value;
		} catch (NumberFormatException e) {
		}
		try {
			double value = Double.parseDouble(s);
			return value;
		} catch (NumberFormatException e) {
		}
		try {
			float value = Float.parseFloat(s);
			return value;
		} catch (NumberFormatException e) {
		}

		return s;
	}

	public static GUID typeIdOf(Object value) {
		Debug.assertTrue(value != null, "Object should not be null");

		Class<?> type = value.getClass();
		if (type == GUID.class)
			return PDStore.GUID_TYPEID;
		if (type == Integer.class || type == Long.class)
			return PDStore.INTEGER_TYPEID;
		if (type == Float.class || type == Double.class)
			return PDStore.DOUBLE_PRECISION_TYPEID;
		if (type == String.class)
			return PDStore.STRING_TYPEID;
		if (type == Blob.class
				|| (type.isArray() && type.getComponentType() == Byte.TYPE))
			return PDStore.BLOB_TYPEID;
		if (type == Boolean.class)
			return PDStore.BOOLEAN_TYPEID;
		if (type == Timestamp.class)
			return PDStore.TIMESTAMP_TYPEID;

		throw new PDStoreException("Cannot find PDStore type for Java object "
				+ value + " of type " + type);
	}

	/**
	 * Converts/casts the given primitive value to the compatible type that is
	 * supported by PDStore. For example, Integers are converted to Longs.
	 * 
	 * @param value
	 *            the primitive value
	 * @return the normalized primitve value
	 */
	public static Object normalize(Object value) {
		if (value == null)
			return null;
		Class<?> type = value.getClass();
		if (type == Integer.class)
			return new Long((Integer) value);
		if (type == Float.class)
			return new Double((Float) value);
		if (type.isArray() && type.getComponentType() == Byte.TYPE)
			return new Blob((byte[]) value);
		return value;
	}

	/**
	 * Returns the length in bytes the given value takes up in the internal
	 * PDStore binary format, as used in the B-tree index.
	 * 
	 * NOTE: changes containing blobs have a different size in the log, as the
	 * log stores the full blob data but the index simply refers to the data in
	 * the log.
	 * 
	 * @param value
	 * @return internal length in bytes
	 */
	public static long length(Object value) {
		switch (typeOf(value)) {
		case GUID:
			return 16;
		case BLOB:
			Blob blob = (Blob) value;
			if (blob.getLength() > MAX_VALUE_INLINE_SIZE)
				return 16; /* length, position */
			return 8 + blob.getLength(); /* length, data */
		case BOOLEAN:
			return 1;
		case DOUBLE:
			return 8;
		case INTEGER:
			return 8;
		case STRING:
			String s = (String) value;
			return 8 + s.getBytes().length;
		case TIMESTAMP:
			return 8;
		default:
			throw new RuntimeException("length() does not support type "
					+ typeOf(value));
		}
	}

	public static int compare(Object a, Object b) {
		// make sure to normalize values to Double if they are numbers
		if (a instanceof Integer || a instanceof Float || a instanceof Long)
			a = new Double(a.toString());
		if (b instanceof Integer || b instanceof Float || b instanceof Long)
			b = new Double(b.toString());

		return ((Comparable<Object>) a).compareTo(b);
	}
}
