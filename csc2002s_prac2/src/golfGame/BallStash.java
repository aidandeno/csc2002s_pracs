package golfGame;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class BallStash
{
    //static variables
    private static int sizeStash;
    private static int sizeBucket;
    static BlockingQueue<golfBall> stash;
    private AtomicInteger ballsInStash;

    BallStash()
    {
        stash = new ArrayBlockingQueue<>(sizeStash, true);
        ballsInStash = new AtomicInteger(sizeStash);
        for (int i = 0; i < sizeStash; i++)
        {
            stash.add(new golfBall());
        }
    }

    static void setSizeBucket(int noBalls)
    {
        sizeBucket = noBalls;
    }

    static int getSizeBucket()
    {
        return sizeBucket;
    }

    static void setSizeStash(int noBalls)
    {
        sizeStash = noBalls;
    }

    static int getSizeStash()
    {
        return sizeStash;
    }

    golfBall[] getBucketBalls(golfBall[] golferBucket) throws InterruptedException
    {
        synchronized (this)
        {
            for (int i = 0; i < sizeBucket; i++)
            {
                golferBucket[i] = stash.take();
            }
            ballsInStash.set(stash.size());
            return golferBucket;
        }
    }

    void addBallsToStash(ArrayBlockingQueue<golfBall> ballsCollected)
    {
        synchronized (this)
        {
            ballsCollected.drainTo(stash);
            ballsInStash.set(stash.size());
        }

    }

    int getBallsInStash()
    {
        synchronized (this)
        {
            return ballsInStash.get();
        }
    }
}
