package com.dominiccobo.bruneluni.cs2004tsp.domain;

/**
 * Adapter for permitting different implementations of solutions for
 * problems of varying solution representation and solution evaluation.
 *
 * @author Dominic Cobo (contact@dominiccobo.com)
 *
 * @param <T1> The type specifying the evaluation of the fitness.
 * @param <T2> The data type specifying the representation.
 */
public interface SolutionAdapter<T1 extends Number, T2> {

    /**
     * Set the current representation
     * @param representation representation value to set.
     */
    void setRepresentation(T2 representation);

    /**
     * Retrieve the current representation of the solution.
     * @return the representation of the solution.
     */
    T2 getRepresentation();

    /**
     * Retrieve the current fitness of the solution.
     * @return the current fitness.
     */
    T1 getFitness();

    /**
     * Retrieve the current fitness of the solution passed.
     * @param representation the solution to evaluate the fitness of.
     *
     * @return the evaluated fitness.
     */
    T1 getFitness(T2 representation);

    /**
     * Returns the difference between two fitness values. Implementation should be
     * parameter sensitive.
     *
     *
     * @param representationA first fitness value.
     * @param representationB second fitness value.
     *
     * @return the difference between both fitnesses.
     */
    T1 getFitnessDifference(T2 representationA, T2 representationB);

    /**
     * Propose a change to the representation, in order to improve the suitability.
     * @return the proposed change to be evaluated.
     */
    T2 proposeChange();

    /**
     * Propose another representatio
     *
     * Functions in a similar way to strcmp in C :)
     *
     * Negative magnitudes of returned values an inferior value in comparison to the comparision fitness object.
     * No magnitude indicates an value comparison with the comparison object.
     * Positive magnitude indicates a higher value in comparison to the comparison fitness object.
     *
     */
    int comparePerformanceTo(T2 representationToCompare);
}

