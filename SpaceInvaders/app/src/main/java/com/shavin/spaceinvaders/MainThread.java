package com.shavin.spaceinvaders;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class MainThread extends Thread {

    /**
     * FPS is the time speed of the game being run.
     * A good game will run anywhere between 30 - 60 fps.
     * 30 fps used here as this game will involve quite a bit
     * of objects.
     */
    private int FPS = 30;

    /**
     * avgFPS will tell us if the game is running too fast or too
     * slow, for the time the game is being used.
     */
    private double avgFPS;

    private SurfaceHolder surfaceHolder;

    private GamePanel gamePanel;

    // Checks to see if the thread is running or not.
    private boolean isActive;

    /**
     *  Canvas class allows us to draw any image onto the screen.
     *  The basic components to draw include:
     *  > A bitmap to hold pixels
     *  > A canvas so that the draw method is called (allows
     *  for writing onto the bitmap)
     *  > A drawing primitive (e.g. root, path, text, bitmap)
     *  > A paint
     */
    public static Canvas canvas;

    /**
     * Thread constructor calls upon the references of Content view objects.
     * @param surfaceHolder
     * @param gamePanel
     */

    public MainThread(SurfaceHolder surfaceHolder, GamePanel gamePanel) {
        super();
        this.surfaceHolder = surfaceHolder;
        this.gamePanel = gamePanel;
    }


    /**
     * Every thread has a run method
     */
    @Override
    public void run(){
        /**
         * The run method will catch 30 frames for every second that
         * passes.
         */

        long startTime;
        long timeMs;
        long waitTime;
        long totalTime = 0;

        long frameCount = 0;

        long targetTime = 1000/FPS;

        while(isActive){

            startTime = System.nanoTime();

            /**
             * We need canvas in order to be able to draw object(s) on the screen for each frame passed.
             * try()catch() will be used to avoid errors if something goes wrong.
             */

            canvas = null;

            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder){

                    /**
                     * This is the game data update method (e.g. character positioning on x and y axis).
                     */
                    this.gamePanel.update();

                    /**
                     * This method draws the picture viewed by the user. Constant call of this method will
                     * make the game look like a film (i.e. make the game look a bit realistic).
                     */
                    this.gamePanel.draw(canvas);
                }
            } catch (Exception e){
            }

            finally{
                if(canvas != null)
                {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }

            // Getting the time in milliseconds as required.
            timeMs = (System.nanoTime() - startTime) / 1000000;

            // The waiting time required until the next frame enters the loop.
            waitTime = targetTime - timeMs;

            try {
                this.sleep(waitTime);
            } catch (Exception e){
            }

            totalTime += System.nanoTime() - startTime;
            frameCount++;

            /**
             * Game loop lasts 1 second, meaning the average fps must be calculated.
             * In a perfect system, each frame will last for 33.3 millisecs. However, not all
             * CPUs work the same and don't have the same processing power. The FPS value will depend
             * on the type of CPU the user uses for the game when they play it.
             */
            if(frameCount == FPS){
                avgFPS = 1000/((totalTime/frameCount) / 1000000);
                frameCount = 0;
                totalTime = 0;
                System.out.println("Average FPS: " + avgFPS);
            }

        }
    }

    public void setActive(boolean b){
        isActive = b;
    }
}
