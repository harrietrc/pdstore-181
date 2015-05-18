package pdstore.sparql;

import nz.ac.auckland.se.genoupe.tools.Debug;
import nz.ac.auckland.se.genoupe.tools.L;
import pdstore.PDChange;
import pdstore.PDStore;
import pdstore.generic.GenericPDStore;
import pdstore.generic.PDStoreI;
import pdstore.generic.Pairable;

/**
 * Instances of this class fulfil the interface of a filter, but have the
 * purpose to create side effects. It looks like a FilterChange, i.e. a filter
 * that checks for the existence of a Change. Instead of checking for the
 * existence of the change, it simply adds this change. Since PDStore is
 * changebased, we can realize insertions, as well as deletions in this way.
 * 
 * @author gweb017
 * 
 * @param <TransactionType>
 * @param <InstanceType>
 * @param <RoleType>
 */
public class InsertionPseudoFilter<TransactionType extends Comparable<TransactionType>, InstanceType, RoleType extends Pairable<RoleType>>
		extends FilterChange<TransactionType, InstanceType, RoleType> {

	public static final String DEBUG_TOPIC = "InsertionPseudoFilter";
	private TransactionType insertTransaction = null;

    public static boolean printInsert = false;

	public long countInserts = 0;

	public TransactionType getInsertTransaction() {
		return insertTransaction;
	}

	/**
	 * The pseudofilter can add the changes to a different transaction than the
	 * one where the query is executed. This is done by using this method.
	 * 
	 * @param insertTransaction
	 *            the transaction where the change will be added.
	 */
	public void setInsertTransaction(TransactionType insertTransaction) {
		this.insertTransaction = insertTransaction;
	}

	@Override
	protected boolean processChange(
			PDChange<TransactionType, InstanceType, RoleType> change) {
		// check if the side effect should go to a different transaction
		PDChange<TransactionType, InstanceType, RoleType> changeToAdd = new PDChange<TransactionType, InstanceType, RoleType>(
				change);
		Debug.println(L.s("pseudofilter called for change: " , changeToAdd),			this.DEBUG_TOPIC);
		Debug.assertTrue(!change.isTemplate(), "tried to insert template");
		// TODO: the following if can be simplified by computing the correct transaction first.
		if (insertTransaction != null) {
			changeToAdd.setTransaction(insertTransaction);

			// add change only if it doesn't exist yet
			if (!store.getChanges(changeToAdd).iterator().hasNext()) {
				store.addChange(changeToAdd);
				++countInserts;
				Debug.println(L.s("pseudofilter inserted change: " , changeToAdd),	this.DEBUG_TOPIC);
			}
		} else {

			// For transaction null, each added change is in an own transaction
			TransactionType t = store.begin();
			changeToAdd.setTransaction(t);

			// add change only if it doesn't exist yet
			if (!store.getChanges(changeToAdd).iterator().hasNext()) {
				store.addChange(changeToAdd);
				++countInserts;
				Debug.println(L.s("pseudofilter inserted change: " , changeToAdd),	this.DEBUG_TOPIC);
			}
			store.commit(t);
		}

        if (printInsert) System.err.print("!");

		// The side effect, the core function of the pseudofilter: adding the change.
		// A pseudofilter always returns a constant result.
		return isNotNegated();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InsertionPseudoFilter(
			PDChange<TransactionType, InstanceType, RoleType> template,
			PDStoreI<TransactionType, InstanceType, RoleType> store) {
		super(template, store, true);
		// Auto-generated constructor stub
	}

}
