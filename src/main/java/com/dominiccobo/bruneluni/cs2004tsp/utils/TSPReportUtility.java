package com.dominiccobo.bruneluni.cs2004tsp.utils;

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Quick utility class thrown together for easing aggregated report generation for the TSP problem applied
 * with Solution-Optimisation Search Algorithms.
 */
public class TSPReportUtility {

    /**
     * Number of iterations ran for each algorithm.
     */
    private int iterations;

    /**
     * List of finesses logged.
     */
    private List<Double> fitnessesLogged;

    /**
     * List of solution qualities logged.
     */
    private List<Double> solutionQualitiesLogged;

    /**
     * The time taken for each algorithm to be executed.
     */
    private List<Long> executionTimes;

    /**
     * Initial Fitness, Can be used to represent optimal solution as well.
     */
    @SuppressWarnings("unused")
    private long initialFitness;

    /**
     * Number of repeats made to the running.
     */
    private long solutionsEvaluated;

    /**
     * Identifying name of the logging file.
     */
    private String logName;

    /**
     * Identification of the sample being logged.
     */
    private String sampleName;

    /**
     * Error prevention zero argument class.
     */
    @SuppressWarnings("unused")
    private TSPReportUtility() {
        throw new IllegalArgumentException("No argument constructor not permitted");
    }

    /**
     * General constructor for the TSP report utility class. Instantiates all fields, as well as forces values
     * to be supplied for those requiring them.
     *
     * @param sampleName Identifying name of the sample.
     * @param iterations Number of iterations being ran.
     * @param solutionsEvaluated the number of solutions to be evaluated.
     * @param logName the named instance identifier.
     */
    public TSPReportUtility(String sampleName, long solutionsEvaluated, int iterations, String logName) {
        this.solutionsEvaluated = solutionsEvaluated;
        this.logName = logName;
        this.iterations = iterations;
        this.sampleName = sampleName;

        this.fitnessesLogged = new ArrayList<>();
        this.solutionQualitiesLogged = new ArrayList<>();
        this.executionTimes = new ArrayList<>();
    }

    /**
     * Appends a logged fitness value and solution quality to the log, in order to future allow statistical
     * analysis.
     * @param fitnessValue the fitness value for the current run to log.
     * @param solutionQuality the solution quality for the current run to log.
     * @param executionTimeForRun the time in nano seconds taken for the current run.
     */
    public void appendRun(Double fitnessValue, Double solutionQuality, Long executionTimeForRun) {
        fitnessesLogged.add(fitnessValue);
        solutionQualitiesLogged.add(solutionQuality);
        executionTimes.add(executionTimeForRun);
    }

    /**
     * Overloaded, double parameter call of the createLog functional, not permitting additional parameters.
     *
     *  @param initialFitness the initial fitness to log.
     *  @param mst the mst cost to log.
     */
    public void createLog(double initialFitness, double mst) {
        createLog(initialFitness, mst, null, null);
    }

    /**
     * Finalises the logging and appends the run. Requires previous runs to be logged to work.
     *  @param mst the mst cost to log
     *  @param initialFitness the initial fitness to log.
     *  @param additionalHeaders the additional column headers to appending for extra details.
     *  @param additionalLog the details of the additional column headers to append.
     */
    public void createLog(double initialFitness, double mst, String[] additionalHeaders, String[] additionalLog) {

        if(solutionsEvaluated != fitnessesLogged.size() || solutionsEvaluated != solutionQualitiesLogged.size()) {
            //throw new Exception("Not all solutions were logged");
            return;
        }

        if((additionalHeaders != null && additionalLog != null) && additionalHeaders.length != additionalLog.length) {
            //throw new Exception("Headers length must match additional log length.");
            return;
        }

        String[] logHeaders = new String[]{
            "Algorithm Name", "Resamples", "Fitness (min)", "Fitness (max)", "Fitness (range)",
            "Fitness (mean)", "Sol Quality (min)", "Sol Quality (max)", "Sol Quality (range)", "Sol Quality (mean)",
                "Initial Fitness", "Iterations", "Quickest Run (ns)", "Slowest Run (ns)", "Run Range (ns)",
                "Average Run (ns)", "MST"
        };

        // resize the array - #ApacheCommons FTW :)
        if(additionalHeaders != null) {
            logHeaders = ArrayUtils.addAll(logHeaders, additionalHeaders);
        }

        String logContent[] = new String[logHeaders.length];
        logContent[0] = sampleName;
        logContent[1] = String.valueOf(solutionsEvaluated);
        logContent[2] = Collections.min(fitnessesLogged).toString();
        logContent[3] = Collections.max(fitnessesLogged).toString();
        logContent[4] = String.valueOf(Double.valueOf(logContent[3]) - Double.valueOf(logContent[2]));
        logContent[5] = String.valueOf(fitnessesLogged.stream().mapToDouble(dbl -> dbl).average().getAsDouble());
        logContent[6] = Collections.min(solutionQualitiesLogged).toString();
        logContent[7] = Collections.max(solutionQualitiesLogged).toString();
        logContent[8] = String.valueOf(Double.valueOf(logContent[7]) - Double.valueOf(logContent[6]));
        logContent[9] = String.valueOf(solutionQualitiesLogged.stream().mapToDouble(dbl -> dbl).average().getAsDouble());
        logContent[10] = String.valueOf(initialFitness);
        logContent[11] = String.valueOf(iterations);
        logContent[12] = String.valueOf(Collections.min(executionTimes));
        logContent[13] = String.valueOf(Collections.max(executionTimes));
        logContent[14] = String.valueOf(Double.valueOf(logContent[13]) - Double.valueOf(logContent[12]));
        logContent[15] = String.valueOf(executionTimes.stream().mapToDouble(dbl -> dbl).average().getAsDouble());
        logContent[16] = String.valueOf(mst);

        // append the new content to the predefined content.
        if(additionalLog != null) {
            for(int i = 17, j = 0; i < logContent.length && j < additionalLog.length; i++, j++) {
                logContent[i] = additionalLog[j];
            }
        }

        // log the summaries
        AlgorithmAnalysisLogger summaryLogger = new AlgorithmAnalysisLogger(
                logName,
                logHeaders
        );

        summaryLogger.insertLog(logContent);
        summaryLogger.close();
    }
}
