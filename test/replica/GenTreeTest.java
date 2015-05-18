package replica;

import org.junit.Test;

import static org.junit.Assert.*;

public class GenTreeTest {

    @Test
    public void baseEquality() {
        GenTree<Integer> gt1 = new GenTree<Integer>(1);
        GenTree<Integer> gt2 = new GenTree<Integer>(2);
        GenTree<Integer> gt1p = new GenTree<Integer>(1);

        assertFalse(gt1.equals(gt2));
        assertTrue(gt1.equals(gt1)); // Reflexivity
        assertTrue(gt1.equals(gt1p)); // Non-trivial path

        // Ordering semantics
        gt1.addChild(new GenTree<Integer>(2));
        gt2.addChild(new GenTree<Integer>(1));

        assertFalse(gt1.equals(gt2));
    }
}
