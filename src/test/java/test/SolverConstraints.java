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

    public SolverConstraints(Boolean UseConstraintFirstAffectation, Boolean UseHeuristicMaxClique, Boolean UseHeuristicNValue) {
        this.UseConstraintFirstAffectation = UseConstraintFirstAffectation;
        this.UseHeuristicMaxClique = UseHeuristicMaxClique;
        this.UseHeuristicNValue = UseHeuristicNValue;
    }

    @Override
    public String toString() {
        String out = "";
        if(UseConstraintFirstAffectation && UseHeuristicMaxClique && UseHeuristicNValue){
            out = "AllConstraints";
        } else if((!UseConstraintFirstAffectation) && (!UseHeuristicMaxClique) && (!UseHeuristicNValue)){
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
    }
        return out;
    }
    
}
