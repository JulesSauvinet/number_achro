package test;

import benchmark.TestResult;
import graphmodel.ColoredGraph;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;
import search.SearchType;
import solver.AchroSolver;
import utils.GraphEReader;

/**
 *
 * @author Bruno
 */
public class TestSolver {
    private static final String resourcePath = "benchmark/";
    private static final ArrayList<TestResult> summary = new ArrayList<>();
    private static final boolean CSV_OUTPUT = false;
    private static final boolean ASSERT = false;

    @AfterClass
    public static void displaySummary(){
        if(CSV_OUTPUT){
            System.out.println("File_name;Strategy;Vertices;Edges;w/ NValue (ms);w/o NValue (ms)");
        } else {
            System.out.println("\n##################################################");
            System.out.println(String.format("%-30s", "File_name")
                    + String.format("%-15s", "Strategy")
                    + String.format("%-15s", "Vertices")
                    + String.format("%-15s", "Edges")
                    + String.format("%-15s", "w/ NValue (ms)")
                    + String.format("%-15s", "w/o NValue (ms)"));
        }
        for (TestResult a : summary) {
                System.out.println(a.toString(CSV_OUTPUT));
            }
    }

    public void testGraphFile(String filename, int expectedAchromaticNumber){
        testGraphFile(filename, SearchType.DEFAULT, expectedAchromaticNumber);
        //testGraphFile(filename, SearchType.GREEDY, expectedAchromaticNumber);
        //testGraphFile(filename, SearchType.MINDOM, expectedAchromaticNumber);
        testGraphFile(filename, SearchType.MAXCONSTRAINTS, expectedAchromaticNumber);
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

        // No constraint
//        results.setElapsedTimeInMsNoConstraint(testGraphWithConstraints(solver, expectedAchromaticNumber, new SolverConstraints(false, false, false, false)));

        // UseConstraintFirstAffectation
//        results.setElapsedTimeInMsUseConstraintFirstAffectation(testGraphWithConstraints(solver, expectedAchromaticNumber, new SolverConstraints(true, false, false, false)));

        // UseHeuristicMaxClique
//        results.setElapsedTimeInMsUseHeuristicMaxClique(testGraphWithConstraints(solver, expectedAchromaticNumber, new SolverConstraints(false, true, false, false)));

//        // UseHeuristicNValue
//        results.setElapsedTimeInMsUseHeuristicNValue(testGraphWithConstraints(solver, expectedAchromaticNumber, new SolverConstraints(false, false, true, false)));

        // Without UseHeuristicNValue
        results.setElapsedTimeWithoutHeuristicNValue(testGraphWithConstraints(g, expectedAchromaticNumber, false));

        // UseHeuristicSortedNode
//        results.setElapsedTimeInMsUseHeuristicSortedNode(testGraphWithConstraints(solver, expectedAchromaticNumber, new SolverConstraints(false, false, false, true)));

        // All constraints
        results.setElapsedTimeWithHeuristicNValue(testGraphWithConstraints(g, expectedAchromaticNumber, true));
        summary.add(results);
    }

    public long testGraphWithConstraints(ColoredGraph g, int expectedAchromaticNumber, boolean useNvalueHeuristic){
//    public long testGraphWithConstraints(AchroSolver solver, int expectedAchromaticNumber, SolverConstraints cstr){
        AchroSolver solver = new AchroSolver(g, true);
        solver.setConstraintSupp(true, useNvalueHeuristic);
        Instant startTime = Instant.now(); // Measure duration

        int achromaticNumber = solver.solve(); // Start solver
        if(ASSERT){
            String nvalueUsed = useNvalueHeuristic?"w/ Nvalue":"w/o Nvalue";
            Assert.assertEquals("[" + nvalueUsed + "] Achromatic number differs from expected value", expectedAchromaticNumber, achromaticNumber);
        }

        Instant stopTime = Instant.now(); // Measure duration
        return Duration.between(startTime, stopTime).toMillis();
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

//    @Test
//    public void testK16(){
//	testGraphFile("k16", 16);
//    }
//    
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
    public void testMarine(){
        testGraphFile("marine", 6);
    }

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
        testGraphFile("contiguous-usa", 12);
    }

    @Test
    public void testGraphs(){
        for (int i = 0; i < 100; i++) {
            testGraphFile("graph" + i, -2);
        }
    }
}