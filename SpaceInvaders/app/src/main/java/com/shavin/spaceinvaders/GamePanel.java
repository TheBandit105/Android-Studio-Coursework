package com.shavin.spaceinvaders;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;

// Implements this interface to receive information about changes to the surface.
public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    private Random rand = new Random();

    /**
     * Set game screen width and height.
     */
    public static final int WIDTH = 856;
    public static final int HEIGHT = 480;


    /**
     * Background speed movement set
     */
    public static final int MSPEED = -5;

    /**
     * Object reference of Background class.
     */
    private Background bg;

    /**
     * Object reference of Hero class.
     */
    private Hero hero;

    /**
     * Arraylist object reference to laser and timer reference.
     * Arraylist needed as it is not know exactly how many frames laser image
     * has.
     */
    private ArrayList<Laser> laser;
    private long laserStartTime;

    /**
     * Arraylist object reference to enemy and timer reference.
     */
    private ArrayList<Enemy> alien;
    private long alienStartTime;


    private MainThread thread;

    /**
     *  GamePanel allows access to application-specific resources and classes,
     *  as well as up-calls for application-level operations such as launching activities,
     *  broadcasting and receiving intents, etc.
     * @param c
     */

    public GamePanel(Context c) {

        /**
         * Context tells the current state of the application or object.
         * Allows for instantiated objects to grasp what events have taken
         * place. This means that information about another section of the program
         * can be called.
         */
        super(c);

        // getHolder() has a callback method which intercepts events.
        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this);

        // setFocusable makes GamePanel focus on handling events.
        setFocusable(true);

    }

    /**
     * Called immediately after surface is first instantiated. Implementations
     * of this should start up whatever rendering code chosen.
     * @param holder
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        // Draw background image on screen.
        bg = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.background));

        // Hero object called.
        hero = new Hero(BitmapFactory.decodeResource(getResources(), R.drawable.hero), 30, 45, 2);

        /**
         * Created laser arraylist object and set laser timer to system's timer.
         * Same thing happens with enemy.
         */
        laser = new ArrayList<Laser>();
        laserStartTime = System.nanoTime();

        alien = new ArrayList<Enemy>();
        alienStartTime = System.nanoTime();


        // Starts game loop.
        thread.setActive(true);
        thread.start();
    }

    /**
     * Called after any structural changes (format or size) have been made to the surface.
     * @param holder
     * @param format
     * @param width
     * @param height
     */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    /**
     * Called immediately before surface is destroyed. Access to this surface will not be allowed
     * after call is returned.
     * @param holder
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;

        while (retry){

            /**
             * join method used to hold the execution of currently running thread until
             * the specified thread is terminated (finished execution).
             */
            try{
                thread.setActive(false);
                thread.join();
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }

    }

    /**
     * onTouchEvent allows a check to see if user touches the screen or not
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event){

        if(event.getAction() == MotionEvent.ACTION_DOWN){
            if(!hero.isPlay()){
                hero.setPlay(true);
            } else{
                hero.setUp(true);
            }
            return true;
        }

        if(event.getAction() == MotionEvent.ACTION_UP) {
            hero.setUp(false);
            return true;
        }

        return super.onTouchEvent(event);
    }

    // Update method.
    public void update() {

        if(hero.isPlay()) {
            bg.update();
            hero.update();

            // Added laser to timer.
            long laserTimer = (System.nanoTime() - laserStartTime)/1000000;

            /**
             * Check the delay among laser fired from hero. This means that if the player
             * increases their score each time, the laser fired will become faster than last
             * one fired.
             */
            if(laserTimer > (2500 - hero.getScore()/4)){

                // Positioning of amo fire from hero.
                laser.add(new Laser((BitmapFactory.decodeResource(getResources(), R.drawable.laser)), hero.getX()+60, hero.getY()+24, 15, 7, 7));
                laserStartTime = System.nanoTime();
            }

            // For loop to animate and update the frames of the laser image.
            for(int i = 0; i < laser.size(); i++){
                laser.get(i).update();

                // If laser is off the screen limits, it is removed.
                if(laser.get(i).getX() < -10){
                    laser.remove(i);
                }
            }

            // Added enemy to timer.
            long alienElapsed = (System.nanoTime() - alienStartTime)/1000000;

            if(alienElapsed > (10000 - hero.getScore()/4)){
                alien.add(new Enemy(BitmapFactory.decodeResource(getResources(), R.drawable.enemy),
                        WIDTH + 10, (int)(rand.nextDouble() * (HEIGHT - 50)), 40, 60, hero.getScore(), 3));

                // Reset timer.
                alienStartTime = System.nanoTime();
            }

            // Loop through every alien.
            for(int i = 0; i < alien.size(); i++){
                // Update alien.
                alien.get(i).update();

                // Remove alien if it is way off screen.
                if (alien.get(i).getX() < -100){
                    alien.remove(i);
                    break;
                }
            }

        }
    }

    @Override
    public void draw(Canvas canvas) {

        /**
         * This draw method will help to scale the game to run on devices
         * that have a different screen size.
         */
        final float scaleFactorX = getWidth()/(WIDTH * 1.f);
        final float scaleFactorY = getHeight()/(HEIGHT * 1.f);

        /**
         * If anything appears on screen, it is scaled to fit the device
         * the game is being run on.
         */
        if(canvas != null){
            final int savedState = canvas.save();
            canvas.scale(scaleFactorX, scaleFactorY);
            bg.draw(canvas);
            hero.draw(canvas);

            for(Laser fp: laser){
                fp.draw(canvas);
            }

            for(Enemy aln: alien){
                aln.draw(canvas);
            }

            canvas.restoreToCount(savedState);
        }

    }

}
