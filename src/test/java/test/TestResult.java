/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

/**
 *
 * @author Bruno
 */
public class TestResult {
    private long elapsedTimeInMs;
    private String filename;
    private int nbVertices;

    public TestResult(long elapsedTimeInMs, String filename, int nbVertices) {
	this.elapsedTimeInMs = elapsedTimeInMs;
	this.filename = filename;
	this.nbVertices = nbVertices;
    }

    public long getElapsedTimeInMs() {
	return elapsedTimeInMs;
    }

    public void setElapsedTimeInMs(long elapsedTimeInMs) {
	this.elapsedTimeInMs = elapsedTimeInMs;
    }

    public String getFilename() {
	return filename;
    }

    public void setFilename(String filename) {
	this.filename = filename;
    }

    public int getNbVertices() {
	return nbVertices;
    }

    public void setNbVertices(int nbVertices) {
	this.nbVertices = nbVertices;
    }

    @Override
    public String toString() {
	return filename + "\t\t(" + nbVertices + " v.) : \t\t" + elapsedTimeInMs + "ms";
    }
    
    
    
}
