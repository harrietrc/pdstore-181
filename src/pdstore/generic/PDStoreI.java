package pdstore.generic;

import java.util.Collection;
import java.util.List;

import pdstore.*;

/**
 * PDStoreI is the interface that introduces three aspects of higher
 * sophistication, if compared to PDCore: - updates can be performed one-by-one
 * using a typical transactional interface with begin/commit. - additionally the
 * PDStore innovations of branch management (branch and merge) are available. -
 * the updates can be performed based on individual parameters, instead of using
 * change objects. - basic queries can be performed in a form that lends itself
 * to index-based implementations.
 * 
 * @author gweb017
 * 
 * @param <TransactionType>
 * @param <InstanceType>
 * @param <RoleType>
 */
public interface PDStoreI<TransactionType extends Comparable<TransactionType>, InstanceType, RoleType extends Pairable<RoleType>>
		extends PDCoreI<TransactionType, InstanceType, RoleType> {

	/**
	 * Begins a new transaction. All the parameters are given in the Transaction
	 * object. The transaction gets a temporary ID that is representing the
	 * begin timestamp.
	 * 
	 * @param transaction
	 *            object containing all transaction parameters
	 * @return the begin-ID of the new transaction
	 */
	public TransactionType begin(
			Transaction<TransactionType, InstanceType, RoleType> transaction);

	// TODO consider using class Transaction for all methods such as addLink,
	// commit to identify transaction instead of GUID (will also simplify
	// transaction management)

	/**
	 * Begins a new transaction using default parameters for branch, isolation
	 * level etc.
	 * 
	 * @return the begin-ID of the new transaction
	 */
	public TransactionType begin();

	/**
	 * Commits the current transaction and starts a new transaction. Until
	 * commit() is called no other transactions can see the changes of the
	 * current transaction. If commit() is not called at all, the changes are
	 * lost. Only at commit the durable transaction ID can be returned, since
	 * this transaction ID contains a timestamp. The order of the durable
	 * transaction ID is consistent with the serialization history, that is a
	 * transaction only sees changes of earlier transactions, and later
	 * transactions see the changes of this transaction.
	 * 
	 * PDCore does not allow empty transactions to be committed, the transaction
	 * will fail.
	 * 
	 * @return the durable transaction ID, its timestamp is consistent with the
	 *         serialization history. null if the transaction failed to commit.
	 */
	public TransactionType commit(TransactionType transaction);

	/**
	 * Rolls the transaction with the given ID back.
	 * 
	 * @param transaction
	 *            ID of transaction to roll back
	 */
	public void rollback(TransactionType transaction);

	public void addChange(
			PDChange<TransactionType, InstanceType, RoleType> change);

	/**
	 * Queries for changes in the database that match the given change template.
	 * 
	 * @param change
	 *            a PDChange acting as template for matching the triples
	 *            returned by this method
	 * @return a collection of the matching triples in the database
	 * @throws PDStoreException
	 */
	Collection<PDChange<TransactionType, InstanceType, RoleType>> getChanges(
			PDChange<TransactionType, InstanceType, RoleType> change);
}
