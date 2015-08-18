package golfGame;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class BallStash
{
    //static variables
    private static int sizeStash;
    private static int sizeBucket;
    public static BlockingQueue<golfBall> stash;

    //ADDED CONSTRUCTOR
    BallStash()
    {
        stash = new ArrayBlockingQueue<>(sizeStash, true);
        for (int i = 0; i < sizeStash; i++)
        {
            stash.add(new golfBall());
        }
    }

    //TODO: getters and setters for static variables - you need to edit these
    public static void setSizeBucket(int noBalls)
    {
        sizeBucket = noBalls;
    }

    public static int getSizeBucket()
    {
        return sizeBucket;
    }

    public static void setSizeStash(int noBalls)
    {
        sizeStash = noBalls;
    }

    public static int getSizeStash()
    {
        return sizeStash;
    }

    //CHECK CONCURRENCY ISSUES
    public synchronized golfBall[] getBucketBalls(golfBall[] golferBucket) throws InterruptedException
    {
        for (int i = 0; i < sizeBucket; i++)
        {
            golferBucket[i] = stash.take();
        }
        return golferBucket;
    }

    public void addBallsToStash()
    {
        //ADD CODE
    }

    public synchronized int getBallsInStash()
    {
        return stash.size();
    }
}
