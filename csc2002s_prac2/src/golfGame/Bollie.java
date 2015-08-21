package golfGame;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class Bollie extends Thread
{
    private AtomicBoolean done;  // flag to indicate when threads should stop

    private final BallStash sharedStash; //link to shared stash
    private final Range sharedField; //link to shared field
    private Random waitTime;

    //link to shared field
    Bollie(BallStash stash, Range field, AtomicBoolean doneFlag)
    {
        sharedStash = stash; //shared 
        sharedField = field; //shared
        waitTime = new Random();
        done = doneFlag;
    }

    @Override
    public void run()
    {
        ArrayBlockingQueue<golfBall> ballsCollected = new ArrayBlockingQueue<>(BallStash.getSizeStash());
        while (!done.get())
        {
            try
            {
                sleep(waitTime.nextInt(6000));
                synchronized(sharedField)
                {
                System.out.println("***********   Bollie collecting balls    ************");
                sharedField.collectAllBallsFromField(ballsCollected);
                System.out.println("*********** Bollie collected " + ballsCollected.size() + " balls from range ***********");
                }
                sleep(2000);
                System.out.println("*********** Bollie adding " + ballsCollected.size() + " balls to stash ************");
                sharedStash.addBallsToStash(ballsCollected);
            }
            catch (InterruptedException e)
            {
                System.err.println("Thread Interrupted");
            }
        }
    }
}
