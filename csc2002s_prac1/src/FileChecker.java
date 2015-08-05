
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

        ArrayList<Double> array1 = new ArrayList<>();
        ArrayList<Double> array2 = new ArrayList<>();

        scan = new Scanner(file1);
        int inputSize1 = scan.nextInt();
        for (int i = 0; i < inputSize1; i++)
        {
            scan.nextInt();
            double nextDouble = Double.parseDouble(scan.nextLine());
            array1.add(nextDouble);
        }

        scan = new Scanner(file2);
        int inputSize2 = scan.nextInt();
        if (inputSize1 != inputSize2)
        {
            System.out.println("File sizes are different. Exiting.");
            System.exit(0);
        }
        for (int i = 0; i < inputSize2; i++)
        {
            scan.nextInt();
            double nextDouble = Double.parseDouble(scan.nextLine());
            array2.add(nextDouble);
        }

        for (int i = 0; i < inputSize1; i++)
        {
            if (!Objects.equals(array1.get(i), array2.get(i)))
            {
                System.out.println("Elements at " + (i + 1) + " not equal. Exiting.");
                System.exit(0);
            }
        }
        System.out.println("Files are identical!");
    }
}
