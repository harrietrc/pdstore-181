package pdstore.dal;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

import nz.ac.auckland.se.genoupe.tools.Debug;

import pdstore.GUID;
import pdstore.OperationI;
import pdstore.PDStore;
import pdstore.PDStoreException;
import pdstore.ui.graphview.dal.PDGraph;
import pdstore.ui.graphview.dal.PDNode;

public class PDGen implements OperationI {

	static {
		// Debug.addDebugTopic("PDGen");
	}

	public final static GUID EXTRA_DAL_CODE_ROLEID = new GUID(
			"127a8d50248411e19ec000224300a31a");

	PDStore store;
	String typeName;
	String className;
	String packageName;
	String sourceRoot;

	@Override
	public Object apply(PDStore store, GUID transaction, Object parameter) {
		// TODO implement with superparameter
		return null;
	}

	public static void main(String[] args) {
		if (args.length == 0 || args.length > 2) {
			System.out.println("PDGen");
			System.out
					.println("Generates Java accessor classes for Models or individual Types in PDStore.");
			System.out
					.println("All accessor classes are generated into package pdstore.");
			System.out.println("Usage (arguments in [] are optional):");
			System.out
					.println("\tjava pdstore.PDGen \"Model name\" [pdstoreFile]");
			System.out.println("or");
			System.out
					.println("\tjava pdstore.PDGen \"Type name\" [pdstoreFile]");
			System.out.println("where");
			System.out
					.println("pdstoreFile is a path from the working directory to the PDStore");
			System.out
					.println("\tfile where the model to generate from is stored.");
			System.out
					.println("\tIf this is not provided, then the PDStore default file name is used.");
			return;
		}

		try {
			String instanceName = args[0];
			String sourceRoot = "src";
			String packageName = "pdstore.dal.generated";

			PDStore store;
			if (args.length >= 2)
				store = new PDStore(args[1]);
			else
				store = new PDStore("pdstore");

			if (store.instanceExists(PDStore.MODEL_TYPEID,
					store.getId(instanceName))) {
				// argument is model name
				generateModel(store, instanceName, sourceRoot, packageName);

			} else if (store.instanceExists(PDStore.TYPE_TYPEID,
					store.getId(instanceName))) {
				// argument is type name
				String className = "PD" + makeCamelCase(instanceName);
				PDType type1 = PDType.load(store, store.getId(instanceName));
				if (type1.getIsPrimitive())
					throw new Exception(
							"Cannot generate a wrapper class for a primitive type.");

				PDGen g = new PDGen(type1, className, packageName, sourceRoot);
				g.generate(type1);

			} else
				throw new Exception(
						"First argument is neither a Model name nor a Type name.");

		} catch (Exception e) {
			System.out.println("error");
			e.printStackTrace();
		}
	}

	/**
	 * this method can be used from another class to run pdgen and specify
	 * additional arguments such as the store to be used
	 */
	public static void generate(PDStore store, String instanceName,
			String className, String sourceRoot, String packageName) {
		if (sourceRoot == null || sourceRoot == "") {
			sourceRoot = "src";
		}
		if (packageName == null || packageName == "") {
			packageName = "pdstore";
		}
		try {
			if (store.instanceExists(PDModel.typeId, store.getId(instanceName))) {

				generateModel(store, instanceName, sourceRoot, packageName);

			} else if (store.instanceExists(PDType.typeId,
					store.getId(instanceName))) {
				// argument is type name
				if (className == null || className == "") {
					className = "PD" + makeCamelCase(instanceName);
				}

				PDType type1 = (PDType) store.load(PDType.typeId,
						store.getId(instanceName));
				if (type1.getIsPrimitive())
					throw new RuntimeException(
							"Cannot generate a wrapper class for a primitive type.");

				PDGen g = new PDGen(type1, className, packageName, sourceRoot);
				g.generate(type1);

			} else
				throw new RuntimeException(
						"Could not generate with those arguments");

		} catch (Exception e) {
			System.out.println("error");
			e.printStackTrace();
		}
	}

	public static void generateModel(PDStore store, String modelName,
			String sourceRoot, String packageName) {
		Debug.println("Generating DAL classes for model \"" + modelName + "\"",
				"PDGen");
		GUID modelId = store.getId(modelName);
		if (modelId == null) {
			throw new PDStoreException("Cannot find a model with name \""
					+ modelName + "\" in the store.");
		}

		PDModel model = PDModel.load(store, modelId);
		generateModel(store, model, sourceRoot, packageName);
	}

	public static void generateModel(PDStore store, PDModel model,
			String sourceRoot, String packageName) {
		try {
			Class.forName("pdstore.dal.PDType");
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
		for (PDType type1 : model.getTypes()) {
			// generate only classes for types with names
			String typeName = type1.getName();
			if (typeName == null) {
				Debug.println("Skipping type \"" + type1.getId()
						+ "\" because it does not have a name.");
				continue;
			}
			if (type1.getIsPrimitive() != null && type1.getIsPrimitive()) {
				Debug.println("Skipping type \"" + typeName
						+ "\" because it is primitive.");
				continue;
			}
			String className = "PD" + makeCamelCase(typeName);
			PDGen g = new PDGen(type1, className, packageName, sourceRoot);
			g.generate(type1);
		}
		Debug.println("Finished DAL generation.", "PDGen");
	}

	public static void generateForGraph(PDStore store, PDGraph graph,
			String packageName) {
		for (PDNode node : graph.getNodes()) {
			Object shownInstance = node.getShownInstance();
			GUID typeID = store.getType(shownInstance);

			if (!typeID.equals(PDStore.TYPE_TYPEID))
				continue;

			PDType type1 = PDType.load(store, (GUID) shownInstance);

			// generate only classes for types with names
			String typeName = type1.getName();
			if (typeName == null) {
				Debug.println("Skipping type \"" + type1.getId()
						+ "\" because it does not have a name.");
				continue;
			}
			if (type1.getIsPrimitive() != null && type1.getIsPrimitive()) {
				Debug.println("Skipping type \"" + typeName
						+ "\" because it is primitive.");
				continue;
			}

			String sourceRoot = System.getProperty("user.dir") + "/src";
			String className = "PD" + makeCamelCase(typeName);

			PDGen g = new PDGen(type1, className, packageName, sourceRoot);
			g.generate(type1);
		}
		Debug.println("Finished DAL generation.");
	}

	public File createfile(String fileName) {
		File file = null;
		// TODO is this necessary?
		if (System.getProperty("os.name").contains("Windows")) {
			file = new File(
					new File(sourceRoot).getAbsolutePath()
							+ "\\"
							+ new File(packageName.replace(".", "\\\\") + "\\")
									.getPath() + "\\" + fileName + ".java");
		} else {
			file = new File(new File(sourceRoot).getAbsolutePath() + "/"
					+ new File(packageName.replace(".", "//") + "/").getPath()
					+ "/" + fileName + ".java");

		}
		// Create folders if they do not exist
		File path = new File(file.getParent());
		if (!path.exists()) {
			path.mkdirs();
		}
		return file;
	}

	public PDGen(PDType type1, String className, String packageName,
			String sourceRoot) {
		this.store = type1.getStore();
		this.typeName = type1.getName();
		this.className = className;
		this.packageName = packageName;
		this.sourceRoot = sourceRoot;
	}

	PrintWriter initializeWriter(String Name) {
		File file;
		PrintWriter writer;
		file = createfile(Name);
		try {
			writer = new PrintWriter(new BufferedWriter(new FileWriter(file)));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return writer;
	}

	public void generateInterfaceBoilerPlate(PrintWriter InterfaceWriter) {
		Debug.println("Generating Interface " + className + "I"
				+ " for type \"" + typeName + "\"... ", "PDGen");
		InterfaceWriter.println("package " + packageName + ";");
		InterfaceWriter.println();
		InterfaceWriter.println("import java.util.Collection;");
		InterfaceWriter.println("import pdstore.GUID;");
		InterfaceWriter.println("import pdstore.dal.*;");
		InterfaceWriter.println();
		InterfaceWriter.println("/**");
		InterfaceWriter
				.println(" *Interface for the Data access class of type \""
						+ typeName + "\" in memory.");
		InterfaceWriter.println(" * @author PDGen");
		InterfaceWriter.println(" */");
		InterfaceWriter.println("public interface " + className + "I" + "{");
		InterfaceWriter.println();

	}

	public void generate(PDType type1) {
		Debug.println("Generating DAL class " + className + " for type \""
				+ typeName + "\"... ", "PDGen");
		PrintWriter writer;
		// Check for supertypes
		Collection<PDType> supertypes = type1.getSupertypes();

		// Interface
		writer = initializeWriter(className + "I");
		generateInterfaceBoilerPlate(writer);
		generateGettersSettersAndRemovers(true, writer, type1);
		writer.println("}");
		writer.close();
		// Class
		writer = initializeWriter(className);
		generateClassBoilerPlate(writer, type1, supertypes);
		generateGettersSettersAndRemovers(false, writer, type1);
		for (PDType t : supertypes)
			// Adding methods from supertype
			generateGettersSettersAndRemovers(false, writer, t);

		Debug.println("Generating the getters, setters and removers... ",
				"PDGen");
		for (Object extraCode : store.getInstances(type1.getId(),
				EXTRA_DAL_CODE_ROLEID)) {
			Debug.println("Adding user-defined extra code... ");
			writer.print(extraCode);
		}
		writer.println("}");
		writer.close();
	}

	/**
	 * generates either a setR() or an addR() method, getter and remover methods
	 * for each named accessible role R
	 * 
	 * @param type1
	 * 
	 * @throws Exception
	 */
	void generateGettersSettersAndRemovers(boolean isSignatureOnly,
			PrintWriter writer, PDType type1) {
		for (PDRole role1 : type1.getOwnedRoles()) {
			PDRole role2 = role1.getPartner();

			// generate code only for named roles to named types
			if (role2.getName() == null || role2.getOwner().getName() == null)
				continue;

			generateGettersSettersAndRemovers(role2, isSignatureOnly, writer);
		}
	}

	void generateClassBoilerPlate(PrintWriter writer, PDType type1,
			Collection<PDType> supertypes) {
		/* generating package and imports */
		// TODO:Should I import the interface even if in the same package ?
		writer.println("package " + packageName + ";");
		writer.println();
		writer.println("import java.util.*;");
		if (!packageName.equals("pdstore"))
			writer.println("import pdstore.*;");
		writer.println("import pdstore.dal.*;");
		writer.println();
		writer.println("/**");
		writer.println(" * Data access class to represent instances of type \""
				+ typeName + "\" in memory.");
		writer.println(" * Note that this class needs to be registered with PDCache by calling:");
		writer.println(" *    Class.forName(\"" + packageName + "." + className
				+ "\");");
		writer.println(" * @author PDGen");
		writer.println(" */");
		String declaration = "public class " + className
				+ " implements PDInstance";
		declaration = declaration + "," + className + "I";
		for (PDType t : supertypes)
			declaration = declaration + "," + "PD" + makeCamelCase(t.getName())
					+ "I";
		declaration = declaration + "{";
		writer.println(declaration);
		writer.println();
		writer.println("\tpublic static final GUID typeId = new GUID(\""
				+ type1.getId() + "\"); ");
		writer.println();

		/* Generating the role constants */
		for (PDRole role1 : type1.getOwnedRoles()) {
			PDRole role2 = role1.getPartner();

			Debug.println(
					"Generating the constant for role \"" + role2.getName()
							+ "\"...", "PDGen");

			// generate code only for named roles
			if (role2.getName() == null)
				continue;

			String constName = "role" + makeCamelCase(role2.getName()) + "Id";
			writer.println("\tpublic static final GUID " + constName
					+ " = new GUID(\"" + role2.getId() + "\");");
		}
		/* Generating the role constants for supertype */
		for (PDType t : supertypes)
			for (PDRole role1 : t.getOwnedRoles()) {
				PDRole role2 = role1.getPartner();

				Debug.println(
						"Generating the constant for role \"" + role2.getName()
								+ "\"...", "PDGen");

				// generate code only for named roles
				if (role2.getName() == null)
					continue;

				String constName = "role" + makeCamelCase(role2.getName())
						+ "Id";
				writer.println("\tpublic static final GUID " + constName
						+ " = new GUID(\"" + role2.getId() + "\");");
			}
		writer.println();

		Debug.println("Generating the constant methods... ", "PDGen");

		/* generate the static initializer that registers the wrapper */
		writer.println("	static {");
		writer.println("		register();");
		writer.println("	}");
		writer.println();
		writer.println("	/**");
		writer.println("	 * Registers this DAL class with the PDStore DAL layer.");
		writer.println("	 */");
		writer.println("	public static void register() {");
		writer.println("		PDStore.addDataClass(typeId, " + className
				+ ".class);");
		writer.println("	}");
		writer.println();
		writer.println("	private PDStore store;");
		writer.println("	private GUID id;");
		writer.println();
		writer.println("	public String toString() {");
		writer.println("		String name = getName();");
		writer.println("		if(name!=null)");
		writer.println("			return \"" + className + ":\" + name;");
		writer.println("		else");
		writer.println("			return \"" + className + ":\" + id;");
		writer.println("	}");
		writer.println();
		/* generate constructors */
		writer.println("	/**");
		writer.println("	 * Creates an " + className
				+ " object representing a PDStore instance of type " + typeName
				+ ".");
		writer.println("	 * @param store the store the instance should be in");
		writer.println("	 */");
		writer.println("	public " + className + "(PDStore store) {");
		writer.println("		this(store, new GUID());");
		writer.println("	}");
		writer.println();
		writer.println("	/**");
		writer.println("	 * Creates an " + className
				+ " object representing the instance with the given ID. ");
		writer.println("	 * The ID must be of an instance of type " + typeName
				+ ".");
		writer.println("	 * @param store the store the instance should be in");
		writer.println("	 * @param id GUID of the instance");
		writer.println("	 */");
		writer.println("	public " + className + "(PDStore store, GUID id) {");
		writer.println("		this.store = store;");
		writer.println("		this.id = id;");
		writer.println("		// set the has-type link for this instance");
		writer.println("		store.setType(id, typeId);");
		writer.println("	}");
		writer.println();
		writer.println("	/**");
		writer.println("	 * Loads an object for the instance of PDStore type "
				+ typeName + " with the given ID.");
		writer.println("	 * If an object for the instance is already available in "
				+ "the given PDStore object, it is returned.");
		writer.println("	 * @param store store to load the instance into");
		writer.println("	 * @param id GUID of the instance");
		writer.println("	 */");
		writer.println("	public static " + className
				+ " load(PDStore store, GUID id) {");
		writer.println("		PDInstance instance = store.load(typeId, id);");
		writer.println("		return (" + className + ")instance;");
		writer.println("	}");
		writer.println();
		writer.println("	/**");
		writer.println("	 * Gets the PDStore this object is stored in.");
		writer.println("	 */");
		writer.println("	public PDStore getStore() {");
		writer.println("		return store;");
		writer.println("	}");
		writer.println();
		writer.println("	/**");
		writer.println("	 * Gets the GUID of the instance represented by this object.");
		writer.println("	 */");
		writer.println("	public GUID getId() {");
		writer.println("		return id;");
		writer.println("	}");
		writer.println();
		writer.println("	/**");
		writer.println("	 * Gets the GUID of the type of the instance represented by this object.");
		writer.println("	 * This method isn't static so that it can be part of the PDInstance interface.");
		writer.println("	 */");
		writer.println("	public GUID getTypeId() {");
		writer.println("		return typeId;");
		writer.println("	}");
		writer.println();
		writer.println("	/**");
		writer.println("Get pdWorkingCopy */");
		writer.println("	public PDWorkingCopy getPDWorkingCopy() {");
		writer.println("		// TODO Auto-generated method stub");
		writer.println("return null;");
		writer.println("	}");
		writer.println();
		writer.println("	/**");
		writer.println("	 * Gets a textual label for this instance, for use in UIs.");
		writer.println("	 * @return a textual label for the instance");
		writer.println("	 */");
		writer.println("	public String getLabel() {");
		writer.println("		return store.getLabel(id);");
		writer.println("	}");
		writer.println();
		writer.println("	/**");
		writer.println("	 * Gets the name of this instance.");
		writer.println("	 * In PDStore every instance can be given a name.");
		writer.println("	 * @return name the instance name");
		writer.println("	 */");
		writer.println("	public String getName() {");
		writer.println("		return store.getName(id);");
		writer.println("	}");
		writer.println();
		writer.println("	/**");
		writer.println("	 * Sets the name of this instance.");
		writer.println("	 * In PDStore every instance can be given a name.");
		writer.println("	 * If the instance already has a name, the name will be overwritten.");
		writer.println("	 * If the given name is null, an existing name will be removed.");
		writer.println("	 * @return name the new instance name");
		writer.println("	 */");
		writer.println("	public void setName(String name) {");
		writer.println("		store.setName(id, name);");
		writer.println("	}");
		writer.println();
		writer.println("	/**");
		writer.println("	 * Removes the name of this instance.");
		writer.println("	 * In PDStore every instance can be given a name.");
		writer.println("	 * If the instance does not have a name, nothing happens.");
		writer.println("	 */");
		writer.println("	public void removeName() {");
		writer.println("		store.removeName(id);");
		writer.println("	}");
		writer.println();
		writer.println("	/**");
		writer.println("	 * Gets the icon of this instance.");
		writer.println("	 * In PDStore every instance can be given an icon.");
		writer.println("	 * @return icon the instance icon");
		writer.println("	 */");
		writer.println("	public Blob getIcon() {");
		writer.println("		return store.getIcon(id);");
		writer.println("	}");
		writer.println();
		writer.println("	/**");
		writer.println("	 * Sets the icon of this instance.");
		writer.println("	 * In PDStore every instance can be given an icon.");
		writer.println("	 * If the instance already has an icon, the icon will be overwritten.");
		writer.println("	 * If the given icon is null, an existing icon will be removed.");
		writer.println("	 * @return icon the new instance icon");
		writer.println("	 */");
		writer.println("	public void setIcon(Blob icon) {");
		writer.println("		store.setIcon(id, icon);");
		writer.println("	}");
		writer.println();
		writer.println("	/**");
		writer.println("	 * Removes the icon of this instance.");
		writer.println("	 * In PDStore every instance can be given an icon.");
		writer.println("	 * If the instance does not have an icon, nothing happens.");
		writer.println("	 */");
		writer.println("	public void removeIcon() {");
		writer.println("		store.removeIcon(id);");
		writer.println("	}");
	}

	void generateGettersSettersAndRemovers(PDRole role2,
			boolean isSignatureOnly, PrintWriter writer) {
		Debug.println("Generating getters, setters and removers for role \""
				+ role2.getName() + "\"...", "PDGen");
		String roleName = role2.getName();
		String camelName = PDGen.makeFirstSmall(PDGen.makeCamelCase(roleName));
		String upperCamelName = PDGen.makeFirstBig(camelName);
		PDType type2 = role2.getOwner();

		// determine the kind of object to use as parameter in a getter/setter
		boolean useGeneratedClass =
		// use generated class if the type is not ANY and primitive or at least
		// not known to be primitive...
		!type2.getId().equals(PDStore.ANY_TYPEID)
				&& ((type2.getIsPrimitive() != null && !type2.getIsPrimitive())
						|| type2.getIsPrimitive() == null
				// ...or if the type is Object
				|| type2.getId().equals(PDStore.OBJECT_TYPEID));

		// determine the corresponding java type to use for the parameter
		String javaTypeName = null;
		Class<?> javaValueType = GUID.class;

		if (useGeneratedClass) {
			Class<?> registeredClass = DALClassRegister.getDataClass(type2
					.getId());
			if (registeredClass != null) {
				// if a DAL class was already registered, use its name
				javaTypeName = registeredClass.getSimpleName();
			} else if (type2.getId().equals(PDStore.OBJECT_TYPEID)) {
				// if the relation is to Object, then use PDInstance
				javaTypeName = "PDInstance";
			} else {
				// if no class was registered, use the default name
				// for generated access classes
				javaTypeName = "PD" + PDGen.makeCamelCase(type2.getName());
			}
		} else {
			// if it is a primitive non-Object type (e.g. String),
			// then use corresponding Java primitive type
			javaValueType = PDGen.getJavaValueType(type2);
			javaTypeName = javaValueType.getSimpleName();
		}

		// Getters
		writer.println("	/**");
		writer.println("	 * Returns the instance connected to this instance through the role "
				+ roleName + ".");
		writer.println("	 * @return the connected instance");
		writer.println("	 */");
		writer.print("	public " + javaTypeName + " get" + upperCamelName + "()");
		if (isSignatureOnly) {
			writer.println(";");
		} else {
			writer.println("{");
			if (useGeneratedClass) {
				writer.println("		GUID instanceId = (GUID) store.getInstance(this.id, role"
						+ upperCamelName + "Id);");
				/*
				 * For PDStore type Object, we don't know the concrete type of
				 * the returned instance yet (Object is polymorphic), so we have
				 * to use a different version of load() that looks the type up
				 * dynamically.
				 */
				if (type2.getId().equals(PDStore.OBJECT_TYPEID)) {
					writer.println("	 	return (" + javaTypeName
							+ ") store.load(instanceId);");
				} else
					writer.println("	 	return (" + javaTypeName
							+ ") store.load(" + javaTypeName
							+ ".typeId, instanceId);");
			} else {
				writer.println("	 	return (" + javaTypeName
						+ ")store.getInstance(this.id, role" + upperCamelName
						+ "Id);");
			}
			writer.println("	}");
		}
		writer.println();
		writer.println("	/**");
		writer.println("	 * Returns the instance(s) connected to this instance through the role "
				+ roleName + ".");
		writer.println("	 * @return the connected instance(s)");
		writer.println("	 */");
		writer.print("	 public Collection<" + javaTypeName + "> get"
				+ upperCamelName + "s()");
		if (isSignatureOnly) {
			writer.println(";");
		} else {
			writer.println("{");
			if (useGeneratedClass) {
				/*
				 * For PDStore type Object, we don't know the concrete type of
				 * the returned instance yet (Object is polymorphic), so we have
				 * to use a different version of load() that looks the type up
				 * dynamically.
				 */
				if (type2.getId().equals(PDStore.OBJECT_TYPEID)) {
					writer.println("		return (Collection<"
							+ javaTypeName
							+ ">)(Object)store.getAndLoadInstances(this.id, role"
							+ upperCamelName + "Id);");
				} else
					writer.println("		return (Collection<"
							+ javaTypeName
							+ ">)(Object)store.getAndLoadInstances(this.id, role"
							+ upperCamelName + "Id, " + javaTypeName
							+ ".typeId);");
			} else {
				writer.println("		return (Collection<" + javaTypeName
						+ ">)(Object)store.getInstances(this.id, role"
						+ upperCamelName + "Id);");
			}

			writer.println("	 }");
		}
		writer.println();
		writer.println("   /**");
		writer.println("	 * Connects this instance to the given instance using role \""
				+ roleName + "\".");
		writer.println("	 * If the given instance is null, nothing happens.");
		writer.println("	 * @param " + camelName + " the instance to connect");
		writer.println("	 */");
		writer.print("	public void add" + upperCamelName + "("
				+ javaValueType.getSimpleName() + " " + camelName + ")");
		if (isSignatureOnly) {
			writer.println(";");
		} else {
			writer.println("{");
			writer.println("		if (" + camelName + " != null) {");
			// since we are using addLink it shouldn't matter whether it is an
			// add
			// or a set (TODO ???)
			writer.println("			store.addLink(this.id, role" + upperCamelName
					+ "Id, " + camelName + ");");
			writer.println("		}");
			writer.println("	}");
		}

		/*
		 * If no DAL class is used for the owner type of this role, then offer
		 * power version of add() for the primitive type associated with the
		 * owner type of the role. This is to avoid a Java type system conflict
		 * between the two add..s(),one with the primitive and one with a DAL
		 * type (see below).
		 */
		if (!useGeneratedClass) {
			writer.println("	/**");
			writer.println("	 * Connects this instance to the given instances using role \""
					+ roleName + "\".");
			writer.println("	 * If the given collection of instances is null, nothing happens.");
			writer.println("	 * @param " + camelName
					+ " the Collection of instances to connect");
			writer.println("	 */");
			writer.print("	public void add" + upperCamelName + "s(Collection<"
					+ javaValueType.getSimpleName() + "> " + camelName + "s)");
			if (isSignatureOnly) {
				writer.println(";");
			} else {
				writer.println("{");
				writer.println("		if (" + camelName + "s == null)");
				writer.println("			return;");
				writer.println("		for (" + javaValueType.getSimpleName()
						+ " instance : " + camelName + "s)");
				writer.println("			add" + upperCamelName + "(instance);");
				writer.println("	}");
			}
		}

		// If a DAL class is used for the owner type of this role,
		// then offer to use it in another version of add().
		if (useGeneratedClass) {
			writer.println("	/**");
			writer.println("	 * Connects this instance to the given instance using role \""
					+ roleName + "\".");
			writer.println("	 * If the given instance is null, nothing happens.");
			writer.println("	 * @param " + camelName
					+ " the instance to connect");
			writer.println("	 */");
			writer.print("	public void add" + upperCamelName + "("
					+ javaTypeName + " " + camelName + ")");
			if (isSignatureOnly) {
				writer.println(";");
			} else {
				writer.println("{");
				writer.println("		if (" + camelName + " != null)");
				writer.println("			add" + upperCamelName + "(" + camelName
						+ ".getId());");
				writer.println("	}");
			}
			writer.println();
			writer.println("	/**");
			writer.println("	 * Connects this instance to the given instance using role \""
					+ roleName + "\".");
			writer.println("	 * If the given collection of instances is null, nothing happens.");
			writer.println("	 * @param " + camelName
					+ " the Collection of instances to connect");
			writer.println("	 */");
			writer.print("	public void add" + upperCamelName + "s(Collection<"
					+ javaTypeName + "> " + camelName + "s)");
			if (isSignatureOnly) {
				writer.println(";");
			} else {
				writer.println("{");
				writer.println("		if (" + camelName + "s == null)");
				writer.println("			return;");
				writer.println("		for (" + javaTypeName + " instance : "
						+ camelName + "s)");
				writer.println("			add" + upperCamelName + "(instance);");
				writer.println("	}");
			}
		}
		writer.println();

		// REMOVER METHOD GENERATION
		writer.println("	/**");
		writer.println("	 * Removes the link from this instance through role \""
				+ roleName + "\".");
		writer.println("	 */");
		writer.print("	public void remove" + upperCamelName + "()");
		if (isSignatureOnly) {
			writer.println(";");
		} else {
			writer.println("{");
			writer.println("		store.removeLink(this.id, role" + upperCamelName
					+ "Id,");
			writer.println("			store.getInstance(this.id, role"
					+ upperCamelName + "Id));");
			writer.println("	}");
		}
		writer.println();
		writer.println("	/**");
		writer.println("	 * Removes the link from this instance through role \""
				+ roleName + "\" to the given instance, if the link exists.");
		writer.println("	 * If there is no such link, nothing happens.");
		writer.println("	 * If the given instance is null, nothing happens.");
		writer.println("	 */");
		writer.print("	public void remove" + upperCamelName + "("
				+ javaTypeName + " " + camelName + ")");
		if (isSignatureOnly) {
			writer.println(";");
		} else {
			writer.println("{");
			writer.println("		if (" + camelName + " == null)");
			writer.println("			return;");
			if (useGeneratedClass) {
				writer.println("		store.removeLink(this.id, role"
						+ upperCamelName + "Id, " + camelName + ".getId());");
			} else {
				writer.println("		store.removeLink(this.id, role"
						+ upperCamelName + "Id, " + camelName + ");");
			}
			writer.println("	}");
		}
		writer.println();
		// If a DAL class is used for the owner type of this role,
		// then offer to use it in a power version of remove().
		if (useGeneratedClass) {
			writer.println("	/**");
			writer.println("	 * Removes the links from this instance through role \""
					+ roleName + "\" to the instances");
			writer.println("	 * in the given Collection, if the links exist.");
			writer.println("	 * If there are no such links or the collection argument is null, nothing happens.");
			writer.println("	 */");
			writer.print("	public void remove" + upperCamelName
					+ "s(Collection<" + javaTypeName + "> " + camelName + "s)");
			if (isSignatureOnly) {
				writer.println(";");
			} else {
				writer.println("{");
				writer.println("		if (" + camelName + "s == null)");
				writer.println("			return;");
				writer.println("		for (" + javaTypeName + " instance : "
						+ camelName + "s)");
				if (useGeneratedClass) {
					writer.println("			store.removeLink(this.id, role"
							+ upperCamelName + "Id, instance.getId());");
				} else {
					writer.println("			store.removeLink(this.id, role"
							+ upperCamelName + "Id, instance);");
				}
				writer.println("	}");
			}
			writer.println();
		}

		// SETTER METHOD GENERATION
		writer.println("	/**");
		writer.println("	 * Connects this instance to the given instance using role \""
				+ roleName + "\".");
		writer.println("	 * If there is already an instance connected to this instance through role \""
				+ roleName + "\", the link will be overwritten.");
		writer.println("	 * If the given instance is null, an existing link is removed.");
		writer.println("	 * @param " + camelName + " the instance to connect");
		writer.println("	 */");
		writer.print("	public void set" + upperCamelName + "("
				+ javaValueType.getSimpleName() + " " + camelName + ") ");
		if (isSignatureOnly) {
			writer.println(";");
		} else {
			writer.println("{");
			writer.println("		store.setLink(this.id,  role" + upperCamelName
					+ "Id, " + camelName + ");");
			writer.println("	}");
		}
		if (useGeneratedClass) { // overload it
			writer.println("	/**");
			writer.println("	 * Connects this instance to the given instance using role \""
					+ roleName + "\".");
			writer.println("	 * If there is already an instance connected to this instance through role \""
					+ roleName + "\", the link will be overwritten.");
			writer.println("	 * If the given instance is null, an existing link is removed.");
			writer.println("	 * @param " + camelName
					+ " the instance to connect");
			writer.println("	 */");
			writer.println("	public void set" + upperCamelName + "("
					+ javaTypeName + " " + camelName + ") ");
			if (isSignatureOnly) {
				writer.print(";");
			} else {
				writer.print("{");
				writer.println("		set" + upperCamelName + "(" + camelName
						+ ".getId());");
				writer.println("	}");
			}
		}
	}

	/**
	 * This method returns the string with its first letter in lower case.
	 * 
	 * @param s
	 */
	public static String makeFirstSmall(String s) {
		return s.substring(0, 1).toLowerCase() + s.substring(1);
	}

	/**
	 * This method returns the string supplied with its front letter in upper
	 * case.
	 * 
	 * @param s
	 */
	public static String makeFirstBig(String s) {
		return s.substring(0, 1).toUpperCase() + s.substring(1);
	}

	/**
	 * This method returns the string as parameter with its first letter in
	 * upper case. If there is more than one word in the string supplied, they
	 * would we be joined together into one "word" without any space between
	 * them. The first letter of each word would be in upper case in the joined
	 * "word".
	 * 
	 * @param name
	 * @return result
	 */
	public static String makeCamelCase(String name) {
		String result = "";
		name = name.trim();
		name = name.replace('-', '_');
		for (String part : name.split(" ")) {
			result += makeFirstBig(part);
		}
		return result;
	}

	/**
	 * Converts a name without whitespace to a valid Java identifier by checking
	 * for Java keyword collisions and appending a postfix if necessary.
	 * 
	 * @param name
	 *            name without whitespace
	 * @return a valid Java identifier
	 */
	public static String makeIdentifier(String name) {
		if (name.equals("abstract") || name.equals("assert")
				|| name.equals("boolean") || name.equals("break")
				|| name.equals("byte") || name.equals("case")
				|| name.equals("catch") || name.equals("char")
				|| name.equals("class") || name.equals("const")
				|| name.equals("default") || name.equals("do")
				|| name.equals("double") || name.equals("else")
				|| name.equals("enum") || name.equals("extends")
				|| name.equals("final") || name.equals("finally")
				|| name.equals("float") || name.equals("for")
				|| name.equals("goto") || name.equals("if")
				|| name.equals("implements") || name.equals("import")
				|| name.equals("instanceof") || name.equals("int")
				|| name.equals("interface") || name.equals("long")
				|| name.equals("native") || name.equals("new")
				|| name.equals("package") || name.equals("private")
				|| name.equals("protected") || name.equals("public")
				|| name.equals("return") || name.equals("short")
				|| name.equals("static") || name.equals("strictfp")
				|| name.equals("super") || name.equals("switch")
				|| name.equals("synchronized") || name.equals("this")
				|| name.equals("throw") || name.equals("throws")
				|| name.equals("transient") || name.equals("try")
				|| name.equals("void") || name.equals("volatile")
				|| name.equals("while"))
			name += "Value";
		return name;
	}

	/**
	 * This method returns the java value type corresponding to the SQL type of
	 * the PDType object supplied as a parameter.
	 * 
	 * @param type
	 * @return
	 * @throws PDStoreException
	 */
	public static Class<?> getJavaValueType(PDType type)
			throws PDStoreException {
		if (type.getId().equals(PDStore.ANY_TYPEID)) {
			return Object.class;
		} else if (type.getId().equals(PDStore.GUID_TYPEID)) {
			return GUID.class;
		} else if (type.getId().equals(PDStore.INTEGER_TYPEID)) {
			return Long.class;
		} else if (type.getId().equals(PDStore.BOOLEAN_TYPEID)) {
			return Boolean.class;
		} else if (type.getId().equals(PDStore.TIMESTAMP_TYPEID)) {
			return java.util.Date.class;
		} else if (type.getId().equals(PDStore.CHAR_TYPEID)) {
			return Character.class;
		} else if (type.getId().equals(PDStore.STRING_TYPEID)) {
			return String.class;
		} else if (type.getId().equals(PDStore.BLOB_TYPEID)) {
			return byte[].class;
		} else if (type.getId().equals(PDStore.DOUBLE_PRECISION_TYPEID)) {
			return Double.class;
		}
		throw new PDStoreException("Unknown type: " + type.getId());
	}
}
