package com.shavin.spaceinvaders;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Laser extends GameObject{

    // Speed of laser.
    private int speed;

    // Animation of laser image.
    private Animation animation = new Animation();

    // Bitmap reference of laser image.
    private Bitmap spritesheet;

    public Laser(Bitmap res, int x, int y, int w, int h, int frameNo){
        super.x = x;
        super.y = y;
        width = w;
        height = h;
        speed = 13;

        // All frames of laser image.
        Bitmap[] image = new Bitmap[frameNo];
        spritesheet = res;

        /**
         * Stores all frames of laser image to new table.
         */
        for(int i = 0; i < image.length; i++){
            image[i] = Bitmap.createBitmap(spritesheet, 0, i*height, width, height);
        }

        // Set animation.
        animation.setFrames(image);

        // Set delay of animation between frames.
        animation.setDelay(120-speed);
    }

    public void update(){

        // Every sec, speed of bullet changes.
        x+=speed-4;

        animation.update();

    }

    public void draw(Canvas canvas){

        // Laser drawn on the screen.
        try{
            canvas.drawBitmap(animation.getImage(), x-30, y, null);
        }catch (Exception e){}
    }

}
