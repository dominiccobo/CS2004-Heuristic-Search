package com.dominiccobo.bruneluni.cs2004tsp.algorithms;

import com.dominiccobo.bruneluni.cs2004tsp.domain.Graph;
import com.dominiccobo.bruneluni.cs2004tsp.domain.SolutionType;
import com.dominiccobo.bruneluni.cs2004tsp.domain.TSPSolution;
import com.dominiccobo.bruneluni.cs2004tsp.utils.Utilities;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.Vector;

@RunWith(JUnit4.class)
public class RandomMutatingHillClimberTest {

    @Test
    public void run() {

        double[][] testMatrix = new double[][]{
                {0.0000,  4726.0,   1204.0,  6362.0},
                {4726.0,  0.0000,   3587.0,  2011.0},
                {1204.0,  3587.0,   0.0000,  5162.0},
                {6362.0,  2011.0,   5162.0,  0.0000}
        };

        Graph graph = new Graph(testMatrix);

        Vector<Integer> representation = graph.getRandomRoute();

        int iterationsToPerform = 10000;

        TSPSolution tspSolution = new TSPSolution(
                representation,
                graph
        );

        RandomMutatingHillClimber<TSPSolution, Double, Vector<Integer>> rmhc = new RandomMutatingHillClimber<>(
            iterationsToPerform,
            tspSolution,
            SolutionType.MINIMISATION
        );

        tspSolution = rmhc.runAlgorithm();

        System.out.println(Arrays.deepToString(tspSolution.getRepresentation().toArray()));
        System.out.println(tspSolution.getFitness());
    }

    @Test
    public void givenTextFileWithDistanceMatrix_RunsAlgorithm() {

        double[][] matrix = Utilities.readArrayFile(
                Utilities.getResourcePath("data/TSP_574.txt"),
                " "
        );

        Graph graph = new Graph(matrix);

        Vector<Integer> representation = graph.getRandomRoute();

        int iterationsToPerform = 1000;

        TSPSolution tspSolution = new TSPSolution(
                representation,
                graph
        );

        RandomMutatingHillClimber<TSPSolution, Double, Vector<Integer>> rmhc = new RandomMutatingHillClimber<>(
                iterationsToPerform,
                tspSolution,
                SolutionType.MINIMISATION
        );


        tspSolution = rmhc.runAlgorithm();

        System.out.println(Arrays.deepToString(tspSolution.getRepresentation().toArray()));
        System.out.println(tspSolution.getFitness());
        System.out.println(tspSolution.getSolutionQuality());
    }

    @Test
    public void givenTextFileWithDistanceMatrixAndOptimalSolution_RunsAlgorithm() {

        double[][] matrix = Utilities.readArrayFile(
                Utilities.getResourcePath("data/TSP_48.txt"),
                " "
        );

        Graph graph = new Graph(matrix);

        Vector<Integer> representation = Utilities.readIntegerFile(
                Utilities.getResourcePath("data/TSP_48_OPT.txt")
        );



        int iterationsToPerform = 100000;

        TSPSolution tspSolution = new TSPSolution(
                representation,
                graph
        );

        RandomMutatingHillClimber<TSPSolution, Double, Vector<Integer>> rmhc = new RandomMutatingHillClimber<>(
                iterationsToPerform,
                tspSolution,
                SolutionType.MINIMISATION
        );

        tspSolution = rmhc.runAlgorithm();

        System.out.println(Arrays.deepToString(tspSolution.getRepresentation().toArray()));
        System.out.println(tspSolution.getFitness());
        System.out.println(tspSolution.getSolutionQuality());
    }
}