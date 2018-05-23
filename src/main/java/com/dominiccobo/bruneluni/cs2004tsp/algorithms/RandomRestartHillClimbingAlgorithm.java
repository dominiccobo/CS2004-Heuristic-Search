package com.dominiccobo.bruneluni.cs2004tsp.algorithms;

import com.dominiccobo.bruneluni.cs2004tsp.domain.SolutionAdapter;
import com.dominiccobo.bruneluni.cs2004tsp.domain.SolutionType;


/**
 *  Lab 15 - Random Mutating Hill Climber Implementation
 *
 *  Brunel University London
 *  Department of Computer Science
 *  http://www.brunel.ac.uk/computer-science
 *
 * @author Dominic Cobo (contact@dominiccobo.com)
 *
 * Class representing a random mutating hill climber algorithm, generified to allow for easy modification
 * of the problem it can be applied to.
 *
 * @param <T> The type representing the whole solutions details.
 * @param <T1> The type representing the solution fitness.
 * @param <T2> The type representing the solution representation.
 */
public class RandomRestartHillClimbingAlgorithm<T extends SolutionAdapter, T1 extends Number, T2> {

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
     * The number of times to run the RMHC algorithm each iteration.
     */
    private int rmhcIterationsToRun;

    /**
     * No argument constructor, hidden, to prevent non parameterised instantiation.
     */
    @SuppressWarnings("unused")
    private RandomRestartHillClimbingAlgorithm() {
        throw new IllegalArgumentException("No non-parameterised constructor permitted");
    }

    /**
     * The default constructor, which specifies the required parameters for execution.
     * @param iterationsToPerform the number of iterations to perform.
     * @param startingSolution the starting solution representation.
     * @param solutionType the aim of the solution algorithm.
     * @param rmhcIterationsToRun the number of internal rmnhc iterations to run.
     */
    public RandomRestartHillClimbingAlgorithm(int iterationsToPerform, T startingSolution, SolutionType solutionType,
                                              int rmhcIterationsToRun) {

        this.iterationsToPerform = iterationsToPerform;
        this.currentBestSolution = startingSolution;
        this.solutionType = solutionType;
        this.rmhcIterationsToRun = rmhcIterationsToRun;
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
     * Performs a single iteration of the RMHC algorithm, evaluating the fitness of the current and possible solution.
     *
     * @return the best solution in the case.
     */
    @SuppressWarnings("unchecked")
    private T performIteration() {

        RandomMutatingHillClimber<T, T1, T2> rhmc = new RandomMutatingHillClimber<>(
                rmhcIterationsToRun,
                currentBestSolution,
                solutionType
        );

        T proposedSolution = rhmc.runAlgorithm();
        T2 proposedSolutionRepresentation = (T2) proposedSolution.getRepresentation();

        int comparisonResult = this.currentBestSolution.comparePerformanceTo(proposedSolutionRepresentation);

        if(solutionType == SolutionType.MINIMISATION) {
            if(comparisonResult < 0) {
                this.currentBestSolution.setRepresentation(proposedSolution);
            }
        }
        if(solutionType == SolutionType.MAXIMISATION) {
            if(comparisonResult > 0) {
                this.currentBestSolution.setRepresentation(proposedSolution);
            }
        }
        return this.currentBestSolution;
    }
}
