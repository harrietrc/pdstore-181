package pdstore.changeindex;

import java.util.Iterator;

import nz.ac.auckland.se.genoupe.tools.Debug;
import nz.ac.auckland.se.genoupe.tools.StatefulIterator;
import pdstore.PDChange;
import pdstore.generic.GlobalTypeAdapter;
import pdstore.generic.Pairable;
import pdstore.generic.TypeAdapter;

/**
 * Given an iterator of sorted changes starting with a matching change, this
 * implements an iterator of changes that match the given pattern.
 * 
 * @author Christof
 * 
 * @param <TransactionType>
 * @param <InstanceType>
 * @param <RoleType>
 */
public class PatternIterator<TransactionType extends Comparable<TransactionType>, InstanceType, RoleType extends Pairable<RoleType>>
		extends
		StatefulIterator<PDChange<TransactionType, InstanceType, RoleType>> {

	/**
	 * The adaptor object that encapsulates all operations on generic arguments.
	 */
	public final TypeAdapter<TransactionType, InstanceType, RoleType> typeAdaptor = (TypeAdapter<TransactionType, InstanceType, RoleType>) GlobalTypeAdapter.typeAdapter;

	Iterator<PDChange<TransactionType, InstanceType, RoleType>> input;
	PDChange<TransactionType, InstanceType, RoleType> pattern;

	PatternIterator(
			Iterator<PDChange<TransactionType, InstanceType, RoleType>> input,
			PDChange<TransactionType, InstanceType, RoleType> pattern) {
		this.input = input;
		this.pattern = pattern;
	}

	protected PDChange<TransactionType, InstanceType, RoleType> computeNext() {
		if (!input.hasNext())
			return null;

		PDChange<TransactionType, InstanceType, RoleType> change = input.next();

		Debug.println(change, "patternIterator");

		// TODO think about how to match ChangeType and Transaction; maybe even
		// merge in BranchIterator?
		
		if (pattern.getInstance1() != null
				&& !pattern.getInstance1().equals(change.getInstance1()))
			return null;

		if (pattern.getRole2() != null
				&& !pattern.getRole2().equals(change.getRole2()))
			return null;

		if (pattern.getInstance2() != null
				&& !pattern.getInstance2().equals(change.getInstance2()))
			return null;

		return change;
	}
}
