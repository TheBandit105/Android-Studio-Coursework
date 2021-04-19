package com.shavin.spaceinvaders;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Borderfloor extends GameObject{

    private Bitmap image;

    public Borderfloor(Bitmap res, int x, int y){
        height = 150;
        width = 20;

        // Coordinates of border floor.
        this.x = x;
        this.y = y;

        // Set move speed of border floor.
        dx = GamePanel.MSPEED;

        //
        image = Bitmap.createBitmap(res, 0, 0, width, height);

    }

    public void update(){

        // We change the border position.
        x += dx;
    }

    public void draw(Canvas canvas){

        // Draw border.
        canvas.drawBitmap(image, x, y, null);
    }
}
