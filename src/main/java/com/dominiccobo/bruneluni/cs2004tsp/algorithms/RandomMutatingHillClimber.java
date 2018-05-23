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
public class RandomMutatingHillClimber<T extends SolutionAdapter, T1 extends Number, T2> {

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
     * No argument constructor, hidden, to prevent non parameterised instantiation.
     */
    @SuppressWarnings("unused")
    private RandomMutatingHillClimber() {
        throw new IllegalArgumentException("No non-parameterised constructor permitted");
    }

    /**
     * Default instantiation constructor, specifies required parameters for execution.
     *
     * @param iterationsToPerform the number of iterations to perform.
     * @param startingSolution the starting solution representation.
     * @param solutionType the aim of the algorithm.
     */
    public RandomMutatingHillClimber(int iterationsToPerform, T startingSolution, SolutionType solutionType) {
        this.iterationsToPerform = iterationsToPerform;
        this.currentBestSolution = startingSolution;
        this.solutionType = solutionType;
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

        final T2 proposedSolutionRepresentation = (T2) currentBestSolution.proposeChange();
        final int comparisonResult = currentBestSolution.comparePerformanceTo(proposedSolutionRepresentation);

        if(solutionType == SolutionType.MINIMISATION) {
            if(comparisonResult < 0) {
                this.currentBestSolution.setRepresentation(proposedSolutionRepresentation);
            }
        }
        if(solutionType == SolutionType.MAXIMISATION) {
            if(comparisonResult > 0) {
                this.currentBestSolution.setRepresentation(proposedSolutionRepresentation);
            }
        }
        return this.currentBestSolution;
    }
}
