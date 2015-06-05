package bit.fostt2.sensortest;


import android.graphics.Rect;
import android.graphics.drawable.shapes.OvalShape;

public class Ball
{
    public float xPosition, xAcceleration,xVelocity = 0.0f;
    public float yPosition, yAcceleration,yVelocity = 0.0f;
    public float frameTime = 0.222f;
    public float xmax,ymax;

    public float lastX, lastY;

    int xToInt = (int) xPosition;
    int yToInt = (int) yPosition;

    Rect boundbox = new Rect(xToInt,yToInt,xToInt+50,yToInt+50);

    public void updateBall()
    {
        //last proper position
        lastX = xPosition;
        lastY = yPosition;

        //check each box

        boolean collide = false;

        if (xPosition > xmax)
        {
            xPosition = xmax;
        }
        else if (xPosition < 0)
        {
            xPosition = 0;
        }
        if (yPosition > ymax)
        {
            yPosition = ymax;
        }
        else if (yPosition < 0)
        {
            yPosition = 0;
        }

        //calculate the new speed
        xVelocity += (xAcceleration * frameTime);
        yVelocity += (yAcceleration * frameTime);

        //calculate the distance travelled
        float xS = (xVelocity/2)*frameTime;
        float yS = (yVelocity/2)*frameTime;

        //allow for negatives
        xPosition -= xS;
        yPosition -= yS;


        xToInt = (int) xPosition;
        yToInt = (int) yPosition;
    }

    public void lastPosition()
    {
        xPosition = lastX;
        yPosition = lastY;
    }

    public void resetBall()
    {
        setxAcceleration(0);
        setyAcceleration(0);
        setxPosition(50);
        setyPosition(50);
    }

    public float getXPosition()
    {
        return this.xPosition;
    }
    public float getYPosition()
    {
        return this.yPosition;
    }
    public float getxVelocity() { return this.xVelocity; }
    public float getyVelocity() { return this.yVelocity; }
    public float getFrameTime() { return this.frameTime; }
    public void setxPosition(float x) { xPosition = x; }
    public void setyPosition(float y) { yPosition = y; }
    public void setxAcceleration(int x)
    {
        xAcceleration = x;
    }
    public void setyAcceleration(int y)
    {
        xAcceleration = y;
    }
    public Rect getRect()
    {
        return boundbox;
    }

    public void updateBounds()
    {
        boundbox.left = xToInt;
        boundbox.top = yToInt;
        boundbox.right = xToInt+50;
        boundbox.bottom = yToInt+50;
    }
}
