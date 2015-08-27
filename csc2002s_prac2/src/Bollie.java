import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Driving range employee who periodically collects balls from the field and
 * replenishes the central supply stash. Golfers cannot play shots while
 * Bollie is collecting balls.
 *
 * @author Aidan Gilles de Nobrega DNBAID001
 * @since 22/08/2015
 */
public class Bollie extends Thread
{
    //===CLASS VARIABLES===//
    /**
     * Flag to indicate when threads should stop.
     *
     * true -> driving range closed.
     * false -> driving range open.
     */
    private static AtomicBoolean done;

    /**
     * Link to central supply stash
     *
     * @see BallStash
     */
    private static BallStash sharedStash;

    /**
     * Link to driving range
     *
     * @see Range
     */
    private static Range sharedField;

    //===INSTANCE VARIABLES===//
    /**
     * Random time between collections. Reset after each collection.
     */
    private Random waitTime;

    //===CONSTRUCTORS===//
    /**
     * Instantiates the shared fields. Creates initial waiting time for first
     * collection.
     *
     * @param stash Central supply stash.
     * @param field Driving range.
     * @param doneFlag Shared flag for thread safety.
     */
    public Bollie(BallStash stash, Range field, AtomicBoolean doneFlag)
    {
        sharedStash = stash;
        sharedField = field;
        waitTime = new Random();
        done = doneFlag;
    }
    
    /**
     * Starts Bollie's thread.
     *
     * While the range is open, Bollie collects balls from the field and adds
     * them to the central supply stash. The thread sleeps between actions to
     * mimic execution time. Golfers cannot play shots while Bollie is collect-
     * ing, but they can while Bollie is replenishing the stash.
     */
    @Override
    public void run()
    {
        //Collection of balls picked up by Bollie.
        ArrayBlockingQueue<GolfBall> ballsCollected = new ArrayBlockingQueue<>(sharedStash.getSizeStash());
        while (!done.get())
        {
            try
            {
                sleep(waitTime.nextInt(6000));
                /*
                 * While Bollie is collecting balls, he acquires the lock
                 * for the range.
                 */
                synchronized (sharedField)
                {
                    sharedField.collectAllBallsFromField(ballsCollected);
                }
                sleep(2000);
                sharedStash.addBallsToStash(ballsCollected);
            }
            catch (InterruptedException e)
            {
                System.err.println("Thread Interrupted");
            }
        }
        System.out.println("Bollie is going home");
    }
}
