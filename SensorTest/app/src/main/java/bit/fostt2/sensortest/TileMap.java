package bit.fostt2.sensortest;

import android.content.Context;
import android.graphics.Rect;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Daniel on 5/27/2015.
 */
public class TileMap
{
    private TileList tileList;
    private int columns, rows, tileSize;

    private int[][] map;

    public TileMap( TileList tiles, int cols, int rows, int size,InputStream IS) throws IOException
    {
        //mainContext = context;

        //60x34
        this.tileList = tiles;
        this.columns = cols;
        this.rows = rows;
        this.tileSize = size;

        map = new int[cols][rows];

        //start of both arrays
        int c = 0, r = 0;

        InputStreamReader ir = new InputStreamReader(IS);
        BufferedReader in = new BufferedReader(ir);

        String line;

        while ((line = in.readLine()) != null) {
            String[] values = line.split(",");

            for (String str : values)
            {
                int tileInt = Integer.parseInt(str);
                map[c][r] = tileInt;
                ++c;
            }
            ++r;
            c = 0;
        }

        in.close();
    }

    public int wallCollision(Rect ball)
    {
        Rect tileRect;

        int result = 0;

        for (int c = 0; c < columns; c++)
        {
            for (int r = 0; r < rows; r++)
            {//only checks solid tiles
                if(map[c][r] == 1)
                {
                    tileRect = new Rect(c*tileSize, r*tileSize, c*tileSize + tileSize,r * tileSize + tileSize);
                    if (tileRect.intersect(ball))
                    {
                        if ((ball.left < tileRect.right) && (ball.right > tileRect.left))
                        {
                            result = 1;
                        }
                        if ((ball.top < tileRect.bottom) && (ball.bottom > tileRect.top) && (!(ball.left < tileRect.right) && (ball.right > tileRect.left)))
                        {
                            result = 2;
                        }

                        return result;
                    }
                }
            }
        }

        result = 0;

        return result;
    }

    public int[][] getMap() {
        return map;
    }

    public int getColumns()
    {
        return columns;
    }

    public int getRows()
    {
        return rows;
    }

    public int getTileSize() {
        return tileSize;
    }
}
