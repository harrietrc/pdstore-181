package pdstore.changeindex;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import pdstore.PDChange;
import pdstore.PDStore;
import pdstore.generic.Pairable;

public class IndexEntry<TransactionType extends Comparable<TransactionType>, InstanceType, RoleType extends Pairable<RoleType>>
		implements Serializable, Iterable<PDChange<TransactionType, InstanceType, RoleType>> {

	public IndexEntry() {
		super();
		list = new CopyOnWriteArrayList<PDChange<TransactionType, InstanceType, RoleType>>();
	}

	private static final long serialVersionUID = 597398460521194000L;

	private List<PDChange<TransactionType, InstanceType, RoleType>> list;

	public List<PDChange<TransactionType, InstanceType, RoleType>> getChangeList() {
		return list;
	}

	public boolean add(PDChange<TransactionType, InstanceType, RoleType> e) {
		return list.add(e);
	}

	public boolean remove(Object o) {
		return list.remove(o);
	}

	@Override
	public Iterator<PDChange<TransactionType, InstanceType, RoleType>> iterator() {
		return list.iterator();
	}
}
