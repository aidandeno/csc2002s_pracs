
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.concurrent.ForkJoinPool;

/**
 * Naive algorithm for filtering a collection of values according to either
 * their medians or means (user-determined). This program compares a sequential
 * implementation to parallel ones. There are 2 parallel implementations: one
 * using the Java ForkJoin Framework, and another using standard threads (also
 * user-determined).
 *
 * @author Aidan de Nobrega DNBAID001
 * @since 29/07/2015
 */
public class Main
{
    //******FIELDS******//

    public static final ForkJoinPool pool = new ForkJoinPool();

    /**
     * Holds the values from the input file. Once values are added to the array,
     * the array remains unchanged.
     */
    public static final ArrayList<Double> startArray = new ArrayList<>();
    /**
     * Holds the filtered values to be outputted.
     */
    public static ArrayList<Double> finalArray;

    /**
     * The size of the window over which filtering will take place.
     */
    public static int filterSize;

    /**
     * true -> median filtering false -> mean filtering
     */
    public static boolean filterType;

    //******METHODS******//
    /**
     * Main process.
     *
     * Takes user input, reads data from inputFile, applies appropriate
     * filtering method with appropriate implementation, records wall-time
     * efficiency, and prints to output file in same format as input file.
     *
     * Note: the wall-time efficiency is only recorded over the period during
     * which the filtering is undertaken. It stops before the program prints to
     * file. Time is recorded in milliseconds because of the infinitesimal
     * nature of the results.
     *
     * @param args None
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException, InterruptedException
    {
        Scanner scan = new Scanner(System.in);

        //Single line of user input split into 3 tokens
        File inputFile = new File(args[0]);
        filterSize = Integer.parseInt(args[1]);
        File outputFile = new File(args[2]);

        //Input validation
        while (filterSize % 2 == 0 || filterSize < 3 || filterSize > 21)
        {
            System.out.println("Your filter size must be odd and "
                    + "range from 3 to 21 inclusive.");

            System.out.print("Submit input in the following form:"
                    + "\n<input file> <filter size> <output file>\n>>> ");
            inputFile = new File(scan.next());
            filterSize = scan.nextInt();
            outputFile = new File(scan.next());
        }

        //User chooses whether to run a median or mean filter
        System.out.print("Filter Type?\n(1) Median\n(2) Mean\nType '1' or '2'\n>>> ");
        int filterTypeAnswer = scan.nextInt();
        filterType = filterTypeAnswer == 1;

        //User chooses from 1 of 3 impementations
        System.out.print("Method?\n(1) Sequential\n(2) Parallel (ForkJoin "
                + "Framework)\n(3) Parallel (Standard Threads)\nType '1', '2', "
                + "or '3'\n>>> ");
        int method = scan.nextInt();

        scan = new Scanner(inputFile);

        //First line in submitted .txt file. Number of subsequent lines.
        int inputSize = scan.nextInt();

        //Arrays to hold the unfiltered and filtered values respectively.
        //Iterate through the input
        for (int i = 0; i < inputSize; i++)
        {
            //Scan and discard line number
            scan.nextInt();

            //Scan nextLine() and cast to double to keep negative operator
            //Values are added to initial array
            double nextDouble = Double.parseDouble(scan.nextLine());
            startArray.add(nextDouble);
        }

        //finalArray is instantiated as same size as original array.
        finalArray = new ArrayList<>(startArray);

        //The initial elements outside the borders are added to finalArray
        for (int i = 0; i < filterSize / 2; i++)
        {
            finalArray.set(i, startArray.get(i));
        }

        //Filtering will run within the borders
        FilterObject filt = new FilterObject(filterSize / 2,
                startArray.size() - (filterSize / 2), filterType);

        ArrayList<Double> times = new ArrayList<>(20);//to calculate average run time
        int numP = Runtime.getRuntime().availableProcessors();

        for (int run = 1; run < 21; run++) //algorithm will run 20 times
        {
            long startTime = System.nanoTime();

            switch (method)
            {
                case 1: //Sequential
                    filt.seqFilter();
                    break;
                case 2: //ForkJoin Framework
                    pool.invoke(new FilterObject(filterSize / 2,
                            startArray.size() - (filterSize / 2), filterType)); //anonymous object pass
                    break;
                case 3://Standard Threads (4 threads)
                    ProcessorThread[] threads = new ProcessorThread[numP];
                    int subArraySize = inputSize - (filterSize - 1);
                    for (int i = 0; i < numP; i++)
                    {
                        threads[i] = new ProcessorThread(((i * subArraySize) / numP) + (filterSize / 2),
                                (((i + 1) * subArraySize) / numP) + (filterSize / 2), filterType);
                        threads[i].start();
                    }
                    while (ProcessorThread.activeCount() != 1)
                    {
                        //the program only continues parsing if the threads have all executed completely.
                    }
                    break;
                default:
                    System.out.println("You did not choose a valid option. Exiting.");
                    System.exit(0);
                    break;
            }

            //The last few elements outside the borders are added to finalArray
            for (int i = startArray.size() - (filterSize / 2); i < startArray.size(); i++)
            {
                finalArray.set(i, startArray.get(i));
            }

            //Calculates time elapsed per run
            Double timeElapsed = (System.nanoTime() - startTime) / 1000000.0;
            times.add(run - 1, timeElapsed);
            System.out.println("Run " + run + ": " + timeElapsed + " milliseconds.");
        }

        //Calculates average run time over 20 runs after removing highest value
        times.remove(Collections.max(times));
        double timesSum = 0;
        for (Double e : times)
        {
            timesSum += e;
        }
        double average = timesSum / 19;
        if (method == 1)
        {
            System.out.println("Adjusted sequential run average: " + average + " milliseconds.");
        }
        else if (method == 2)
        {
            System.out.println("Adjusted parallel (ForkJoin framework) run average:  " + average + " milliseconds.");
        }
        else
        {
            System.out.println("Adjusted parallel (standard threads) run average:  " + average + " milliseconds.");
        }

        //All that follows is for printing to the output file.
        PrintStream outStream = new PrintStream(outputFile);

        System.out.print("Printing to file...");
        //Size of the file and all values are printed to file
        outStream.println(startArray.size());

        for (int i = 0; i < startArray.size(); i++)
        {
            outStream.println((i + 1) + " " + finalArray.get(i));
        }
        System.out.println("Finished!");
    }
}
