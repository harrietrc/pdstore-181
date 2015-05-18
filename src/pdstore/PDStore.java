package pdstore;

import java.lang.ref.SoftReference;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import nz.ac.auckland.se.genoupe.tools.Debug;
import pdstore.changeindex.GenericIndexStore;
import pdstore.changeindex.IndexOnlyStore;
import pdstore.changelog.ChangeLogStore;
import pdstore.changelog.PrimitiveType;
import pdstore.concurrent.ConcurrentStore;
import pdstore.concurrent.DetachedListenerDispatcher;
import pdstore.dal.PDInstance;
import pdstore.dal.PDModel;
import pdstore.dal.PDRole;
import pdstore.dal.PDType;
import pdstore.generic.GenericPDStore;
import pdstore.generic.PDCoreI;
import pdstore.generic.PDStoreI;
import pdstore.notify.PDListener;
import pdstore.notify.PDListenerAdapter;
import pdstore.sparql.Query;
import pdstore.sparql.Query.OptimimzerType;
import pdstore.sparql.Query.*;
import pdstore.sparql.Variable;

/**
 * PDStore is the convenience class that is mostly used to set up a PDStore
 * connection. Following the principle of separation of concerns, the
 * functionality of PDStore is implemented in a series of Classes that act
 * together in a tier architecture. The clients only connect PDStore, but
 * benefit from the underlying classes:
 * 
 * The main description of the functional features can be found in the base
 * interfaces PDCoreI and PDStoreI. The description of the non-functional
 * properties can be found in the following classes. - The class ConcurrentStore
 * describes the concurrency properties of PDStore. - The class LogAndIndexStore
 * describes the properties of efficient access. The PDStore class can be used
 * for local file-based stores, in which cases it serves as an embedded
 * database, or for remote connections, in which case it serves as an improved
 * proxy object and primarily removes annoying Java Remote Exceptions.
 * 
 * @author Christof, Gerald
 * 
 */
public class PDStore extends GenericPDStore<GUID, Object, GUID> {

	public static final String PD_STORE_GLOBAL_DEBUG = "PDStore";

	static {
		// Debug.addDebugTopic(PD_STORE_GLOBAL_DEBUG);
	}

	/**
	 * Type for any type, i.e. this type does not constrain the type an instance
	 * can have. The type of the instance may be primitive or complex. This is
	 * represented by the Java type Object, using the Java wrapper classes for
	 * primitive types.
	 */
	public final static GUID ANY_TYPEID = new GUID(
			"e03d39e09c9311e1a00900224300a31a");

	/** GUIDs for primitive types **/

	/**
	 * Type for binary data arrays of arbitrary length, represented by Java type
	 * pdstore.Blob.
	 */
	public final static GUID BLOB_TYPEID = new GUID(
			"4f8a986c4062db11afc0b95b08f50e2f");

	/**
	 * Type for boolean values true or false, represented by Java type Boolean.
	 */
	public final static GUID BOOLEAN_TYPEID = new GUID(
			"4d8a986c4062db11afc0b95b08f50e2f");
	public final static GUID CHAR_TYPEID = new GUID(
			"508a986c4062db11afc0b95b08f50e2f");

	/**
	 * Type for floating point values, represented by Java type Double.
	 */
	public final static GUID DOUBLE_PRECISION_TYPEID = new GUID(
			"4c8a986c4062db11afc0b95b08f50e2f");

	/**
	 * Type for GUIDs, represented by Java type pdstore.GUID.
	 */
	public final static GUID GUID_TYPEID = new GUID(
			"538a986c4062db11afc0b95b08f50e2f");

	/**
	 * Type for integer values, represented by Java type Long.
	 */
	public final static GUID INTEGER_TYPEID = new GUID(
			"4b8a986c4062db11afc0b95b08f50e2f");

	/**
	 * Type for strings, represented by Java type String.
	 */
	public final static GUID STRING_TYPEID = new GUID(
			"4a8a986c4062db11afc0b95b08f50e2f");

	/**
	 * Type for timestamps, represented by Java type java.sql.Timestamp, which
	 * extends java.util.Date.
	 */
	public final static GUID TIMESTAMP_TYPEID = new GUID(
			"4e8a986c4062db11afc0b95b08f50e2f");

	// TODO is this supported?
	public final static GUID IMAGE_TYPEID = new GUID(
			"d19fffbbf28bdb118ab1d56a70f8a30f");

	/** GUIDs for complex types **/

	// TODO type Object should only be used for complex types with DAL classes,
	// right?
	public final static GUID OBJECT_TYPEID = new GUID(
			"70da26e0fc3711dfa87b842b2b9af4fd");

	/**
	 * The type of all PDStore types.
	 */
	public final static GUID TYPE_TYPEID = new GUID(
			"518a986c4062db11afc0b95b08f50e2f");

	/**
	 * The type of all PDStore roles.
	 */
	public final static GUID ROLE_TYPEID = new GUID(
			"528a986c4062db11afc0b95b08f50e2f");

	/**
	 * The type of all PDStore repositories. TODO explain what a repo is
	 */
	public final static GUID REPOSITORY_TYPEID = new GUID(
			"6d35b690232e11e1a16400235411d565");

	/**
	 * The type of all PDStore data models. A data model consists of a set of
	 * types with roles between them.
	 */
	public final static GUID MODEL_TYPEID = new GUID(
			"31c54c264e6fdd11a5dba737f860105f");

	/**
	 * The type of all branches in PDStore.
	 */
	public final static GUID BRANCH_TYPEID = new GUID(
			"5921e0a079b811dfb27f002170295281");

	/**
	 * The type of all transactions in PDStore.
	 */
	public final static GUID TRANSACTION_TYPEID = new GUID(
			"5921e0a179b811dfb27f002170295281");

	/**
	 * The type of reified changes in PDStore.
	 */
	public final static GUID CHANGE_TYPEID = new GUID(
			"005422218cf711e1926b842b2b9af4fd");

	/**
	 * The type of operations that can be applied to superparameters.
	 */
	public final static GUID OPERATION_TYPEID = new GUID(
			"dbe74a2f89cb11e197441cc1dec00ed3");

	/**
	 * The type of PDStore widgets that can draw an instance.
	 */
	public final static GUID WIDGET_TYPEID = new GUID(
			"005422208cf711e1926b842b2b9af4fd");

	/** GUIDs for roles **/
	public final static GUID OWNED_ROLE_ROLEID = new GUID(
			"648a986c4062db11afc0b95b08f50e2f");
	public final static GUID OWNER_TYPE_ROLEID = OWNED_ROLE_ROLEID.getPartner();
	public final static GUID HAS_SUPERTYPE_ROLEID = new GUID(
			"a093c5b1b6aa11e183df842b2b9af4fd");
	public final static GUID ICON_ROLEID = new GUID(
			"88bf14821704dc11b933e6037c01b10a");
	public final static GUID NAME_ROLEID = new GUID(
			"84bf14821704dc11b933e6037c01b10a");
	public final static GUID DESCRIPTION_ROLEID = new GUID(
			"ca5984600b7c11e19db300224300a31a");
	public final static GUID PARTNER_ROLEID = new GUID(
			"6d8a986c4062db11afc0b95b08f50e2f");
	public final static GUID MODELTYPE_ROLEID = new GUID(
			"54134d264e6fdd11a5dba737f860105f");
	public final static GUID ISPRIMITIVE_ROLEID = new GUID(
			"5d8a986c4062db11afc0b95b08f50e2f");
	public final static GUID PARENT_TRANSACTION_ROLEID = new GUID(
			"5921e0a279b811dfb27f002170295281");
	public final static GUID PARENTTRANSACTION_CHILDBRANCH_ROLEID = PARENT_TRANSACTION_ROLEID
			.getPartner();
	public final static GUID BRANCH_ROLEID = new GUID(
			"5921e0a479b811dfb27f002170295281");
	public final static GUID TRANSACTION_ROLEID = BRANCH_ROLEID.getPartner();
	public final static GUID MIN_MULT_ROLEID = new GUID(
			"678a986c4062db11afc0b95b08f50e2f");
	public final static GUID MAX_MULT_ROLEID = new GUID(
			"698a986c4062db11afc0b95b08f50e2f");
	public final static GUID ROLE_XRX_SELECTIVITY_ROLEID = new GUID(
			"b7d6dc40e02c11e1833c842b2b9af4fd");
	public final static GUID ROLE_IRX_SELECTIVITY_ROLEID = new GUID(
			"b7d70350e02c11e1833c842b2b9af4fd");
	public final static GUID ROLE_XRI_SELECTIVITY_ROLEID = new GUID(
			"33427bfe6f5311e2b2090026b982fa2f");
	public final static GUID FILTER_SELECTIVITY_LOOKUPID = new GUID(
			"6da3671106c711e2ad0d0026b982fa2f");
	public final static GUID EQUAL_SELECTIVITY_ROLEID = new GUID(
			"6da3671006c711e2ad0d0026b982fa2f");
	public final static GUID LESSTHAN_SELECTIVITY_ROLEID = new GUID(
			"6da3670f06c711e2ad0d0026b982fa2f");
	public final static GUID GREATERTHAN_SELECTIVITY_ROLEID = new GUID(
			"6da3670e06c711e2ad0d0026b982fa2f");
	public final static GUID HAS_CHANGETEMPLATEKIND_ROLEID = new GUID(
			"6ca3670e06c721e2ad0d0026b982fa2f");
	public final static GUID HAS_VALUE_ROLEID = new GUID(
			"3ca3670e06c721e2ad0d0026b952fc2f");
	public final static GUID REPOSITORYMODEL_ROLEID = new GUID(
			"d2911880a5cc11dfa9e40026bb06e946");
	public final static GUID HAS_TYPE_ROLEID = new GUID(
			"ee32adf0f68b11df860e1cc1dec00ed3");
	public final static GUID ISOLATIONLEVEL_ROLEID = new GUID(
			"878e3970134d11e1b3151cc1dec00ed3");
	public final static GUID DURABLE_TRANSACTION_ROLEID = new GUID(
			"878e396d134d11e1b3151cc1dec00ed3");

	public final static GUID CHANGE_TRANSACTION_ROLEID = new GUID(
			"0054221f8cf711e1926b842b2b9af4fd");
	public final static GUID CHANGE_CHANGETYPE_ROLEID = new GUID(
			"0054221e8cf711e1926b842b2b9af4fd");
	public final static GUID CHANGE_INSTANCE1_ROLEID = new GUID(
			"0054221d8cf711e1926b842b2b9af4fd");
	public final static GUID CHANGE_ROLE2_ROLEID = new GUID(
			"0054221c8cf711e1926b842b2b9af4fd");
	public final static GUID CHANGE_INSTANCE2_ROLEID = new GUID(
			"0054221b8cf711e1926b842b2b9af4fd");

	public final static GUID SPARQL_QUERY_TYPEID = new GUID(
			"43fdb564e1bb11e1bf76020054554e01");
	public final static GUID WHERE_TYPEID = new GUID(
			"b1492f61e5aa11e1af630026b982fa2f");
	public final static GUID FILTER_TYPEID = new GUID(
			"b1492f60e5aa11e1af630026b982fa2f");
	public final static GUID WHERE_TUPLE_ROLEID = new GUID(
			"43fdb560e1bb11e1bf76020054554e01");
	public final static GUID FILTER_ROLEID = new GUID(
			"43fdb565e1bb11e1bf76020054554e01");
	public final static GUID SUBQUERY_ROLEID = new GUID(
			"58d680c6e27111e1ac55020054554e01");
	public final static GUID OPTIONAL_ROLEID = new GUID(
			"b1492f5fe5aa11e1af630026b982fa2f");
	public final static GUID VARIABLE_ROLEID = new GUID(
			"a8b899c1e5bf11e1a40d0026b982fa2f");
	public final static GUID CONSTANT_ROLEID = new GUID(
			"79d44281e5c411e189790026b982fa2f");
	public final static GUID FILTER_LESSTHAN_TYPEID = new GUID(
			"6e3c57c1e5c211e1880c0026b982fa2f");
	public final static GUID FILTER_GREATERTHAN_TYPEID = new GUID(
			"6e3c57c0e5c211e1880c0026b982fa2f");
	public final static GUID FILTER_EQUAL_TYPEID = new GUID(
			"6e3c57bfe5c211e1880c0026b982fa2f");
	public final static GUID FILTER_NOTEQUAL_TYPEID = new GUID(
			"6e3c57bee5c211e1880c0026b982fa2f");
	public final static GUID FILTER_AND_TYPEID = new GUID(
			"6e3c57bde5c211e1880c0026b982fa2f");
	public final static GUID FILTER_OR_TYPEID = new GUID(
			"6e3c57bce5c211e1880c0026b982fa2f");
	public static GUID SPARQL_VARIABLE_TYPEID = new GUID(
			"eebcf713e60211e18c7a441ea1dc5b4f");
	public static GUID FILTER_AND_ROLEID1 = new GUID(
			"87d22ac0e60811e1b88d441ea1dc5b4f");
	public static GUID FILTER_AND_ROLEID2 = new GUID(
			"53fc663be65e11e18e59441ea1dc5b4f");
	public static GUID FILTER_OR_ROLEID1 = new GUID(
			"87d22ac1e60811e1b88d441ea1dc5b4f");
	public static GUID FILTER_OR_ROLEID2 = new GUID(
			"53fc663ce65e11e18e59441ea1dc5b4f");
	public final static GUID IS_NOT_IN_INDEX_ROLEID = new GUID(
			"730324c0154811e1b4f61cc1dec00ed3");
	public final static GUID IS_NOT_IN_LOG_ROLEID = new GUID(
			"73034bd1154811e1b4f61cc1dec00ed3");

	public final static GUID USES_ROLE_ROLEID = new GUID(
			"cdc181d1319311e1b10c1cc1dec00ed3").getFirst();

	public final static GUID PARAMETER_TYPE_ROLEID = new GUID(
			"dbe74a3089cb11e197441cc1dec00ed3");
	public final static GUID OPERATION_IMPLEMENTATION_ROLEID = new GUID(
			"dbe74a3189cb11e197441cc1dec00ed3");

	public final static GUID WIDGET_IMPLEMENTATION_ROLEID = new GUID(
			"0054221a8cf711e1926b842b2b9af4fd");
	public final static GUID VISUALIZED_BY_ROLEID = new GUID(
			"005422198cf711e1926b842b2b9af4fd");

	public final static GUID MODEL_PACKAGENAME_ROLEID = new GUID(
			"3ed1ed50c8b011e1a6cd00224300a31a");

	/** GUIDs for instances **/
	public final static GUID PDMETAMODEL_ID = new GUID(
			"55134d264e6fdd11a5dba737f860105f");

	/**
	 * GUID for the transaction that adds the metamodel. This is the same for
	 * all databases. By using getFirst() we ensure that the GUID is a durable
	 * transaction ID.
	 */
	public final static GUID METAMODEL_TRANSACTION = new GUID(
			"4c3397f01e4211e1b51b00188b6fa587").getFirst();

	/** Defaults for data file name **/
	public static final String DEFAULT_FILEPATH = "pddata/";
	public static final String DEFAULT_FILENAME = "pdstore";
	public static final String DEFAULT_FILELOCATION = DEFAULT_FILEPATH
			+ DEFAULT_FILENAME;

	private String fileName;
	private String filePath;

	/**
	 * Can be used to switch of concurrency and Persistence: Note: Data will not
	 * be saved at all if this is true!
	 * 
	 */
	public static boolean isNotConcurrentNotPersistent = false;

	public IndexOnlyStore<GUID, Object, GUID> indexOnlyStore;

	/**
	 * Can be used to switch off storing of Isolation level
	 * 
	 */
	public static boolean isStoringIsolationlevel = true;

	/**
	 * This constructor creates a PDStore just as a facade to use the
	 * convenience functions, does not do the initPDStore().
	 * 
	 * @param store
	 */
	public PDStore(PDStoreI<GUID, Object, GUID> store) {
		this.store = store;
	}

	public PDStore(String fileName, String filePath) throws PDStoreException {
		this.fileName = fileName;
		this.filePath = filePath;
		if (isNotConcurrentNotPersistent) {
			// Query.isUsingVariableRoles=false;
			// Query.currentOptimizerType = OptimimzerType.NoOpt;
			this.indexOnlyStore = new IndexOnlyStore<GUID, Object, GUID>();
			this.store = indexOnlyStore;

		} else
			this.store = new ConcurrentStore(filePath + fileName);
		initPDStore();
	}

	/**
	 * It is preferred that the single-string Constructor adds a filepath and in
	 * particular the default extension.
	 * 
	 * @param fileName
	 * @throws PDStoreException
	 */
	public PDStore(String fileName) throws PDStoreException {
		this(fileName, DEFAULT_FILEPATH);
	}

	public PDStore() {
	}

	/**
	 * A dummy constructor to avoid premature initialisation
	 * 
	 * @param c
	 */
	public PDStore(Class<?> c) {
		this.fileName = DEFAULT_FILENAME;
		this.filePath = DEFAULT_FILEPATH;
		this.store = new ConcurrentStore(this.filePath + this.fileName);
	}

	private void initPDStore() {
		// add the metamodel to the index
		// TODO should the metamodel have a normal begin-ID, instead of having
		// METAMODEL_TRANSACTION as begin-ID and durable ID?
		GUID transactionId = METAMODEL_TRANSACTION;
		Transaction<GUID, Object, GUID> metamodelTransaction = new Transaction<GUID, Object, GUID>(
				transactionId);
		metamodelTransaction.setDurableId(METAMODEL_TRANSACTION);
		metamodelTransaction.setPersistenceLevel(PersistenceLevel.INDEX_ONLY);
		begin(metamodelTransaction);
		addMetamodel(transactionId);
		commit(transactionId);

		// merge metamodel branch into default branch
		transactionId = GUID.newTransactionId(getRepository().getBranchID());
		Transaction<GUID, Object, GUID> transaction = new Transaction<GUID, Object, GUID>(
				transactionId);
		transaction.setPersistenceLevel(PersistenceLevel.INDEX_ONLY);
		begin(transaction);
		merge(transactionId, METAMODEL_TRANSACTION);
		commit(transactionId);

		// add the listener & interceptor dispatchers
		getDetachedListenerList().add(listenerDispatcher);
		getInterceptorList().add(interceptorDispatcher);
		immediateDispatcher.getMatchingTemplates().add(
				new PDChange<GUID, Object, GUID>(ChangeType.LINK_NOW_ADDED,
						null, null, null, null));
		getInterceptorList().add(immediateDispatcher);
		// soon there might be a READ changetype
		// readDispatcher.getMatchingTemplates()
		// .add(new PDChange<GUID, Object, GUID>(ChangeType.LINK_READ, null,
		// null, null, null));
		getInterceptorList().add(readDispatcher);

		// make sure there is a PDStore for debugging
		debugStore = this;

		// used for testRegisteredViewListener.
		PDChange<GUID, Object, GUID> changeTemplate = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, null, null,
				LanguageViewListener.LANGUAGE_ROLEID, null);
		PDListener<GUID, Object, GUID> listener = new LanguageViewListener();
		getViewDispatcher().addListener(listener, changeTemplate);
	}

	public final static GUID CompleteTestRole2 = new GUID(
			"0e1b1950e27a11e198fb020054554e01");
	public final static GUID Complete_TestInstance1 = new GUID(
			"0e1b8e80e27a11e198fb020054554e01");

	/**
	 * Connects to the PDStore server (pdstore.rmi.PDStoreServer) running on the
	 * computer with the given domain name / IP address.
	 * 
	 * @param host
	 *            the domain name or IP address of the PDStore server
	 * @return a PDStore instance for using the store
	 * @throws PDStoreException
	 *             Various things may go wrong here, e.g. because the host name
	 *             is wrong or the server is not running.
	 */
	public static pdstore.rmi.PDStore connectToServer(String host)
			throws PDStoreException {
		return new pdstore.rmi.PDStore(host);
	}

	/**
	 * Connects to the PDStore server (pdstore.rmi.PDStoreServer) running on the
	 * computer with the given domain name / IP address using the given RMI key.
	 * Usually the standard key is the right one, which is used by the other
	 * variant of connectToServer().
	 * 
	 * @param host
	 *            the domain name or IP address of the PDStore server
	 * @param rmiKey
	 *            the RMi key to use for the PDSTore server (see RMI
	 *            documentation)
	 * @return a PDStore instance for using the store
	 * @throws PDStoreException
	 *             Various things may go wrong here, e.g. because the host name
	 *             is wrong or the server is not running.
	 */
	public static pdstore.rmi.PDStore connectToServer(String host, String rmiKey)
			throws PDStoreException {
		return new pdstore.rmi.PDStore(host, rmiKey);
	}

	/**
	 * Gets all the instances of the given type that are stored in the database.
	 * 
	 * @param transactionId
	 *            transaction id on which to search
	 * @param typeid
	 *            ID of the type
	 * @return all stored instances of that type
	 */
	public Collection<Object> getAllInstancesOfType(GUID transaction, GUID type) {
		return getInstances(transaction, type, HAS_TYPE_ROLEID.getPartner());
	}

	/**
	 * Gets all the instances of the given type that are stored in the database.
	 * 
	 * @param typeid
	 *            ID of the type
	 * @return all stored instances of that type
	 */
	public Collection<Object> getAllInstancesOfType(GUID type) {
		return getInstances(getCurrentTransaction(), type,
				HAS_TYPE_ROLEID.getPartner());
	}

	/**
	 * Gets the GUID that is associated with the given name.
	 * 
	 * @param transactionId
	 *            transaction id on which to search
	 * @param name
	 *            the name
	 * @return the GUID associated with the name, or null if no GUID is
	 *         associated with that name
	 */
	public GUID getId(GUID transaction, String name) {
		return (GUID) getInstance(transaction, name,
				PDStore.NAME_ROLEID.getPartner());
	}

	/**
	 * Gets the GUID that is associated with the given name.
	 * 
	 * @param name
	 *            the name
	 * @return the GUID associated with the name, or null if no GUID is
	 *         associated with that name
	 */
	public GUID getId(String name) {
		return getId(getCurrentTransaction(), name);
	}

	/**
	 * Gets or creates the GUID that is associated with the given name. If no
	 * GUID exists with that name, a GUID is created and the name is set.
	 * 
	 * @param name
	 *            the name
	 * @return the GUID associated with the name,
	 * 
	 */
	public GUID getGUIDwithName(String name) {
		GUID id = getId(name);
		if (id != null)
			return id;
		id = new GUID();
		setName(id, name);
		return id;
	}

	/**
	 * Gets all the GUIDs that are associated with the given name.
	 * 
	 * @param transactionId
	 *            transaction id on which to search
	 * @param name
	 *            the name
	 * @return the GUID associated with the name
	 */
	public Collection<GUID> getIds(GUID transaction, String name) {
		Set<GUID> Ids = new HashSet<GUID>();
		for (Object result : getInstances(transaction, name,
				PDStore.NAME_ROLEID.getPartner()))
			Ids.add((GUID) result);
		return Ids;
	}

	/**
	 * Gets all the GUIDs that are associated with the given name.
	 * 
	 * @param name
	 *            the name
	 * @return the GUID associated with the name
	 */
	public Collection<GUID> getIds(String name) {
		return getIds(getCurrentTransaction(), name);
	}

	/**
	 * Gets the name associated with a GUID. In PDStore every instance can be
	 * given a name.
	 * 
	 * @param transactionId
	 *            transaction id on which to search
	 * @param id
	 *            GUID to get the name for
	 * @return the name associated with the GUID
	 */
	public String getName(GUID transaction, Object instance) {
		return (String) getInstance(transaction, instance, PDStore.NAME_ROLEID);
	}

	/**
	 * Gets the name associated with a GUID. In PDStore every instance can be
	 * given a name.
	 * 
	 * @param id
	 *            GUID to get the name for
	 * @return the name associated with the GUID
	 */
	public String getName(Object instance) {
		return getName(getCurrentTransaction(), instance);
	}

	/**
	 * Returns the name of the given instance if it has one, otherwise the
	 * String representation of the instance value.
	 * 
	 * @param transaction
	 * @param instance
	 * @return
	 */
	public String getNameOrValue(GUID transaction, Object instance) {
		String name = getName(transaction, instance);
		if (name != null)
			return name;

		return instance.toString();
	}

	/**
	 * Returns the name of the given instance if it has one, otherwise the
	 * String representation of the instance value.
	 * 
	 * @param instance
	 * @return
	 */
	public String getNameOrValue(Object instance) {
		return getNameOrValue(getCurrentTransaction(), instance);
	}

	/**
	 * Sets the name associated with a GUID. In PDStore every instance can be
	 * given a name. If the instance already has a name, the name will be
	 * overwritten. If the given name is null, an existing name will be removed.
	 * 
	 * @param id
	 *            GUID of the instance
	 * @param name
	 *            the new instance name
	 */
	public void setName(GUID transaction, Object instance, String name) {
		setLink(transaction, instance, PDStore.NAME_ROLEID, name);
	}

	/**
	 * Sets the name associated with a GUID. In PDStore every instance can be
	 * given a name. If the instance already has a name, the name will be
	 * overwritten. If the given name is null, an existing name will be removed.
	 * 
	 * @param id
	 *            GUID of the instance
	 * @param name
	 *            the new instance name
	 */
	public void setName(Object instance, String name) {
		setName(getCurrentTransaction(), instance, name);
	}

	/**
	 * Removes the name associated with a GUID. In PDStore every instance can be
	 * given a name. If the instance does not have a name, nothing happens.
	 * 
	 * @param id
	 *            GUID of the instance
	 */
	public void removeName(GUID transaction, Object instance) {
		String name = getName(transaction, instance);
		if (name != null)
			removeLink(transaction, instance, PDStore.NAME_ROLEID, name);
	}

	/**
	 * Removes the name associated with a GUID. In PDStore every instance can be
	 * given a name. If the instance does not have a name, nothing happens.
	 * 
	 * @param id
	 *            GUID of the instance
	 */
	public void removeName(Object instance) {
		removeName(getCurrentTransaction(), instance);
	}

	/**
	 * Gets the icon associated with a GUID. In PDStore every instance can be
	 * given an icon.
	 * 
	 * @param id
	 *            GUID to get the name for
	 * @return the icon associated with the GUID
	 */
	public Blob getIcon(GUID transaction, Object instance) {
		return (Blob) getInstance(transaction, instance, PDStore.ICON_ROLEID);
	}

	/**
	 * Gets the icon associated with a GUID. In PDStore every instance can be
	 * given an icon.
	 * 
	 * @param id
	 *            GUID to get the name for
	 * @return the icon associated with the GUID
	 */
	public Blob getIcon(Object instance) {
		return getIcon(getCurrentTransaction(), instance);
	}

	/**
	 * Sets the icon associated with a GUID. In PDStore every instance can be
	 * given an icon. If the instance already has an icon, the icon will be
	 * overwritten. If the given icon is null, an existing icon will be removed.
	 * 
	 * @param id
	 *            GUID of the instance
	 * @param icon
	 *            the new instance icon
	 */
	public void setIcon(GUID transaction, Object instance, Blob icon) {
		setLink(transaction, instance, PDStore.ICON_ROLEID, icon);
	}

	/**
	 * Sets the icon associated with a GUID. In PDStore every instance can be
	 * given an icon. If the instance already has an icon, the icon will be
	 * overwritten. If the given icon is null, an existing icon will be removed.
	 * 
	 * @param id
	 *            instance to set icon for
	 * @param icon
	 *            the new instance icon
	 */
	public void setIcon(Object instance, Blob icon) {
		setIcon(getCurrentTransaction(), icon);
	}

	/**
	 * Removes the icon of the given instance using the given transaction.
	 * 
	 * @param transaction
	 *            - current transaction id
	 * @param instance
	 */
	public void removeIcon(GUID transaction, Object instance) {
		Blob icon = getIcon(transaction, instance);
		if (icon != null)
			removeLink(transaction, instance, PDStore.ICON_ROLEID, icon);
	}

	/**
	 * Removes the icon of the given instance using the current transaction.
	 * 
	 * @param instance
	 */
	public void removeIcon(Object instance) {
		removeIcon(getCurrentTransaction(), instance);
	}

	/**
	 * Provides a String with a description of the given instance.
	 * 
	 * @param transaction
	 * @param instance
	 */
	public String instanceToString(GUID transaction, Object instance) {
		GUID type = getType(transaction, instance);
		Collection<GUID> accessibleRoles = getAccessibleRoles(transaction, type);

		String result = "Instance " + getNameOrValue(transaction, instance)
				+ " has type " + getNameOrValue(transaction, type)
				+ " and accessible roles with values:\n";

		for (GUID role : accessibleRoles) {
			Collection<Object> values = getInstances(transaction, instance,
					role);

			result += getNameOrValue(transaction, role) + ": ";
			for (Object v : values)
				result += getNameOrValue(transaction, v) + ", ";
			result += "\n";
		}
		return result;
	}

	/**
	 * Provides a String with a description of the given instance.
	 * 
	 * @param instance
	 */
	public String instanceToString(Object instance) {
		return instanceToString(getCurrentTransaction(), instance);
	}

	// TODO: deprecated?
	/**
	 * Cleans up any connections the Store has to external dependencies. This
	 * may be a NO-OP depending on the store.
	 */
	public void close() {
	}

	/***
	 * Creates a new PDStore model.
	 * 
	 * @param transaction
	 *            the transaction to create the new model on
	 * @param modelId
	 *            the ID of the new model
	 * @param modelName
	 *            the textual name of the new model
	 */
	public void createModel(GUID transaction, GUID modelId, String modelName) {
		setName(transaction, modelId, modelName);
		addLink(transaction, this.getRepository(),
				PDStore.REPOSITORYMODEL_ROLEID, modelId);
		setType(transaction, this.getRepository(), PDStore.REPOSITORY_TYPEID);
		setType(transaction, modelId, PDStore.MODEL_TYPEID);
	}

	/***
	 * Creates a new PDStore model.
	 * 
	 * @param modelId
	 *            the ID of the new model
	 * @param modelName
	 *            the textual name of the new model
	 */
	public void createModel(GUID modelId, String modelName) {
		createModel(getCurrentTransaction(), modelId, modelName);
	}

	/***
	 * Creates a new PDStore type and adds it to a model
	 * 
	 * @param transaction
	 * @param modelId
	 * @param typeId
	 * @param typeName
	 */
	public void createType(GUID transaction, GUID modelId, GUID typeId,
			String typeName) {
		addLink(transaction, modelId, PDStore.MODELTYPE_ROLEID, typeId);
		setType(transaction, typeId, PDStore.TYPE_TYPEID);
		if (typeName != null)
			setName(transaction, typeId, typeName);
		addLink(transaction, typeId, PDStore.ISPRIMITIVE_ROLEID, false);
	}

	/***
	 * Creates a new PDStore type and adds it to a model
	 * 
	 * @param modelId
	 * @param typeId
	 * @param typeName
	 */
	public void createType(GUID modelId, GUID typeId, String typeName) {
		createType(getCurrentTransaction(), modelId, typeId, typeName);
	}

	/**
	 * Creates a primitive type in PDStore. Use this method only if you are know
	 * what you are doing. TODO make private?
	 * 
	 * @param transaction
	 * @param modelId
	 * @param typeId
	 * @param typeName
	 */
	public void createPrimitiveType(GUID transaction, GUID modelId,
			GUID typeId, String typeName) {
		addLink(transaction, modelId, PDStore.MODELTYPE_ROLEID, typeId);
		setType(transaction, typeId, PDStore.TYPE_TYPEID);
		setName(transaction, typeId, typeName);
		addLink(transaction, typeId, PDStore.ISPRIMITIVE_ROLEID, true);
	}

	/**
	 * Link an instance to another through a role
	 * 
	 * @param transaction
	 *            - current transaction id
	 * @param instance1
	 *            - accessor instance
	 * @param type1
	 *            - type id of the accessor instance
	 * @param role2
	 *            - role to link with
	 * @param instance2
	 *            - object to be linked to
	 */
	public void addLink(GUID transaction, Object instance1, GUID type1,
			GUID role2, Object instance2) throws PDStoreException {
		addLink(transaction, instance1, role2, instance2);
	}

	/***
	 * Creates a new relation between two existing types, consisting of two new
	 * roles. TODO should be private since there is a version that also sets
	 * names?
	 * 
	 * @param transaction
	 *            the transaction to create the relation on
	 * @param type1Id
	 *            ID of the first type involved in the relation
	 * @param role2Id
	 *            ID of the new role to use for the second type
	 * @param type2Id
	 *            ID of the second type involved in the relation
	 */
	public void createRelation(GUID transaction, GUID type1Id, GUID role2Id,
			GUID type2Id) {
		GUID role1Id = role2Id.getPartner();
		addLink(transaction, type1Id, PDStore.OWNED_ROLE_ROLEID, role1Id);
		addLink(transaction, type2Id, PDStore.OWNED_ROLE_ROLEID, role2Id);

		// TODO is this necessary?
		addLink(transaction, role2Id, PDStore.PARTNER_ROLEID, role1Id);
		addLink(transaction, role1Id, PDStore.PARTNER_ROLEID, role2Id);

		setType(transaction, role1Id, PDStore.ROLE_TYPEID);
		setType(transaction, role2Id, PDStore.ROLE_TYPEID);
	}

	/***
	 * Creates a new relation between two existing types, consisting of two new
	 * roles.
	 * 
	 * @param transaction
	 *            the transaction to create the relation on
	 * @param type1
	 *            ID of the first type involved in the relation
	 * @param role1Name
	 *            textual name of the new role to use for the first type
	 * @param role2Name
	 *            textual name of the new role to use for the second type
	 * @param role2
	 *            ID of the new role to use for the second type
	 * @param type2
	 *            ID of the second type involved in the relation
	 */
	public void createRelation(GUID transaction, GUID type1, String role1Name,
			String role2Name, GUID role2, GUID type2) {
		GUID role1 = role2.getPartner();
		createRelation(transaction, type1, role2, type2);
		if (role1Name != null)
			setName(transaction, role1, role1Name);
		if (role2Name != null)
			setName(transaction, role2, role2Name);
	}

	/***
	 * Creates a new relation between two existing types, consisting of two new
	 * roles.
	 * 
	 * @param type1
	 *            ID of the first type involved in the relation
	 * @param role1Name
	 *            textual name of the new role to use for the first type
	 * @param role2Name
	 *            textual name of the new role to use for the second type
	 * @param role2
	 *            ID of the new role to use for the second type
	 * @param type2
	 *            ID of the second type involved in the relation
	 */
	public void createRelation(GUID type1, String role1Name, String role2Name,
			GUID role2, GUID type2) {
		createRelation(getCurrentTransaction(), type1, role1Name, role2Name,
				role2, type2);
	}

	public void addMetamodel(GUID transaction) {
		createModel(transaction, PDStore.PDMETAMODEL_ID, "PD Metamodel");

		// "Any" type (supertype of all types)
		addLink(transaction, PDMETAMODEL_ID, MODELTYPE_ROLEID, ANY_TYPEID);
		setType(transaction, ANY_TYPEID, PDStore.TYPE_TYPEID);
		setName(transaction, ANY_TYPEID, "Any");

		/* Primitive types */
		createPrimitiveType(transaction, PDMETAMODEL_ID, PDStore.GUID_TYPEID,
				"GUID");
		createPrimitiveType(transaction, PDStore.PDMETAMODEL_ID,
				PDStore.DOUBLE_PRECISION_TYPEID, "Double");
		createPrimitiveType(transaction, PDStore.PDMETAMODEL_ID,
				PDStore.INTEGER_TYPEID, "Integer");
		createPrimitiveType(transaction, PDStore.PDMETAMODEL_ID,
				PDStore.BOOLEAN_TYPEID, "Boolean");
		createPrimitiveType(transaction, PDStore.PDMETAMODEL_ID,
				PDStore.TIMESTAMP_TYPEID, "Timestamp");
		createPrimitiveType(transaction, PDStore.PDMETAMODEL_ID,
				PDStore.CHAR_TYPEID, "Char");
		createPrimitiveType(transaction, PDStore.PDMETAMODEL_ID,
				PDStore.STRING_TYPEID, "String");
		createPrimitiveType(transaction, PDStore.PDMETAMODEL_ID,
				PDStore.BLOB_TYPEID, "Blob");
		createPrimitiveType(transaction, PDStore.PDMETAMODEL_ID,
				PDStore.IMAGE_TYPEID, "Image");

		// TODO do we need the Object type?
		/* Type Object (supertype of all complex types) */
		createType(transaction, PDStore.PDMETAMODEL_ID, PDStore.OBJECT_TYPEID,
				"Object");

		// TODO the relations below should be for type ANY
		/* Object - name */
		createRelation(transaction, PDStore.OBJECT_TYPEID, null, "name",
				PDStore.NAME_ROLEID, PDStore.STRING_TYPEID);

		/* Object - icon */
		createRelation(transaction, PDStore.OBJECT_TYPEID, null, "icon",
				PDStore.ICON_ROLEID, PDStore.IMAGE_TYPEID);

		/* Metatype Type */
		createType(transaction, PDStore.PDMETAMODEL_ID, PDStore.TYPE_TYPEID,
				"Type");

		// is primitive
		createRelation(transaction, PDStore.TYPE_TYPEID, null, "is primitive",
				PDStore.ISPRIMITIVE_ROLEID, PDStore.BOOLEAN_TYPEID);

		// has supertype
		createRelation(transaction, PDStore.TYPE_TYPEID, "subtype",
				"supertype", PDStore.HAS_SUPERTYPE_ROLEID, PDStore.TYPE_TYPEID);

		/* Metatype Role */
		createType(transaction, PDStore.PDMETAMODEL_ID, PDStore.ROLE_TYPEID,
				"Role");

		// owned roles
		createRelation(transaction, PDStore.TYPE_TYPEID, "owner", "owned role",
				PDStore.OWNED_ROLE_ROLEID, PDStore.ROLE_TYPEID);

		/*
		 * Partner relation is recursive and symmetrical, therefore only one
		 * role entry
		 */
		createRelation(transaction, PDStore.ROLE_TYPEID, null, "partner",
				PDStore.PARTNER_ROLEID, PDStore.ROLE_TYPEID);

		// min mult
		createRelation(transaction, PDStore.ROLE_TYPEID, null, "min mult",
				PDStore.MIN_MULT_ROLEID, PDStore.INTEGER_TYPEID);

		// max mult
		createRelation(transaction, PDStore.ROLE_TYPEID, null, "max mult",
				PDStore.MAX_MULT_ROLEID, PDStore.INTEGER_TYPEID);

		// selectivity (approximation)
		createRelation(transaction, PDStore.ROLE_TYPEID, null,
				"XRX selectivity", PDStore.ROLE_XRX_SELECTIVITY_ROLEID,
				PDStore.INTEGER_TYPEID);
		createRelation(transaction, PDStore.ROLE_TYPEID, null,
				"IRX selectivity", PDStore.ROLE_IRX_SELECTIVITY_ROLEID,
				PDStore.INTEGER_TYPEID);
		createRelation(transaction, PDStore.ROLE_TYPEID, null,
				"Combination selectivity", PDStore.FILTER_SELECTIVITY_LOOKUPID,
				PDStore.INTEGER_TYPEID);
		createRelation(transaction, PDStore.ROLE_TYPEID, null,
				"Equal selectivity", PDStore.EQUAL_SELECTIVITY_ROLEID,
				PDStore.INTEGER_TYPEID);
		createRelation(transaction, PDStore.ROLE_TYPEID, null,
				"LessThan selectivity", PDStore.LESSTHAN_SELECTIVITY_ROLEID,
				PDStore.INTEGER_TYPEID);
		createRelation(transaction, PDStore.ROLE_TYPEID, null,
				"GreaterThan selectivity",
				PDStore.GREATERTHAN_SELECTIVITY_ROLEID, PDStore.INTEGER_TYPEID);

		/* Type Model */
		createType(transaction, PDStore.PDMETAMODEL_ID, PDStore.MODEL_TYPEID,
				"Model");

		// models-types
		createRelation(transaction, PDStore.MODEL_TYPEID, "model", "type",
				PDStore.MODELTYPE_ROLEID, PDStore.TYPE_TYPEID);

		// objects to types (for dynamic type info)
		createRelation(transaction, PDStore.OBJECT_TYPEID, "has instance",
				"has type", PDStore.HAS_TYPE_ROLEID, PDStore.TYPE_TYPEID);

		/* Types for branching */
		createType(transaction, PDStore.PDMETAMODEL_ID, PDStore.BRANCH_TYPEID,
				"Branch");
		createType(transaction, PDStore.PDMETAMODEL_ID,
				PDStore.TRANSACTION_TYPEID, "Transaction");

		// parent to child transaction
		createRelation(transaction, PDStore.TRANSACTION_TYPEID, "parent",
				"child", PDStore.PARENTTRANSACTION_CHILDBRANCH_ROLEID,
				PDStore.TRANSACTION_TYPEID);

		// branch to transaction
		createRelation(transaction, PDStore.TRANSACTION_TYPEID, "transaction",
				"branch", PDStore.BRANCH_ROLEID, PDStore.BRANCH_TYPEID);

		// transaction to isolation level
		createRelation(transaction, PDStore.TRANSACTION_TYPEID, "transaction",
				"isolation level", PDStore.ISOLATIONLEVEL_ROLEID,
				PDStore.INTEGER_TYPEID);

		// transaction to durable transaction
		createRelation(transaction, PDStore.TRANSACTION_TYPEID,
				"begin-transaction", "durable transaction",
				PDStore.DURABLE_TRANSACTION_ROLEID, PDStore.TRANSACTION_TYPEID);

		// Object - USES_ROLE - Role
		createRelation(transaction, PDStore.OBJECT_TYPEID, "user", "used role",
				PDStore.USES_ROLE_ROLEID, PDStore.ROLE_TYPEID);

		// Change
		createType(transaction, PDStore.PDMETAMODEL_ID, CHANGE_TYPEID, "Change");
		createRelation(transaction, CHANGE_TYPEID, null, "transaction",
				CHANGE_TRANSACTION_ROLEID, TRANSACTION_TYPEID);
		createRelation(transaction, CHANGE_TYPEID, null, "change type",
				CHANGE_CHANGETYPE_ROLEID, STRING_TYPEID);
		createRelation(transaction, CHANGE_TYPEID, null, "instance1",
				CHANGE_INSTANCE1_ROLEID, OBJECT_TYPEID);
		createRelation(transaction, CHANGE_TYPEID, null, "role2",
				CHANGE_ROLE2_ROLEID, ROLE_TYPEID);
		createRelation(transaction, CHANGE_TYPEID, null, "instance2",
				CHANGE_INSTANCE2_ROLEID, OBJECT_TYPEID);

		// Sparql Query
		createRelation(transaction, SPARQL_QUERY_TYPEID, null, "where",
				WHERE_TUPLE_ROLEID, CHANGE_TYPEID);
		createRelation(transaction, SPARQL_QUERY_TYPEID, null, "filter",
				FILTER_ROLEID, CHANGE_TYPEID);
		createRelation(transaction, SPARQL_QUERY_TYPEID, null, "subquery",
				SUBQUERY_ROLEID, SPARQL_QUERY_TYPEID);

		// Operation
		createType(transaction, PDStore.PDMETAMODEL_ID, OPERATION_TYPEID,
				"Operation");
		createRelation(transaction, OPERATION_TYPEID, "applicable operation",
				"parameter type", PARAMETER_TYPE_ROLEID, TYPE_TYPEID);
		createRelation(transaction, OPERATION_TYPEID, null,
				"operation implementation", OPERATION_IMPLEMENTATION_ROLEID,
				STRING_TYPEID);

		// Widget
		createType(transaction, PDStore.PDMETAMODEL_ID, WIDGET_TYPEID, "Widget");
		createRelation(transaction, OPERATION_TYPEID, null,
				"widget implementation", WIDGET_IMPLEMENTATION_ROLEID,
				STRING_TYPEID);
		createRelation(transaction, OBJECT_TYPEID, "visualizes",
				"is visualized by", VISUALIZED_BY_ROLEID, WIDGET_TYPEID);
	}

	/**
	 * Get the accessor type of a role. Instances of that type can use the role
	 * to navigate to other instances.
	 * 
	 * @param transaction
	 *            ID of the transaction to read from
	 * @param role2Id
	 *            ID of the role to get the accessor type for
	 * @return ID of the accessor type
	 */
	public GUID getAccessorType(GUID transaction, GUID role2Id) {
		return (GUID) getInstance(transaction, role2Id.getPartner(),
				PDStore.OWNER_TYPE_ROLEID);
	}

	/**
	 * Get the accessor type of a role. Instances of that type can use the role
	 * to navigate to other instances.
	 * 
	 * @param role2Id
	 *            ID of the role to get the accessor type for
	 * @return ID of the accessor type
	 */
	public GUID getAccessorType(GUID role2Id) {
		return getAccessorType(getCurrentTransaction(), role2Id);
	}

	/**
	 * Get the owner type of a role. Instances of that type can appear in that
	 * role, i.e. they can be navigated to from instances of the corresponding
	 * accessor type.
	 * 
	 * @param transaction
	 *            ID of the transaction to read from
	 * @param role2Id
	 *            ID of the role to get the owner type for
	 * @return ID of the owner type
	 */
	public GUID getOwnerType(GUID transaction, GUID role2Id) {
		return (GUID) getInstance(transaction, role2Id,
				PDStore.OWNER_TYPE_ROLEID);
	}

	/**
	 * Get the owner type of a role. Instances of that type can appear in that
	 * role, i.e. they can be navigated to from instances of the corresponding
	 * accessor type.
	 * 
	 * @param role2Id
	 *            ID of the role to get the owner type for
	 * @return ID of the owner type
	 */
	public GUID getOwnerType(GUID role2Id) {
		return getOwnerType(getCurrentTransaction(), role2Id);
	}

	/**
	 * Get the dynamic type for the given instance.
	 * 
	 * @param transaction
	 *            ID of the transaction to read from
	 * @param instance
	 *            instance to get the type of
	 * @return ID of the type of the given instance
	 */
	public GUID getType(GUID transaction, Object instance) {
		GUID type = (GUID) getInstance(transaction, instance,
				PDStore.HAS_TYPE_ROLEID);

		// Assuming that all complex types have a has-type link,
		// use the GUIDs for primitive types in case no has-type link is found.
		if (type == null)
			type = pdstore.changelog.PrimitiveType.typeIdOf(instance);

		return type;
	}

	/**
	 * Get the dynamic type for the given instance.
	 * 
	 * @param instance
	 *            instance to get the type of
	 * @return ID of the type of the given instance
	 */
	public GUID getType(Object instance) {
		return getType(getCurrentTransaction(), instance);
	}

	/**
	 * Set the dynamic type for the given instance.
	 * 
	 * @param transaction
	 *            ID of the transaction to write on
	 * @param instance
	 *            instance to set the type for
	 * @param type
	 *            ID of the type to set
	 */
	public void setType(GUID transaction, Object instance, GUID type) {
		setLink(transaction, instance, HAS_TYPE_ROLEID, type);
	}

	/**
	 * Set the dynamic type for the given instance.
	 * 
	 * @param instance
	 *            instance to set the type for
	 * @param type
	 *            ID of the type to set
	 */
	public void setType(Object instance, GUID type) {
		setType(getCurrentTransaction(), instance, type);
	}

	/**
	 * Gets the IDs of the roles that are accessible from the given type. For
	 * example, a type "Address" could have accessible roles "street name",
	 * "house number" etc.
	 * 
	 * @param transaction
	 *            ID of the transaction to read from
	 * @param type
	 *            the ID of the type to get the acessiible roles for
	 * @return the roles that are accessible from the given type
	 */
	public Collection<GUID> getAccessibleRoles(GUID transaction, GUID type) {
		Collection<GUID> result = new HashSet<GUID>();
		for (Object role : getInstances(transaction, type,
				PDStore.OWNED_ROLE_ROLEID))
			result.add(((GUID) role).getPartner());
		return result;
	}

	/**
	 * Gets the IDs of the roles that are accessible from the given type. For
	 * example, a type "Address" could have accessible roles "street name",
	 * "house number" etc.
	 * 
	 * s * @param type the ID of the type to get the acessiible roles for
	 * 
	 * @return the roles that are accessible from the given type
	 */
	public Collection<GUID> getAccessibleRoles(GUID type) {
		return getAccessibleRoles(getCurrentTransaction(), type);
	}

	/**
	 * Creates a new branch from the given parent transaction, and begins a new
	 * transaction on that branch. The transaction will be open and needs to be
	 * committed (possibly with other changes on the new branch) to conclude the
	 * branch operation.
	 * 
	 * @param parentTransactionId
	 *            the transaction to branch from (branch source)
	 * @return ID of the first transaction on the new branch (branch target)
	 * @throws PDStoreException
	 */
	public GUID branch(GUID parentTransactionId) throws PDStoreException {
		GUID branchId = GUID.newBranchId();
		GUID transactionId = begin(branchId);
		merge(transactionId, parentTransactionId);
		return transactionId;
	}

	/**
	 * Merge the given parent transaction into the current transaction.
	 * 
	 * @param transactionId
	 *            ID of the current transaction (merge target)
	 * @param parentTransactionId
	 *            ID of the parent transaction (merge source)
	 */
	public void merge(GUID transactionId, GUID parentTransactionId) {
		addLink(transactionId, parentTransactionId,
				PDStore.PARENTTRANSACTION_CHILDBRANCH_ROLEID,
				transactionId.getBranchID());
	}

	/**
	 * Merge the given parent transaction into the current transaction.
	 * 
	 * @param parentTransactionId
	 *            ID of the parent transaction (merge source)
	 */
	public void merge(GUID parentTransactionId) {
		merge(getCurrentTransaction(), parentTransactionId);
	}

	/**
	 * Constructs a best-effort human-readable name for an instance. TODO: unify
	 * with getNameOrValue
	 * 
	 * @param transaction
	 *            transaction to read from
	 * @param instance
	 *            the instance to construct a label for
	 * @return a label for the give instance
	 */
	public String getLabel(GUID transaction, Object instance) {
		String typeName = null;
		String name;

		if (instance == null)
			return "null";

		if (instance instanceof Variable) {
			return instance.toString();
		} else if (instance instanceof GUID) {
			GUID type = getType(transaction, instance);
			if (type != null)
				typeName = getName(transaction, type);

			name = getName(transaction, instance);
			if (name != null)
				name = "\"" + name + "\"";
			else {
				GUID guid = (GUID) instance;
				name = getName(transaction, guid.getPartner());
				if (name != null)
					name = "^\"" + name + "\"";
				else
					name = guid.toRawString();
			}
		} else {
			PrimitiveType typeOf = PrimitiveType.typeOf(instance);
			if (typeOf == null)
				typeName = "null";
			else
				typeName = typeOf.toString();
			name = instance.toString();
		}

		String label = "";
		if (typeName != null)
			label += typeName + " ";
		label += name;

		return label;
	}

	/**
	 * Constructs a best-effort human-readable name for an instance.
	 * 
	 * @param instance
	 *            the instance to construct a label for
	 * @return a label for the give instance
	 */
	public String getLabel(Object instance) {
		return getLabel(getCurrentTransaction(), instance);
	}

	/**
	 * Constructs a human-readable string representation for the given change.
	 * 
	 * @param transaction
	 *            transaction to read the change data (i.e names of instances)
	 *            from
	 * @param change
	 *            the change to construct a string for
	 * @return a human-readable string representation of the change
	 */
	public String toString(GUID transaction, PDChange<GUID, Object, GUID> change) {
		return change.toString(this, transaction);
	}

	/**
	 * Constructs a human-readable string representation for the given change.
	 * 
	 * @param change
	 *            the change to construct a string for
	 * @return a human-readable string representation of the change
	 */
	public String toString(PDChange<GUID, Object, GUID> change) {
		return change.toString(this, getCurrentTransaction());
	}

	/**
	 * Constructs a best-effort human-readable representation for an Iterable of
	 * PDChanges.
	 * 
	 * @param transaction
	 * @param changes
	 * @return
	 */
	public String toString(GUID transaction,
			Iterable<PDChange<GUID, Object, GUID>> changes) {
		StringBuffer result = new StringBuffer("[");
		for (PDChange<GUID, Object, GUID> change : changes)
			result.append(change.toString(this, transaction) + " ");
		result.append("]");
		return result.toString();
	}

	/**
	 * Constructs a best-effort human-readable representation for an Iterable of
	 * PDChanges.
	 * 
	 * @param changes
	 * @return
	 */
	public String toString(Iterable<PDChange<GUID, Object, GUID>> changes) {
		return toString(getCurrentTransaction(), changes);
	}

	/**
	 * Generic function to execute Java code from PDStore, where the Java code
	 * is dynamically loaded.
	 * 
	 * @param transaction
	 *            transaction to apply the operation on
	 * @param operation
	 *            ID of the operation to apply
	 * @param parameter
	 *            the superparameter to apply the operation on
	 * @return value returned by the operation
	 */
	public Object applyOperation(GUID transaction, GUID operation,
			Object parameter) {
		String implementation = (String) getInstance(transaction, operation,
				OPERATION_IMPLEMENTATION_ROLEID);
		Debug.assertTrue(implementation != null, "Operation instance "
				+ getLabel(transaction, operation)
				+ "does not specify an implementation.");

		OperationI singleton = null;
		Class<?> operationClass;
		try {
			operationClass = Class.forName(implementation);
			singleton = (OperationI) operationClass.newInstance();
		} catch (ClassNotFoundException e) {
			throw new PDStoreException("", e);
		} catch (InstantiationException e) {
			throw new PDStoreException("", e);
		} catch (IllegalAccessException e) {
			throw new PDStoreException("", e);
		}
		return singleton.apply(this, transaction, parameter);
	}

	/**
	 * Generic function to execute Java code from PDStore, where the Java code
	 * is dynamically loaded.
	 * 
	 * @param operation
	 *            ID of the operation to apply
	 * @param parameter
	 *            the superparameter to apply the operation on
	 * @return value returned by the operation
	 */
	public Object applyOperation(GUID operation, Object parameter) {
		return applyOperation(getCurrentTransaction(), operation, parameter);
	}

	/**
	 * Returns the IDs of the operations that can be applied to the given
	 * instance.
	 * 
	 * @param transaction
	 *            the transaction to query the operations on
	 * @param instance
	 *            the instance that is the potential actual superparameter
	 * @return IDs of operations that can be applied to instance
	 */
	public Collection<GUID> getApplicableOperations(GUID transaction,
			Object instance) {
		GUID type = getType(transaction, instance);
		return (Collection<GUID>) (Object) getInstances(transaction, type,
				PARAMETER_TYPE_ROLEID.getPartner());
	}

	/**
	 * Returns the IDs of the operations that can be applied to the given
	 * instance.
	 * 
	 * @param instance
	 *            the instance that is the potential actual superparameter
	 * @return IDs of operations that can be applied to instance
	 */
	public Collection<GUID> getApplicableOperations(Object instance) {
		return getApplicableOperations(getCurrentTransaction(), instance);
	}

	// //////// DAL functionality //////////

	/**
	 * Mapping between PDType GUIDs and their Java data access classes
	 */
	private static Dictionary<GUID, Class<?>> ClassForPDType = new Hashtable<GUID, Class<?>>();

	static {
		// register most important classes
		addDataClass(PDStore.MODEL_TYPEID, PDModel.class);
		addDataClass(PDStore.TYPE_TYPEID, PDType.class);
		addDataClass(PDStore.ROLE_TYPEID, PDRole.class);
	}

	/**
	 * Registers a DAL class with the cache, so that the class can be used for
	 * representing instances in memory.
	 * 
	 * @throws PDStoreException
	 */
	public static void addDataClass(GUID typeId, Class<?> dalClass)
			throws PDStoreException {
		ClassForPDType.put(typeId, dalClass);
	}

	public static Class<?> getDataClass(GUID typeId) throws PDStoreException {
		return ClassForPDType.get(typeId);
	}

	/**
	 * Maps GUIDs of cached instances to their object representations in memory.
	 * Uses soft references so that instances may be removed from the cache.
	 */
	public Dictionary<GUID, SoftReference<PDInstance>> ObjectForGUID = new Hashtable<GUID, SoftReference<PDInstance>>();

	public PDInstance load(GUID instanceId) throws PDStoreException {
		Object type = getInstance(getCurrentTransaction(), instanceId,
				PDStore.HAS_TYPE_ROLEID);

		// error checking
		if (type == null)
			throw new PDStoreException(
					"Type lookup without explicit type link not yet implemented.\n"
							+ "Please specify the type of instance "
							+ instanceId + ".");
		else if (!(type instanceof GUID))
			throw new PDStoreException(
					"The has-type link (PDStore.HAS_TYPE_ROLEID) of instance "
							+ instanceId
							+ " does not link to a type, but to a non-GUID: "
							+ type
							+ "\n"
							+ "Please make sure you added a correct has-type link for that instance.");

		GUID typeId = (GUID) type;
		return load(typeId, instanceId);
	}

	/**
	 * Loads an instance from the database into this PDWorkingCopy. If the
	 * typeId is statically known, this method should not be used, but instead
	 * the static load method of the corresponding DAL class should be used,
	 * since these static load methods have statically correctly typed return
	 * values.
	 * 
	 * @param typeId
	 *            GUID of the instance type
	 * @param instanceId
	 *            GUID of the instance
	 * @return an object representing the instance in memory
	 * @throws PDStoreException
	 */
	public PDInstance load(GUID typeId, GUID instanceId) {
		Debug.assertTrue(typeId != null, "typeId must not be null");

		/*
		 * If no ID is given, then return null. This shouldn't do harm while
		 * slightly simplifying the code in getters of generated DAL classes
		 * (saving the null special case).
		 */
		if (instanceId == null)
			return null;

		PDInstance i = null;
		Class<?> dalClass = getDataClass(typeId);
		if (dalClass != null) {
			// if there is a registered wrapper class, then use it
			SoftReference<PDInstance> ref = ObjectForGUID.get(instanceId);
			if (ref != null)
				i = ref.get();
			if (i == null) {
				// if the instance is not in the cache, then
				// create and register a new PDInstance object

				Debug.println("Instantiating new DAL object of " + dalClass
						+ "...", "DAL");

				try {
					Constructor<?> constr = dalClass.getDeclaredConstructor(
							PDStore.class, GUID.class);
					i = (PDInstance) constr.newInstance(this, instanceId);
				} catch (NoSuchMethodException e) {
					throw new PDStoreException(
							"Could not find a constructor(PDStore, GUID) for class "
									+ dalClass.getName());
				} catch (SecurityException e) {
					throw new PDStoreException(
							"The constructor(PDWorkingCopy, GUID) for class "
									+ dalClass.getName()
									+ " is not accessible.");
				} catch (Exception e) {
					throw new PDStoreException(
							"Something went wrong when instantiating class "
									+ dalClass.getName() + ".\n"
									+ "See inner exception.", e);
				}

				ObjectForGUID.put(instanceId, new SoftReference<PDInstance>(i));
			}
		} else {
			throw new PDStoreException(
					"Could not find specific data access class for type "
							+ getLabel(getCurrentTransaction(), typeId));
		}
		return i;
	}

	public Collection<PDInstance> getAndLoadInstances(Object instance1,
			GUID role2, GUID type2) {
		Collection<PDInstance> result = new ArrayList<PDInstance>();
		for (Object i : getInstances(instance1, role2))
			result.add(load(type2, (GUID) i));
		return result;
	}

	public Collection<PDInstance> getAndLoadInstances(Object instance1,
			GUID role2) {
		Collection<PDInstance> result = new ArrayList<PDInstance>();
		for (Object i : getInstances(instance1, role2))
			result.add(load((GUID) i));
		return result;
	}

	public PDInstance newInstance(GUID typeId) {
		return load(typeId, new GUID());
	}

	/**
	 * Adds a listener to the PDStore that prints out any changes that get
	 * committed, when they get committed. This is useful for debugging as it
	 * shows what is actually going into the PDStore.
	 */
	public void addDebugListener() {
		List<PDListener<GUID, Object, GUID>> listeners = store
				.getDetachedListenerList();
		listeners.add(new PDListenerAdapter<GUID, Object, GUID>() {
			@Override
			public void transactionCommitted(
					List<PDChange<GUID, Object, GUID>> transaction,
					List<PDChange<GUID, Object, GUID>> matchedChanges,
					PDCoreI<GUID, Object, GUID> core) {
				for (PDChange<GUID, Object, GUID> change : transaction)
					System.out.println(change);
			}
		});
	}

	public void waitForDetachedListeners() {
		try {
			DetachedListenerDispatcher.activeListeners.acquire(10000);
			DetachedListenerDispatcher.activeListeners.drainPermits();
			DetachedListenerDispatcher.activeListeners.release(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Reverse listener method: blocks until a transaction is committed. This
	 * method is only implemented in pdstore.rmi.PDStore, but has to be defined
	 * here so that code written for pdstore.PDStore will work for
	 * pdstore.rmi.PDStore.
	 * 
	 * @return
	 */
	public List<PDChange<GUID, Object, GUID>> nextTransaction() {
		throw new UnsupportedOperationException(
				"This method is only implemented in pdstore.rmi.PDStore");
	}

	public boolean linkChangedSince(GUID transaction, GUID sinceTransaction,
			Object instance1, GUID role2) {
		Collection<PDChange<GUID, Object, GUID>> changes = getChanges(new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_EFFECTIVE, transaction, instance1, role2, null));
		for (PDChange<GUID, Object, GUID> change : changes)
			if (change.getTransaction().later(sinceTransaction))
				return true;
		return false;
	}

	public List<List<PDChange<GUID, Object, GUID>>> newTransactions()
			throws PDStoreException {
		throw new UnsupportedOperationException(
				"this method should not be called on a local store.");
	}

	public PDStoreI<GUID, Object, GUID> getStore() {
		return this.store;
	}

	public Transaction getTransaction(GUID transactionId) {
		return ((ConcurrentStore) this.store).getTransaction(transactionId);
	}

	public static pdstore.rmi.PDStoreLocal connectToServerReplica(
			String localName, String host, String rmiKey)
			throws PDStoreException {
		return new pdstore.rmi.PDStoreLocal(localName, host, rmiKey);
	}

	/**
	 * Gets the roles that the given instance uses, i.e. links exist for the
	 * instance and the roles.
	 */
	public Collection<GUID> getRoles(GUID transaction, Object instance) {
		// TODO this (and similar model methods) could even be part of GUID,
		// i.e. similar to making getName part of GUID using static debugStore
		return (Collection<GUID>) (Object) getInstances(transaction, instance,
				PDStore.USES_ROLE_ROLEID);
	}

	/**
	 * Gets the roles that the given instance uses, i.e. links exist for the
	 * instance and the roles.
	 */
	public Collection<GUID> getRoles(Object instance) {
		return getRoles(getCurrentTransaction(), instance);
	}

	/** Gets the role with the given name that the given instance uses. */
	public GUID getRoleFromInstance(GUID transaction, Object instance,
			String roleName) {
		// TODO using very simplistic implementation here; use query instead
		Collection<GUID> roles = getRoles(transaction, instance);
		for (GUID role : roles) {
			String name = getName(transaction, (Object) role);
			if (name != null && name.equals(roleName))
				return role;
		}
		return null;
	}

	/** Gets the role with the given name that the given instance uses. */
	public GUID getRoleFromInstance(Object instance, String roleName) {
		return getRoleFromInstance(getCurrentTransaction(), instance, roleName);
	}
}