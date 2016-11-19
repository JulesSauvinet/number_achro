/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import graphmodel.ColoredGraph;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    
    public void testGraphFile(String filename, int expectedAchromaticNumber){
	try {
	    ColoredGraph g = new ColoredGraph(filename);
	    GraphReader.buildGraphFromFile(g, resourcePath + filename);
	    g.setUiProps();
	    AchroSolver solver = new AchroSolver();
	    int achromaticNumber = solver.solve(g);
	    Assert.assertEquals("Achromatic number differs from expected value", expectedAchromaticNumber, achromaticNumber);
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
