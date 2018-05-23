package com.dominiccobo.bruneluni.cs2004tsp.utils;

import java.io.*;
import java.util.Vector;

/**
 * Class containing a series of utilities for handling files.
 *
 * @author Dominic Cobo (contact@dominiccobo.com)
 */
public class Utilities {

    @SuppressWarnings("unused")
    private Utilities() {
        throw new IllegalArgumentException("No constructor permitted");
    }

    /**
     * Reads in text file and parses numbers into an integer array.
     *
     * "Code is not very good and should work" - Dr Stephen Swift 2k18
     *
     * @param filename the filename, including path, to read.
     * @return the array list of integers read in.
     */
    public static Vector<Integer> readIntegerFile(String filename) {

        Vector<Integer> readInValues = new Vector<>();
        Reader reader;

        try {

            reader = new BufferedReader(new FileReader(filename));
            StreamTokenizer streamTokenizer = new StreamTokenizer(reader);
            streamTokenizer.parseNumbers();
            streamTokenizer.nextToken();

            while (streamTokenizer.ttype != StreamTokenizer.TT_EOF) {
                if (streamTokenizer.ttype == StreamTokenizer.TT_NUMBER) {
                    readInValues.add((int) (streamTokenizer.nval));
                }
                streamTokenizer.nextToken();
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return readInValues;
    }


    /**
     * Reads in matrix of array data set, returning a double array.
     *
     * @param filename the filename, including path, to read in.
     * @param sep the delimiter separating columns.
     * @return the read in array of doubles.
     */
    public static double[][] readArrayFile(String filename, String sep) {

        double readValue[][] = null;

        try {

            BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));
            String currentLine;
            int numberOfColumns = 0;
            int numberOfRows = 0;

            while ((currentLine = bufferedReader.readLine()) != null) {
                ++numberOfRows;
                String[] columns = currentLine.split(sep);
                numberOfColumns = Math.max(numberOfColumns, columns.length);
            }

            readValue = new double[numberOfRows][numberOfColumns];
            bufferedReader = new BufferedReader(new FileReader(filename));

            int rowIndex = 0;

            while ((currentLine = bufferedReader.readLine()) != null) {

                String[] columns = currentLine.split(sep);
                for (int columnIndex = 0; columnIndex < columns.length; ++columnIndex) {
                    readValue[rowIndex][columnIndex] = Double.parseDouble(columns[columnIndex]);
                }
                ++rowIndex;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return readValue;
    }

    /**
     * Returns the absolute path of a resource by Name.
     *
     * @param resourceName the resource name to retrieve, which may include path.
     * @return the absolute file system path to the resource.
     */
    public static String getResourcePath(String resourceName) {

        String foundPath = "";

        final ClassLoader klassLoader = Utilities.class.getClassLoader();

        try {
            final File file = new File(klassLoader.getResource(resourceName).getFile());
            foundPath = file.getAbsolutePath();
        }
        catch (NullPointerException e) {
            e.printStackTrace();
        }

        return foundPath;
    }

}
