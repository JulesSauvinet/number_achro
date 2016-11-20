/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import graphmodel.ColoredGraph;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;
import solver.AchroSolver;
import utils.GraphReader;

/**
 *
 * @author Bruno
 */
public class TestSolver {
    
    private static final ArrayList<TestResult> summary = new ArrayList<>();
    
    @AfterClass
    public static void displaySummary(){
	System.out.println("\n##################################################");
	System.out.println(String.format("%-30s", "File_name") + String.format("%-15s", "Vertices") + String.format("%-15s", "Edges") + String.format("%-10s", "Time (ms)"));
	for (TestResult a : summary) {
	    System.out.println(a.toString());
	}
	System.out.println("##################################################\n");
    }
    
    public void testGraphFile(String directory, String filename, int expectedAchromaticNumber){
	System.out.println("\nTesting " + filename + "...");
	try {
	    // Setup graph
	    ColoredGraph g = new ColoredGraph(filename);
	    GraphReader.buildGraphFromFile(g, directory + "/" + filename);
	    g.setUiProps();
	    AchroSolver solver = new AchroSolver();
	    
	    
	    Instant startTime = Instant.now(); // Measure duration
	    
	    int achromaticNumber = solver.solve(g); // Start solver
	    Assert.assertEquals("Achromatic number differs from expected value", expectedAchromaticNumber, achromaticNumber);
	    
	    Instant stopTime = Instant.now(); // Measure duration
	    Duration timeElapsed = Duration.between(startTime, stopTime);
	    
	    summary.add(new TestResult(timeElapsed.toMillis(), filename, g.getNodeCount(), g.getEdgeCount())); // Log results
	} catch (IOException | NullPointerException ex) {
	    Assert.fail("File not found : " + directory + "/" + filename);
	}
	
	
    }
    
    @Test
    public void testK3(){
	testGraphFile("ks", "k3", 3);
    }
    
    @Test
    public void testK4(){
	testGraphFile("ks", "k4", 4);
    }
    
    @Test
    public void testK5(){
	testGraphFile("ks", "k5", 5);
    }
    
    @Test
    public void testK6(){
	testGraphFile("ks", "k6", 6);
    }
    
    @Test
    public void testK7(){
	testGraphFile("ks", "k7", 7);
    }
    
    @Test
    public void testK8(){
	testGraphFile("ks", "k8", 8);
    }
    
    @Test
    public void testK9(){
	testGraphFile("ks", "k9", 9);
    }
    
    @Test
    public void testK10(){
	testGraphFile("ks", "k10", 10);
    }
    
    @Test
    public void testK11(){
	testGraphFile("ks", "k11", 11);
    }
    
    @Test
    public void testK12(){
	testGraphFile("ks", "k12", 12);
    }
    
    @Test
    public void testK13(){
	testGraphFile("ks", "k13", 13);
    }
    
    @Test
    public void testK14(){
	testGraphFile("ks", "k14", 14);
    }
    
    @Test
    public void testK15(){
	testGraphFile("ks", "k15", 15);
    }
    
    @Test
    public void testK16(){
	testGraphFile("ks", "k16", 16);
    }
    
    @Test
    public void testK17(){
	testGraphFile("ks", "k17", 17);
    }
    
    @Test
    public void testK5_minus_1(){
	testGraphFile("ks_mod", "k5_minus_1", 4);
    }
    
    @Test
    public void testK5_minus_2_independant(){
	testGraphFile("ks_mod", "k5_minus_2_independant", 3);
    }
    
    @Test
    public void testK5_minus_2_linked(){
	testGraphFile("ks_mod", "k5_minus_2_linked", 4);
    }
    
    @Test
    public void testK5_minus_3_linked(){
	testGraphFile("ks_mod", "k5_minus_3_linked", 3);
    }
    
    @Test
    public void testK5_minus_3_focused(){
	testGraphFile("ks_mod", "k5_minus_3_focused", 4);
    }
    
    @Test
    public void testPetersen(){
	testGraphFile("data_raw", "petersen", 5);
    }
    
    @Test
    public void testMyciel3(){
	testGraphFile("data_raw", "myciel3.col", 6);
    }
}
