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
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import solver.AchroSolver;
import utils.GraphReader;

/**
 *
 * @author Bruno
 */
public class TestSolver {
    
    private static final String resourcePath = "data_raw/";
    private static final ArrayList<TestResult> summary = new ArrayList<>();
    
    @After
    public void displaySummary(){
	for (TestResult a : summary) {
	    System.out.println(a.toString());
	}
    }
    
    public void testGraphFile(String filename, int expectedAchromaticNumber){
	try {
	    // Setup graph
	    ColoredGraph g = new ColoredGraph(filename);
	    GraphReader.buildGraphFromFile(g, resourcePath + filename);
	    g.setUiProps();
	    AchroSolver solver = new AchroSolver();
	    
	    
	    Instant startTime = Instant.now(); // Measure duration
	    
	    int achromaticNumber = solver.solve(g); // Start solver
	    Assert.assertEquals("Achromatic number differs from expected value", expectedAchromaticNumber, achromaticNumber);
	    
	    Instant stopTime = Instant.now(); // Measure duration
	    Duration timeElapsed = Duration.between(startTime, stopTime);
	    
	    summary.add(new TestResult(timeElapsed.toMillis(), filename, g.getNodeCount())); // Log results
	} catch (IOException | NullPointerException ex) {
	    Assert.fail("File not found : " + resourcePath + filename);
	}
	
	
    }
    
    @Test
    public void testK3(){
	testGraphFile("k3.txt", 3);
    }
    
    @Test
    public void testK4(){
	testGraphFile("k4.txt", 4);
    }
    
    @Test
    public void testK5(){
	testGraphFile("k5.txt", 5);
    }
    
    @Test
    public void testPetersen(){
	testGraphFile("petersen.txt", 5);
    }
    
}
