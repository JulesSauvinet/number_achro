/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import graphmodel.ColoredGraph;
import java.io.IOException;
import solver.AchroSolverBase;
import utils.GraphReader;

/**
 ** Created by teamgraphe
 */
public class TestBase {

    static String GRAPHNAME = "benchmark/marine";
    public static void main (String[] args) throws IOException {

        //AchroSolver.testSolver1();
        ColoredGraph g = GraphReader.buildGraphFromFile(GRAPHNAME);
        g.setUiProps();

        AchroSolverBase solver = new AchroSolverBase();
        int achroNumber = solver.solve(g);
        System.out.println("Achromatic number : " + achroNumber);
        g.display();
    }
}
