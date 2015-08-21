package golfGame;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class DrivingRangeApp
{
    private static Random runTime = new Random();
    
    public static void main(String[] args) throws InterruptedException
    {
        AtomicBoolean done = new AtomicBoolean(false);
        AtomicBoolean cartOnField = new AtomicBoolean(false);

        //read these in as command line arguments instead of hard coding
        int noGolfers = 3;
        int sizeStash = 20;
        int sizeBucket = 5;
        BallStash.setSizeStash(sizeStash);
        BallStash.setSizeBucket(sizeBucket);
        Range.setSizeStash(sizeStash);
        Golfer.setBallsPerBucket(sizeBucket);

        //initialize shared variables
        //create threads and set them running
        System.out.println("=======   River Club Driving Range Open  ========");
        System.out.println("======= Golfers:" + noGolfers + " | Balls: " + sizeStash + " | BucketSize:" + sizeBucket + "  ======");

        BallStash ballStash = new BallStash();
        Range range = new Range();
        Bollie bollie = new Bollie(ballStash, range, done);
        bollie.start();
        for (int i = 0; i < noGolfers; i++)
        {
            new Golfer(ballStash, range, cartOnField, done).start();
        }

        Thread.sleep(runTime.nextInt(60000));
        done.set(true);
        System.out.println("=======  River Club Driving Range Closing ========");
    }
}
