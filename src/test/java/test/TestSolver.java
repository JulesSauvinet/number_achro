package test;

import benchmark.TestResult;
import graphmodel.ColoredGraph;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;
import search.SearchType;
import solver.AchroSolver;
import solver.exception.SolverTimeOutException;
import utils.GraphEReader;

/**
 *
 * @author Bruno
 */
public class TestSolver {
    private static final String resourcePath = "benchmark/";
    private static final ArrayList<TestResult> summary = new ArrayList<>();
    private static final boolean CSV_OUTPUT = true;
    private static final boolean ASSERT = false;

    @AfterClass
    public static void displaySummary(){
        if(CSV_OUTPUT){
            System.out.println("File_name;Strategy;Vertices;Edges;NbAchro;w/ NValue (ms);w/o NValue (ms)");
        } else {
            System.out.println("\n##################################################");
            System.out.println(String.format("%-30s", "File_name")
                    + String.format("%-15s", "Strategy")
                    + String.format("%-15s", "Vertices")
                    + String.format("%-15s", "Edges")
                    + String.format("%-15s", "NbAchro")
                    + String.format("%-15s", "w/ NValue (ms)")
                    + String.format("%-15s", "w/o NValue (ms)"));
        }
        for (TestResult a : summary) {
            System.out.println(a.toString(CSV_OUTPUT));
        }
    }

    public void testGraphFile(String filename, int expectedAchromaticNumber){
        testGraphFile(filename, SearchType.DEFAULT, expectedAchromaticNumber);
        //testGraphFile(filename, SearchType.MINDOM, expectedAchromaticNumber);
        //testGraphFile(filename, SearchType.MAXCONSTRAINTS, expectedAchromaticNumber);
        testGraphFile(filename, SearchType.DOMOVERWDEG, expectedAchromaticNumber);
        testGraphFile(filename, SearchType.ACTIVITY, expectedAchromaticNumber);
        testGraphFile(filename, SearchType.INTVAR, expectedAchromaticNumber);
        //testGraphFile(filename, SearchType.RANDOM, expectedAchromaticNumber);
        testGraphFile(filename, SearchType.MAXCONS_RAND, expectedAchromaticNumber);
    }

    public void testGraphFile(String filename, SearchType strategy, int expectedAchromaticNumber){
        System.out.println("\nTesting " + filename + "...");
        TestResult results = new TestResult(filename, strategy);
//        AchroSolver solver;
        ColoredGraph g;
        try {
            g = GraphEReader.buildGraphFromFile(resourcePath + "/" + filename);
            g.setUiProps();
//            solver = new AchroSolver(g, false);
        } catch (IOException | NullPointerException ex) {
            Assert.fail("File not found : " + resourcePath + "/" + filename);
            return;
        }
        results.setNbEdges(g.getEdgeCount());
        results.setNbVertices(g.getNodeCount());

        // Without UseHeuristicNValue
        long[] resNoNvalue = testGraphWithConstraints(g, expectedAchromaticNumber, strategy, false);
        results.setElapsedTimeWithoutHeuristicNValue(resNoNvalue[0]);
        results.setNbAchro((int)resNoNvalue[1]);

        // All constraints
        /*long[] resNvalue = testGraphWithConstraints(g, expectedAchromaticNumber, strategy, true);
        results.setElapsedTimeWithHeuristicNValue(resNvalue[0]);
        results.setNbAchro((int)resNvalue[1]);*/
        summary.add(results);
    }
    
    public long[] testGraphWithConstraints(ColoredGraph g, int expectedAchromaticNumber, SearchType strategy, boolean useNvalueHeuristic){
//    public long testGraphWithConstraints(AchroSolver solver, int expectedAchromaticNumber, SolverConstraints cstr){
        AchroSolver solver = new AchroSolver(g);
        solver.setConstraintSupp(/*true, */useNvalueHeuristic);
        Instant startTime = Instant.now(); // Measure duration

        int achromaticNumber = 0;
        try {
            achromaticNumber = solver.solve(strategy); // Start solver

            if(ASSERT){
                String nvalueUsed = useNvalueHeuristic?"w/ Nvalue":"w/o Nvalue";
                //Assert.assertEquals("[" + nvalueUsed + "] Achromatic number differs from expected value", expectedAchromaticNumber, achromaticNumber);
            }
        } catch (SolverTimeOutException ex) {
            Assert.fail(ex.getMessage());
        }
        

        Instant stopTime = Instant.now(); // Measure duration
        return new long[]{Duration.between(startTime, stopTime).toMillis(), achromaticNumber};
    }

    @Test
    public void testK3(){
        testGraphFile("k3", 3);
    }

    @Test
    public void testK4(){
        testGraphFile("k4", 4);
    }

    @Test
    public void testK5(){
        testGraphFile("k5", 5);
    }

    @Test
    public void testK6(){
        testGraphFile("k6", 6);
    }

    @Test
    public void testK7(){
        testGraphFile("k7", 7);
    }

    @Test
    public void testK8(){
        testGraphFile("k8", 8);
    }

    @Test
    public void testK9(){
        testGraphFile("k9", 9);
    }

    @Test
    public void testK10(){
        testGraphFile( "k10", 10);
    }

    @Test
    public void testK11(){
        testGraphFile("k11", 11);
    }

    @Test
    public void testK12(){
        testGraphFile("k12", 12);
    }

    @Test
    public void testK13(){
        testGraphFile("k13", 13);
    }

    @Test
    public void testK14(){
        testGraphFile("k14", 14);
    }

    @Test
    public void testK15(){
        testGraphFile("k15", 15);
    }

    @Test
    public void testK16(){
	testGraphFile("k16", 16);
    }
    
//    @Test
//    public void testK17(){
//	testGraphFile("k17", 17);
//    }
//
//    @Test
//    public void testK18(){
//        testGraphFile("k18", 18);
//    }
//
//    @Test
//    public void testK19(){
//	    testGraphFile("k19", 19);
//    }
//
//    @Test
//    public void testK20(){
//        testGraphFile("k20", 20);
//    }
//
//    @Test
//    public void testK21(){
//        testGraphFile("k21", 21);
//    }


    @Test
    public void testClebsh(){
        testGraphFile("clebsh", 8);
    }

    @Test
    public void testK5_minus_1(){
        testGraphFile("k5_minus_1", 4);
    }

    @Test
    public void testK5_minus_2_independant(){
        testGraphFile("k5_minus_2_independant", 3);
    }

    @Test
    public void testK5_minus_2_linked(){
        testGraphFile("k5_minus_2_linked", 4);
    }

    @Test
    public void testK5_minus_3_linked(){
        testGraphFile("k5_minus_3_linked", 3);
    }

    @Test
    public void testK5_minus_3_focused(){
        testGraphFile("k5_minus_3_focused", 4);
    }

    @Test
    public void testPetersen(){
        testGraphFile("petersen", 5);
    }

    @Test
    public void testMyciel3(){
        testGraphFile("myciel3.col", 6);
    }

    @Test
    public void testContiguousUSA(){
        testGraphFile("contiguous-usa", 11);
    }

    @Test
    public void testGraphs(){
        for (int i = 0; i < 20; i++) {
            if (i ==10)
                testGraphFile("graph" + i, -2);
        }
    }
}