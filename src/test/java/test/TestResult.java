/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import search.SearchType;

/**
 *
 * @author Bruno
 */
public class TestResult {
    private long elapsedTimeWithHeuristicNValue;
    private long elapsedTimeWithoutHeuristicNValue;
    private String filename;
    private SearchType strategy;
    private int nbVertices;
    private int nbEdges;

    public TestResult(String filename, SearchType strategy) {
        this.filename = filename;
        this.strategy = strategy;
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

    public long getElapsedTimeWithHeuristicNValue() {
        return elapsedTimeWithHeuristicNValue;
    }

    public void setElapsedTimeWithHeuristicNValue(long elapsedTimeWithHeuristicNValue) {
        this.elapsedTimeWithHeuristicNValue = elapsedTimeWithHeuristicNValue;
    }

    public long getElapsedTimeWithoutHeuristicNValue() {
        return elapsedTimeWithoutHeuristicNValue;
    }

    public void setElapsedTimeWithoutHeuristicNValue(long elapsedTimeWithoutHeuristicNValue) {
        this.elapsedTimeWithoutHeuristicNValue = elapsedTimeWithoutHeuristicNValue;
    }
    
    public String toString(boolean csvOutput){
        if(csvOutput){
            String out = filename + ";" + 
                    strategy.name() + ";" + 
                    nbVertices + ";" + 
                    nbEdges + ";" + 
                    elapsedTimeWithoutHeuristicNValue + ";" + 
                    elapsedTimeWithHeuristicNValue;

            return out;
        } else return this.toString();
    }

    @Override
    public String toString() {
        String out = String.format("%-30s", filename)
                + String.format("%-15s", strategy.name())
                + String.format("%-15s", nbVertices)
                + String.format("%-15s", nbEdges)
                + String.format("%-15s", elapsedTimeWithoutHeuristicNValue)
                + String.format("%-15s", elapsedTimeWithHeuristicNValue);
	
        return out;
    }
}
