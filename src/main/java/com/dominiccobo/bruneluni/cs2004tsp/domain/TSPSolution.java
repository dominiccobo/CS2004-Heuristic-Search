package com.dominiccobo.bruneluni.cs2004tsp.domain;

import com.dominiccobo.bruneluni.cs2004tsp.algorithms.MinimumSpanningTree;

import java.util.Vector;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Implementation of the SolutionAdapter interface, specific to the requirements and idiosyncracies of the
 * Travelling Salesman problem.
 *
 * Implementation uses the fitness representation as a Double and the Solution Representation as a Vector of Integers.
 *
 * @author Dominic Cobo (contact@dominiccbo.com)
 */
public class TSPSolution implements SolutionAdapter<Double, Vector<Integer>> {

    /**
     * A representation of the sorted nodes indices toured, where the node is a zero indexed representation
     * of its position in the distance matrix.
     */
    private Vector<Integer> representation;

    /**
     * A representation of the graph to solve for the problem.
     */
    private Graph distanceGraph;

    /**
     * Validator constructor, preventing TSP solution object from being instantiated without parameters.
     */
    @SuppressWarnings("unused")
    private TSPSolution() {
        throw new IllegalArgumentException("No non-parameterised constructor permitted");
    }

    /**
     * Generic parameterised constructor for solution.
     *
     * @param representation representation of the solution in terms of a series of weights.
     * @param distanceGraph distance matrix of doubles representing routes.
     */
    public TSPSolution(Vector<Integer> representation, Graph distanceGraph) {
        this.representation = representation;
        this.distanceGraph = distanceGraph;
    }


    @Override
    public void setRepresentation(Vector<Integer> representation) {
        this.representation = representation;
    }

    @Override
    public Vector<Integer> getRepresentation() {
        return this.representation;
    }

    @Override
    public Double getFitness() {
        return this.distanceGraph.getDistanceGivenRoute(representation);
    }

    @Override
    public Double getFitness(Vector<Integer> rep) {
        return this.distanceGraph.getDistanceGivenRoute(rep);
    }

    @Override
    public Double getFitnessDifference(Vector<Integer> representationA, Vector<Integer> representationB) {
        return Math.abs(this.getFitness(representationA) - this.getFitness(representationB));
    }

    @Override
    public Vector<Integer> proposeChange() {

        int firstRandomIndex = 0;
        int secondRandomIndex = 0;

        while (firstRandomIndex == secondRandomIndex) {
            firstRandomIndex = ThreadLocalRandom.current().nextInt(0, getRepresentation().size()-1);
            secondRandomIndex = ThreadLocalRandom.current().nextInt(0, getRepresentation().size()-1);
        }

        final int valueOfFirstIndex = getRepresentation().get(firstRandomIndex);
        final int valueOfSecondIndex = getRepresentation().get(secondRandomIndex);

        // NB. pointing it directly to getRepresentation will return a soft-copy.. this is a better solution.
        Vector<Integer> representationCopy = new Vector<>();
        representationCopy.addAll(getRepresentation());
        representationCopy.set(firstRandomIndex, valueOfSecondIndex);
        representationCopy.set(secondRandomIndex, valueOfFirstIndex);

        return new Vector<>(representationCopy);
    }

    @Override
    public int comparePerformanceTo(Vector<Integer> representationToCompare) {

        final double proposedFitness = this.distanceGraph.getDistanceGivenRoute(representationToCompare);
        int result = Double.compare(proposedFitness, getFitness());

        return result;
    }

    /**
     * Calculates the minimum spanning tree of the distance current distance graph and then calculates its cost.
     * @return the cost of the current MST.
     */
    public double getMinimumSpanningTreeCost() {
        final double[][] mstResult = MinimumSpanningTree.primsMST(
                distanceGraph.getDistanceMatrix()
        );

        // functional programming -> takes 2D array and sums up values to generate MST.
        final double minimumSpanningTreeCost = MinimumSpanningTree.getMSTCost(mstResult);

        return minimumSpanningTreeCost;
    }

    /**
     * Retrieves the solution quality based on the assumption that the MST cost is the
     * best solution that could be achieved and works out a ratio against the fitness achieved.
     *
     * @return the evaluation of the solution quality.
     */
    public double getSolutionQuality() {

        final double tspFitness = this.getFitness();
        final double efficiency = this.getSolutionQuality(tspFitness);

        return efficiency;
    }

    /**
     *  Retrieves the solution quality based on the assumption that the MST cost is the
     *  best solution that could be achieved and works out a ratio against the fitness achieved.
     *
     * @param fitness the fitness value to assess.
     * @return the solution quality.
     */
    public double getSolutionQuality(double fitness) {

        final double minimumSpanningTreeCost = getMinimumSpanningTreeCost();

        final double efficiency = getSolutionQuality(fitness, minimumSpanningTreeCost);

        return efficiency;
    }

    /**
     *  Retrieves the solution quality based on the assumption that the MST cost is the
     *  best solution that could be achieved and works out a ratio against the fitness achieved.
     *
     *  Use this overloaded method to reduce the number of calls on the MST algorithm.
     *
     * @param fitness the fitness value to asses
     * @param minimumSpanningTreeCost the cost of the minimum spanning tree
     * @return the solution quality
     */
    public double getSolutionQuality(double fitness, double minimumSpanningTreeCost) {

        final double tspFitness = fitness;

        final double efficiency = ((minimumSpanningTreeCost / tspFitness) * 100);

        return efficiency;
    }
}
