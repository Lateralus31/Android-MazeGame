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
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;


public class MainActivity extends Activity implements SensorEventListener
{
    Ball ball = new Ball();
    Finish finish;
    Finish hole1;

    private TileList tileList;
    private TileMap tileMap;

    private Bitmap mBall;
    private Bitmap mFinish;
    private Bitmap mHole;
    private Bitmap mBackground;

    //TIMER
    private Handler handler = new Handler();
    private String levelTime;
    //Create an instance of sensorManager
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
        //Stop screen from dimming
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        finish = new Finish(600,1100);
        hole1 = new Finish(600,350);

        //creating tiles and tilemap
        Bitmap bmp;
        tileList = new TileList(2);
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.blank);
        tileList.setArrayEntry(0, bmp, true);
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.block1);
        tileList.setArrayEntry(1, bmp, false);

        try {
            AssetManager am = getAssets();
            //using the s3 layout
            InputStream is = am.open("map(s3).txt");
            //  s3 720 x 1280     23,40,32
            tileMap = new TileMap(23,40,32,is);
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
        ball.setxPosition(40);
        ball.setyPosition(40);

        Point size = new Point();
        display.getSize(size);
        ball.xmax = size.x - 30; //(float)display.getWidth()
        ball.ymax = size.y - 30; //(float)display.getHeight()\

        //Set boundbox for Finish etc
        finish.updateBounds();
        hole1.updateBounds();

        //Timer
        handler.postDelayed(runnable, 100);
    }

    //Timer
    //Using handler as timer as it uses less memory
    private Runnable runnable = new Runnable()
    {
        int currentTime = 0;
        @Override
        public void run()
        {
            currentTime++;
            levelTime = String.valueOf(currentTime);
            handler.postDelayed(this, 1000);
        }
    };

    //Custom view for drawing game to
    public class CustomDrawableView extends View
    {
        public CustomDrawableView(Context context)
        {
            super(context);
            Bitmap ball = BitmapFactory.decodeResource(getResources(), R.drawable.ball);
            Bitmap finish = BitmapFactory.decodeResource(getResources(), R.drawable.finish);
            Bitmap hole = BitmapFactory.decodeResource(getResources(), R.drawable.hole);
            final int dstWidth = 30;
            final int dstHeight = 30;
            mBall = Bitmap.createScaledBitmap(ball, dstWidth, dstHeight, true);
            mFinish = Bitmap.createScaledBitmap(finish, 100, 100, true);
            mHole = Bitmap.createScaledBitmap(hole, 100, 100, true);
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
            //Draw timer
            Paint paint = new Paint();
            paint.setColor(Color.WHITE);
            paint.setTextSize(36);
            canvas.drawText(levelTime, 10, 35, paint);
            invalidate();
        }
    }

    public Bitmap createMap()
            //the tilemap, draws all the tiles in here then stores it as a bitmap
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
            xPos = c * tileSize;
            for (int r = 0; r < rows; r++)
            {
                yPos = r * tileSize;
                int textureType = map[c][r];
                Bitmap texture = tileList.getTileBitmap(textureType);
                mapCanvas.drawBitmap(texture, xPos, yPos, tilePaint);
            }
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
        //hole
        if(hole1.checkForBall(ball.boundbox))
        {
            ball.resetBall();
        }
        //finish line
        if(finish.checkForBall(ball.boundbox))
        {
            ball.setxAcceleration(0);
            ball.setyAcceleration(0);
            ball.setxPosition(100000);
            ball.setyPosition(100000);
            Toast.makeText(getApplicationContext(), "YOU WIN!!!!", Toast.LENGTH_LONG).show();
            sensorManager.unregisterListener(this);
            super.onStop();
            handler.removeCallbacks(runnable);
        }
        else if (event.sensor.getType() == Sensor.TYPE_ORIENTATION)
        {
            float yAcceleration = event.values[1];
            float xAcceleration = event.values[2];
            ball.updateBall(xAcceleration,yAcceleration, tileMap);
        }
    }
}