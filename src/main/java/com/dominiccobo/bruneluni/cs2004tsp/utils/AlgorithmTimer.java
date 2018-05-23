package com.dominiccobo.bruneluni.cs2004tsp.utils;

/**
 * Simple Timer for analysing algorithm execution times.
 *
 * @author Dominic Cobo (contact@dominiccobo.com)
 */
public class AlgorithmTimer {


    /**
     * Timer start time.
     */
    private long startTime;

    /**
     * Timer end time.
     */
    private long endTime;

    /**
     * Starts algorithm timer.
     */
    public void start() {
        this.startTime = System.nanoTime();
    }

    /**
     * Stops algorithm timer.
     */
    public void end() {
        this.endTime = System.nanoTime();
    }

    /**
     * Calculates duration in nanoseconds between timers.
     * @return the duration in ms.
     */
    public long getDuration() {
        return this.endTime - startTime;
    }


    @SuppressWarnings("unused")
    public long getStartTime() {
        return startTime;
    }

    @SuppressWarnings("unused")
    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    @SuppressWarnings("unused")
    public long getEndTime() {
        return endTime;
    }

    @SuppressWarnings("unused")
    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
}
