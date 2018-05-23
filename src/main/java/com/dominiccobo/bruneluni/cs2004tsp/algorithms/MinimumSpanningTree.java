package com.dominiccobo.bruneluni.cs2004tsp.algorithms;

import com.dominiccobo.bruneluni.cs2004tsp.domain.GraphEdge;
import com.dominiccobo.bruneluni.cs2004tsp.domain.GraphEdgeComparator;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Lab 8 - Graph Traversal - MST
 *
 *  Custom re-writing of Swifty's lab code, providing a more suitable
 *  following of the code-styling and principles of Java.
 *
 *  Brunel University London
 *  Department of Computer Science
 *  http://www.brunel.ac.uk/computer-science
 *
 * @author Dominic Cobo (contact@dominiccobo.com)
 *
 */
public class MinimumSpanningTree {

    /**
     * Locates the next applicable edge in a list of vertices and edges.
     *
     * @param vertices the list of vertices to search
     * @param edges the list of edges to search
     * @return the located graph edge
     */
    private static GraphEdge locateEdge(ArrayList<Integer> vertices, ArrayList<GraphEdge> edges) {

        for (GraphEdge edge : edges) {
            int rowIdx = edge.getRowIndex();
            int colIdx = edge.getColumnIndex();

            int rowPosInVertexListIdx = vertices.indexOf(rowIdx);
            int colPosInVertexListIdx = vertices.indexOf(colIdx);

            // if edge was found then return
            if ((rowPosInVertexListIdx > -1 && colPosInVertexListIdx == -1) ||
                    (rowPosInVertexListIdx == -1 && colPosInVertexListIdx > -1)) {

                return edge;
            }
        }
        // return what swifty calls the error condition.
        return new GraphEdge(-1, -1, 0.0);
    }

    /**
     * Implementation of the common Prim's Matrix.
     *
     * @param distanceMatrix a matrix of distances to apply prim's to.
     * @return the resultant generated graph from the algorithm.
     */
    public static double[][] primsMST(double[][] distanceMatrix) {

        if(distanceMatrix == null) {
            throw new IllegalArgumentException("Null input");
        }

        int squaredSizeOfMatrix = distanceMatrix.length;

        double[][] resultantGraph = new double[squaredSizeOfMatrix][squaredSizeOfMatrix];
        ArrayList<GraphEdge> edges = new ArrayList<>();

        for(int rowIdx = 0; rowIdx < squaredSizeOfMatrix; rowIdx++) {

            for(int colIdx = 0; colIdx < squaredSizeOfMatrix; colIdx++) {

                if(distanceMatrix[rowIdx].length != distanceMatrix[colIdx].length) {
                    throw new IllegalArgumentException("Algorithm will only work with a symmetric matrix");
                }

                if(distanceMatrix[rowIdx][colIdx] != 0.0) {
                    edges.add(new GraphEdge(rowIdx, colIdx, distanceMatrix[rowIdx][colIdx]));
                }
            }
        }

        // sort the edges using our useless implementation of a double comparator.
        edges.sort(new GraphEdgeComparator());

        if(edges.size() == 0) {
            return resultantGraph;
        }

        ArrayList<Integer> vertices = new ArrayList<>();
        vertices.add(edges.get(0).getRowIndex());

        // connect all nodes without forming cycle.
        while(vertices.size() != squaredSizeOfMatrix) {

            GraphEdge currentEdge = locateEdge(vertices, edges);
            if(vertices.indexOf(currentEdge.getRowIndex()) == -1) {
                vertices.add(currentEdge.getRowIndex());
            }
            if(vertices.indexOf(currentEdge.getColumnIndex()) == -1) {
                vertices.add(currentEdge.getColumnIndex());
            }
            resultantGraph[currentEdge.getRowIndex()][currentEdge.getColumnIndex()] = currentEdge.getEdgeWeight();
            resultantGraph[currentEdge.getColumnIndex()][currentEdge.getRowIndex()] = currentEdge.getEdgeWeight();
        }

        return resultantGraph;
    }


    /**
     * Calculates the MST cost of a given minimum spanning tree.
     *
     * @param minimumSpanningTreeMatrix the minimum spanning tree to calculate the cost of.
     * @return the resulting cost.
     */
    public static double getMSTCost(double[][] minimumSpanningTreeMatrix) {
        return (Arrays.stream(minimumSpanningTreeMatrix).mapToDouble(row -> Arrays.stream(row).sum()).sum())/2;
    }
}
