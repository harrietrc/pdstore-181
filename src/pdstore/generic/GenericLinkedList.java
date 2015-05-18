package pdstore.generic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * A generic linked list of instances in PDStore. A parent instance is linked to
 * the list elements through a "collection" role. The instances that are the
 * list elements are linked to each other using a "next" role, which defines
 * their order. Note that each instance can only appear at most once in a list.
 * 
 * @author clut002
 * 
 * @param <TransactionType>
 * @param <InstanceType>
 * @param <RoleType>
 * @param <E>
 */
public class GenericLinkedList<TransactionType extends Comparable<TransactionType>, InstanceType, RoleType extends Pairable<RoleType>, E extends InstanceType>
		implements Serializable, Iterable<E>, List<E> {

	private static final long serialVersionUID = 1L;

	public GenericPDStore<TransactionType, InstanceType, RoleType> store;
	InstanceType parentInstance;
	RoleType collectionRole;
	RoleType nextRole;

	public InstanceType getParentInstance() {
		return parentInstance;
	}

	public void setParentInstance(InstanceType parentInstance) {
		this.parentInstance = parentInstance;
	}

	public RoleType getCollectionRole() {
		return collectionRole;
	}

	public void setCollectionRole(RoleType collectionRole) {
		this.collectionRole = collectionRole;
	}

	public RoleType getNextRole() {
		return nextRole;
	}

	public void setNextRole(RoleType nextRole) {
		this.nextRole = nextRole;
	}

	public GenericPDStore<TransactionType, InstanceType, RoleType> getStore() {
		return store;
	}

	/**
	 * Constructor
	 * 
	 * @param parentInstance
	 *            the instance linking to each element in the list
	 * @param collectionRole
	 *            the role that is used to link the parent and each element in
	 *            the list
	 * @param nextRole
	 *            the role that is used between list elements to define their
	 *            order
	 */
	public GenericLinkedList(GenericPDStore<TransactionType, InstanceType, RoleType> store,
			InstanceType parentInstance, RoleType collectionRole, RoleType nextRole) {
		this.store = store;
		this.parentInstance = parentInstance;
		this.collectionRole = collectionRole;
		this.nextRole = nextRole;
	}

	/**
	 * Adds an element to the end of the list.
	 * 
	 * @param element
	 *            the element to add to the list
	 */
	public boolean add(E element) {
		// TODO find a way to handle exceptions
		add(size(), element);
		return true;
	}

	/**
	 * Add an element at a given index in the list.
	 * 
	 * @param index
	 *            the position to add the element at
	 * @param element
	 *            the element to add to the list
	 * @throws IndexOutOfBoundsException
	 *             if the index is not contained in the current list
	 */
	public void add(int index, E element) {
		List<E> sorted = sortList();
		int size = sorted.size();

		if (contains(element) || element == null) {
			throw new IndexOutOfBoundsException();
			// TODO: change exception type
		}

		if (index > size + 1 || index < 0) {
			throw new IndexOutOfBoundsException();
		}

		// link element to history
		store.addLink(parentInstance, collectionRole, element);

		if (size == 0) {
			// first instance in list, nothing else in list to link too
			; // do nothing else
		} else if (index == size) {
			// add to end of list
			store.addLink(sorted.get(size - 1), nextRole, element);
		} else if (index == 0 && size != 0) {
			// add to front of the list
			store.addLink(element, nextRole, sorted.get(0));
		} else {
			// add to middle of the list
			E before = sorted.get(index - 1);
			E after = sorted.get(index);
			store.removeLink(before, nextRole, after);
			store.addLink(before, nextRole, element);
			store.addLink(element, nextRole, after);
		}
	}

	/**
	 * Tests if a the list contains the given element.
	 * 
	 * @param element
	 *            the element to search for
	 * @return true if the list contains the element, false otherwise
	 */
	public boolean contains(Object element) {
		List<E> sortedList = sortList();
		return sortedList.contains(element);
	}

	/**
	 * Gets the element at the given index.
	 * 
	 * @param index
	 *            the position of the element
	 * @return the element at the given index
	 */
	public E get(int index) {
		return sortList().get(index);
	}

	/**
	 * Returns the index of the given element in the list.
	 * 
	 * @param element
	 *            the element to get the index of
	 * @return the index of the element, or -1 if the element is not in the list
	 */
	public int indexOf(Object element) {
		List<E> sorted = sortList();
		for (int i = 0; i < sorted.size(); i++) {
			if (sorted.get(i).equals(element))
				return i;
		}
		return -1;
	}

	/**
	 * Tests if the list is empty.
	 * 
	 * @return true if the list is empty, false otherwise
	 */
	public boolean isEmpty() {
		E anElement = (E) store.getInstance(parentInstance, collectionRole);
		return anElement == null;
	}

	/**
	 * Removes the element at the given index from the list.
	 * 
	 * @param index
	 *            the index of the element to remove
	 * @return 
	 * @throws IndexOutOfBoundsException
	 */
	public synchronized E remove(int index) {
		List<E> sorted = sortList();
		int size = sorted.size();

		if (index >= size || index < 0) {
			throw new IndexOutOfBoundsException("invalid index, can not perform remove");
		}
		E element = sorted.get(index);

		// remove link between the parent instance and the element
		store.removeLink(parentInstance, collectionRole, element);

		if (index > 0) {
			// remove "next" link from the element
			store.removeLink(element, nextRole.getPartner(), sorted.get(index - 1));
		}
		if (index < size - 1) {
			// remove "previous" link from the element
			store.removeLink(element, nextRole, sorted.get(index + 1));
		}
		if (index != 0 && index != size - 1) {
			// link the previous and the next elements
			store.addLink(sorted.get(index - 1), nextRole, sorted.get(index + 1));
		}
		return element;
	}

	/**
	 * Remove the given element from the list.
	 * 
	 * @param element
	 *            the element to remove
	 * @return 
	 * @throws IndexOutOfBoundsException
	 */
	public synchronized boolean remove(Object element) {
		List<E> sorted = sortList();
		for (int i = 0; i < sorted.size(); i++) {
			if (sorted.get(i).equals(element)) {
				remove(i);
				return true;
			}
		}
		throw new IndexOutOfBoundsException("Element is not contained in the list, cannot remove");
	}

	/**
	 * Return the number of elements in the list
	 * 
	 * @return the number of elements in the list
	 */
	public synchronized int size() {
		return sortList().size();
	}

	/**
	 * Sorts the elements of the list as represented in PDStore into a Java
	 * List.
	 * 
	 * @return a list of elements
	 */
	private List<E> sortList() {
		List<E> sortedList = new ArrayList<E>();

		// get any one of the elements in the list
		E anElement = (E) store.getInstance(parentInstance, collectionRole);

		if (anElement == null)
			return sortedList;

		sortedList.add(anElement);

		// add the elements following after anElement in order
		E nextElement;
		while ((nextElement = nextElement(sortedList)) != null)
			sortedList.add(nextElement);

		// add the elements preceding before anElement in order
		E previousElement;
		while ((previousElement = previousElement(sortedList)) != null)
			sortedList.add(0, previousElement);

		return sortedList;
	}

	private E nextElement(List<E> sortedList) {
		E lastElement = sortedList.get(sortedList.size() - 1);
		E next = (E) store.getInstance(lastElement, nextRole);
		return next;
	}

	private E previousElement(List<E> sortedList) {
		E firstElement = sortedList.get(0);
		E previous = (E) store.getInstance(firstElement, nextRole.getPartner());
		return previous;
	}

	/**
	 * Iterator method
	 * 
	 * @return a new iterator for the list
	 */
	@Override
	public Iterator<E> iterator() {
		return new GenericLinkListIterator(sortList());
	}

	/**
	 * Iterator class for the linked list.
	 * 
	 */
	class GenericLinkListIterator implements Iterator<E> {

		private Iterator<E> state;
		private boolean hasNext = false;
		private E nextInstance = null;

		/**
		 * Constructor for the iterator
		 * 
		 * @param sorted
		 *            , a list of ordered elements
		 */
		GenericLinkListIterator(List<E> sorted) {
			this.state = sorted.iterator();
			calcNext();
		}

		@Override
		public boolean hasNext() {
			return this.hasNext;
		}

		@Override
		public E next() {
			E ret = nextInstance;
			calcNext();
			return ret;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();

		}

		private void calcNext() {
			this.hasNext = false;
			while (!hasNext && state.hasNext()) {
				this.nextInstance = state.next();
				this.hasNext = true;
			}
		}

	}

	@Override
	public Object[] toArray() {
		throw new NotImplementedException();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		throw new NotImplementedException();
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		throw new NotImplementedException();
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		throw new NotImplementedException();
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		throw new NotImplementedException();
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		throw new NotImplementedException();
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new NotImplementedException();
	}

	@Override
	public void clear() {
		throw new NotImplementedException();
		
	}

	@Override
	public E set(int index, E element) {
		throw new NotImplementedException();
	}

	@Override
	public int lastIndexOf(Object o) {
		throw new NotImplementedException();
	}

	@Override
	public ListIterator<E> listIterator() {
		throw new NotImplementedException();
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		throw new NotImplementedException();
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		throw new NotImplementedException();
	}
}
