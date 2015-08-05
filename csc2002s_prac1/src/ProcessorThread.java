
/**
 * Filters subarrays according to filtering type using standard threads
 *
 * @author Aidan
 */
public class ProcessorThread extends Thread
{
    //******FIELDS******//

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
    public ProcessorThread(int lo, int hi, boolean filterType)
    {
        this.lo = lo;
        this.hi = hi;
        this.filterType = filterType;
    }

    @Override
    /**
     * Sequentially filters a subarray according to filtering type using
     * standard threads
     */
    public void run()
    {
        FilterObject filt = new FilterObject(lo, hi, filterType);
        filt.seqFilter();
    }
}
