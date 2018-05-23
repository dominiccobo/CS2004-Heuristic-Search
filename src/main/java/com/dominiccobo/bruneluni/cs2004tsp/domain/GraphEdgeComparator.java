package com.dominiccobo.bruneluni.cs2004tsp.domain;

import java.util.Comparator;

/**
 * Lab 8 - Graph Traversal - MST
 *
 *  Custom re-writing of Swifty's lab code, providing a more suitable
 *  following of the code-styling and principles of Java.
 *
 *  Implementation of a java comparator for comparing Graph Edges.
 *
 *  Brunel University London
 *  Department of Computer Science
 *  http://www.brunel.ac.uk/computer-science
 *
 * @author Dominic Cobo (contact@dominiccobo.com)
 *
 */
public class GraphEdgeComparator implements Comparator<GraphEdge>{

    @Override
    public int compare(GraphEdge a, GraphEdge b) {
        return Double.compare(a.getEdgeWeight(), b.getEdgeWeight());
    }
}
