package com.dominiccobo.bruneluni.cs2004tsp.samples;

import com.dominiccobo.bruneluni.cs2004tsp.algorithms.*;
import com.dominiccobo.bruneluni.cs2004tsp.domain.Graph;
import com.dominiccobo.bruneluni.cs2004tsp.domain.SolutionType;
import com.dominiccobo.bruneluni.cs2004tsp.domain.TSPSolution;
import com.dominiccobo.bruneluni.cs2004tsp.utils.AlgorithmAnalysisLogger;
import com.dominiccobo.bruneluni.cs2004tsp.utils.AlgorithmTimer;
import com.dominiccobo.bruneluni.cs2004tsp.utils.TSPReportUtility;
import com.dominiccobo.bruneluni.cs2004tsp.utils.Utilities;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Class running all the hill climber implementations for a given list of solutions and evaluating their performance.
 *
 * @author Dominic Cobo (contact@dominiccobo.com)
 */
public class TSPSampleRunner {

    private final static String[] SOLUTIONS = new String[]{
            "48", "51", "52", "70", "76", "100", "101", "105", "107", "124", "125", "130", "136", "144", "150",
            "152", "159", "198", "200", "226", "262", "264", "299", "318", "400", "417", "439", "442",
            "493", "532", "574"
    };

    private final static String[] OPTIMAL_SOLUTIONS = new String[]{
            "48", "51", "52", "70", "76", "100", "105", "442"
    };

    /**
     * Current text file identifying name of distances being evaluated.
     */
    private String currentSample;

    /**
     * Current text matrix being ran.
     */
    private double[][] testMatrix;

    /**
     * Optional test representation, useful for evaluating samples against a known representation.
     */
    private Vector<Integer> testRepresentation;

    /**
     * The number of times to resample an algorithm and obtain readings from to increase reliability.
     */
    private int resamplesToRun;

    /**
     * The solution type to run, whether it be minimisation of maximisation.
     */
    private SolutionType solutionType;

    /**
     * The number of times to iterate within each algorithm.
     */
    private int algorithmIterations;

    /**
     * The number of times to rerun the RRHC RMHC internal algorithm for.
     */
    private int rrhcRMHCRepetitions;

    /**
     * The Stochastic HC convergence parameter, defined as T, in the lab worksheets.
     */
    private double scaConvergenceParameter;

    /**
     * The Simulated Annealing starting temperature.
     */
    private double saStartingTemperature;

    /**
     * The Simulated Annealing cooling rate, to be calculated.
     */
    private double saCoolingRate;


    public TSPSampleRunner(int resamplesToRun, SolutionType solutionType, int algorithmIterations, int rrhcRMHCRepetitions) {
        this.resamplesToRun = resamplesToRun;
        this.solutionType = solutionType;
        this.algorithmIterations = algorithmIterations;
        this.rrhcRMHCRepetitions = rrhcRMHCRepetitions;
    }

    /**
     * Execute all the tests.
     */
    public void runTests() {
        iterateThroughDataSet();
        iterateThroughOptimalDataSet();
    }

    /**
     * Iterate through the data sets without an understanding of the optimal solution.
     *
     * Currently runs in a threaded mode to obtain faster results., easily changeable.
     */
    private void iterateThroughDataSet() {

        testRepresentation = null;

        for (int i = 0; i < SOLUTIONS.length; i++) {
            String sample = SOLUTIONS[i];
            currentSample = sample;
            testMatrix = Utilities.readArrayFile(
                    Utilities.getResourcePath("data/TSP_" + currentSample + ".txt"),
                    " "
            );

            // progress bar, so you don't feel like nothing is happening.
            String update;
            update = String.format(
                    "\rNon Optimal: %s [%s%s] %d/%d",
                    currentSample,
                    StringUtils.repeat("=", i+1),
                    StringUtils.repeat(" ", SOLUTIONS.length - i - 1),
                    i + 1,
                    SOLUTIONS.length
            );

            try {
                System.out.write(update.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }

            ExecutorService executorService = Executors.newFixedThreadPool(4);
            List<Future<?>> futureList = new ArrayList<>();

            Runnable t1 = this::runRMHC;
            Runnable t2 = this::runRRHC;
            Runnable t3 = this::runSCA;
            Runnable t4 = this::runSA;

            futureList.add(executorService.submit(t1));
            futureList.add(executorService.submit(t2));
            futureList.add(executorService.submit(t3));
            futureList.add(executorService.submit(t4));

            for(Future<?> future: futureList) {
                try {
                    future.get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
            executorService.shutdown();
        }
    }

    /**
     * Iterate through the data set to understand how each algorithm will behave with an optimal solution, to assess the
     * ability and effect of escaping the local optima.
     */
    private void iterateThroughOptimalDataSet() {

        for (int i = 0; i < OPTIMAL_SOLUTIONS.length; i++) {
            String sample = OPTIMAL_SOLUTIONS[i];
            currentSample = sample;
            testMatrix = Utilities.readArrayFile(
                    Utilities.getResourcePath("data/TSP_" + currentSample + ".txt"),
                    " "
            );
            currentSample = sample + "_OPT";
            testRepresentation = Utilities.readIntegerFile(
                    Utilities.getResourcePath("data/TSP_" + currentSample + ".txt")
            );

            // progress bar, so you don't feel like nothing is happening.
            String update;
            update = String.format(
                    "\r %s [%s%s] %d/%d",
                    currentSample,
                    StringUtils.repeat("=", i+1),
                    StringUtils.repeat(" ", OPTIMAL_SOLUTIONS.length - i - 1),
                    i + 1,
                    OPTIMAL_SOLUTIONS.length
            );

            try {
                System.out.write(update.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }

            getOptimal();
        }
    }

    /**
     * Runs a number of samples of the Random Mutating Hill Climber algorithm, logging a general analysis.
     */
    private void runRMHC() {

        Graph graph = new Graph(testMatrix);

        TSPReportUtility tspReportUtility = new TSPReportUtility(
                currentSample,
                resamplesToRun,
                algorithmIterations,
                "RMHC_SUMMARY"
        );

        final double mstCost = MinimumSpanningTree.getMSTCost(MinimumSpanningTree.primsMST(graph.getDistanceMatrix()));

        Vector<Integer> representation = (testRepresentation != null) ?
                (testRepresentation) : graph.getRandomRoute();

        AlgorithmTimer algorithmTimer = new AlgorithmTimer();

        for(int i = 0; i < resamplesToRun; i++) {

            representation = graph.getRandomRoute();

            algorithmTimer.start();

            TSPSolution tspSolution = new TSPSolution(
                representation,
                graph
            );

            RandomMutatingHillClimber<TSPSolution, Double, Vector<Integer>> rmhc = new RandomMutatingHillClimber<>(
                    algorithmIterations,
                    tspSolution,
                    solutionType
            );

            tspSolution = rmhc.runAlgorithm();


            algorithmTimer.end();

            final double fitness = tspSolution.getFitness();
            final double solutionQuality = tspSolution.getSolutionQuality(fitness, mstCost);

            tspReportUtility.appendRun(
                    fitness,
                    solutionQuality,
                    algorithmTimer.getDuration()
            );
        }

        TSPSolution tspOptimal = new TSPSolution(
            representation,
            graph
        );

        tspReportUtility.createLog(
                tspOptimal.getFitness(),
                mstCost
        );
    }

    /**
     * Runs a number of samples of the Random Restart Hill Climber algorithm, logging a general analysis.
     */
    private void runRRHC() {

        Graph graph = new Graph(testMatrix);

        TSPReportUtility tspReportUtility = new TSPReportUtility(
                currentSample,
                resamplesToRun,
                algorithmIterations,
                "RRHC_SUMMARY"
        );

        Vector<Integer> representation = (testRepresentation != null) ?
                (testRepresentation) : graph.getRandomRoute();

        final double mstCost = MinimumSpanningTree.getMSTCost(MinimumSpanningTree.primsMST(graph.getDistanceMatrix()));

        AlgorithmTimer algorithmTimer = new AlgorithmTimer();

        for(int i = 0; i < resamplesToRun; i++) {

            representation = graph.getRandomRoute();

            algorithmTimer.start();
            TSPSolution tspSolution = new TSPSolution(
                    representation,
                    graph
            );

            RandomRestartHillClimbingAlgorithm<TSPSolution, Double, Vector<Integer>> rrhc = new RandomRestartHillClimbingAlgorithm<>(
                    algorithmIterations/rrhcRMHCRepetitions,
                    tspSolution,
                    solutionType,
                    rrhcRMHCRepetitions
            );

            tspSolution = rrhc.runAlgorithm();


            algorithmTimer.end();

            final double fitness = tspSolution.getFitness();
            final double solutionQuality = tspSolution.getSolutionQuality(fitness, mstCost);

            tspReportUtility.appendRun(
                    fitness,
                    solutionQuality,
                    algorithmTimer.getDuration()
            );
        }

        TSPSolution tspOptimal = new TSPSolution(
                representation,
                graph
        );

        tspReportUtility.createLog(
                tspOptimal.getFitness(),
                tspOptimal.getMinimumSpanningTreeCost()
        );
    }

    /**
     * Runs a number of samples of the Stochastic Hill Climber algorithm, logging a general analysis.
     */
    private void runSCA() {

        Graph graph = new Graph(testMatrix);

        TSPReportUtility tspReportUtility = new TSPReportUtility(
                currentSample,
                resamplesToRun,
                algorithmIterations,
                "SCA_SUMMARY"
        );

        final double OPTIMISATION_CONSTANT = 0.0055;

        final double mstCost = MinimumSpanningTree.getMSTCost(MinimumSpanningTree.primsMST(graph.getDistanceMatrix()));
        scaConvergenceParameter = mstCost * OPTIMISATION_CONSTANT;

        Vector<Integer> representation = (testRepresentation != null) ?
                (testRepresentation) : graph.getRandomRoute();

        AlgorithmTimer algorithmTimer = new AlgorithmTimer();

        for(int i = 0; i < resamplesToRun; i++) {

            representation = graph.getRandomRoute();

            algorithmTimer.start();
            TSPSolution tspSolution = new TSPSolution(
                    representation,
                    graph
            );

            StochasticHillClimbingAlgorithm<TSPSolution, Double, Vector<Integer>> sca = new StochasticHillClimbingAlgorithm<>(
                    algorithmIterations,
                    tspSolution,
                    solutionType,
                    scaConvergenceParameter
            );

            tspSolution = sca.runAlgorithm();

            algorithmTimer.end();

            final double fitness = tspSolution.getFitness();
            final double solutionQuality = tspSolution.getSolutionQuality(fitness, mstCost);

            tspReportUtility.appendRun(
                    fitness,
                    solutionQuality,
                    algorithmTimer.getDuration()
            );
        }

        TSPSolution tspOptimal = new TSPSolution(
                representation,
                graph
        );

        String[] addedHeaders = new String[] {
                "paramT"
        };

        String[] addedData = new String[] {
            String.valueOf(scaConvergenceParameter)
        };

        tspReportUtility.createLog(
                tspOptimal.getFitness(),
                mstCost,
                addedHeaders,
                addedData
        );
    }

    /**
     * Runs a number of samples of the Simulated Annealing Hill Climber algorithm, logging a general analysis.
     */
    private void runSA() {

        Graph graph = new Graph(testMatrix);

        TSPReportUtility tspReportUtility = new TSPReportUtility(
                currentSample,
                resamplesToRun,
                algorithmIterations,
                "SA_SUMMARY"
        );

        Vector<Integer> representation = (testRepresentation != null) ?
                (testRepresentation) : graph.getRandomRoute();


        final double OPTIMISATION_CONSTANT = 0.0000018;

        final double mstCost = MinimumSpanningTree.getMSTCost(MinimumSpanningTree.primsMST(graph.getDistanceMatrix()));
        double tItter = mstCost * OPTIMISATION_CONSTANT;
        double startingTemp = mstCost * 0.95;

        saCoolingRate = Math.pow((tItter / startingTemp), (1.0 / algorithmIterations));
        saStartingTemperature = startingTemp;

        AlgorithmTimer algorithmTimer = new AlgorithmTimer();

        for(int i = 0; i < resamplesToRun; i++) {

            representation = graph.getRandomRoute();

            algorithmTimer.start();
            TSPSolution tspSolution = new TSPSolution(
                    representation,
                    graph
            );

            SimulatedAnnealingAlgorithm<TSPSolution, Double, Vector<Integer>> sa = new SimulatedAnnealingAlgorithm<>(
                    algorithmIterations,
                    tspSolution,
                    solutionType,
                    saStartingTemperature,
                    saCoolingRate
            );

            tspSolution = sa.runAlgorithm();

            algorithmTimer.end();

            final double fitness = tspSolution.getFitness();
            final double solutionQuality = tspSolution.getSolutionQuality(fitness, mstCost);

            tspReportUtility.appendRun(
                    fitness,
                    solutionQuality,
                    algorithmTimer.getDuration()
            );
        }



        TSPSolution tspOptimal = new TSPSolution(
                representation,
                graph
        );

        String[] addedHeaders = new String[] {
                "Starting Temp", "Cooling Rate"
        };

        String[] addedData = new String[] {
                String.valueOf(saStartingTemperature),
                String.valueOf(saCoolingRate)
        };

        tspReportUtility.createLog(
                tspOptimal.getFitness(),
                mstCost,
                addedHeaders,
                addedData
        );
    }

    /**
     * Retrieves the statistics of the optimal representations if available.
     */
    private void getOptimal() {

        if(testRepresentation != null) {
            Graph graph = new Graph(testMatrix);

            AlgorithmAnalysisLogger algorithmAnalysisLogger = new AlgorithmAnalysisLogger(
                    "SUMMARIES",
                    new String[]{"Sample", "Optimal Fitness", "Optimal Solution Quality", "MST Cost"}
            );

            TSPSolution tspSolution = new TSPSolution(
                    testRepresentation,
                    graph
            );


            algorithmAnalysisLogger.insertLog(
                    new String[] {
                            currentSample,
                            String.valueOf(tspSolution.getFitness()),
                            String.valueOf(tspSolution.getSolutionQuality()),
                            String.valueOf(tspSolution.getMinimumSpanningTreeCost())
                    }
            );

            algorithmAnalysisLogger.close();
        }
    }
}
