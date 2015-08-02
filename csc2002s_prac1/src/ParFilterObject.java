
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

/**
 *
 * @author Aidan
 */
public class ParFilterObject extends RecursiveAction
{
    final static int SEQUENTIAL_THRESHOLD = 500;

    public static Double endArray[];
    public Double[] array;
    public int lo;
    public int hi;

    public ParFilterObject(Double[] array, int lo, int hi)
    {
        this.array=array;
        this.lo=lo;
        this.hi=hi;
    }
    
    @Override
    public void compute()
    {
        if(hi-lo<=SEQUENTIAL_THRESHOLD)
        {
            Filter.applyFilter(array);
        }
        else
        {
            ParFilterObject left = new ParFilterObject(array, lo, (hi+lo)/2);
            ParFilterObject right = new ParFilterObject(array,(hi+lo)/2, hi);
            left.fork();
            right.compute();
            left.join();
            
        }
    }
}
