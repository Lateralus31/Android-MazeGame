package bit.fostt2.sensortest;

import android.graphics.Rect;

public class Finish
{
    public int xPosition;
    public int yPosition;
    Rect boundbox = new Rect(xPosition,yPosition,xPosition+100,xPosition+100);

    public Finish(int x, int y)
    {
        xPosition = x;
        yPosition = y;
    }

    public boolean checkForBall(Rect ball)
    {
        boolean finished = false;

        if(boundbox.contains(ball))
        {
            finished = true;
        }
        return finished;
    }

    public void updateBounds()
    {
        boundbox.left = xPosition;
        boundbox.top = yPosition;
        boundbox.right = xPosition+100;
        boundbox.bottom = yPosition+100;
    }

    public Rect getRect()
    {
        return boundbox;
    }
}
