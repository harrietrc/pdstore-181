package diagrameditor;

import java.util.Iterator;

import nz.ac.auckland.se.genoupe.tools.MapIterator;
import pdstore.GUID;
import pdstore.PDStore;
import pdstore.generic.GenericLinkedList;
import diagrameditor.dal.PDDiagram;
import diagrameditor.dal.PDDiagramSet;

/**
 * Extends GenericLinkedList and implements diagram editor specific methods.
 * 
 */
public class DiagramList implements Iterable<PDDiagram> {

	private boolean debug = false;
	private static final long serialVersionUID = 1L;

	PDStore store;
	GenericLinkedList<GUID, Object, GUID, GUID> list;

	public DiagramList(PDStore store, PDDiagramSet diagramSet, GUID collectionRole, GUID nextRole) {
		this.store = store;
		list = new GenericLinkedList<GUID, Object, GUID, GUID>(store, diagramSet.getId(), collectionRole, nextRole);
	}

	/**
	 * Method to move an operation up or down in the list. Will jump commutative
	 * blocks of operations.
	 * 
	 * @param diagram
	 * @param down
	 */
	public void move(PDDiagram diagram, boolean down) {
		int steps = 1;

		// remove selected operation
		int index = list.indexOf(diagram.getId());
		list.remove(index);

		// add selected operation into new position
		if (down) {
			list.add(index + steps, diagram.getId());
		} else {
			list.add(index - steps, diagram.getId());
		}

		store.commit();
	}

	public Iterator<PDDiagram> iterator() {
		return new MapIterator<GUID, PDDiagram>(list.iterator()) {
			public PDDiagram map(GUID nextInBase) {
				return PDDiagram.load(store, nextInBase);
			}
		};
	}

	public void add(PDDiagram element) {
		list.add(element.getId());
	}

	public void remove(PDDiagram element) {
		list.remove(element.getId());
	}

	public int size() {
		return list.size();
	}

	public PDDiagram get(int i) {
		return PDDiagram.load(store, list.get(i));
	}
}
