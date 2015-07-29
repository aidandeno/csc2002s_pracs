
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
        Scanner scan = new Scanner(System.in);

        //single line of user input split into 3 tokens
        String inputFile = scan.next();
        int filterSize = scan.nextInt();
        String outputFile = scan.next();

        File inFile = new File(inputFile);
        scan = new Scanner(inFile);

        int inputSize = scan.nextInt();

        double[] startArray = new double[inputSize];
        double[] endArray = new double[inputSize];

        for (int i = 0; i < inputSize; i++)
        {
            scan.nextInt();

            double nextDouble = Double.parseDouble(scan.nextLine());
            startArray[i] = nextDouble;
            
            if (i < filterSize / 2 || i >= inputSize - (filterSize / 2))
            {
                endArray[i] = nextDouble;
            }
        }

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
        
        PrintStream outStream = new PrintStream(new FileOutputStream(outputFile));
        
        outStream.println(inputSize);
        for (int i = 0; i < inputSize; i++)
        {
            outStream.println((i+1) + " " + endArray[i]);
        }
    }
}
