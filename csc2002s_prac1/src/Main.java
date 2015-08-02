
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Naive algorithm for filtering a collection of values according to their
 * medians This program compares the sequential implementation to the parallel
 * one.
 *
 * @author Aidan de Nobrega DNBAID001
 * @since 29/07/2015
 */
public class Main
{
    static File inputFile;
    static int filterSize;
    static File outputFile;
    static int inputSize;

    public static void main(String[] args) throws FileNotFoundException
    {
        Scanner scan = new Scanner(System.in);
        double timesSum = 0;

        System.out.print("Submit input in the following form:"
                + "\n<input file><filter size><output file>\n>>>");
        //Single line of user input split into 3 tokens
        inputFile = new File(scan.next());
        filterSize = scan.nextInt();
        outputFile = new File(scan.next());

        //Input check
        while (filterSize % 2 == 0 || filterSize < 3 || filterSize > 21)
        {
            System.out.println("Your filter size must be odd and "
                    + "range from 3 to 21 inclusive.");

            System.out.print("Submit input in the following form:"
                    + "\n<input file><filter size><output file>\n>>>");
            inputFile = new File(scan.next());
            filterSize = scan.nextInt();
            outputFile = new File(scan.next());
        }

        //Scanner now reads from the input file
        scan = new Scanner(inputFile);

        //First line in submitted .txt file. Number of subsequent lines.
        inputSize = scan.nextInt();

        //Arrays to hold the unfiltered and filtered values respectively.
        ArrayList<Double> startArray = new ArrayList<>();

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

//        for (Double element : startArray)
//        {
//            System.out.println(element.toString());
//        }
        SeqFilterObject.run(startArray);

        PrintStream outStream = new PrintStream(outputFile);

        //Size of the file and all values are printed to file
        outStream.println(inputSize);

        for (int i = 0; i < inputSize; i++)
        {
            outStream.println((i + 1) + " " + SeqFilterObject.endArray.get(i));
        }

    }
}
