
import java.util.ArrayList;

/**
 *
 * @author Aidan
 */
public class SeqFilterObject
{
    public static ArrayList<Double> endArray = new ArrayList<>();

    public static void run(ArrayList<Double> startArray)
    {
        double timesSum = 0;

        for (int run = 1; run < 11; run++)
        {
            long startTime = System.nanoTime();

            /*Iterates through all the values within the borders, creates an
             array of the window, and then applies median filter to that window.
             The appropriate value is added to the final array.*/
            for (int i = 0; i < Main.inputSize; i++)
            {

                /*If the indices fall outside any filtering window, the values at
                 those indices are added to the final array unchanged. Otherwise,
                 the median is found and swapped for the old value.*/
                if (i < Main.filterSize / 2 || i >= Main.inputSize - (Main.filterSize / 2))
                {
                    endArray.add(startArray.get(i));
                }
                else
                {
                    ArrayList<Double> window = new ArrayList<>();

                    //Iterates through window
                    for (int k = i - (Main.filterSize / 2); k <= i + (Main.filterSize / 2); k++)
                    {
                        window.add(startArray.get(k));
                    }

                    //Array is sorted and the middle value is picked out.
                    endArray.add(Filter.getMedian(window));
                }
            }
            long duration = System.nanoTime() - startTime;
            double secondsElapsed = (double) duration / 1000000000.0;
            System.out.println("Elapsed time for sequential algorithm run "
                    + run + ": " + secondsElapsed);
            timesSum += secondsElapsed;
        }
        double averageTime = (double) timesSum / 10;

        System.out.println("Average elapsed time over 10 runs: " + averageTime);
    }
}
