package com.shavin.spaceinvaders;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

public class Gem extends GameObject {

    // Score when enemy is killed.
    private int score;

    // Enemy speed
    private int speed;

    // Random class to give enemies different speeds.
    private Random rand = new Random();

    // Animation class.
    private Animation animation = new Animation();

    // Bitmap reference of enemy image.
    private Bitmap spritesheet;

    public Gem(Bitmap res, int x, int y, int w, int h, int frameNo){

        // Obtain coordinates from GameObject class.
        super.x = x;
        super.y = y;
        width = w;
        height = h;

        // Set different speed for the enemy each gameloop.
        speed = 4 + (int)(rand.nextDouble()*score/30);

        // Max speed of the enemy.
        if(speed > 40){
            speed = 40;
        }

        /** Create bitmap object to save later all the versions of the enemy image to
         * animate.
         */
        Bitmap[] image = new Bitmap[frameNo];
        spritesheet = res;

        // For loop to help save all versions of enemy image in bitmap table.
        for(int i = 0; i < image.length; i++){
            image[i] = Bitmap.createBitmap(spritesheet, i*width, 0, width, height);
        }

        // Enemy animated.
        animation.setFrames(image);

        // Animation delay set.
        animation.setDelay(100-speed);
    }

    public void update(){

        /**
         * Movement of enemy set to move closer to the hero each time.
         */
        x-=speed;
        animation.update();
    }

    public void draw(Canvas canvas){

        // Enemy drawn.
        try{
            canvas.drawBitmap(animation.getImage(), x, y, null);
        } catch (Exception e){}

    }

    @Override
    public int getWidth() {

        // Slight offset to make collision detection more realistic.
        return width-10;
    }
}
