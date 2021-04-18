package com.shavin.spaceinvaders;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Background {

    /**
     * Bitmap class depicts a bitmap image. This class will be used along
     * with the Canvas class.
     */
    private Bitmap image;
    private int x, y, dx;

    public Background(Bitmap res){
        image = res;
        dx = GamePanel.MSPEED;
    }

    public void update(){

        /**
         * Changes the x coordinate of the background.
         */
        x+=dx;

        /**
         * Checks to see if the background starts to move off screen.
         * GamePanel.WIDTH represents the width of the device screen.
         */
        if(x < -GamePanel.WIDTH){
            x = 0;
        }
    }

    public void draw(Canvas canvas){

        /**
         * Draws first background onto the screen.
         */
        canvas.drawBitmap(image, x, y, null);

        /**
         * If background is x limits off screen, then behind first
         * background screen the same background image is placed
         * again.
         */
        if(x < 0){
            canvas.drawBitmap(image, x+GamePanel.WIDTH, y, null);
        }

    }

}
