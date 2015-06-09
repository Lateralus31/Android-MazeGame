package bit.fostt2.sensortest;


import android.graphics.Rect;
import android.graphics.drawable.shapes.OvalShape;

public class Ball
{
    public float xPosition, xAcceleration,xVelocity = 0.0f;
    public float yPosition, yAcceleration,yVelocity = 0.0f;
    public float frameTime = 0.1f;
    public float xmax,ymax;

    public float lastX, lastY;

    int xToInt = (int) xPosition;
    int yToInt = (int) yPosition;

    Rect boundbox = new Rect(xToInt,yToInt,xToInt+50,yToInt+50);

    public void updateBall(float x, float y, TileMap tileMap)
    {
        //last proper position
        lastX = xPosition;
        lastY = yPosition;

        xAcceleration = x;
        yAcceleration = y;

        int collide = 0;

        //calculate the new speed
        xVelocity += (xAcceleration * frameTime);
        yVelocity += (yAcceleration * frameTime);

        //calculate the distance travelled
        float xS = (xVelocity/2)*frameTime;
        float yS = (yVelocity/2)*frameTime;

        float newX = xPosition -= xS;
        float newY = yPosition -= yS;
        //allow for negatives

        updateBounds();

        int left = (int) (boundbox.left - xS);
        int right = left + 50;
        int top = (int) (boundbox.top - yS);
        int bottom = top + 50;

        Rect bounds = new Rect(left,top,right,bottom);

        collide = tileMap.wallCollision(bounds);

        if ((newX < xmax) && (newX > 0) && (collide != 1))
        {
            xPosition = newX;
        }
        else
        {
            xPosition = lastX;
            xVelocity *= -.75;
        }
        if ((newY < ymax) && (newY > 0) && (collide != 2))
        {
            yPosition = newY;
        }
        else
        {
            yPosition = lastY;
            yVelocity *= -.75;
        }

        xToInt = (int) xPosition;
        yToInt = (int) yPosition;
    }

    public void resetBall()
    {
        xAcceleration = 0;
        yAcceleration = 0;
        xVelocity = 0;
        yVelocity = 0;
        xPosition = 40;
        yPosition = 40;
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
