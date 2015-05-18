package pdstore.deduction;

import nz.ac.auckland.se.genoupe.tools.Debug;
import pdstore.PDStore;

/**
 * Created by clut002 on 07-May-14.
 */
public class FullPointsToTest extends PointsToFromCSVTest {
    static { RELATIONS_PATH = PDStore.DEFAULT_FILEPATH+"testrelations/relationsCycleFree/";
	    Debug.addDebugTopic(RuleListener.class.getName()+"basic");

    }
}
