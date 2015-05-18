/**
 * 
 */
package pdstore.changeindex;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import nz.ac.auckland.se.genoupe.tools.ConcatenationIterator;
import nz.ac.auckland.se.genoupe.tools.Debug;
import nz.ac.auckland.se.genoupe.tools.FilterIterator;
import nz.ac.auckland.se.genoupe.tools.IteratorBasedCollection;
import nz.ac.auckland.se.genoupe.tools.MapIterator;
import nz.ac.auckland.se.genoupe.tools.ReverseListIterator;
import pdstore.ChangeType;
import pdstore.GUID;
import pdstore.PDChange;
import pdstore.PDStoreException;
import pdstore.Transaction;
import pdstore.changeindex.btree.BTreeIndex;
import pdstore.generic.ChangeTemplateKind;
import pdstore.generic.GlobalTypeAdapter;
import pdstore.generic.PDStoreI;
import pdstore.generic.TypeAdapter;
import pdstore.notify.PDListener;
import pdstore.sparql.Query;
import pdstore.sparql.Variable;

/**
 * A genericPDStore that caches result sets in Maps. Stores the history of
 * changes and can therefore answer historic queries correctly. This class is
 * usually accessed through the class LogAndIndexStore, so most of the relevant
 * documentation can be found there.
 * 
 * @author Christof, Gerald
 */
public class BTreeIndexStore extends GenericIndexStore<GUID, Object, GUID> {

	/**
	 * The adaptor object that encapsulates all operations on generic arguments.
	 */
	public final TypeAdapter<GUID, Object, GUID> typeAdapter = GlobalTypeAdapter.typeAdapter;

	private BTreeIndex changeIndex;
	private PDStoreI<GUID, Object, GUID> concurrentStore;

	/**
	 * Pre-loads the indices with all changes from the given core.
	 * 
	 * @param core
	 *            the core whose changes will be added to the indices
	 * @param fullStore
	 *            TODO
	 * @param logFile
	 */
	public BTreeIndexStore(String fileName,
			PDStoreI<GUID, Object, GUID> fullStore, String blobFileName) {
		concurrentStore = fullStore;
		changeIndex = new BTreeIndex(fileName, blobFileName);
	}

	/**
	 * Adds the given change to all the indices, if it is not flagged as being
	 * log-only.
	 * 
	 * @param change
	 *            the change to add to the indices
	 */
	public void addChange(PDChange<GUID, Object, GUID> change) {
		//TODO this can be removed if it is the same as in GenericIndexStore
		
		if (!typeAdapter.isDurable(change.getTransaction())) {
		} else {
			Debug.warningAssertTrue(change.isInIndex(),
					"Change should not have reached here");
		}

		addChangeToIndices(change);

		// The following part of the method ensures that it is registered
		// that instance 1 has a link using role2.
		// This is done in the PS/PO index, using the role constant
		// typeAdapter.USES_ROLE_ROLEID()
		// This logic currently ignores deletions, since the USES_ROLE_ROLEID
		// links
		// are currently not deleted.
		// Currently the code is not optimal but on the safe side.

		// TODO:
		// The check for inserting USES_ROLE links should be
		// only done in the open Transaction store.
		// One possible alternative would be to add the USES_ROLE links
		// to the transaction object. Then they would also be deleted
		// correctly from the open transaction store.
		// Then the following line should be correct here:
		// if(typeAdapter.isDurable(change.getTransaction())) return;

		// a recursive use of plain addChange would lead
		// to the following problem of recursion: A link
		// Bert isFriendOf Ernie
		// leads to the following links being inserted:
		// Bert USES_ROLE isFriendOf
		// Ernie USES_ROLE ~isFriendof (the partner Role).
		// but without stopping recursion, insertion of these
		// links would lead to the following links being
		// inserted as well:
		// Bert USES_ROLE USES_ROLE
		// Ernie USES_ROLE USES_ROLE
		// isFriendOf USES_ROLE ~USES_ROLE
		// ~isFriendOf USES_ROLE ~USES_ROLE
		// and the first time even:
		// USES_ROLE USES_ROLE ~USES_ROLE
		// ~USES_ROLE USES_ROLE ~USES_ROLE
		// USES_ROLE USES_ROLE USES_ROLE
		// ~USES_ROLE USES_ROLE USES_ROLE
		// To stop this we insert the following check:

		// First allow this to be turned off.
		if(!Query.isUsingVariableRoles) return;

		// look if role2 is already registered, and register it if not
		PDChange<GUID, Object, GUID> role2registered = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, // TODO should be LINK_EFFECTIVE?
				change.getTransaction(), change.getInstance1(),
				typeAdapter.USES_ROLE_ROLEID(),
				typeAdapter.instanceFromRole(change.getRole2()));
		Collection<PDChange<GUID, Object, GUID>> result = concurrentStore
				.getChanges(role2registered);
		if (result.isEmpty())
			addChangeToIndices(role2registered);

		// look if role1 is already registered, and register it if not
		PDChange<GUID, Object, GUID> role1registered = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, // TODO should be LINK_EFFECTIVE?
				change.getTransaction(), change.getInstance2(),
				typeAdapter.USES_ROLE_ROLEID(),
				typeAdapter.instanceFromRole(change.getRole2().getPartner()));
		result = concurrentStore.getChanges(role1registered);
		if (result.isEmpty())
			addChangeToIndices(role1registered);
	}

	private void addChangeToIndices(PDChange<GUID, Object, GUID> change) {
		// add change in both direction
		changeIndex.add((PDChange<GUID, Object, GUID>) change);
		changeIndex.add((PDChange<GUID, Object, GUID>) change
				.getPartnerChange());
	}

	@Override
	public GUID addTransaction(Transaction<GUID, Object, GUID> transaction)
			throws PDStoreException {
		//TODO this can be removed if it is the same as in GenericIndexStore
		
		if (transaction.isEmpty())
			return transaction.getId();

		/*
		 * Transaction must be added to index. Make sure that redundant changes
		 * are not added by using AggregationIterator.
		 */
		/*
		 * TODO This is only called for the committed transaction index and
		 * therefore aggregation works here. If we wanted to use the BTreeIndex
		 * for the open transactions, then we would have to remove redundant
		 * changes right when they are added in addChange(). This would be
		 * necessary because (unlike in GenericTransactionStore) the BTreeIndex
		 * destroys the order of changes within a transaction. It replaces it
		 * with the tree order, hence it would not be possible to aggregate the
		 * changes properly within a transaction later on.
		 */
		// get changes of transaction in reverse time order
		Iterator<PDChange<GUID, Object, GUID>> reverseChanges = new ReverseListIterator<PDChange<GUID, Object, GUID>>(
				transaction);
		Iterator<PDChange<GUID, Object, GUID>> aggregatedChanges = new AggregationIterator<GUID, Object, GUID>(
				reverseChanges, false);
		while (aggregatedChanges.hasNext()) {
			PDChange<GUID, Object, GUID> c = aggregatedChanges.next();

			// Changes that do not belong into index are skipped
			if (!c.isInIndex())
				continue;

			addChange(c);
		}
		return transaction.getId();
	}

	@Override
	public GUID begin() {
		throw new UnsupportedOperationException("not intended to be used");
	}

	@Override
	public GUID begin(Transaction<GUID, Object, GUID> transaction) {
		throw new UnsupportedOperationException("not intended to be used");
	}

	public GUID commit(GUID transaction) {
		throw new UnsupportedOperationException("not intended to be used");
	}

	public void rollback(GUID transaction) {
		throw new UnsupportedOperationException("not intended to be used");
	}

	public GUID getRepository() {
		throw new UnsupportedOperationException("not intended to be used");
	}

	public Iterator<PDChange<GUID, Object, GUID>> iterator() {
		/*
		 * Collect reverse iterators for the change lists in all the
		 * IndexEntry's in the linkIndex.
		 * 
		 * Note that this achieves only partial reverse time order, i.e. the
		 * instances with the same instance1 and instance2 are ordered
		 * correctly. However, this is enough for the AggregationIterator that
		 * is used to get only the effective changes: it requires that changes
		 * referring to the same link (i.e. instance1, instance2 and also role2)
		 * are ordered in reverse time. TODO
		 */
		return changeIndex.get(new PDChange<GUID, Object, GUID>(null, null,
				null, null, null));
	}

	public List<PDListener<GUID, Object, GUID>> getDetachedListenerList() {
		throw new UnsupportedOperationException("not intended to be used");
	}

	public List<PDListener<GUID, Object, GUID>> getInterceptorList() {
		throw new UnsupportedOperationException("not intended to be used");
	}

	@Override
	public Collection<PDChange<GUID, Object, GUID>> getChanges(
			PDChange<GUID, Object, GUID> pattern) {

		// First clone the pattern to keep the method non-destructive.
		pattern = new PDChange<GUID, Object, GUID>(pattern);

		/*
		 * The index knows nothing of variables. If there is a variable in the
		 * pattern, then it needs to be replaced with null.
		 */
		if (pattern.getTransaction() instanceof Variable)
			pattern.setTransaction(null);
		if (pattern.getInstance1() instanceof Variable)
			pattern.setInstance1(null);
		if (pattern.getRole2() instanceof Variable)
			pattern.setRole2(null);
		if (pattern.getInstance2() instanceof Variable)
			pattern.setInstance2(null);

		// classify the change pattern
		ChangeTemplateKind templateKind = ChangeTemplateKind.getKind(pattern);

		Debug.println(new Object[] { "GetChanges(", pattern, "), case ",
				templateKind }, "getChanges");
		Debug.printCallStack(3, "getChanges");

		// get the matching information from the change pattern
		final ChangeType changeType = pattern.getChangeType();
		final GUID transaction = pattern.getTransaction();
		final Object instance1 = pattern.getInstance1();
		final GUID role2 = pattern.getRole2();
		final Object instance2 = pattern.getInstance2();

		final boolean isMaximumBranchID = transaction.equals(typeAdapter
				.maxTransactionId(typeAdapter.getBranchID(transaction)));
		if (isMaximumBranchID) {
			Debug.println("starting with pure branchID", "ReadUncommitted");
		}

		/*
		 * Get the changes that match the instance1-role2-instance2 pattern of
		 * the template.
		 * 
		 * Aggregation condition: the changeIterator returns changes referring
		 * to the same link in reverse time order. This is satisfied because all
		 * iterators are obtained as reverse iterators from IndexEntry's in
		 * indexes, where changes referring to the same link are added to the
		 * same IndexEntry in time-order.
		 */
		Iterator<PDChange<GUID, Object, GUID>> changeIterator = null;
		switch (templateKind) {
		case XXX:
			changeIterator = iterator();
			break;
		case XRX:
		case IRX:
		case IRI:
			changeIterator = getXRX(pattern);
			break;
		case XRI:
			changeIterator = getXRX(pattern.getPartnerChange());
			break;
		case IXX:
		case IXI:
			changeIterator = getIXX(pattern);
			break;
		case XXI:
			changeIterator = getIXX(pattern.getPartnerChange());
			break;
		default:
			throw new UnsupportedOperationException("Case " + templateKind
					+ " not supported.");
		}

		// TODO think about the case transaction == null

		// filter the changes that are already committed or part of the given
		// transaction (which may not be committed yet)
		Iterator<PDChange<GUID, Object, GUID>> openTransactionIterator = new FilterIterator<PDChange<GUID, Object, GUID>>(
				changeIterator) {
			public boolean filterCondition(PDChange<GUID, Object, GUID> change) {
				Debug.println(change, "openTransactionIterator");

				// TODO: needs isolationlevel, currently assumes snapshot
				GUID changeTransaction = change.getTransaction();

				// committed changes are always ok, the branchiterator should
				// deal with the differences of the transactionIsolationLevels.
				if (typeAdapter.isDurable(changeTransaction))
					return true;

				// if the open transaction store is queried with a pure branch
				// ID, it returns
				// all open transactions.
				// This is used in the isolationlevels with isDirtyReading()==
				// true
				// Set in GenericConcurrentStore.getchanges()
				Debug.println("testPureBranchId", "ReadUncommitted");
				if (isMaximumBranchID) {
					Debug.println("isPureBranchId", "ReadUncommitted");
					return true;
				}

				// Snapshot isolation: the only open transaction permitted is
				// the one given in the changeTemplate
				if (changeTransaction.equals(transaction))
					return true;

				return false;
			}
		};

		// filter the changes that belong to the requested branch, up to the
		// requested transaction
		Iterator<PDChange<GUID, Object, GUID>> branchIterator = new BranchIterator<GUID, Object, GUID>(
				openTransactionIterator, this, transaction);

		// denormalize the changes, depending on the changeTemplate
		Iterator<PDChange<GUID, Object, GUID>> denormalizeIterator = null;
		switch (templateKind) {
		case XXX:
			// no denormalization necessary
			denormalizeIterator = branchIterator;
			break;
		case IXX:
		case IXI:
			// denormalize by checking instance1
			denormalizeIterator = new MapIterator<PDChange<GUID, Object, GUID>, PDChange<GUID, Object, GUID>>(
					branchIterator) {
				public PDChange<GUID, Object, GUID> map(
						PDChange<GUID, Object, GUID> change) {
					Debug.println(change, "denormalizeIterator");

					// return changes which are already in the right direction
					if (change.getInstance1().equals(instance1))
						return change;

					// change direction of changes that are in the wrong
					// direction
					return change.getPartnerChange();
				}
			};
			break;
		case XRX:
		case IRX:
		case XRI:
		case IRI:
			// denormalize by checking role2
			denormalizeIterator = new MapIterator<PDChange<GUID, Object, GUID>, PDChange<GUID, Object, GUID>>(
					branchIterator) {
				public PDChange<GUID, Object, GUID> map(
						PDChange<GUID, Object, GUID> change) {
					Debug.println(change, "denormalizeIterator");

					// return changes which are already in the right direction
					if (change.getRole2().equals(role2))
						return change;

					// change direction of changes that are in the wrong
					// direction
					return change.getPartnerChange();
				}
			};
			break;
		case XXI:
			// denormalize by checking instance2
			denormalizeIterator = new MapIterator<PDChange<GUID, Object, GUID>, PDChange<GUID, Object, GUID>>(
					branchIterator) {
				public PDChange<GUID, Object, GUID> map(
						PDChange<GUID, Object, GUID> change) {
					Debug.println(change, "denormalizeIterator");

					// return changes which are already in the right direction
					if (change.getInstance2().equals(instance2))
						return change;

					// change direction of changes that are in the wrong
					// direction
					return change.getPartnerChange();
				}
			};
			break;
		default:
			throw new UnsupportedOperationException("Case " + templateKind
					+ " not supported.");
		}

		// aggregate or filter the changes considering the changeType of the
		// template
		Iterator<PDChange<GUID, Object, GUID>> aggregationIterator = null;
		if (changeType == null) {
			// don't filter or aggregate the changes
			aggregationIterator = denormalizeIterator;
		} else if (changeType == ChangeType.LINK_ADDED
				|| changeType == ChangeType.LINK_REMOVED) {
			aggregationIterator = new FilterIterator<PDChange<GUID, Object, GUID>>(
					denormalizeIterator) {
				public boolean filterCondition(
						PDChange<GUID, Object, GUID> change) {
					Debug.println(change, "aggregationIterator");

					// return only the changes with the template ChangeType
					return change.getChangeType() == changeType;
				}
			};
		} else if (changeType == ChangeType.LINK_EFFECTIVE) {
			// Return only the most recent change for each link (could be an add
			// or a remove). These changes are non-redundant, i.e. only changes
			// that have a final effect on the database state remain.
			aggregationIterator = new AggregationIterator<GUID, Object, GUID>(
					denormalizeIterator, false);
		} else {
			throw new UnsupportedOperationException("ChangeType " + changeType
					+ " not supported.");
		}

		// return the changes as an IteratorBasedCollection
		return new IteratorBasedCollection<PDChange<GUID, Object, GUID>>(
				aggregationIterator);
	}

	Iterator<PDChange<GUID, Object, GUID>> getXRX(
			PDChange<GUID, Object, GUID> pattern) {
		Iterator<PDChange<GUID, Object, GUID>> input = changeIndex.get(pattern);
		return new PatternIterator<GUID, Object, GUID>(input, pattern);
	}

	private Iterator<PDChange<GUID, Object, GUID>> getIXX(
			PDChange<GUID, Object, GUID> pattern) {
		// get iterator for all changes that state that the given instance uses
		// a certain role
		PDChange<GUID, Object, GUID> usesRolePattern = new PDChange<GUID, Object, GUID>(
				null, null, pattern.getInstance1(),
				typeAdapter.USES_ROLE_ROLEID(), null);
		Iterator<PDChange<GUID, Object, GUID>> roleChangeIterator = getXRX(usesRolePattern);

		// collect the iterators of the changes that use the given instance and
		// any of the reportedly used roles
		List<Iterator<PDChange<GUID, Object, GUID>>> changeIterators = new ArrayList<Iterator<PDChange<GUID, Object, GUID>>>();
		while (roleChangeIterator.hasNext()) {
			// get the next change linking the given instance to a role
			PDChange<GUID, Object, GUID> roleChange = roleChangeIterator.next();

			// extract the role2 that the instance reportedly uses
			GUID role2;
			if (roleChange.getRole2().equals(typeAdapter.USES_ROLE_ROLEID()))
				role2 = typeAdapter.RoleFromInstance(roleChange.getInstance2());
			else
				role2 = typeAdapter.RoleFromInstance(roleChange.getInstance1());

			// add the iterator for the changes that use the instance1 and the
			// role2 and possibly the instance2 if given
			PDChange<GUID, Object, GUID> patternIRX = new PDChange<GUID, Object, GUID>(
					null, null, pattern.getInstance1(), role2,
					pattern.getInstance2());
			Iterator<PDChange<GUID, Object, GUID>> partialResult = getXRX(patternIRX);
			changeIterators.add(partialResult);
		}

		// concatenate all the collected change iterators into on big iterator
		return new ConcatenationIterator<PDChange<GUID, Object, GUID>>(
				changeIterators);
	}

	/**
	 * Low-level access method that is used in BranchIterator for the high-level
	 * getChanges method. TODO this should probably go away sometime; use getXRX
	 * instead
	 * 
	 * @param instance1
	 * @param role2
	 * @return
	 */
	Collection<PDChange<GUID, Object, GUID>> getChanges(Object instance1,
			GUID role2) {
		PDChange<GUID, Object, GUID> pattern = new PDChange<GUID, Object, GUID>(
				null, null, instance1, role2, null);
		Iterator<PDChange<GUID, Object, GUID>> matches = getXRX(pattern);
		return new IteratorBasedCollection<PDChange<GUID, Object, GUID>>(
				matches);
	}
}