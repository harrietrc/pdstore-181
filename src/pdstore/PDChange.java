package pdstore;

import java.io.IOException;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import nz.ac.auckland.se.genoupe.tools.Debug;
import pdstore.changelog.PrimitiveType;
import pdstore.generic.ChangeTemplateKind;
import pdstore.generic.GlobalTypeAdapter;
import pdstore.generic.Pairable;
import pdstore.generic.TypeAdapter;
import pdstore.sparql.QueryOptimizerBase;
import pdstore.sparql.Variable;

/**
 * Analysis: This class represents a change item in PDStore and is a fundamental
 * propositional unit of information that is stored in PDStore. 
 * A propositional unit of information is describing a fact that makes sense on its
 * own.
 * Changes are typically assumed to be part of a transaction. There are two types of
 * changes: link addition or link removal. As has been discussed in [Lutteroth,
 * Weber 2009], changes have an idempotent semantics within a transaction. This
 * means that changes don't need an artificial key, nor a running number within
 * a transaction. The main attributes: changetype, transaction, instance1,
 * role2, instance2 comprise a compound key.
 * 
 * Persistence design: To fulfil the requirement of persistence, normally
 * PDChanges are stored in the persistent log. But PDStore offers the
 * possibility of reduced persistence with the attributes isInLog and isInIndex.
 * They can be independently set.
 * 
 * isInLog=true, isInIndex=true is the default setting for classical persistent
 * data, and matches typically the only mode available in all databases.
 * 
 * isInLog=true, isInIndex=false is a setting recommended for logging or
 * journaling data that is infrequently read, typically only in a case of post
 * mortem analysis. This data is persistent but does not clutter the queryable
 * indices of PDStore.
 * 
 * isInLog=false, isInIndex=false is a setting for auxiliary data that is only
 * needed during a single transaction.
 * 
 * isInLog=false, isInIndex=true is a setting for auxiliary data that is needed
 * during the lifetime of one java virtual machine. This means this data gets
 * lost during crashes. Because of its exposure to crashes it can be considered
 * to be the most low-level of all settings.
 * 
 * Due to the symmetric approach of PDStore (see PDCore) there is always a
 * partner change with the roles interchanged.
 * 
 * Implementation: This class is the top class of a class hierarchy with
 * different implementations. This ensures that information is not duplicated.
 * 
 * PDChange object have two important uses: as changes that are stored in the database,
 * and as change templates that are used to query the database, or to subscribe to
 * listen to changes.
 * Changes that are stored in the database only use the two change types 
 * ChangeType.LINK_ADDED and ChangeType.LINK_REMOVED. Also the transaction,
 * instance1, role2 and instance2 are non-null.
 * 
 * For change templates, more change types are used for specific queries,
 * furthermore the fields transaction, instance1, role2 and instance2 can
 * be either a Variable, or in some contexts null, which represents then an 
 * unnamed variable.
 * 
 * 
 * @author gweb017
 * @author clut017
 * @author Daniel DENG
 */
public class PDChange<TransactionType extends Comparable<TransactionType>, InstanceType, RoleType extends Pairable<RoleType>>
		implements Serializable, Comparable {

	/**
	 * version UID for Serializable interface
	 */
	private static final long serialVersionUID = 1L;
	
	public final static GUID NULL_CHANGE =  new GUID(
	        "3232d6b0e21511e3b3aa28d2446fc01a");


	private TransactionType transaction = null;
	private InstanceType instance1 = null;
	private RoleType role2 = null;
	private InstanceType instance2 = null;
	private ChangeType changeType;

	boolean isInLog = true;
	boolean isInIndex = true;

	private boolean isLocal = false;

	public TransactionType getTransaction() {
		return transaction;
	}

	public void setTransaction(TransactionType transaction) {
		this.transaction = transaction;
	}

	public InstanceType getInstance1() {
		return instance1;
	}

	public void setInstance1(InstanceType instance1) {
		this.instance1 = instance1;
	}

	public RoleType getRole2() {
		return role2;
	}

	public void setRole2(RoleType role2) {
		this.role2 = role2;
	}

	public InstanceType getInstance2() {
		return instance2;
	}

	public void setInstance2(InstanceType instance2) {
		this.instance2 = instance2;
	}

	public ChangeType getChangeType() {
		return changeType;
	}

	public void setChangeType(ChangeType changeType) {
		this.changeType = changeType;
	}

	/**
	 * This property is telling whether a change is stored in the log, i.e.
	 * whether the change is persistent. The default is "true". See also
	 * isInIndex()
	 * 
	 * 
	 * @return
	 */
	public boolean isInLog() {
		return isInLog;
	}

	public void setInLog(boolean isInLog) {
		this.isInLog = isInLog;
	}

	/**
	 * This property is telling whether a change is stored in the in-memory
	 * index. The default is "true". See also isInIndex()
	 * 
	 * @return
	 */
	public boolean isInIndex() {
		return isInIndex;
	}

	public void setInIndex(boolean isInIndex) {
		this.isInIndex = isInIndex;
	}

	public boolean isLocal() {
		return isLocal;
	}

	public void setLocal(boolean isLocal) {
		this.isLocal = isLocal;
	}

	public PDChange(ChangeType type, TransactionType transaction,
			InstanceType instance1, RoleType role2, InstanceType instance2) {
		this.setInstance1(instance1);
		this.setRole2(role2);
		this.setInstance2(instance2);
		this.setTransaction(transaction);
		this.setChangeType(type);
		normalize();
	}

	/**
	 * Copy constructor
	 * 
	 * @param template
	 *            The object that is copied
	 */
	public PDChange(PDChange<TransactionType, InstanceType, RoleType> template) {
		this(template.getChangeType(), template.getTransaction(), template
				.getInstance1(), template.getRole2(), template.getInstance2());
	}

	public PDChange() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		PDStore debugStore = PDStore.getDebugStore();

		// if there is no debug store yet, fall back to un-prettified output
		if (debugStore == null)
			return toRawString();

		GUID transaction = debugStore.getCurrentTransaction();

		return toString(debugStore, transaction);
	}

	public String toRawString() {
		return "PDChange(transaction=" + getTransaction() + ", "
				+ getChangeType() + ", " + "instance1=" + getInstance1()
				+ ", role2=" + getRole2() + ", instance2=" + getInstance2()
				+ ")";
	}

	/**
	 * Constructs a best-effort human-readable name for a PDChange.
	 * 
	 * @param store
	 *            store to read names from
	 * @param transaction
	 *            transaction to read the names on
	 * @return a best-effort human-readable name for this change
	 */
	public String toString(PDStore store, GUID transaction) {
		/*
		 * TODO The use of PDStore here reduces the genericity of this class.
		 * The potentially generic methods of PDStore, such as getName which is
		 * used here, should be moved into a generic superclass of PDStore, e.g.
		 * PDStoreBase, and this superclass should be used here instead of
		 * PDStore.
		 */
		String result = "PDChange(";
		result += "transaction="
				+ store.getLabel(transaction, getTransaction()) + ", ";
		result += getChangeType() + ", ";
		result += "instance1=" + store.getLabel(transaction, getInstance1())
				+ ", ";
		result += "role2=" + store.getLabel(transaction, getRole2()) + ", ";
		result += "instance2=" + store.getLabel(transaction, getInstance2())
				+ ")";
		return result;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((getChangeType() == null) ? 0 : getChangeType().hashCode());
		result = prime
				* result
				+ ((getTransaction() == null) ? 0 : getTransaction().hashCode());
		result = prime * result
				+ ((getInstance1() == null) ? 0 : getInstance1().hashCode());
		result = prime * result
				+ ((getRole2() == null) ? 0 : getRole2().hashCode());
		result = prime * result
				+ ((getInstance2() == null) ? 0 : getInstance2().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof PDChange<?, ?, ?>))
			return false;
		PDChange<?, ?, ?> other = (PDChange<?, ?, ?>) obj;
		if (getTransaction() == null) {
			if (other.getTransaction() != null)
				return false;
		} else if (!getTransaction().equals(other.getTransaction()))
			return false;
		if (getChangeType() == null) {
			if (other.getChangeType() != null)
				return false;
		} else if (!getChangeType().equals(other.getChangeType()))
			return false;
		if (getInstance1() == null) {
			if (other.getInstance1() != null)
				return false;
		} else if (!getInstance1().equals(other.getInstance1()))
			return false;
		if (getRole2() == null) {
			if (other.getRole2() != null)
				return false;
		} else if (!getRole2().equals(other.getRole2()))
			return false;
		if (getInstance2() == null) {
			if (other.getInstance2() != null)
				return false;
		} else if (!getInstance2().equals(other.getInstance2()))
			return false;
		return true;
	}

	public PDChange<TransactionType, InstanceType, RoleType> getPartnerChange() {
		RoleType role1 = (role2 == null) ? null : role2.getPartner();
		return new PDChange<TransactionType, InstanceType, RoleType>(
				changeType, transaction, instance2, role1, instance1);
	}

	public PDChange<TransactionType, InstanceType, RoleType> getNormalizedChange() {
		// if there is no role given, the change is already normalized
		if (getRole2() == null)
			return this;

		if (getRole2().isFirst())
			return this;
		return getPartnerChange();
	}

	public InstanceType getNormalAccessedInstance() {
		if (getRole2().isFirst())
			return getInstance2();
		return getInstance1();
	}

	/**
	 * This method is used in computing the partners of object instance1 over
	 * role2. This method assumes that "result" is collecting the partners. This
	 * method encapsulates the semantics of the change object. It applies this
	 * change to "result". This method does not check for transactions, i.e this
	 * method should only be called if the transaction attribute has been taken
	 * into account.
	 * 
	 * @param instance1
	 * @param role2
	 * @param result
	 * @return true if a conflict was detected
	 */
	public boolean attemptChange(Object instance1, GUID role2,
			Collection<Object> result) {
		if (getRole2().equals(role2)) {
			if (getInstance1().equals(instance1)) {
				boolean conflict = applyChangeForInstance2(result);
				return conflict;
			}
		}
		if (getRole2().equals(role2.getPartner())) {
			// check link in other direction
			if (getInstance2().equals(instance1)) {
				boolean conflict = applyChangeForInstance1(result);
				return conflict;
			}
		}
		return true;
	}

	/**
	 * See method applyChangeForInstance1
	 * 
	 * @param result
	 * @return
	 */
	public boolean applyChangeForInstance2(Collection<Object> result) {
		if (getChangeType().equals(ChangeType.LINK_ADDED)) {
			result.add(getInstance2());
			// since only double remove is seen as a conflict:
			// for adds we always return true.
			return true;
		} else if (getChangeType().equals(ChangeType.LINK_REMOVED)) {
			return result.remove(getInstance2());
		}
		return true;
	}

	/**
	 * This method detects conflicts without changing result.
	 * 
	 * @param result
	 * @return true iff there is no conflict
	 */
	public boolean isApplyingForInstance1(Collection<Object> result) {
		if (getChangeType().equals(ChangeType.LINK_ADDED)) {
			// since only double remove is seen as a conflict:
			// for adds we always return true.
			return true;
		} else if (getChangeType().equals(ChangeType.LINK_REMOVED)) {
			return result.contains(getInstance1());
		}
		return false;
	}

	/**
	 * See method isApplyingForInstance1 This method detects conflicts without
	 * changing result.
	 * 
	 * @param result
	 * @return true iff there is no conflict
	 */
	public boolean isApplyingForInstance2(Collection<Object> result) {
		if (getChangeType().equals(ChangeType.LINK_ADDED)) {
			// since only double remove is seen as a conflict:
			// for adds we always return true.
			return true;
		} else if (getChangeType().equals(ChangeType.LINK_REMOVED)) {
			return result.contains(getInstance2());
		}
		return true;
	}

	/**
	 * This method encapsulates the application of changeTypes to the set
	 * "result." It also encapsulates the detection of conflicts. A conflict is
	 * a remove that does not have an effect.
	 * 
	 * @param result
	 * @return true iff there is no conflict
	 */
	public boolean applyChangeForInstance1(Collection<Object> result) {
		if (getChangeType().equals(ChangeType.LINK_ADDED)) {
			result.add(getInstance1());
			// since only double remove is seen as a conflict:
			// for adds we always return true.
			return true;
		} else if (getChangeType().equals(ChangeType.LINK_REMOVED)) {
			return result.remove(getInstance1());
		}
		return false;
	}
	
	/**  A method normalizing change Templates, so that they only contain nulls, no variables.
	 *   Needed for the Listener Dispatchers.
	 * @param wildCard TODO
	 * @return
	 */
	public PDChange<TransactionType, InstanceType, RoleType> removeVariables(InstanceType wildCard){
		PDChange<TransactionType, InstanceType, RoleType> changeTemplate = 
			new PDChange<TransactionType, InstanceType, RoleType>(this);
		if(changeType==null) changeTemplate.changeType = ChangeType.ANY_CHANGE_TYPE;
		if(role2==null || role2 instanceof Variable) changeTemplate.role2 = (RoleType) wildCard;
		if(instance1==null || instance1 instanceof Variable) changeTemplate.instance1 = wildCard;
		if(instance2==null || instance2 instanceof Variable) changeTemplate.instance2 = wildCard;
		return changeTemplate;
	}
	
	
	/** 
	 * 
	 * @return true if Change object is not a concrete object, but a change template
	 * 
	 */
	public boolean 	isTemplate (){
		return !(instance1IsSet () && instance2IsSet () && role2IsSet ());
	}
	
	public boolean 	instance1IsSet (){
		return instance1 != null && ! (instance1 instanceof Variable);
	}
	public boolean 	instance2IsSet (){
		return instance2 != null && ! (instance2 instanceof Variable);
	}
	public boolean 	role2IsSet (){
		return role2 != null && ! (role2 instanceof Variable);
	}


	/**  A method matching a concrete change against a change template.
	 *   This method must only be called on a normalized change, i.e. should be called
	 *   change.getNormalizedChange().matches(template)
	 *   @return true if this change matches the given change template
	 */
	public boolean matches(PDChange<TransactionType, InstanceType, RoleType> changeTemplate){
		if(changeTemplate== null) return false;
		Debug.assertTrue(role2.isFirst());
		PDChange<TransactionType, InstanceType, RoleType> changeTemplate2  = changeTemplate.getNormalizedChange();
		if(changeTemplate2.instance1IsSet () && !changeTemplate2.getInstance1().equals(instance1)) return false;
		if(changeTemplate2.instance2IsSet () && !changeTemplate2.getInstance2().equals(instance2)) return false;
		if(changeTemplate2.role2IsSet () && !changeTemplate2.getRole2().equals(role2)) return false;
		return true;
	}


	public PDChange<TransactionType, InstanceType, RoleType> substituteVariables(
			Map<Variable, Object> variableAssignment,
			ChangeTemplateKind changeTemplateKind) {
		ChangeType changeType = getChangeType();
		InstanceType instance1 = getInstance1();
		RoleType role2 = getRole2();
		InstanceType instance2 = getInstance2();

		// TODO: should there be a simple default behavior as the following?
//		if(changeTemplateKind==null)
//			 changeTemplateKind = ChangeTemplateKind.getKind(this);
		switch (changeTemplateKind) {
		case XXX:
		case XXI:
		case XRI:
		case XRX:
			instance1 = (InstanceType) variableAssignment
					.get((Variable) instance1);
			break;
		default:
			break;

		}
		switch (changeTemplateKind) {
		case XXX:
		case IXX:
		case IXI:
		case XXI:
			role2 = (RoleType) variableAssignment.get((Variable) role2);
			break;
		default:
			break;

		}

		switch (changeTemplateKind) {

		case XXX:
		case IXX:
		case XRX:
		case IRX:
			instance2 = (InstanceType) variableAssignment
					.get((Variable) instance2);
			break;
		default:
			break;

		}
		PDChange<TransactionType, InstanceType, RoleType> tempChange;
		tempChange = new PDChange<TransactionType, InstanceType, RoleType>(
				changeType, getTransaction(), instance1, role2, instance2);
		return tempChange;
	}

	/**
	 * Gets the size in bytes of PDStore's persistent binary representation of
	 * this change, as used in the B-tree index.
	 * 
	 * NOTE: changes containing blobs have a different size in the log, as the
	 * log stores the full blob data but the index simply refers to the data in
	 * the log.
	 * 
	 * @return
	 */
	public long getSize() {
		return 1 /* change header (ChangeType and PrimitiveTypes) */
				+ PrimitiveType.length(getTransaction())
				+ PrimitiveType.length(getInstance1())
				+ PrimitiveType.length(getRole2())
				+ PrimitiveType.length(getInstance2());
	}

	/**
	 * Reads an encoded instance of the given primitive type from the given
	 * ByteBuffer at current buffer position.
	 * 
	 * @param type
	 *            the primitive type of the instance to read
	 * @param blobFileName
	 * @return the Object representing the read instance
	 */
	static Object readInstance(PrimitiveType type, ByteBuffer buffer,
			String blobFileName) {
		switch (type) {
		case GUID: {
			long time = buffer.getLong();
			long clockSeqAndNode = buffer.getLong();
			return new GUID(time, clockSeqAndNode);
		}
		case INTEGER: {
			return buffer.getLong();
		}
		case DOUBLE: {
			return buffer.getDouble();
		}
		case STRING: {
			// TODO refer to long strings in the log instead of inlining, as for
			// blobs
			long length = buffer.getLong();
			byte[] bytes = new byte[(int) length];
			buffer.get(bytes, 0, (int) length);
			return new String(bytes);
		}
		case BLOB: {
			long length = buffer.getLong();
			// if the blob is small, it is inlined
			if (length <= PrimitiveType.MAX_VALUE_INLINE_SIZE) {
				byte[] bytes = new byte[(int) length];
				buffer.get(bytes, 0, (int) length);
				return new Blob(bytes);
			} else {
				// otherwise it is only referred to in the log
				long positionInLog = buffer.getLong();
				return new Blob(blobFileName, positionInLog, length);
			}
		}
		case BOOLEAN: {
			return (buffer.get() != 0);
		}
		case TIMESTAMP: {
			return new Timestamp(buffer.getLong());
		}
		}
		throw new RuntimeException("Reading of instances of type " + type
				+ " not implemented.");
	}

	/**
	 * Encodes and writes the given instance to the given buffer at the current
	 * buffer position.
	 * 
	 * @param instance
	 *            the instance to write
	 */
	static void writeInstance(Object instance, ByteBuffer buffer) {
		switch (PrimitiveType.typeOf(instance)) {
		case GUID: {
			GUID guid = (GUID) instance;
			buffer.putLong(guid.getTime());
			buffer.putLong(guid.getClockSeqAndNode());
			break;
		}
		case INTEGER: {
			if (instance instanceof Integer)
				instance = new Long((Integer) instance);
			buffer.putLong((Long) instance);
			break;
		}
		case DOUBLE: {
			buffer.putDouble((Double) (instance));
			break;
		}
		case STRING: {
			byte[] bytes = ((String) instance).getBytes();
			buffer.putLong(bytes.length);
			buffer.put(bytes, 0, bytes.length);
			break;
		}
		case BLOB: {
			Blob blob = (Blob) instance;

			// if the blob is small, inline it
			if (blob.getLength() <= PrimitiveType.MAX_VALUE_INLINE_SIZE) {
				buffer.putLong(blob.getLength());
				buffer.put(blob.getData(), 0, (int) blob.getLength());
			} else {
				// otherwise only refer to it in the log
				buffer.putLong(blob.getLength());
				buffer.putLong(blob.getPosition());
			}
			break;
		}
		case BOOLEAN: {
			boolean b = (Boolean) instance;
			buffer.put((byte) ((b) ? 1 : 0));
			break;
		}
		case TIMESTAMP: {
			buffer.putLong(((Timestamp) instance).getTime());
			break;
		}
		default:
			throw new RuntimeException("Writing of instances of type "
					+ PrimitiveType.typeOf(instance) + " not implemented.");
		}
	}
	
	

	/**
	 * Reads a change from an index buffer.
	 * 
	 * @param buffer
	 *            the buffer of the index with the cursor at the right position
	 * @param blobFileName
	 *            name of the file where long blobs etc. are stored (typically
	 *            the log file)
	 * @return
	 */
	public static PDChange<GUID, Object, GUID> read(ByteBuffer buffer,
			String blobFileName) {
		// the change data is read in the following order: change header, role2,
		// instance1, instance2, transactions

		// read the header of this change
		byte header = buffer.get();

		// test bit 7 as marker for last change in transaction
		// note: this info is irrelevant for the index, only used for the log
		boolean transactionFinished = ((header & 0x80) != 0);

		// test bit 6 as change type
		ChangeType changeType;
		if ((header & 0x40) != 0)
			changeType = ChangeType.LINK_ADDED;
		else
			changeType = ChangeType.LINK_REMOVED;

		// read bits 3-5 as instance1 primitive type
		PrimitiveType type1 = PrimitiveType.typeForCode((header >>> 3) & 0x07);

		// read bits 0-2 as instance2 primitive type
		PrimitiveType type2 = PrimitiveType.typeForCode(header & 0x07);

		// read instance1, role2, instance 2
		GUID role2 = (GUID) readInstance(PrimitiveType.GUID, buffer,
				blobFileName);
		Object instance1 = readInstance(type1, buffer, blobFileName);
		Object instance2 = readInstance(type2, buffer, blobFileName);
		GUID transaction = (GUID) readInstance(PrimitiveType.GUID, buffer,
				blobFileName);

		return new PDChange<GUID, Object, GUID>(changeType, transaction,
				instance1, role2, instance2);
	}

	/**
	 * Write the given change to the current changelog file position. Changes
	 * must only be written after a transaction header.
	 * 
	 * @param change
	 *            the change to write
	 * @param isLastChange
	 *            flag that must be false for all changes except the last one in
	 *            the transaction
	 * @throws IOException
	 */
	public void write(ByteBuffer buffer) {
		Object instance1 = getInstance1();
		Object role2Id = getRole2();
		Object instance2 = getInstance2();

		Debug.assertTrue(instance1 != null,
				"Instance1 of a link cannot be null!");
		Debug.assertTrue(role2Id != null, "Role2 of a link cannot be null!");
		Debug.assertTrue(instance2 != null,
				"Instance2 of a link cannot be null!");

		int header = 0;

		// TODO use bit 7 as prefix compression flag

		// set bit 6 as change type
		if (getChangeType() == ChangeType.LINK_ADDED)
			header = header | 0x40;
		else if (getChangeType() == ChangeType.LINK_REMOVED)
			;

		// set bits 3-5 as instance1 primitive type
		int type1 = PrimitiveType.typeOf(instance1).ordinal();
		header = header | (type1 << 3);

		// set bits 0-2 as instance2 primitive type
		int type2 = PrimitiveType.typeOf(instance2).ordinal();
		header = header | type2;

		// write change header, role2, instance1, instance2
		buffer.put((byte) header);
		writeInstance(role2Id, buffer);
		writeInstance(instance1, buffer);
		writeInstance(instance2, buffer);
		writeInstance(getTransaction(), buffer);
	}

	/**
	 * Compare method that defines the order used in the B-tree index. Note:
	 * This method assumes that the changes are normalized.
	 * 
	 * @param arg0
	 * @return
	 */
	@Override
	public int compareTo(Object arg0) {
		// TODO this assumes specific type parameters; this could be fixed if
		// all type parameters implemented Comparable
		PDChange<GUID, Object, GUID> change = (PDChange<GUID, Object, GUID>) arg0;

		// compare role2
		if (getRole2() == null) {
			if (change.getRole2() == null)
				return 0;
			else
				return -1; // null is smaller than any value
		}
		int order = ((Comparable) getRole2()).compareTo(change.getRole2());
		if (order != 0)
			return order;

		// compare instance1
		// handle null cases
		if (getInstance1() == null) {
			if (change.getInstance1() == null)
				return 0;
			else
				return -1; // null is smaller than any value
		}
		// if the instances are of different types, the order is defined by
		// their types
		order = PrimitiveType.typeOf(getInstance1()).compareTo(
				PrimitiveType.typeOf(change.getInstance1()));
		if (order != 0)
			return order;
		// otherwise use normal element order
		order = ((Comparable) getInstance1()).compareTo(change.getInstance1());
		if (order != 0)
			return order;

		// compare instance2
		// handle null cases
		if (getInstance2() == null) {
			if (change.getInstance2() == null)
				return 0;
			else
				return -1; // null is smaller than any value
		}
		// if the instances are of different types, the order is defined by
		// their types
		order = PrimitiveType.typeOf(getInstance2()).compareTo(
				PrimitiveType.typeOf(change.getInstance2()));
		if (order != 0)
			return order;
		// otherwise use normal element order
		order = ((Comparable) getInstance2()).compareTo(change.getInstance2());
		if (order != 0)
			return order;

		// compare transaction
		if (getTransaction() == null) {
			if (change.getTransaction() == null)
				return 0;
			else
				return -1; // null is smaller than any value
		}
		order = ((Comparable) getTransaction()).compareTo(change
				.getTransaction());
		if (order != 0)
			return -order; // reverse time order necessary for query processing

		// compare change type
		if (getChangeType() == null) {
			if (change.getChangeType() == null)
				return 0;
			else
				return -1; // null is smaller than any value
		}
		order = getChangeType().compareTo(change.getChangeType());
		if (order != 0)
			return order;

		return 0;
	}

	/**
	 * Normalize the change instances (e.g. convert Integer to Long).
	 * TODO: move that into constructor
	 */
	public void normalize() {
		setInstance1((InstanceType) PrimitiveType.normalize(getInstance1()));
		setInstance2((InstanceType) PrimitiveType.normalize(getInstance2()));
	}

	/**
	 * rewrites query with incoming assignment
	 * 
	 * @param preAssignedVariables
	 * @param queryOptimizerBase TODO
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Set<Variable> bindValuesToVariables(Map<Variable, InstanceType> preAssignedVariables) {
		Set<Variable> tempassignedVariables = new HashSet<Variable>();
	
		for (Entry<Variable, InstanceType> var : preAssignedVariables.entrySet()) {
			if (getInstance1() instanceof Variable
					&& getInstance1() == var.getKey()) {
				setInstance1(var.getValue());
				tempassignedVariables.add(var.getKey());
			} else if (getInstance2() instanceof Variable
					&& getInstance2() == var.getKey()) {
				setInstance2(var.getValue());
				tempassignedVariables.add(var.getKey());
			} else if (getRole2() instanceof Variable
					&& getRole2() == var.getKey()) {
				setRole2((RoleType) var.getValue());
				tempassignedVariables.add(var.getKey());
			} else if (getTransaction() instanceof Variable
					&& getTransaction() == var.getKey()) {
				setTransaction((TransactionType) var.getValue());
				tempassignedVariables.add(var.getKey());
			}
		}
		return tempassignedVariables;
	}
}
