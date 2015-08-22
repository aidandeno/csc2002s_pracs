package golfGame;

/**
 * Golf balls are used to fill the central supply stash, are played by golfers,
 * and then collected by Bollie who replenishes the stash. Golf balls are not
 * created or destroyed, but rather passed from one object to another.
 *
 * @author Aidan Gilles de Nobrega DNBAID001
 * @since 22/08/2015
 */
public class GolfBall
{
    //===CLASS VARIABLES===//
    /**
     * The number of golf balls that have been created. This value is used to 
     * assign IDs to new balls.
     */
    private static int noBalls;

    //===INSTANCE VARIABLES===//
    /**
     * The identification number for this ball.
     */
    private int myID;

    //===CONSTRUCTORS===//
    /**
     * Instantiates the identification number for this ball based on how many
     * balls have been created.
     */
    public GolfBall()
    {
        myID = ++noBalls;
    }

    //===ACCESSORS===//
    /**
     * Accessor for myID.
     * 
     * @return The identification number for this ball.
     */
    public int getID()
    {
        return myID;
    }
}
