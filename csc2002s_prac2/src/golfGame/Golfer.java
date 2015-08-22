package golfGame;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Golfers who acquire a bucket of balls (size determined by user) and proceed
 * to shoot them onto the driving range. Golfers cannot shoot when Bollie is on
 * the field collecting balls. Golfers wait in an unordered line to fill their
 * buckets, but multiple golfers cannot fill their buckets at the same time.
 *
 * @author Aidan Gilles de Nobrega DNBAID001
 * @since 22/08/2015
 */
public class Golfer extends Thread
{
    //===CLASS VARIABLES===//
    /**
     * The number of golfers on the driving range. This value is used to deter-
     * mine each golfer's ID.
     */
    private static int noGolfers = 0;

    /**
     * true -> driving range closed.
     * false -> driving range open.
     */
    private static AtomicBoolean done;

    /**
     * Central supply stash.
     *
     * @see BallStash
     */
    private static BallStash sharedStash;

    /**
     * Driving range
     *
     * @see Range
     */
    private static Range sharedField;

    //===INSTANCE VARIABLES===//
    /**
     * Identification number for this golfer. Used in print statements.
     */
    private int myID;

    /**
     * This golfer's bucket. Gets filled at central supply stash.
     */
    private GolfBall[] golferBucket;

    /**
     * This golfer's randomly determined pause between swings.
     */
    private Random swingTime;

    //===CONTRUCTORS===//
    /**
     * Instantiates the shared fields and the golfer's bucket. Randomly deter-
     * mines this golfer's initial swing time. Assigns golfer an ID based on
     * how many golfers have been created.
     *
     * @param stash Central supply stash.
     * @param field Driving range.
     * @param doneFlag Shared flag for thread safety.
     */
    public Golfer(BallStash stash, Range field, AtomicBoolean doneFlag)
    {
        sharedStash = stash;
        sharedField = field;
        done = doneFlag;
        golferBucket = new GolfBall[sharedStash.getSizeBucket()];
        swingTime = new Random();
        myID = ++noGolfers;
    }

    //===ACCESSORS===//
    /**
     * Accessor for myID
     *
     * @return This golfer's identification number
     */
    public int getID()
    {
        return myID;
    }

    /**
     * Starts thread for this golfer.
     *
     * While the range is open, this golfer acquires a bucket of balls and
     * shoots them onto the range. A golfer cannot acquire more balls after the
     * range has closed. A golfer cannot shoot while Bollie is on the field
     * collecting balls. Golfers can swing simultaneously and wait in line to
     * acquire balls, but no two golfers can fill their buckets at the same
     * time.
     */
    @Override
    public void run()
    {
        while (!done.get())
        {
            System.out.println(">>> Golfer #" + myID + " trying to fill bucket "
                    + "with " + sharedStash.getSizeBucket() + " balls.");
            try
            {
                //This golfer acquires a bucket of balls
                golferBucket = sharedStash.getBucketBalls(golferBucket, this);
            }
            catch (InterruptedException e)
            {
                Logger.getLogger(Golfer.class.getName()).log(Level.SEVERE, null, e);
            }

            /*
             * If a golfer has acquired a buckett of balls, he may finish the 
             * entire bucket even if the range is closed.
             */
            for (int b = 0; b < sharedStash.getSizeBucket(); b++)
            {
                try
                {
                    sleep(swingTime.nextInt(2000));
                    //This golfer shoots a ball from his bucket onto the range
                    sharedField.hitBallOntoField(golferBucket[b], myID);
                }
                catch (InterruptedException e)
                {
                    Logger.getLogger(Golfer.class.getName()).log(Level.SEVERE, null, e);
                    e.getMessage();
                }
            }
        }
    }
}
