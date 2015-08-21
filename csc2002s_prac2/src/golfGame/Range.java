package golfGame;

import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class Range
{
    private static int sizeStash;
    private AtomicBoolean cartOnField;

    private BlockingQueue<golfBall> ballsOnField;

    Range()
    {
        ballsOnField = new ArrayBlockingQueue<>(sizeStash, true);
        cartOnField = new AtomicBoolean(false);
    }

    public static void setSizeStash(int size)
    {
        sizeStash = size;
    }

    //CONCURRENCY ISSUES
    public void collectAllBallsFromField(ArrayBlockingQueue<golfBall> ballsCollected) throws InterruptedException
    {
        cartOnField.set(true);
        ballsOnField.drainTo(ballsCollected);
        sleep(3000);
        cartOnField.set(false);
    }

    public void hitBallOntoField(golfBall ball)
    {
        ballsOnField.add(ball);
    }
}
