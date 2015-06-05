package bit.fostt2.sensortest;

import android.graphics.Bitmap;

import static android.graphics.BitmapFactory.decodeFile;

/**
 * Created by Daniel on 5/27/2015.
 */
public class Tile
{
    private Bitmap tileImage;
    private boolean solid;

    public Tile(Bitmap bmp, boolean type)
    {
        //tileImage = decodeFile(imageName);
        tileImage = bmp;
        solid = type;
    }

    //gets
    public Bitmap getTileImage() {return tileImage;}
    public boolean getSolid() {return solid;}

    //sets
    public void setTileImage(String imageName) {tileImage = decodeFile(imageName);}
    public void setSolid(boolean newType) {solid = newType;}
}
