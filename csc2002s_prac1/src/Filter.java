
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Aidan
 */
public class Filter
{
    public static Double[] applyFilter(Double[] startArray)
    {
        Double[] endArray = new Double[Main.inputSize];

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
                endArray[i] = startArray[i];
            }
            else
            {
                ArrayList<Double> tempArray = new ArrayList<>(Main.filterSize);

                //Iterates through window
                for (int k = i - (Main.filterSize / 2); k <= i + (Main.filterSize / 2); k++)
                {
                    tempArray.add(startArray[k]);
                }

                //Array is sorted and the middle value is picked out.
                Collections.sort(tempArray);
                endArray[i] = tempArray.get(Main.filterSize / 2);
            }
        }
        return endArray;
    }

    public static Double getMedian(ArrayList<Double> array)
    {
        Collections.sort(array);
        return array.get(Main.filterSize / 2);
    }
}
