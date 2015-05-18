package pdstore;

import java.util.Collection;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Before;

import pdstore.generic.PDSorter;
import pdstore.generic.SortByOrder;
import diagrameditor.dal.PDDiagram;
import diagrameditor.dal.PDOperationApplication;

/**
 * Testing SortByOrder Implementation of PDSorter. Gets an unordered collection
 * of PDStore instances and sorts them on a specific role type which links them
 * together in a list.
 * 
 * @author cbue001
 * 
 */
public class SortByOrderTest extends TestCase {

	public PDStore store;

	PDOperationApplication op1, op2, op3, op4, op5;
	GUID histGUID;
	GUID transaction;

	static final String[] OP_NAMES = { "Operation 1", "Operation 2",
			"Operation 3", "Operation 4", "Operation 5" };

	@Before
	public void setUp() {
		store = new PDStore("DiagramEditor");

		transaction = store.begin();

		PDDiagram diagram = new PDDiagram(store);
		histGUID = diagram.getId();
		diagram.setName("History");
		diagram.removeOperationApplication();

		op1 = new PDOperationApplication(store);
		op2 = new PDOperationApplication(store);
		op3 = new PDOperationApplication(store);
		op4 = new PDOperationApplication(store);
		op5 = new PDOperationApplication(store);

		op1.setName(OP_NAMES[0]);
		op2.setName(OP_NAMES[1]);
		op3.setName(OP_NAMES[2]);
		op4.setName(OP_NAMES[3]);
		op5.setName(OP_NAMES[4]);

		op1.setNext(op2);
		op2.setNext(op3);
		op3.setNext(op4);
		op4.setNext(op5);

		diagram.addOperationApplication(op1);
		diagram.addOperationApplication(op2);
		diagram.addOperationApplication(op3);
		diagram.addOperationApplication(op4);
		diagram.addOperationApplication(op5);

		store.commit();
	}

	public final void testSortByOrder() {
		PDDiagram hist = (PDDiagram) store.load(PDDiagram.typeId,
				histGUID);
		Collection<PDOperationApplication> unordered = hist.getOperationApplications();

		PDSorter<GUID, Object, GUID> sorter = new SortByOrder<GUID, Object, GUID>(
				transaction, PDOperationApplication.roleNextId);
		List<PDOperationApplication> sorted = sorter.sort(unordered);

		for (int i = 0; i < sorted.size(); i++) {
			assertEquals(OP_NAMES[i], sorted.get(i).getName());
		}
	}
}
