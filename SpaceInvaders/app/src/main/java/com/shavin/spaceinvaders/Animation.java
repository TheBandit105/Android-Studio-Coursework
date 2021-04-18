package com.shavin.spaceinvaders;

import android.graphics.Bitmap;

/**
 * Animation class will help animate the images via the sprites
 * made in the game.
 */
public class Animation {

    // Bitmap table to store the number of image frames.
    private Bitmap[] frames;

    // Checks which frame of the image is shown each time.
    private int currentFrame;

    // Timer for animation.
    private long startTime;

    /** Delay (as in delay between frames) to determine how fast
     *  the animation is going to be.
     */
    private long delay;

    /**
     * Boolean method for images that will only appear once on
     * the screen. For example, the explosion image will occur once
     * an enemy is killed.
     */
    private boolean playedOnce;

    /**
     * Frames of an image needed, the current frame that the game is in
     * and a timer are needed to create the animation.
     * @param frames
     */
    public void setFrames(Bitmap[] frames)
    {
     // Get frame images.
     this.frames = frames;

     // Every image will begin from the 1st frame.
     currentFrame = 0;

     // Set time of animation to systems timer.
     startTime = System.nanoTime();

    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    public void setCurrentFrame(int currentFrame) {
        this.currentFrame = currentFrame;
    }

    public void update(){
        /**
         * Timer will determine which frame of the image is going to be
         * returned each time. Timers used generally to determine when an object
         * will appear on the screen and what actions will the object do.
         * All objects have a time (e.g. hero, enemy, etc...).
         */
        long elapsed = (System.nanoTime()-startTime)/1000000;

        /**
         * Delay of hero animation is set to given millisecs, meaning that the animation starts
         * given millisecs after the game begins. If this delay is too big, animation will slow.
         */
        if(elapsed > delay){
            currentFrame++;
            startTime = System.nanoTime();
        }

        if(currentFrame == frames.length){
            currentFrame = 0;
            playedOnce = true;
        }
    }

    /**
     * Hero class drawings will be be determined by getImage().
     * @return
     */
    public Bitmap getImage() {
        return frames[currentFrame];
    }

    /**
     * Gets frames and boolean value of playedOnce.
     * @return
     */

    public int getCurrentFrame() {
        return currentFrame;
    }

    public boolean isPlayedOnce() {
        return playedOnce;
    }
}
