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
    private int nbEdges;

    public TestResult(long elapsedTimeInMs, String filename, int nbVertices, int nbEdges) {
	this.elapsedTimeInMs = elapsedTimeInMs;
	this.filename = filename;
	this.nbVertices = nbVertices;
	this.nbEdges = nbEdges;
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

    public int getNbEdges() {
	return nbEdges;
    }

    public void setNbEdges(int nbEdges) {
	this.nbEdges = nbEdges;
    }

    @Override
    public String toString() {
	return String.format("%-30s", filename) + String.format("%-15s", nbVertices) + String.format("%-15s", nbEdges) + String.format("%-10s", elapsedTimeInMs + "ms");
    }
}
