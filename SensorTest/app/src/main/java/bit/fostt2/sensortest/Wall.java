package bit.fostt2.sensortest;


import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Wall
{
    public int height;
    public int width;
    public int xPosition;
    public int yPosition;

    public Wall(int height, int width, int xPosition, int yPosition)
    {
        this.height = height;
        this.width = width;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
    }
    public void Draw(Canvas canvas, Paint paint)
    {
        Rect wall = new Rect(xPosition,yPosition,xPosition+width,yPosition+height);
        canvas.drawRect(wall, paint);
    }
}
