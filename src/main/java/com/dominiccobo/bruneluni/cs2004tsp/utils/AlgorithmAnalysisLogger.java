package com.dominiccobo.bruneluni.cs2004tsp.utils;

import org.apache.commons.lang3.SystemUtils;

import java.io.*;

/**
 * Implementation of a simple logger enabling logging of algorithm execution times, with their names
 * and respective input sizes.
 *
 * @author Dominic Cobo (contact@dominiccobo.com)
 */
public class AlgorithmAnalysisLogger {

    /**
     * File to write the logged values to.
     */
    private File logFile;

    /**
     * Writer handler
     */
    private PrintWriter printWriter;

    /**
     * Name of the algorithm being tested.
     */
    private String algorithmName;

    /**
     * Headers to log
     */
    private String[] columnHeaders;

    /**
     * Constructor for preventing non parameterized instantiation of model.
     */
    @SuppressWarnings("unused")
    private AlgorithmAnalysisLogger() {
        throw new IllegalArgumentException("No args constructors are not permitted.");
    }

    /**
     * Simple constructor initiating a logger.
     * @param algorithmName the name of the algorithm to log.
     */
    public AlgorithmAnalysisLogger(String algorithmName, String[] columnHeaders) {
        this.algorithmName = algorithmName;
        this.columnHeaders = columnHeaders;
        this.setLogFile(algorithmName);
        this.init();
    }

    /**
     * Initialise the log file.
     *
     * Creates a new file if the log file does not exist, else if it exists, opens it in append mode.
     */
    @SuppressWarnings("all")
    private void init() {
        final String logHeaders = String.join(",", columnHeaders)+"\n";
        try {

            if(!logFile.exists()) {
                logFile.createNewFile();
                printWriter = new PrintWriter(logFile);
                printWriter.write(logHeaders);
            }
            else {
                printWriter = new PrintWriter(new BufferedWriter(
                    new FileWriter(logFile, true)
                ));
            }


        } catch (IOException  e) {
            e.printStackTrace();
        }


    }

    /**
     * Establishes the log file directory as the desktop depending on the environment in which it is executed.
     *
     * (Tested on Windows 10, OSX Capitan and Debian)
     *
     * @param algorithmName the algorithm filename to log.
     */
    private void setLogFile(String algorithmName) {
        // windows
        if(SystemUtils.IS_OS_WINDOWS) {
            this.logFile = new File(System.getProperty("user.home") + "/Desktop/" + algorithmName + ".csv");
        }
        // mac
        else if(SystemUtils.IS_OS_MAC) {
            this.logFile = new File(
                    "/Users/" + System.getProperty("user.name") +"/desktop/" + algorithmName + ".csv"
            );
        }
        // unix
        else if(SystemUtils.IS_OS_UNIX) {
            this.logFile = new File(
                    System.getProperty("user.home") + "/Desktop/" + algorithmName + ".csv"
            );
        }
        else {
            throw new RuntimeException("Unable to determine OS");
        }
    }

    /**
     * Log an item.
     *
     * @param logItems the array of String columns to append
     */
    public void insertLog(String logItems[]) {

        if(logItems.length != columnHeaders.length) {
            throw new IllegalArgumentException(
                    "The number of items logged, must match the expected " + columnHeaders.length + " items"
            );
        }

        final String entry = String.join(",", logItems) + "\n";

        printWriter.print(entry);
    }

    /**
     * Close the print writer connection.
     */
    public void close() {
        printWriter.close();
    }
}
