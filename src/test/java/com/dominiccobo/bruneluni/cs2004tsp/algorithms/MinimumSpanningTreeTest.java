package com.dominiccobo.bruneluni.cs2004tsp.algorithms;

import org.junit.Test;

import java.util.Arrays;

public class MinimumSpanningTreeTest {

    @Test
    public void runOnce() {

        double[][] matrix = new double[][]{
                {0.0000,  4726.0,   1204.0,  6362.0},
                {4726.0,  0.0000,   3587.0,  2011.0},
                {1204.0,  3587.0,   0.0000,  5162.0},
                {6362.0,  2011.0,   5162.0,  0.0000}
        };

        double[][] result = MinimumSpanningTree.primsMST(matrix);

        double MST = Arrays.stream(result).mapToDouble(row -> {
            return Arrays.stream(row).sum();
        }).sum();

        System.out.println(MST);

        System.out.println(Arrays.deepToString(result));
    }

}