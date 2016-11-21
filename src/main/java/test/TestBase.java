/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import graphmodel.ColoredGraph;
import java.io.IOException;
import solver.AchroSolverHeuris;
import utils.GraphReader;

/**
 *
 * @author Marine
 */
public class TestBase {

    static String GRAPHNAME = "benchmark/marine";
    public static void main (String[] args) throws IOException {

        //AchroSolver.testSolver1();
        ColoredGraph g = new ColoredGraph(GRAPHNAME);
        GraphReader.buildGraphFromFile(g,  GRAPHNAME);
        g.setUiProps();

        AchroSolverHeuris solver = new AchroSolverHeuris();
        int achroNumber = solver.solve(g);
        System.out.println("Achromatic number : " + achroNumber);
        g.display();
    }
}
