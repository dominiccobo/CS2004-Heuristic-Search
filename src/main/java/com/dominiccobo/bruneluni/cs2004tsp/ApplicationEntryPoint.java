package com.dominiccobo.bruneluni.cs2004tsp;

import com.dominiccobo.bruneluni.cs2004tsp.domain.SolutionType;
import com.dominiccobo.bruneluni.cs2004tsp.samples.TSPSampleRunner;

/**
 * Entry point for application.
 */
public class ApplicationEntryPoint {

    /**
     * Application entry point
     * @param args NOT USED
     */
    public static void main(String[] args) {

        // Easily accessible parameters for demonstration purposes.
        int resamplesToRun = 25;
        int algorithmIterations = 500000;
        int rrhcRMHCRepetitions = 10000;

        TSPSampleRunner tspSampleRunner = new TSPSampleRunner(
                resamplesToRun,
                SolutionType.MINIMISATION,
                algorithmIterations,
                rrhcRMHCRepetitions
        );

        tspSampleRunner.runTests();
    }
}
