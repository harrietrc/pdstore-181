package pdstore.sparql;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import pdstore.deduction.PointsToFromCSVTest;
import pdstore.deduction.PointsToTest;
import pdstore.stressTest.*;

/**
 * This runs the most common SPARQL tests.
 * 
 * @author clut002
 * 
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ CartesianPowerIteratorTest.class, MatchTest.class,
		IndexIteratorTest.class, QueryExecutionTest.class, OptionalTest.class,OptimizerTest.class, SparqlViewListenerTest.class,
		SubQueryTest.class, PointsToTest.class, PointsToFromCSVTest.class})
public class AllSparqlTests {
	// TODO: move PointsToTest.class, PointsToFromCSVTest.class into separate suite
	// TODO: suite allTests should call all other Suites
}
