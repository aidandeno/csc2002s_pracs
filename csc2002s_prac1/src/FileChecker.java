
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

/**
 *
 * @author Aidan
 */
public class FileChecker
{
    public static void main(String[] args) throws FileNotFoundException
    {
        Scanner scan = new Scanner(System.in);

        System.out.print("Type the names of the two files separated by space\n>>> ");
        File file1 = new File(scan.next());
        File file2 = new File(scan.next());
       
        scan = new Scanner(file1);
        int inputSize1 = scan.nextInt();
        double[] array1 = new double[inputSize1];
        for (int i = 0; i < inputSize1; i++)
        {
            scan.nextInt();
            double nextDouble = Double.parseDouble(scan.nextLine());
            array1[i] = nextDouble;
        }

        scan = new Scanner(file2);
        int inputSize2 = scan.nextInt();
        if (inputSize1 != inputSize2)
        {
            System.out.println("File sizes are different. Exiting.");
            System.exit(0);
        }
        
        double[] array2 = new double[inputSize1];
        for (int i = 0; i < inputSize1; i++)
        {
            scan.nextInt();
            double nextDouble = Double.parseDouble(scan.nextLine());
            array2[i] = nextDouble;
        }

        for (int i = 0; i < inputSize1; i++)
        {
            if (array1[i] != array2[i])
            {
                System.out.println("Elements at " + (i + 1) + " not equal. Exiting.");
                System.exit(0);
            }
        }
        System.out.println("Files are identical!");
    }
}
