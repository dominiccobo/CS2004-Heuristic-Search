package com.dominiccobo.bruneluni.cs2004tsp.domain;

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
public class GraphEdge {

    /**
     * Edge Row index in the graph matrix, otherwise seen as a node.
     */
    private int rowIndex;

    /**
     * Edge column index in the graph matrix, otherwise seen as a node.
     */
    private int columnIndex;

    /**
     * Edge weight.
     */
    private double edgeWeight;

    public GraphEdge(int rowIndex, int columnIndex, double edgeWeight) {
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
        this.edgeWeight = edgeWeight;
    }

    @Override
    public String toString() {
        return String.format("Graph Edge:\nMatrix Node: %d,%d\nWeight: %f",
                this.rowIndex,
                this.columnIndex,
                this.edgeWeight
        );
    }

    public double getEdgeWeight() {
        return edgeWeight;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public int getColumnIndex() {
        return columnIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public void setColumnIndex(int columnIndex) {
        this.columnIndex = columnIndex;
    }

    public void setEdgeWeight(double edgeWeight) {
        this.edgeWeight = edgeWeight;
    }
}
