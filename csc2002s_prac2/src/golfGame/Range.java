package golfGame;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class Range
{
    private static final int sizeStash = 20;
    private AtomicBoolean cartOnField;

    public BlockingQueue<golfBall> ballsOnField;

    Range()
    {
        ballsOnField = new ArrayBlockingQueue<>(sizeStash);
    }

    //CONCURRENCY ISSUES
    public void collectAllBallsFromField(golfBall[] ballsCollected)
    {
        //ADD CODE
        cartOnField.set(true);
        
        cartOnField.set(false);
    }

    public void hitBallOntoField(golfBall ball)
    {
        ballsOnField.add(ball);
    }
}
