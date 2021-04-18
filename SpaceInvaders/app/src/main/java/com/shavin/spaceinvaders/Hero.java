package com.shavin.spaceinvaders;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Hero extends GameObject {

    // User image is going to be 2 images.
    private Bitmap spritesheet;

    // Score counter.
    private int score;

    // Keep track of y value each time screen is touched.
    private double dya;

    // Check to see if user moves up or down.
    private boolean up;

    // Check to see if user wants to start game.
    private boolean play;

    // Call instance of Animation class and need timer for user.
    private Animation animation = new Animation();
    private long startTime;

    public Hero(Bitmap res, int w, int h, int frameNo){

        /**
         * Upon game start, hero will have a position already
         * in the middle and to the left
         */
        x = 100;
        y = GamePanel.HEIGHT / 2;

        /**
         * dy position needed to be determined to know if user is going
         * up or down. Pressing the screen will make the hero go up.
         */
        dy = 0;

        // Score set to 0 at beginning of game.
        score = 0;

        // Width and height of image needed to create a bitmap.
        height = h;
        width = w;

        Bitmap[] image = new Bitmap[frameNo];
        spritesheet = res;

        for (int i = 0; i < image.length; i++){
            /**
             * Hero image has 3 frames (sprites).
             */
            image[i] = Bitmap.createBitmap(spritesheet, i*width, 0, width, height);
        }

        /**
         * Set the animation and the delay.
         */
        animation.setFrames(image);
        animation.setDelay(10);

        /**
         * Initiated timer so that it can be used in the update method.
         */
        startTime = System.nanoTime();
    }

    /**
     * Boolean method to know if user presses screen, hero goes up.
     * @param b
     */
    public void setUp(boolean b){
        up = b;
    }

    public void update(){
        // User timer in millisecs.
        long elapsed = (System.nanoTime()-startTime)/1000000;

        // When time elapsed is greater than 100, the score will be auto incremented.
        if(elapsed > 100) {
            score++;
            startTime = System.nanoTime();
        }
        animation.update();

        /**
         * Sets speed of hero's ascent (going up) and
         * descent (falling down) in the game area using the dya
         * variable.
         */
        if(up){
            dy = (int)(dya -= 1.1);
        } else{
            dy = (int)(dya += 1.1);
        }

        /**
         * Speed limit set to avoid the user going too fast by pressing
         * continuously on the screen.
         */
        if(dy > 14) {
            dy = 14;
        }

        if(dy < -14){
            dy = -14;
        }

        y += dy*2;
        dy = 0;
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(animation.getImage(), x, y,null);
    }

    public int getScore() {
        return score;
    }

    public boolean isPlay() {
        return play;
    }

    public void setPlay(boolean b) {
        play = b;
    }

    public void resetDYA(){
        dya = 0;
    }

    public void resetScore(){
        score = 0;
    }
}
