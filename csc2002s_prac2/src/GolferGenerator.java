import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Thread that generates golfers to arrive at the range with a predetermined 
 * number of buckets to play.
 * 
 * @author Aidan de Nobrega DNBAID001
 * @since 27/08/2015
 */
public class GolferGenerator extends Thread
{
    private int initialGolfers;
    private BallStash sharedStash;
    private Range sharedRange;
    private AtomicBoolean done;

    public GolferGenerator(int init, BallStash stash, Range range, AtomicBoolean doneFlag)
    {
        initialGolfers = init;
        sharedStash = stash;
        sharedRange = range;
        done = doneFlag;
    }

    @Override
    public void run()
    {
        Random random = new Random();

        for (int i = 0; i < initialGolfers; i++)
        {
            int numBuckets = random.nextInt(5);
            if (numBuckets == 0)
            {
                numBuckets++;
            }
            new Golfer(sharedStash, sharedRange, done, numBuckets).start();
        }

        while (!done.get())
        {
            try
            {
                synchronized (this)
                {
                    wait(random.nextInt(6000));
                }
            }
            catch (InterruptedException e)
            {
                System.out.println("Thread Interrupted.");
                System.out.println(e.getMessage());
            }
            int numBuckets = random.nextInt(5);
            if (numBuckets == 0)
            {
                numBuckets++;
            }

            if (!done.get())
            {
                new Golfer(sharedStash, sharedRange, done, numBuckets).start();
            }
        }
    }
}
