package com.dominiccobo.bruneluni.cs2004tsp.algorithms;

import com.dominiccobo.bruneluni.cs2004tsp.domain.SolutionAdapter;
import com.dominiccobo.bruneluni.cs2004tsp.domain.SolutionType;

import java.util.concurrent.ThreadLocalRandom;

/**
 *  Lab 15 - Stochastic Hill Climbing Algorithm Implementation
 *
 *  Brunel University London
 *  Department of Computer Science
 *  http://www.brunel.ac.uk/computer-science
 *
 * @author Dominic Cobo (contact@dominiccobo.com)
 *
 * Class representing a random restarat hill climber algorithm, generified to allow for easy modification
 * of the problem it can be applied to.
 *
 * @param <T> The type representing the whole solutions details.
 * @param <T1> The type representing the solution fitness.
 * @param <T2> The type representing the solution representation.
 */
public class StochasticHillClimbingAlgorithm<T extends SolutionAdapter, T1 extends Number, T2> {

    /**
     * The number of iterations to perform the algorithm for.
     */
    private int iterationsToPerform;

    /**
     * The current iterations performed.
     */
    private int iterationsPerformed;

    /**
     * The current best solution.
     */
    private T currentBestSolution;

    /**
     * The solution principle to be applied.
     */
    private SolutionType solutionType;

    /**
     * The optimisation convergence parameter
     */
    private double convergenceParameter;

    /**
     * No argument constructor, hidden, to prevent non parameterised instantiation.
     */
    @SuppressWarnings("unused")
    private StochasticHillClimbingAlgorithm() {
        throw new IllegalArgumentException("No non-parameterised constructor permitted");
    }

    /**
     * Generic constructor for instantiating with all required data.
     *
     * @param iterationsToPerform the number of iterations to perform within the algorithm.
     * @param startingSolution the starting solution representation.
     * @param solutionType the aim of the solution, that is whether we aim to maximise or minimise.
     * @param convergenceParameter the convergence parameter, that modifies the acceptance of proposed worse solutions.
     */
    public StochasticHillClimbingAlgorithm(int iterationsToPerform, T startingSolution, SolutionType solutionType, double convergenceParameter) {
        this.iterationsToPerform = iterationsToPerform;
        this.currentBestSolution = startingSolution;
        this.solutionType = solutionType;
        this.convergenceParameter = convergenceParameter;
    }

    /**
     * Executes the algorithm for the specified number of iterations.
     * @return the result of the algorithm's execution.
     */
    public T runAlgorithm() {

        while(iterationsPerformed < iterationsToPerform) {
            this.currentBestSolution = performIteration();
            iterationsPerformed++;
        }
        return currentBestSolution;
    }

    /**
     * Performs a single iteration of the SHC algorithm, evaluating the fitness of the current and possible solution.
     *
     * @return the best solution in the case.
     */
    @SuppressWarnings("unchecked")
    private T performIteration() {

        final T2 proposedSolutionRepresentation = (T2) currentBestSolution.proposeChange();
        final int comparisonResult = currentBestSolution.comparePerformanceTo(proposedSolutionRepresentation);

        T1 fitnessDifference = null;

        if(solutionType == SolutionType.MINIMISATION) {
            fitnessDifference = (T1) this.currentBestSolution.getFitnessDifference(
                    proposedSolutionRepresentation,
                    currentBestSolution.getRepresentation()
            );
        }
        if(solutionType == SolutionType.MAXIMISATION) {
            fitnessDifference = (T1) this.currentBestSolution.getFitnessDifference(
                    currentBestSolution.getRepresentation(),
                    proposedSolutionRepresentation
            );
        }

        // if worse
        if(comparisonResult > 0) {

            if(this.acceptProposal(fitnessDifference)) {
                this.currentBestSolution.setRepresentation(proposedSolutionRepresentation);
            }
        }
        else {
            this.currentBestSolution.setRepresentation(proposedSolutionRepresentation);
        }
        return this.currentBestSolution;
    }

    /**
     * Determines whether the proposed fitness difference can be accepted or not based on a
     * variable random chance and a convergence parameter that will increase or decrease the
     * possibility.
     *
     * @param fitnessDelta the difference between the two fitnesses.
     * @return the evaluation of acceptance.
     */
    private boolean acceptProposal(Number fitnessDelta) {

        final double variableChance = ThreadLocalRandom.current().nextDouble(0, 1);
        final double convergenceParameter = this.convergenceParameter;

        final double probabilityOfAcceptance = (1 / (1 + Math.exp((double) fitnessDelta / convergenceParameter)));

        return (probabilityOfAcceptance > variableChance);
    }
}
