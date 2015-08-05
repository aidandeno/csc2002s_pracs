
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.RecursiveAction;

/**
 *  Filters subarrays according to appropriate implementation and filtering type
 * 
 * @author Aidan
 */
public class FilterObject extends RecursiveAction
{
    //******FIELDS******//

    /**
     * The threshold at which parallel division is no longer necessary
     */
    private static final int SEQUENTIAL_THRESHOLD = 100;

    /**
     * The lower index of the subarray to be filtered
     */
    private final int lo;

    /**
     * The upper index of the subarray to be filtered
     */
    private final int hi;

    /**
     * true -> median filtering false -> mean filtering
     */
    private final boolean filterType;

    //******METHODS******//
    
    public FilterObject(int lo, int hi, boolean filterType)
    {
        this.lo = lo;
        this.hi = hi;
        this.filterType = filterType;
    }

    @Override
    /**
     * Divide-and-Conquer implementation to filter array
     *
     * If subarray is small enough to be within the sequential threshold,
     * sequential filtering is applied to the subarray. Otherwise, subarray is
     * divided recursively into 2 half-size subarrays
     */
    public void compute()
    {
        if (hi - lo <= SEQUENTIAL_THRESHOLD)
        {
            seqFilter();
        }
        else
        {   /*join() unnecessary as program applies filter directly to values
             of Main.startArray*/

            FilterObject left = new FilterObject(lo, (hi + lo) / 2, filterType);
            FilterObject right = new FilterObject((hi + lo) / 2, hi, filterType);
            left.fork();
            right.compute();
            left.join();
        }
    }

    /**
     * Sequential filtering of a subarray
     * 
     * Applies either median filtering or mean filtering according to user input
     */
    public void seqFilter()
    {
        for (int i = lo; i < hi; i++)
        {
            ArrayList<Double> window = new ArrayList<>();

            //Iterates through window
            for (int k = i - (Main.filterSize / 2); k <= i + (Main.filterSize / 2); k++)
            {
                window.add(Main.startArray.get(k));
            }

            //Array is sorted and the appropriate value is picked out.
            if (filterType)
            {
                Main.finalArray.set(i, getMedian(window));
            }
            else
            {
                Main.finalArray.set(i, getMean(window));
            }
        }
    }

    /**
     * Gets median value from an array
     * @param array Array to be evaluated
     * @return The median of the array
     */
    private static Double getMedian(ArrayList<Double> array)
    {
        Collections.sort(array);
        return array.get(Main.filterSize / 2);
    }
    
    /**
     * Gets mean value from an array
     * @param array Array to be evaluated
     * @return The mean of the array
     */
    private static Double getMean(ArrayList<Double> array)
    {
        double sum = 0;
        for (Double e : array)
        {
            sum += e;
        }
        return sum / Main.filterSize;
    }
}
