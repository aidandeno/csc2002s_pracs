import static java.lang.Thread.sleep;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Driving range
 *
 * @author Aidan
 */
public class Range
{
    //===CLASS VARIABLES===//
    /**
     *
     */
    private static AtomicBoolean cartOnField;
    
    private static AtomicBoolean done;

    //===INSTANCE VARIABLES===//
    /**
     *
     */
    private BlockingQueue<GolfBall> ballsOnField;

    //===CONSTRUCTORS===//
    /**
     * Instantiates the BlockingQueue that contains the balls that have been
     * hit onto the field and have not yet been collected by Boole. Also sets
     * cartOnField flag to false intially.
     *
     * @param sharedStash Central supply stash.
     */
    public Range(BallStash sharedStash, AtomicBoolean doneFlag)
    {
        done = doneFlag;
        ballsOnField = new ArrayBlockingQueue<>(sharedStash.getSizeStash(), true);
        cartOnField = new AtomicBoolean(false);
    }

    //===FUNCTIONS===//
    /**
     * Simulated Bollie collecting all the balls that have been hit by golfers
     * onto the field. Golfers cannot play shots while Bollie is on the field.
     *
     * @param ballsCollected
     * @throws InterruptedException
     */
    public void collectAllBallsFromField(ArrayBlockingQueue<GolfBall> ballsCollected) throws InterruptedException
    {
        /*
         * Golfers must acquire this lock to play shots and the lock is only
         * released when Bollie leaves the field.
         */
        synchronized (this)
        {
            if (!done.get())
            {
                System.out.println("*********** Bollie collecting balls ************");
                cartOnField.set(true);
                ballsOnField.drainTo(ballsCollected);
                sleep(3000);
                cartOnField.set(false);
                System.out.println("*********** Bollie collected " + ballsCollected.size() + " balls from range ***********");
                sleep(1000);
            }
        }
    }

    /**
     * Action method that simulates a golfer hitting a ball onto the field.
     * Ball is transferred form golfer's bucket onto the field.
     *
     * @param ball The ball that was hit by the golfer.
     * @param golferID The golfer that hit the ball.
     */
    public void hitBallOntoField(GolfBall ball, int golferID)
    {
        /**
         * Golfers must acquire this lock to play shots therefore they cannot
         * play shots while Bollie is collecting (signified by the cartOnField
         * flag).
         */
        synchronized (this)
        {
            while (cartOnField.get())
            {
            }
            System.out.println("Golfer #" + golferID + " hit ball #" + ball.getID() + " onto field");
            ballsOnField.add(ball);
        }
    }
}
