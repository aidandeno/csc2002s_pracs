import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Central supply stash at the driving range. Dispenses balls to golfers and
 * is replenished by Bollie.
 *
 * @author Aidan Gilles de Nobrega DNBAID001
 * @since 22/08/2015
 */
public class BallStash
{
    //===CLASS VARIABLES===//

    /**
     * true -> driving range closed.
     * false -> driving range open.
     */
    private static AtomicBoolean done;

    /**
     * Bollie is holding collected balls to be added to the stash.
     */
    private static AtomicBoolean holding;

    //===INSTANCE VARIABLES===//
    /**
     * The maximum number of balls that can be held in the BlockingQueue<>. Also
     * determines the fixed number of balls being passed around. Value
     * determined by user as command-line argument.
     */
    private int sizeStash;

    /**
     * The fixed size of each golfer's bucket. Value determined by user as
     * command- line argument. Golfers cannot fill a bucket with fewer (or more)
     * balls than this value.
     */
    private int sizeBucket;

    /**
     * Collection of golf balls in main stash. Golfer threads wait until they
     * can fill their buckets.
     */
    private BlockingQueue<GolfBall> stash;

    /**
     * The number of balls in the stash.
     */
    private AtomicInteger ballsInStash;

    //CONSTRUCTORS//
    /**
     * Instantiates the size of the stash and the number of balls to be
     * dispensed at a time (sizeBucket). Passes the 'done' flag from Main. The
     * machine is then filled to capacity with anonymous GolfBall objects.
     *
     * @param sizeStash The size of the ball stash.
     * @param sizeBucket The number of balls to be dispensed at a time.
     * @param doneFlag Shared flag for thread safety.
     */
    public BallStash(int sizeStash, int sizeBucket, AtomicBoolean doneFlag, AtomicBoolean holdingFlag)
    {
        this.sizeStash = sizeStash;
        this.sizeBucket = sizeBucket;
        stash = new ArrayBlockingQueue<>(sizeStash, true);
        ballsInStash = new AtomicInteger(sizeStash);
        done = doneFlag;
        holding = holdingFlag;
        for (int i = 0; i < sizeStash; i++)
        {
            stash.add(new GolfBall());
        }
    }

    //===ACCESSORS===//
    /**
     * Accessor for sizeBucket.
     *
     * @return The bucket size.
     */
    public synchronized int getSizeBucket()
    {
        return sizeBucket;
    }

    /**
     * Accessor for sizeStash.
     *
     * @return The maximum capacity of the stash.
     */
    public synchronized int getSizeStash()
    {
        return sizeStash;
    }

    //===FUNCTIONS===//
    /**
     * Action method allowing Golfer objects to fill up a bucket with balls.
     *
     * Takes sizeBucket number of balls from the stash and puts them in a
     * golfer's bucket. Golfers wait until there are sizeBucket balls available
     * to be dispensed. Only one golfer can fill his bucket at a time.
     *
     * @param golferBucket The golfer's bucket to be filled from stash.
     * @param golfer The golfer trying to fill his bucket.
     * @return The golfer's bucket filled with balls.
     * @throws InterruptedException
     */
    public synchronized GolfBall[] fillBucket(GolfBall[] golferBucket, Golfer golfer) throws InterruptedException
    {
        //Golfers can only fill their buckets if the range is open.
        if (!done.get())
        {
            /*
             * Golfers wait until there are sizeBucket balls available to be
             * dispensed. When Bollie replenishes the stash, notifyAll() is 
             * called. If Bollie has gone home, waiting golfers check every
             * 3000 milliseconds to make sure the range hasn't closed.
             *
             */
            while (ballsInStash.get() < sizeBucket)
            {
                wait(3000);
                if (done.get())
                {
                    golfer.goHome();
                    return golferBucket;
                }
            }

            //Bucket is filled
            for (int i = 0; i < sizeBucket; i++)
            {
                golferBucket[i] = stash.take();
            }
            //sizeStash is reset for consistency
            ballsInStash.set(stash.size());

            System.out.println("<<< Golfer #" + golfer.getID() + " filled bucket with "
                    + sizeBucket + " balls (remaining stash = "
                    + ballsInStash.get() + ")");
            return golferBucket;
        }
        else //golfer thread will terminate
        {
            golfer.goHome();
            return golferBucket;
        }
    }

    /**
     * Action method allowing Bollie to add collected balls to the stash.
     *
     * Can be done when range is closed provided Bollie started collecting balls
     * before closing time. Only executes if Bollie has actually collected
     * some balls.
     *
     * @param ballsCollected Balls collected from the field by
     * Bollie.
     */
    public synchronized void replenish(ArrayBlockingQueue<GolfBall> ballsCollected)
    {
        if (holding.get())
        {
            System.out.println("*********** Bollie adding " + ballsCollected.size() + " balls to stash ************");
            ballsCollected.drainTo(stash);
            ballsInStash.set(stash.size());
            notifyAll();
        }
        holding.set(false);
    }
}
