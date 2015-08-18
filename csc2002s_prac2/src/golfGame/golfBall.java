package golfGame;

public class golfBall
{
    //add mechanisms for thread safety
    private static int noBalls;
    private int myID;

    golfBall()
    {
        myID = noBalls;
        incID();
    }

    public int getID()
    {
        return myID;
    }

    private static void incID()
    {
        noBalls++;
    }

}
