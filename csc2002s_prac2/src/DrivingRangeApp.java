import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Class containing Main method. Prints opening and closing statements for the
 * driving range. Randomly determines how long the range is open. and raises
 * the 'done' flag when closed.
 *
 * @author Aidan Gilles de Nobrega
 * @since 22/08/2015
 */
public class DrivingRangeApp
{
    /**
     * Length of time the driving range is open for THIS run
     */
    private static Random runTime = new Random();

    public static void main(String[] args) throws InterruptedException
    {
        //flag raised at closing time (determined by Random runTime variable)
        AtomicBoolean done = new AtomicBoolean(false);
        AtomicBoolean holding = new AtomicBoolean(false);

        /*
         * Takes command-line arguments in the form
         * <number of golfers> <size of the main stash> <size of each golfer's
         * bucket>
         */
        int noGolfers = Integer.parseInt(args[0]);
        int sizeStash = Integer.parseInt(args[1]);
        int sizeBucket = Integer.parseInt(args[2]);

        System.out.println("=======   River Club Driving Range Open  ========");
        System.out.println("======= Golfers:" + noGolfers + " | Balls: " + sizeStash + " | BucketSize:" + sizeBucket + "  ======");

        if (sizeStash <= 0)
        {
            System.out.println("There are no balls. Everyone go home.");
            System.exit(0);
        }
        
        if (sizeBucket <= 0)
        {
            System.out.println("All buckets are broken. Everyone go home.");
        }
        
        BallStash sharedStash = new BallStash(sizeStash, sizeBucket, done, holding);
        Range sharedRange = new Range(sharedStash, done, holding);
        Bollie bollie = new Bollie(sharedStash, sharedRange, done);
        //Golfer threads are started
        for (int i = 0; i < noGolfers; i++)
        {
            new Golfer(sharedStash, sharedRange, done).start();
        }
        //Bollie thread is started
        bollie.start();

        Thread.sleep(runTime.nextInt(60000));
        synchronized (sharedStash)
        {
            done.set(true);
            System.out.println("=======  River Club Driving Range Closing ========");
        }
    }
}
