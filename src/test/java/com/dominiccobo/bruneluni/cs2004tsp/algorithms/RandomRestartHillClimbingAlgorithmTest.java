package com.dominiccobo.bruneluni.cs2004tsp.algorithms;

import com.dominiccobo.bruneluni.cs2004tsp.domain.Graph;
import com.dominiccobo.bruneluni.cs2004tsp.domain.SolutionType;
import com.dominiccobo.bruneluni.cs2004tsp.domain.TSPSolution;
import com.dominiccobo.bruneluni.cs2004tsp.utils.Utilities;
import org.junit.Test;

import java.util.Arrays;
import java.util.Vector;

public class RandomRestartHillClimbingAlgorithmTest {

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

        int iterationsToPerform = 1000;
        int rmhcIterations = 10;

        TSPSolution tspSolution = new TSPSolution(
                representation,
                graph
        );

        RandomRestartHillClimbingAlgorithm<TSPSolution, Double, Vector<Integer>> rrhc = new RandomRestartHillClimbingAlgorithm<>(
                iterationsToPerform,
                tspSolution,
                SolutionType.MINIMISATION,
                rmhcIterations
        );

        tspSolution = rrhc.runAlgorithm();

        System.out.println(Arrays.deepToString(tspSolution.getRepresentation().toArray()));
        System.out.println(tspSolution.getFitness());
    }

    @Test
    public void givenTextFileWithDistanceMatrix_RunsAlgorithm() {

        double[][] matrix = Utilities.readArrayFile(
                Utilities.getResourcePath("data/TSP_48.txt"),
                " "
        );

        Graph graph = new Graph(matrix);

        Vector<Integer> representation = graph.getRandomRoute();

        int iterationsToPerform = 1000;
        int rmhcIterations = 100;

        TSPSolution tspSolution = new TSPSolution(
                representation,
                graph
        );

        RandomRestartHillClimbingAlgorithm<TSPSolution, Double, Vector<Integer>> rrhc = new RandomRestartHillClimbingAlgorithm<>(
                iterationsToPerform,
                tspSolution,
                SolutionType.MINIMISATION,
                rmhcIterations
        );



        tspSolution = rrhc.runAlgorithm();

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



        int iterationsToPerform = 1000;
        int rmhcIterations = 100;

        TSPSolution tspSolution = new TSPSolution(
                representation,
                graph
        );

        RandomRestartHillClimbingAlgorithm<TSPSolution, Double, Vector<Integer>> rrhc = new RandomRestartHillClimbingAlgorithm<>(
                iterationsToPerform,
                tspSolution,
                SolutionType.MINIMISATION,
                rmhcIterations
        );

        tspSolution = rrhc.runAlgorithm();

        System.out.println(Arrays.deepToString(tspSolution.getRepresentation().toArray()));
        System.out.println(tspSolution.getFitness());
        System.out.println(tspSolution.getSolutionQuality());
    }
}