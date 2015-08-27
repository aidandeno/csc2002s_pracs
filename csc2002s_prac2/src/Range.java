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
     * true -> Bollie is currently collecting balls. Golfers can't swing.
     * false -> Bollie is not on the field. Golfers can swing.
     */
    private static AtomicBoolean cartOnField;

    /**
     * true -> Range is closed.
     * false -> Range is open.
     */
    private static AtomicBoolean done;

    /**
     * true -> Bollie is holding collected balls to be added to the stash.
     * false -> Bollie has added his last collection to the stash.
     */
    private static AtomicBoolean holding;

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
    public Range(BallStash sharedStash, AtomicBoolean doneFlag, AtomicBoolean holdingFlag)
    {
        done = doneFlag;
        holding = holdingFlag;
        ballsOnField = new ArrayBlockingQueue<>(sharedStash.getSizeStash(), true);
        cartOnField = new AtomicBoolean(false);
    }

    //===FUNCTIONS===//
    /**
     * Simulated Bollie collecting all the balls that have been hit by golfers
     * onto the field. Golfers cannot play shots while Bollie is on the field.
     * Locks the field. Golfers are required to acquire the lock to play shots,
     * so while Bollie is collecting, players wait.
     *
     * @param ballsCollected
     * @throws InterruptedException
     */
    public synchronized void collectAllBallsFromField(ArrayBlockingQueue<GolfBall> ballsCollected) throws InterruptedException
    {
        if (!done.get())
        {
            System.out.println("*********** Bollie collecting balls ************");
            cartOnField.set(true);
            ballsOnField.drainTo(ballsCollected);
            holding.set(true);
            sleep(3000);
            cartOnField.set(false);
            System.out.println("*********** Bollie collected " + ballsCollected.size() + " balls from range ***********");
            sleep(1000);
            notifyAll();
        }
    }

    /**
     * Action method that simulates a golfer hitting a ball onto the field.
     * Ball is transferred form golfer's bucket onto the field.
     *
     * @param ball The ball that was hit by the golfer.
     * @param golferID The golfer that hit the ball.
     */
    public synchronized void hitBallOntoField(GolfBall ball, int golferID) throws InterruptedException
    {
        while (cartOnField.get())
        {
            wait();
        }
        System.out.println("Golfer #" + golferID + " hit ball #" + ball.getID() + " onto field");
        ballsOnField.add(ball);
    }
}
