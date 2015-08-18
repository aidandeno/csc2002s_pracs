package golfGame;

import java.util.concurrent.atomic.AtomicBoolean;

public class DrivingRangeApp
{

    public static void main(String[] args) throws InterruptedException
    {
        AtomicBoolean done = new AtomicBoolean(false);

        //read these in as command line arguments instead of hard coding
        int noGolfers = 3;
        int sizeStash = 10;
        int sizeBucket = 5;
        BallStash.setSizeStash(sizeStash);
        BallStash.setSizeBucket(sizeBucket);
        Golfer.setBallsPerBucket(sizeBucket);

        //initialize shared variables
        //create threads and set them running
        System.out.println("=======   River Club Driving Range Open  ========");
        System.out.println("======= Golfers:" + noGolfers + " | Balls: " + sizeStash + " | BucketSize:" + sizeBucket + "  ======");

        BallStash ballStash = new BallStash();
        Range range = new Range();
        Bollie bollie = new Bollie(ballStash, range, done);
        for (int i = 0; i < noGolfers; i++)
        {
            new Golfer(ballStash, range, done, done).start();
        }
        
        //for testing, just run for a bit
        Thread.sleep(10000);// this is an arbitrary value - you may want to make it random
        done.set(true);
        System.out.println("=======  River Club Driving Range Closing ========");

    }

}
