package bit.fostt2.sensortest;

import android.graphics.Bitmap;

/**
 * Created by Daniel on 5/27/2015.
 */
public class TileList
{
    private Tile[] tileArray;

    public TileList(int size)
    {
        tileArray = new Tile[size];
    }

    public void setArrayEntry(int index, Bitmap bmp, boolean type)
    {
        Tile newTile = new Tile(bmp,type);
        tileArray[index] = newTile;
    }

    public Bitmap getTileBitmap(int index) {return tileArray[index].getTileImage();}
    public boolean getSolid(int index) {return tileArray[index].getSolid();}
}
