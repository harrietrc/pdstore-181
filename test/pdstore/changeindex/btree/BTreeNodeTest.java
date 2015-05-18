package pdstore.changeindex.btree;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

import pdstore.Blob;
import pdstore.ChangeType;
import pdstore.GUID;
import pdstore.PDChange;

public class BTreeNodeTest {

	@Test
	public final void testReadWrite1() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HHmmss");
		Date date = new Date();
		String fileName = "pddata/BTreeNodeTest-testReadWrite1-"
				+ dateFormat.format(date) + ".pdx";

		FileCache cache = new FileCache(fileName);

		// load root node
		BTreeNode node = BTreeNode.load(cache, 1, null);
		assertEquals(1, node.fileBlockIndex);
		assertEquals(BTreeNode.HEADER_SIZE, node.getSize());
		assertEquals(0, node.getLeftChildNodeIndex());

		// write and reload node
		node.writeToCache();
		node = BTreeNode.load(cache, 1, null);
		assertEquals(BTreeNode.HEADER_SIZE, node.getSize());
		assertEquals(0, node.getLeftChildNodeIndex());
	}

	@Test
	public final void testReadWrite2() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HHmmss");
		Date date = new Date();
		String fileName = "pddata/BTreeNodeTest-testReadWrite2-"
				+ dateFormat.format(date) + ".pdx";

		FileCache cache = new FileCache(fileName);

		// load root node
		BTreeNode node = BTreeNode.load(cache, 1, null);
		assertEquals(1, node.fileBlockIndex);
		assertEquals(BTreeNode.HEADER_SIZE, node.getSize());
		assertEquals(0, node.getLeftChildNodeIndex());

		// add changes to node
		GUID transaction1 = new GUID();
		GUID instanceA1 = new GUID();
		GUID roleB1 = new GUID();
		GUID instanceB1 = new GUID();
		PDChange<GUID, Object, GUID> change1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transaction1, instanceA1, roleB1,
				instanceB1);
		node.addChangeAndPointer(new ChangeAndPointer(change1, 0));
		assertEquals(BTreeNode.HEADER_SIZE + 1 + 4 * 16 + 4, node.getSize());
		assertEquals(0, node.getLeftChildNodeIndex());
		assertEquals(change1, node.changesAndPointers.get(0).change);

		GUID transaction2 = new GUID();
		GUID instanceA2 = new GUID();
		GUID roleB2 = new GUID();
		GUID instanceB2 = new GUID();
		PDChange<GUID, Object, GUID> change2 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transaction2, instanceA2, roleB2,
				instanceB2);
		node.addChangeAndPointer(new ChangeAndPointer(change2, 0));
		assertEquals(BTreeNode.HEADER_SIZE + 2 * (1 + 4 * 16 + 4),
				node.getSize());
		assertEquals(0, node.getLeftChildNodeIndex());
		assertEquals(change1, node.changesAndPointers.get(0).change);

		// write and reload node
		node.writeToCache();
		node = BTreeNode.load(cache, 1, null);
		assertEquals(BTreeNode.HEADER_SIZE + 2 * (1 + 4 * 16 + 4),
				node.getSize());
		assertEquals(0, node.getLeftChildNodeIndex());
		assertEquals(change1, node.changesAndPointers.get(0).change);
	}
}
