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

    public SolverConstraints(Boolean UseHeuristicNValue) {
        this.UseHeuristicNValue = UseHeuristicNValue;
    }

    @Override
    public String toString() {
        String out = "";
        if(UseHeuristicNValue){
            out = "w/ NValue";
        } else {
            out = "w/o NValue";
        }
        return out;
    }
    
}
