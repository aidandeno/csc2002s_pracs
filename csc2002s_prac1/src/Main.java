
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;
import java.util.Scanner;

/**
 *
 *
 * @author Aidan de Nobrega DNBAID001
 * @since 29/07/2015
 */
public class Main
{
    public static void main(String[] args) throws FileNotFoundException
    {
        double[] startArray;
        double[] endArray;
        Scanner userScan = new Scanner(System.in);

        String inputFile = userScan.next();
        int filterSize = userScan.nextInt();
        String outputFile = userScan.next();

        //System.out.println(inputFile + " " + filterSize + " " + outputFile);
        File file = new File(inputFile);
        Scanner fileScan;

        System.out.println("Trying to read.........\n\n");

        fileScan = new Scanner(file);

        int inputSize = fileScan.nextInt();

        startArray = new double[inputSize];
        endArray = new double[inputSize];

        for (int i = 0; i < inputSize; i++)
        {
            fileScan.nextInt();

            double nextDouble = fileScan.nextDouble();
            startArray[i] = nextDouble;
            if (i < filterSize / 2 || i >= inputSize - (filterSize / 2))
            {
                endArray[i] = nextDouble;
            }
        }
//
//        System.out.println("START");
//
//        for (double element : startArray)
//        {
//            System.out.println(element);
//        }

        for (int i = filterSize / 2; i < inputSize - filterSize / 2; i++)
        {
            ArrayList<Double> tempArray = new ArrayList<>(filterSize);

            for (int k = i - (filterSize / 2); k <= i + (filterSize / 2); k++)
            {
                tempArray.add(startArray[k]);
            }

            Collections.sort(tempArray);

            endArray[i] = tempArray.get(filterSize / 2);
        }

//        System.out.println("END2");
//
//        for (double element : endArray)
//        {
//            System.out.println(element);
//        }
    }
}
