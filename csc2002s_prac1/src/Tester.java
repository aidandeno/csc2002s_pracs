
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.concurrent.ForkJoinPool;

public class Tester
{
    //******FIELDS******//

    public static final ForkJoinPool pool = new ForkJoinPool();
    
    public static final File inputFile = new File("");
    
    public static final File outputFile = new File("");

    public static final ArrayList<Double> startArray = new ArrayList<>();

    public static ArrayList<Double> finalArray;

    public static int filterSize = 3;

    public static boolean filterType = true;
    
    public static int threshold = 10;

    //******METHODS******//
    public static void main(String[] args) throws FileNotFoundException
    {        
        Scanner scan = new Scanner(inputFile);

        int inputSize = scan.nextInt();

        for (int i = 0; i < inputSize; i++)
        {

            scan.nextInt();

            double nextDouble = Double.parseDouble(scan.nextLine());
            startArray.add(nextDouble);
        }

        finalArray = new ArrayList<>(startArray);

        for (int i = 0; i < filterSize / 2; i++)
        {
            finalArray.set(i, startArray.get(i));
        }

        FilterObject filt = new FilterObject(filterSize / 2,
                startArray.size() - (filterSize / 2), filterType);

        ArrayList<Double> times = new ArrayList<>(20);

        for (int run = 1; run < 21; run++) 
        {
            long startTime = System.nanoTime();

            switch (method)
            {
                case 1: 
                    filt.seqFilter();
                    break;
                case 2: 
                    pool.invoke(filt);
                    break;
                case 3:
                    ProcessorThread[] threads = new ProcessorThread[4];
                    int subArraySize = inputSize - (filterSize - 1);
                    for (int i = 0; i < 4; i++)
                    {
                        threads[i] = new ProcessorThread(((i * subArraySize) / 4) + (filterSize / 2),
                                (((i + 1) * subArraySize) / 4) + (filterSize / 2), filterType);
                        threads[i].start();
                    }
                    break;
                default:
                    System.out.println("You did not choose a valid option. Exiting.");
                    System.exit(0);
                    break;
            }

            for (int i = startArray.size() - (filterSize / 2); i < startArray.size(); i++)
            {
                finalArray.set(i, startArray.get(i));
            }

            Double timeElapsed = (System.nanoTime() - startTime) / 1000000.0;
            times.add(run - 1, timeElapsed);
            System.out.println("Run " + run + ": " + timeElapsed + " milliseconds.");
        }

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

        PrintStream outStream = new PrintStream(outputFile);

        outStream.println(startArray.size());

        for (int i = 0; i < startArray.size(); i++)
        {
            outStream.println((i + 1) + " " + finalArray.get(i));
        }
    }
}
