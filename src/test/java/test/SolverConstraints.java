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
public class SolverConstraints {
    public Boolean UseConstraintFirstAffectation;
    public Boolean UseHeuristicMaxClique;
    public Boolean UseHeuristicNValue;
    public Boolean UseHeuristicSortedNode;

    public SolverConstraints(Boolean UseConstraintFirstAffectation, Boolean UseHeuristicMaxClique, Boolean UseHeuristicNValue, Boolean UseHeuristicSortedNode) {
        this.UseConstraintFirstAffectation = UseConstraintFirstAffectation;
        this.UseHeuristicMaxClique = UseHeuristicMaxClique;
        this.UseHeuristicNValue = UseHeuristicNValue;
        this.UseHeuristicSortedNode = UseHeuristicSortedNode;
    }

    @Override
    public String toString() {
        String out = "";
        if(UseConstraintFirstAffectation && UseHeuristicMaxClique && UseHeuristicNValue && UseHeuristicSortedNode){
            out = "AllConstraints";
        } else if((!UseConstraintFirstAffectation) && (!UseHeuristicMaxClique) && (!UseHeuristicNValue) && (!UseHeuristicSortedNode)){
            out = "NoConstraint";
        } else {
            if(UseConstraintFirstAffectation){
                out += "1stAffect ";
            }
            if(UseHeuristicMaxClique){
                out += "MaxClique ";
            }
            if(UseHeuristicNValue){
                out += "NValue ";
            }
            if(UseHeuristicSortedNode){
                out += "SortedNode";
            }
    }
        return out;
    }
    
}
