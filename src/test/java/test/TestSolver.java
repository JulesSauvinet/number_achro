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
    
    private static final String resourcePath = "benchmark/";
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
		testGraphFile("k10", 10);
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

	@Test
	public void testK18(){
		testGraphFile("k18", 18);
	}

    
    @Test
    public void testPetersen(){
	testGraphFile("petersen", 5);
    }
    
}
