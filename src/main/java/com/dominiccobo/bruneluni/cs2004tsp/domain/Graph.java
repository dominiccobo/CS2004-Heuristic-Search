package com.dominiccobo.bruneluni.cs2004tsp.domain;

import java.util.Vector;
import java.util.concurrent.ThreadLocalRandom;

/**
 * General representation of a distance graph of doubles.
 *
 * Provides useful methods for analysing distance graph.
 *
 * @author Dominic Cobo (contact@dominiccobo.com)
 */
public class Graph {

    /**
     * Square matrix representing the distance between two
     */
    private double[][] distanceMatrix;

    /**
     * Validator constructor preventing Graphs from being instantiated without any data.
     */
    @SuppressWarnings("unused")
    private Graph() {
        throw new IllegalArgumentException("No null constructor permitted");
    }

    /**
     * Constructor instantiating matrix class.
     *
     * @param distanceMatrix the squared array of doubles used to represent the distance matrix.
     */
    public Graph(double[][] distanceMatrix) {
        this.setDistanceMatrix(distanceMatrix);
    }

    /**
     * Retrieves the distance between two specified nodes.
     * @param nodeAIndex the row index of the node to retrieve.
     * @param nodeBIndex the column index of the node to retrieve.
     *
     * @return the distance between the two nodes.
     */
    @SuppressWarnings("all")
    public double getDistanceBetweenNodes(int nodeAIndex, int nodeBIndex) {

        if(nodeAIndex >= distanceMatrix.length || nodeBIndex >= distanceMatrix[0].length) {
            throw new IllegalArgumentException("Provided invalid matrix indices");
        }

        double distance = distanceMatrix[nodeAIndex][nodeBIndex];

        return distance;

    }

    /**
     * Provides the length of a total tour given a described graph and the route taken.
     *
     * @param nodesInTour the ordered list of nodes, numerically indexed, through which a route is taken.
     * @return the total distance traversed.
     */
    public double getDistanceGivenRoute(Vector<Integer> nodesInTour) {

        if(nodesInTour == null || nodesInTour.size() < 1) {
            throw new IllegalArgumentException("Provided invalid list of nodes describing tour.");
        }

        double traversedDistanceTotal = 0D;

        for(int i = 0; i < ( nodesInTour.size() - 1); i++) {
            final int nodeA = nodesInTour.get(i);
            final int nodeB = nodesInTour.get(i+1);
            traversedDistanceTotal += this.getDistanceBetweenNodes(nodeA, nodeB);
        }

        final int endCityNode = nodesInTour.get((nodesInTour.size()-1));
        final int startCityNode = nodesInTour.get(0);

        traversedDistanceTotal += this.getDistanceBetweenNodes(endCityNode, startCityNode);

        return traversedDistanceTotal;
    }

    /**
     * Validated setter for distance matrix attribute, prevents illegal values being used.
     *
     * Prevents:
     *      - Null matrices
     *      - Irregular matrices
     *
     * @param distanceMatrix distance matrix to verify and set.
     */
    private void setDistanceMatrix(double[][] distanceMatrix) {

        if(distanceMatrix == null) {
            throw new IllegalArgumentException("Invalid distance matrix, cannot be null");
        }

        for(double[] rows: distanceMatrix) {
            if(rows.length != distanceMatrix.length) {
                throw new IllegalArgumentException("Invalid distance matrix, must be regular square.");
            }
        }

        this.distanceMatrix = distanceMatrix;
    }

    /**
     * Produces a random permutation of the route based on the presented graph.
     *
     * @return the random permutation.
     */
    public Vector<Integer> getRandomRoute() {

        final int lengthOfRoute = distanceMatrix.length;
        final Vector<Integer> nodesToChoseFrom = new Vector<>();
        final Vector<Integer> randomRoute = new Vector<>();

        for (int i = 0; i < lengthOfRoute; i++) {
            nodesToChoseFrom.add(i);
        }

        while(nodesToChoseFrom.size() > 0) {
            final int randomIndexToChange = ThreadLocalRandom.current().nextInt(0, nodesToChoseFrom.size());
            final int randomNodeToSwap = nodesToChoseFrom.get(randomIndexToChange);
            nodesToChoseFrom.remove(randomIndexToChange);
            randomRoute.add(randomNodeToSwap);
        }

        return randomRoute;
    }

    /**
     * Getter for the distance matrix.
     *
     * @return the current value of the distance matrix.
     */
    public double[][] getDistanceMatrix() {
        return distanceMatrix;
    }
}
