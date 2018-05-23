package com.dominiccobo.bruneluni.cs2004tsp.algorithms;

import com.dominiccobo.bruneluni.cs2004tsp.domain.SolutionAdapter;
import com.dominiccobo.bruneluni.cs2004tsp.domain.SolutionType;

import java.util.concurrent.ThreadLocalRandom;

/**
 *  Lab 15 - Simulated Annealing Algorithm Implementation
 *
 *  Brunel University London
 *  Department of Computer Science
 *  http://www.brunel.ac.uk/computer-science
 *
 * @author Dominic Cobo (contact@dominiccobo.com)
 *
 * Class representing a Simulated Annealing Optimisation algorithm, generified to allow for easy modification
 * of the problem it can be applied to.
 *
 * @param <T> The type representing the whole solutions details.
 * @param <T1> The type representing the solution fitness.
 * @param <T2> The type representing the solution representation.
 */
public class SimulatedAnnealingAlgorithm<T extends SolutionAdapter, T1 extends Number, T2> {
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
     * The temperature at which the search feels that it is not likely to find more answers.
     *
     * Directly affected by the cooling rate.
     */
    private double searchTemperature;


    /**
     * The rate at which the search temperature decays.
     */
    private double coolingRate;

    /**
     * No argument constructor, hidden, to prevent non parameterised instantiation.
     */
    @SuppressWarnings("unused")
    private SimulatedAnnealingAlgorithm() {
        throw new IllegalArgumentException("No non-parameterised constructor permitted");
    }

    /**
     * Default instantiation constructor with required parameters for running the algorithm.
     *
     * @param iterationsToPerform the number of iterations to perform.
     * @param startingSolution the starting solution representation.-
     * @param solutionType the solution aim
     * @param searchTemperature the starting search temperature.
     * @param coolingRate the cooling rate at which the temperature decays
     */
    public SimulatedAnnealingAlgorithm(int iterationsToPerform, T startingSolution, SolutionType solutionType,
                                       double searchTemperature, double coolingRate) {
        this.iterationsToPerform = iterationsToPerform;
        this.currentBestSolution = startingSolution;
        this.solutionType = solutionType;
        this.searchTemperature = searchTemperature;
        this.coolingRate = coolingRate;
    }

    /**
     * Executes the algorithm for the specified number of iterations.
     *
     * @return the result of the algorithm's execution.
     */
    public T runAlgorithm() {

        while (iterationsPerformed < iterationsToPerform) {
            this.currentBestSolution = performIteration();
            iterationsPerformed++;
        }
        return currentBestSolution;
    }

    /**
     * Performs a single iteration of the algorithm, returning the best solution in the case.
     *
     * @return the best solution in the case.
     */
    @SuppressWarnings("unchecked")
    private T performIteration() {
        final T2 proposedSolutionRepresentation = (T2) this.currentBestSolution.proposeChange();
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
        this.setDecay();
        return this.currentBestSolution;
    }

    /**
     * Evaluates whether the proposed worser fitness change is to be accepted.
     *
     * @param fitnessDelta the calculated fitness difference to evaluate.
     * @return whether it is to be accepted or not.
     */
    private boolean acceptProposal(Number fitnessDelta) {

        final double variableChance = ThreadLocalRandom.current().nextDouble(0, 1);
        final double convergenceParameter = this.searchTemperature;

        final double probabilityOfAcceptance = Math.exp(-(double) fitnessDelta / convergenceParameter);

        return (probabilityOfAcceptance > variableChance);
    }

    /**
     * Applies the temperature decay method given the current parameters.
     */
    private void setDecay() {
        this.searchTemperature *= this.coolingRate;
    }
}
