package golfGame;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Golfer extends Thread
{

    //remember to ensure thread safety
    private AtomicBoolean done;
    private AtomicBoolean cartOnField;

    private static int noGolfers = 0; //shared amongst threads
    private static int ballsPerBucket; //shared amongst threads

    private int myID;

    private golfBall[] golferBucket;
    private BallStash sharedStash; //link to shared stash
    private Range sharedField; //link to shared field
    private Random swingTime;

    Golfer(BallStash stash, Range field, AtomicBoolean cartFlag, AtomicBoolean doneFlag)
    {
        sharedStash = stash; //shared 
        sharedField = field; //shared
        cartOnField = cartFlag; //shared
        done = doneFlag;
        golferBucket = new golfBall[ballsPerBucket];
        swingTime = new Random();
        myID = newGolfID();
    }

    public static int newGolfID()
    {
        noGolfers++;
        return noGolfers;
    }

    public static void setBallsPerBucket(int noBalls)
    {
        ballsPerBucket = noBalls;
    }

    public static int getBallsPerBucket()
    {
        return ballsPerBucket;
    }

    @Override
    public void run()
    {
        while (!done.get())
        {
            System.out.println(">>> Golfer #" + myID + " trying to fill bucket "
                    + "with " + getBallsPerBucket() + " balls.");
            synchronized (this)
            {
                try
                {

                    golferBucket = sharedStash.getBucketBalls(golferBucket);
                }
                catch (InterruptedException e)
                {
                    Logger.getLogger(Golfer.class.getName()).log(Level.SEVERE, null, e);
                }
                System.out.println("<<< Golfer #" + myID + " filled bucket with "
                        + getBallsPerBucket() + " balls (remaining stash = "
                        + sharedStash.getBallsInStash() + ")");
            }
            for (int b = 0; b < ballsPerBucket; b++)
            {
                //for every ball in bucket
                try
                {
                    sleep(swingTime.nextInt(2000));
                    if (!cartOnField.get())
                    {
                        sharedField.hitBallOntoField(golferBucket[b]);
                        System.out.println("Golfer #" + myID + " hit ball #" + golferBucket[b].getID() + " onto field");
                    }
                }
                catch (InterruptedException e)
                {
                    Logger.getLogger(Golfer.class.getName()).log(Level.SEVERE, null, e);
                } //      swing

                //!!wait for cart if necessary if cart there
            }

        }
    }
}
