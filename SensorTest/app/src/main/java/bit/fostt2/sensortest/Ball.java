package bit.fostt2.sensortest;


import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.shapes.OvalShape;

public class Ball
{
    public float xPosition, xAcceleration,xVelocity = 0.0f;
    public float yPosition, yAcceleration,yVelocity = 0.0f;
    public float frameTime = 0.15f;
    public float xmax,ymax;

    public float lastX, lastY;

    int xToInt = (int) xPosition;
    int yToInt = (int) yPosition;

    Rect boundbox = new Rect(xToInt,yToInt,xToInt+30,yToInt+30);

    public void updateBall(float x, float y, TileMap tileMap)
    {
        //last proper position
        lastX = xPosition;
        lastY = yPosition;

        xAcceleration = x;
        yAcceleration = y;

        boolean collide;

        //calculate the new speed

        xVelocity += (xAcceleration * frameTime);
        yVelocity += (yAcceleration * frameTime);

        //calculate the distance travelled
        float xS = (xVelocity/2)*frameTime;
        float yS = (yVelocity/2)*frameTime;

        float newX = xPosition -= xS;
        float newY = yPosition -= yS;
        //allow for negatives

        int left = (int) (newY);
        int right = left + 30;
        int top = (int) (newX);
        int bottom = top + 30;

        Rect bounds = new Rect(left,top,right,bottom);

        collide = tileMapCollision(tileMap, bounds);

        if ((newX < xmax) && (newX > 0) && !collide)
        {
            xPosition = newX;
        }
        else
        {
            xPosition = lastX;
        }
        if ((newY < ymax) && (newY > 0) && !collide)
        {
            yPosition = newY;
        }
        else
        {
            yPosition = lastY;
        }

        xToInt = (int) xPosition;
        yToInt = (int) yPosition;

        updateBounds();
    }

    public boolean tileMapCollision(TileMap tileMap, Rect bounds)
    {
        Rect tileRect;

        int map[][] = tileMap.getMap();
        int tileSize = tileMap.getTileSize();
        int cols = tileMap.getColumns();
        int rows = tileMap.getRows();

        for (int c = 0; c < cols; c++)
        {
            for (int r = 0; r < rows; r++)
            {//only checks solid tiles
                if(map[c][r] == 1)
                {
                    int tileLeft = r*tileSize;
                    int tileTop = c*tileSize;
                    int tileRight = r*tileSize+tileSize;
                    int tileBottom = c*tileSize+tileSize;

                    tileRect = new Rect(tileLeft, tileTop,tileRight,tileBottom);

                    if (tileRect.intersect(bounds))
                    {
                        Rect Top = new Rect(tileRect.left, tileRect.top, tileRect.right, tileRect.top+16);
                        Rect Bottom = new Rect(tileRect.left, tileRect.top+16, tileRect.right, tileRect.bottom);
                        Rect Left = new Rect(tileRect.left, tileRect.top, tileRect.right-16, tileRect.bottom);
                        Rect Right = new Rect(tileRect.left+16, tileRect.top, tileRect.right, tileRect.bottom);

                        if ((Bottom.intersect(bounds)) || (Top.intersect(bounds)))
                        {
                             yVelocity *= -0.75;
                        }
                        else if ((Left.intersect(bounds)) || (Right.intersect(bounds)))
                        {
                             xVelocity *= -0.75;
                        }

                        return true;
                    }
                }
            }
        }

        return false;
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
        yAcceleration = y;
    }
    public Rect getRect()
    {
        return boundbox;
    }

    public void updateBounds()
    {
        boundbox.left = xToInt;
        boundbox.top = yToInt;
        boundbox.right = xToInt+30;
        boundbox.bottom = yToInt+30;
    }

    public void methods()
    {/*
        Point Top = new Point(bounds.left+15,bounds.top);
        Point Left = new Point(bounds.left,bounds.top+15);
        Point Bottom = new Point(bounds.left + 15,bounds.bottom);
        Point Right = new Point(bounds.right,bounds.top+15);

        if ((tileRect.contains(Top.x,Top.y)) || tileRect.contains(Bottom.x,Bottom.y))
        {
            yVelocity *= -0.75;
        }
        if ((tileRect.contains(Left.x,Left.y)) || (tileRect.contains(Right.x,Right.y)))
        {
            xVelocity *= -0.75;
        }

        /////////////////
        Rect Top = new Rect(tileRect.left, tileRect.top, tileRect.right, tileRect.top);
        Rect Bottom = new Rect(tileRect.left, tileRect.bottom, tileRect.right, tileRect.bottom);
        Rect Left = new Rect(tileRect.left, tileRect.top, tileRect.left, tileRect.bottom);
        Rect Right = new Rect(tileRect.right, tileRect.top, tileRect.right, tileRect.bottom);
        //For vertical collision (top or bottom of brick) invert Y motion
        if ((bounds.intersect(Top)) || (bounds.intersect(Bottom)))
        {
            yVelocity *= -0.75;
        }
        //For horizontal collision (left or right side of brick) invert X motion
        else if ((bounds.intersect(Left)) || (bounds.intersect(Right)))
        {
            xVelocity *= -0.75;
        }
        */
    }
}
