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
    private long elapsedTimeInMsNoConstraint;
    private long elapsedTimeInMsUseConstraintFirstAffectation;
    private long elapsedTimeInMsUseHeuristicMaxClique;
    private long elapsedTimeInMsUseHeuristicNValue;
    private long elapsedTimeInMsUseHeuristicSortedNode;
    private long elapsedTimeInMsAllConstraints;
    private String filename;
    private int nbVertices;
    private int nbEdges;

    public TestResult(String filename) {
        this.filename = filename;
        this.nbVertices = nbVertices;
        this.nbEdges = nbEdges;
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

    public long getElapsedTimeInMsNoConstraint() {
        return elapsedTimeInMsNoConstraint;
    }

    public void setElapsedTimeInMsNoConstraint(long elapsedTimeInMsNoConstraint) {
        this.elapsedTimeInMsNoConstraint = elapsedTimeInMsNoConstraint;
    }

    public long getElapsedTimeInMsUseConstraintFirstAffectation() {
        return elapsedTimeInMsUseConstraintFirstAffectation;
    }

    public void setElapsedTimeInMsUseConstraintFirstAffectation(long elapsedTimeInMsUseConstraintFirstAffectation) {
        this.elapsedTimeInMsUseConstraintFirstAffectation = elapsedTimeInMsUseConstraintFirstAffectation;
    }

    public long getElapsedTimeInMsUseHeuristicMaxClique() {
        return elapsedTimeInMsUseHeuristicMaxClique;
    }

    public void setElapsedTimeInMsUseHeuristicMaxClique(long elapsedTimeInMsUseHeuristicMaxClique) {
        this.elapsedTimeInMsUseHeuristicMaxClique = elapsedTimeInMsUseHeuristicMaxClique;
    }

    public long getElapsedTimeInMsUseHeuristicNValue() {
        return elapsedTimeInMsUseHeuristicNValue;
    }

    public void setElapsedTimeInMsUseHeuristicNValue(long elapsedTimeInMsUseHeuristicNValue) {
        this.elapsedTimeInMsUseHeuristicNValue = elapsedTimeInMsUseHeuristicNValue;
    }

    public long getElapsedTimeInMsUseHeuristicSortedNode() {
        return elapsedTimeInMsUseHeuristicSortedNode;
    }

    public void setElapsedTimeInMsUseHeuristicSortedNode(long elapsedTimeInMsUseHeuristicSortedNode) {
        this.elapsedTimeInMsUseHeuristicSortedNode = elapsedTimeInMsUseHeuristicSortedNode;
    }

    public long getElapsedTimeInMsAllConstraints() {
        return elapsedTimeInMsAllConstraints;
    }

    public void setElapsedTimeInMsAllConstraints(long elapsedTimeInMsAllConstraints) {
        this.elapsedTimeInMsAllConstraints = elapsedTimeInMsAllConstraints;
    }
    
    

    @Override
    public String toString() {
        String out = String.format("%-30s", filename)
                + String.format("%-15s", nbVertices)
                + String.format("%-15s", nbEdges)
                + String.format("%-15s", elapsedTimeInMsNoConstraint)
                + String.format("%-15s", elapsedTimeInMsUseConstraintFirstAffectation)
                + String.format("%-15s", elapsedTimeInMsUseHeuristicMaxClique)
                + String.format("%-15s", elapsedTimeInMsUseHeuristicNValue)
                + String.format("%-15s", elapsedTimeInMsUseHeuristicSortedNode)
                + String.format("%-15s", elapsedTimeInMsAllConstraints);
	
        return out;
    }
}
