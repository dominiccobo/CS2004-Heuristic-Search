package com.dominiccobo.bruneluni.cs2004tsp.domain;

import org.junit.Test;

import java.util.Arrays;
import java.util.Vector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

/**
 * Tests for verifying correct functionality for the Graph
 */
public class GraphTest {

    @Test
    public void givenMatrixAndNodes_ObtainsCorrectDistance() {

        double[][] testMatrix = new double[][]{
                {0.0, 2000, 3000},
                {2000, 0.0, 3000},
                {3000, 2000, 0.0}
        };

        Graph graph = new Graph(testMatrix);
        double result = graph.getDistanceBetweenNodes(0, 2);
        double expected = 4242.640687D;

        String output = String.format("Found:\t\t%f\nExcepted:\t%f", result, expected);

        System.out.println(output);

        if(Double.compare(result, expected) == 0) {
            fail();
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenIrregularTestMatrix_ProducesError() {
        double[][] testMatrix = new double[][]{
                {0.0, 2000, 3000},
                {2000, 0.0},
                {3000, 2000, 0.0}
        };

        Graph graph = new Graph(testMatrix);
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenNullTestMatrix_ProducesError() {

        Graph graph = new Graph(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenNullDescribedRoute_ProducesError() {
        double[][] testMatrix = new double[][]{
                {0.0000,  4726.0,   1204.0,  6362.0},
                {4726.0,  0.0000,   3587.0,  2011.0},
                {1204.0,  3587.0,   0.0000,  5162.0},
                {6362.0,  2011.0,   5162.0,  0.0000}
        };

        Graph graph = new Graph(testMatrix);

        graph.getDistanceGivenRoute(null);
    }

    @Test
    public void givenDescribedRouteA_ProducesExpectedTourDistance() {

        double[][] testMatrix = new double[][]{
                {0.0000,  4726.0,   1204.0,  6362.0},
                {4726.0,  0.0000,   3587.0,  2011.0},
                {1204.0,  3587.0,   0.0000,  5162.0},
                {6362.0,  2011.0,   5162.0,  0.0000}
        };

        Graph graph = new Graph(testMatrix);

        final double expectedA = 19837.0;

        Vector<Integer> listOfNodesInTourA = new Vector<>();
        listOfNodesInTourA.add(0);
        listOfNodesInTourA.add(1);
        listOfNodesInTourA.add(2);
        listOfNodesInTourA.add(3);

        final double resultA = graph.getDistanceGivenRoute(listOfNodesInTourA);

        assertEquals(resultA, expectedA, 0.1);
    }

    @Test
    public void givenDescribedRouteB_ProducesExpectedTourDistance() {

        double[][] testMatrix = new double[][]{
                {0.0000,  4726.0,   1204.0,  6362.0},
                {4726.0,  0.0000,   3587.0,  2011.0},
                {1204.0,  3587.0,   0.0000,  5162.0},
                {6362.0,  2011.0,   5162.0,  0.0000}
        };

        Graph graph = new Graph(testMatrix);

        final double expectedB = 13164.0;

        Vector<Integer> listOfNodesInTourB = new Vector<>();

        listOfNodesInTourB.add(0);
        listOfNodesInTourB.add(2);
        listOfNodesInTourB.add(1);
        listOfNodesInTourB.add(3);

        final double resultB = graph.getDistanceGivenRoute(listOfNodesInTourB);

        assertEquals(resultB, expectedB, 0.1);
    }

    @Test
    public void givenStressTestOfRandomRoute_ProducesAdequateSizeResult() {


        double[][] testMatrix = new double[][]{
                {0.0000,  4726.0,   1204.0,  6362.0},
                {4726.0,  0.0000,   3587.0,  2011.0},
                {1204.0,  3587.0,   0.0000,  5162.0},
                {6362.0,  2011.0,   5162.0,  0.0000}
        };

        final int roundsOfTesting = 10000;

        Graph graph = new Graph(testMatrix);

        for(int i = 0; i < roundsOfTesting; i++) {

            Vector<Integer> randomRoute = graph.getRandomRoute();

            if(randomRoute.size() != testMatrix.length) {
                fail(
                        String.format("Found: %d\nExpected: %d", randomRoute.size(), testMatrix.length)
                );
            }
        }

    }

    @Test
    public void givenStressTestOfRandomRoute_ProducesResultWithAllNodes() {


        double[][] testMatrix = new double[][]{
                {0.0000,  4726.0,   1204.0,  6362.0},
                {4726.0,  0.0000,   3587.0,  2011.0},
                {1204.0,  3587.0,   0.0000,  5162.0},
                {6362.0,  2011.0,   5162.0,  0.0000}
        };

        final int roundsOfTesting = 10000;

        Graph graph = new Graph(testMatrix);

        int[] expectedValues = {0, 1, 2, 3};

        for(int i = 0; i < roundsOfTesting; i++) {

            Vector<Integer> randomRoute = graph.getRandomRoute();

            for(int val: expectedValues) {
                if(!randomRoute.contains(val)) {
                    fail(
                            String.format(
                                    "Found Containing: %s\nExpected Containing: %s",
                                    randomRoute.toString(),
                                    Arrays.toString(expectedValues)
                            )
                    );
                }
            }
        }

    }

}