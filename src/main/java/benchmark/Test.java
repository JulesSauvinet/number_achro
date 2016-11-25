package benchmark;

import graphmodel.ColoredGraph;
import search.SearchType;
import solver.AchroSolver;
import solver.exception.SolverTimeOutException;
import utils.GraphEReader;

import java.io.IOException;


/**
 ** Created by teamgraphe
 */
public class Test {

    static String GRAPHNAME = "benchmark/graph10";
    public static void main (String[] args) throws IOException, SolverTimeOutException {
        //AchroSolver.testSolver1();
        ColoredGraph g = GraphEReader.buildGraphFromFile(GRAPHNAME);
        g.setUiProps();
        //g.addAttribute("ui.stylesheet", Test.class.getClassLoader().getResource("colors.css"));
        //g.display();

        AchroSolver solver = new AchroSolver(g);
        solver.setConstraintSupp(/*false,*/true);
        int achroNumber = solver.solve(SearchType.ACTIVITY);

        System.out.println("Achromatic number : " + achroNumber);
        g.display();
    }
}
