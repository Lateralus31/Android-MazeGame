package bit.fostt2.sensortest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;


public class MainActivity extends Activity implements SensorEventListener
{//tablet 775x, 1400y
    static final int FINISHX = 600;
    static final int FINISHY = 1100;

    Ball ball = new Ball();
    Finish finish = new Finish(FINISHX,FINISHY);
    //tablet 750,800
    Finish hole1 = new Finish(600,350);

    private TileList tileList;
    private TileMap tileMap;

    private Bitmap mBall;
    private Bitmap mFinish;
    private Bitmap mHole;
    private Bitmap mBackground;

    private SensorManager sensorManager = null;
    CustomDrawableView mCustomDrawableView = null;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //Set FullScreen & portrait
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //creating tiles and tilemap
        Bitmap bmp;

        tileList = new TileList(2);
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.blank);
        tileList.setArrayEntry(0, bmp, true);
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.block1);
        tileList.setArrayEntry(1, bmp, false);


        // all the layout shit should be dynamic apart from the holes which i have moved
        try {
            AssetManager am = getAssets();
            //using the s3 layout
            //the tablet layout is just called "map.txt" and its in the same folder
            //if you use the tablet layout you will have to adjust the  ball and hole positions and change the tilemap below
            InputStream is = am.open("map(s3).txt");
            //  s3 720 x 1280              23,40,32
            //  tablet 1360 x 1920         19,30,64
            tileMap = new TileMap(tileList,23,40,32,is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Get a reference to a SensorManager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);

        mCustomDrawableView = new CustomDrawableView(this);
        setContentView(mCustomDrawableView);
        // setContentView(R.layout.main);

        //Calculate Boundry
        Display display = getWindowManager().getDefaultDisplay();
        ball.setxPosition(50);
        ball.setyPosition(50);

        Point size = new Point();
        display.getSize(size);
        ball.xmax = size.x - 50; //(float)display.getWidth()
        ball.ymax = size.y - 50; //(float)display.getHeight()

        //Set boundbox for Finish etc
        finish.updateBounds();
        hole1.updateBounds();


    }

    public class CustomDrawableView extends View
    {
        public CustomDrawableView(Context context)
        {
            super(context);
            Bitmap ball = BitmapFactory.decodeResource(getResources(), R.drawable.ball);
            Bitmap finish = BitmapFactory.decodeResource(getResources(), R.drawable.finish);
            Bitmap hole = BitmapFactory.decodeResource(getResources(), R.drawable.hole);
            final int dstWidth = 50;
            final int dstHeight = 50;
            mBall = Bitmap.createScaledBitmap(ball, dstWidth, dstHeight, true);
            mFinish = Bitmap.createScaledBitmap(finish, 100, 100, true);
            mHole = Bitmap.createScaledBitmap(hole,100,100, true);
            mBackground = createMap();
        }

        protected void onDraw(Canvas canvas)
        {
            final Bitmap bitmapBall = mBall;
            final Bitmap bitmapFinish = mFinish;
            final Bitmap bitmapHole = mHole;
            final Bitmap bitmapBackground = mBackground;
            canvas.drawBitmap(bitmapBackground,0,0,null);
            canvas.drawBitmap(bitmapFinish, finish.xPosition, finish.yPosition, null);
            canvas.drawBitmap(bitmapHole, hole1.xPosition, hole1.yPosition, null);
            canvas.drawBitmap(bitmapBall, ball.xPosition, ball.yPosition, null);

            invalidate();
        }
    }

    public Bitmap createMap()
            //the tilemap shit, draws all the tiles in here then stores it as a bitmap
            //the stored bitmap is the one thats called so it doesnt run this everytime.
    {
        int map[][] = tileMap.getMap();
        int columns = tileMap.getColumns();
        int rows = tileMap.getRows();
        int tileSize = tileMap.getTileSize();

        Bitmap tileMapImage =Bitmap.createBitmap((tileSize*columns), (tileSize*rows),
                Bitmap.Config.ARGB_8888);
        Canvas mapCanvas = new Canvas(tileMapImage);

        mapCanvas = new Canvas(tileMapImage);
        Paint tilePaint = new Paint();

        int xPos = 0;
        int yPos = 0;

        for(int c = 0; c < columns; c++)
        {
            for (int r = 0; r < rows; r++)
            {
                int textureType = map[c][r];
                Bitmap texture = tileList.getTileBitmap(textureType);
                mapCanvas.drawBitmap(texture, xPos, yPos, tilePaint);
                yPos += tileSize;
            }
            xPos += tileSize;
            yPos = 0;
        }

        return tileMapImage;
    }


    //when this Activity starts
    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
    }

    //When this Activity isn't visible anymore
    @Override
    protected void onStop()
    {
        //unregister the sensor listener
        sensorManager.unregisterListener(this);
        super.onStop();
    }

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1)
    {
        //Do nothing.
    }

    @Override
    public void onSensorChanged(SensorEvent event)
    {
        //red hole
        if(hole1.checkForBall(ball.boundbox))
        {
            ball.resetBall();
        }
        //yellow finish hole
        if(finish.checkForBall(ball.boundbox))
        {
            ball.setxAcceleration(0);
            ball.setyAcceleration(0);
            //Toast.makeText(getApplicationContext(), "You WIN!!", Toast.LENGTH_SHORT).show();
        }
        else if (event.sensor.getType() == Sensor.TYPE_ORIENTATION)
        {
            ball.yAcceleration = event.values[1];
            ball.xAcceleration = event.values[2];
            ball.updateBall();

            // the bones of my collision detection
            //it checks after the ball has moved and if it is in a wall then it moves it back to where it was before
            //ball stores its last position each time.
            // might be not working properly because of the floats and the collision detection being done in ints and rects but idk
            if (wallCheck())
            {//if new position is ina wall then send ball to last position
                ball.lastPosition();
            }
            ball.updateBounds();
        }
    }

    public boolean wallCheck()
    {
        //passes ball to the tilemap which checks solid to see if the ball is on them
        //pretty rough shit
        Rect player = ball.getRect();

        return tileMap.wallCollision(player);
    }
}