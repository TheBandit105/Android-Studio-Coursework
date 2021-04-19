package com.shavin.spaceinvaders;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
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

    Bitmap hearta;
    Bitmap heartb;
    Bitmap heartc;

    Bitmap mypanelscore;

    private int hearts = 3;

    Bitmap gemBonus;
    public int heroGems;
    private long gemStartTime;
    private ArrayList<Gem> myGems;

    Bitmap myPanel;

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

    private ArrayList<Enemy> aliensecond;
    private long  secondalienStartTime;

    /**
     * Arraylist object reference to obstacle and timer reference.
     */
    private ArrayList<Obstacle> obstacle;
    private long obstacleStartTime;

    private ArrayList<Obstacle> obstacletop;
    private long  obstacletopStartTime;

    private ArrayList<Borderfloor> borderfloor;
    private long borderStartTime;

    // Reset game variables.
    private boolean newGameCreated;

    private long startReset;

    private boolean reset;

    private boolean disappear;

    private boolean started;

    private Explosion explosion;

    private int best;

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

        thread = new MainThread(getHolder(), this);

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

        aliensecond=new ArrayList<Enemy>();
        secondalienStartTime= System.nanoTime();

        obstacle = new ArrayList<Obstacle>();
        obstacleStartTime = System.nanoTime();

        obstacletop = new ArrayList<Obstacle>();
        obstacletopStartTime = System.nanoTime();

        borderfloor = new ArrayList<Borderfloor>();
        borderStartTime = System.nanoTime();

        myGems = new ArrayList<Gem>();
        gemStartTime = System.nanoTime();

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

        int counter = 0;

        while (retry && counter < 1000){

            /**
             * join method used to hold the execution of currently running thread until
             * the specified thread is terminated (finished execution).
             */
            try{
                thread.setActive(false);
                thread.join();

                retry = false;
                thread = null;
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
            if(!hero.isPlay() && newGameCreated && reset){
                hero.setPlay(true);
            } else {
                hero.setUp(true);
            }

            if(hero.isPlay()){
                if(!started) {
                    started = true;
                    reset = false;
                    hero.setUp(true);
                }
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

            long gemTime = (System.nanoTime()-gemStartTime)/1000000;

            if(((gemTime > (3000 - hero.getScore()/4)))){
               myGems.add(new Gem((BitmapFactory.decodeResource(getResources(), R.drawable.gem)), WIDTH + 1,
                       (int)(rand.nextDouble() * (HEIGHT -200)), 20, 20, 1));
               gemStartTime = System.nanoTime();
            }

            for(int i = 0; i < myGems.size(); i++){
                myGems.get(i).update();

                if (collision(myGems.get(i), hero)){

                    myGems.remove(i);
                    heroGems+=1;
                    break;
                }

                if(myGems.get(i).getX()<100){
                    myGems.remove(i);
                    break;
                }
            }

            long borderElapsed = (System.nanoTime()-borderStartTime)/1000000;

            if(borderElapsed > 100){
                borderfloor.add(new Borderfloor(BitmapFactory.decodeResource(getResources(), R.drawable.borderfloor),WIDTH + 10, ((HEIGHT -80)+rand.nextInt(10))));
                borderfloor.add(new Borderfloor(BitmapFactory.decodeResource(getResources(), R.drawable.borderroof),WIDTH + 10, ((HEIGHT -540)+rand.nextInt(10))));
                borderStartTime = System.nanoTime();
            }

            for(int i = 0; i<borderfloor.size();i++) {
                //update obstacle
                borderfloor.get(i).update();


                if (collision(borderfloor.get(i), hero)) {
                    hero.setPlay(false);
                    break;
                }

                //if statement to remove border if is of the screen limits
                if( borderfloor.get(i).getX()<10)
                {
                    borderfloor.remove(i);
                }
            }

            long obstacleElapsed = (System.nanoTime()-obstacleStartTime)/1000000;

            if(obstacleElapsed > 15000 - hero.getScore()/4){

                obstacle.add(new Obstacle(BitmapFactory.decodeResource(getResources(), R.drawable.obstacle),
                 WIDTH + 10, HEIGHT -290+rand.nextInt(150), 90, 300, hero.getScore(), 1));

                obstacleStartTime = System.nanoTime();
            }

            for(int i = 0; i < obstacle.size(); i++){
                obstacle.get(i).update();

                if (collision(obstacle.get(i), hero)){
                    hero.setPlay(false);
                    break;
                }
            }

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

            if(alienElapsed > (5000 - hero.getScore()/4)){
                alien.add(new Enemy(BitmapFactory.decodeResource(getResources(), R.drawable.enemy),
                        WIDTH + 10, (int)(rand.nextDouble() * (HEIGHT - 50)), 40, 60, hero.getScore(), 3));

                // Reset timer.
                alienStartTime = System.nanoTime();
            }

            // Loop through every alien.
            for(int i = 0; i < alien.size(); i++){
                // Update alien.
                alien.get(i).update();

                // Collision detection between hero and alien(enemy).
                if (collision(alien.get(i), hero)){
                    alien.remove(i);

                    hearts--;
                    //hero.setPlay(false);
                    break;
                }

                // Remove alien if it is way off screen.
                if (alien.get(i).getX() < -100){
                    alien.remove(i);
                    break;
                }

                // Collision detection between laser and alien.
                for (int j = 0; j < laser.size(); j++){
                    if(collision(alien.get(j), laser.get(j))){

                        explosion = new Explosion(BitmapFactory.decodeResource(getResources(), R.drawable.explosion), alien.get(i).getX(),
                                alien.get(i).getY(), 100, 100, 15);

                        alien.remove(i);
                        laser.remove(j);

                        best+=30;

                        break;
                    }
                    laser.get(j).update();
                }

            }

            long secondalienElapsed = (System.nanoTime()-secondalienStartTime)/1000000;

            if(secondalienElapsed >(10000 - hero.getScore()/4)){

                aliensecond.add(new Enemy(BitmapFactory.decodeResource(getResources(), R.drawable.alienorange),
                        WIDTH + 10, (int) (rand.nextDouble() * (HEIGHT - 50)), 40, 60, hero.getScore(), 3));

                //reset timer

                secondalienStartTime = System.nanoTime();
            }

            //loop through every alien and check collision and remove
            for(int i = 0; i<aliensecond.size();i++) {
                //update alien
                aliensecond.get(i).update();


                if (collision(aliensecond.get(i), hero)) {
                    aliensecond.remove(i);
                    //lose a life
                    hearts--;

                    //player.setPlaying(false);

                    break;
                }

                if (aliensecond.get(i).getX() < -100) {
                    aliensecond.remove(i);
                    break;
                }

                //collision missile with bullet (fire)
                for (int j = 0; j < laser.size(); j++) {


                    if (collision(aliensecond.get(i), laser.get(j))) {
                        //every time the player hit a missile then it destroys and gain 20 points

                        explosion = new Explosion(BitmapFactory.decodeResource(getResources(), R.drawable.alienorangeexplode), aliensecond.get(i).getX(),
                                aliensecond.get(i).getY(), 100, 100, 15);

                        aliensecond.remove(i);
                        laser.remove(j);

                        best += 30;
                        break;
                    }
                    laser.get(j).update();
                    explosion.update();

                }
            }

        } else {
            hero.resetDYA();

            if(!reset){
                newGameCreated = false;
                startReset = System.nanoTime();
                reset = true;
                disappear = true;

                explosion = new Explosion(BitmapFactory.decodeResource(getResources(), R.drawable.explosion), hero.getX(), hero.getY(), 100, 100, 15);
            }

            explosion.update();

            long resetElapsed = (System.nanoTime()-startReset)/1000000;

            if(resetElapsed>2500 && !newGameCreated){
                newGame();
            }

        }

    }

    public boolean collision(GameObject a, GameObject b){
        if(Rect.intersects(a.getRect(), b.getRect())){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void draw(Canvas canvas) {

        /**
         * This draw method will help to scale the game to run on devices
         * that have a different screen size.
         */
        super.draw(canvas);

        final float scaleFactorX = getWidth() / (WIDTH * 1.f);
        final float scaleFactorY = getHeight() / (HEIGHT * 1.f);

        /**
         * If anything appears on screen, it is scaled to fit the device
         * the game is being run on.
         */
        if (canvas != null) {
            final int savedState = canvas.save();
            canvas.scale(scaleFactorX, scaleFactorY);
            bg.draw(canvas);

            if(!disappear) {
                hero.draw(canvas);
            }

            for (Laser fp : laser) {
                fp.draw(canvas);
            }

            for (Enemy aln : alien) {
                aln.draw(canvas);
            }

            for(Enemy saln: aliensecond)
            {
                saln.draw(canvas);
            }

            for(Obstacle tobsb: obstacletop)
            {
                tobsb.draw(canvas);
            }

            for (Obstacle obsb: obstacle){
                obsb.draw(canvas);
            }

            for (Borderfloor brb: borderfloor){
                brb.draw(canvas);
            }

            for(Gem gm: myGems){
                gm.draw(canvas);
            }

            if(started){
                explosion.draw(canvas);
            }

            drawText(canvas);
            canvas.restoreToCount(savedState);
        }

    }

    public void newGame(){
        disappear = false;

        alien.clear();
        aliensecond.clear();
        obstacle.clear();
        obstacletop.clear();

        borderfloor.clear();
        laser.clear();

        hero.resetDYA();
        hero.resetScore();
        myGems.clear();

        hero.setY(HEIGHT/2);

        newGameCreated = true;

    }

    public void drawText(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(Color.YELLOW);
        paint.setTextSize(30);

        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("Distance: " + (hero.getScore()*2), 10, HEIGHT - 10, paint);
        canvas.drawText("Score: " + best, WIDTH - 215, HEIGHT - 10, paint);

        gemBonus = BitmapFactory.decodeResource(getResources(), R.drawable.gem);
        canvas.drawBitmap(gemBonus, WIDTH - 130, 0, null);
        canvas.drawText(" " + heroGems, WIDTH - 90, 25, paint);

        if (hearts == 3){
            hearta = BitmapFactory.decodeResource(getResources(), R.drawable.lifea);
            canvas.drawBitmap(hearta, WIDTH/2 -120, 0, null);
            heartb = BitmapFactory.decodeResource(getResources(), R.drawable.lifeb);
            canvas.drawBitmap(heartb, WIDTH/2 -80, 0, null);
            heartc = BitmapFactory.decodeResource(getResources(), R.drawable.lifec);
            canvas.drawBitmap(heartc, WIDTH/2 -40, 0, null);
        }

        if (hearts == 2){
            hearta = BitmapFactory.decodeResource(getResources(), R.drawable.lifea);
            canvas.drawBitmap(hearta, WIDTH/2 -120, 0, null);
            heartb = BitmapFactory.decodeResource(getResources(), R.drawable.lifeb);
            canvas.drawBitmap(heartb, WIDTH/2 -80, 0, null);
        }

        if (hearts == 1){
            hearta = BitmapFactory.decodeResource(getResources(), R.drawable.lifea);
            canvas.drawBitmap(hearta, WIDTH/2 -120, 0, null);
        }

        if (hearts == 0){
            hero.setPlay(false);
            hearts = 3;
        }

        if(!hero.isPlay() && newGameCreated && reset){
            Paint pnt = new Paint();
            pnt.setTextSize(25);
            pnt.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

            myPanel = BitmapFactory.decodeResource(getResources(), R.drawable.panel);
            canvas.drawBitmap(myPanel, WIDTH/2-240, HEIGHT/2-210, null);

            canvas.drawText("PRESS TO START", WIDTH/2-120, HEIGHT/2-70, pnt);
            canvas.drawText("PRESS AND HOLD TO GO UP", WIDTH/2-190, HEIGHT/2-20, pnt);
            canvas.drawText("RELEASE TO GO DOWN", WIDTH/2-140, HEIGHT/2+20, pnt);
        }

        if(!hero.isPlay() && newGameCreated && reset && heroGems > 0){
            Paint pnt2 = new Paint();
            mypanelscore = BitmapFactory.decodeResource(getResources(), R.drawable.scorepanel);
            canvas.drawBitmap(mypanelscore, WIDTH/2-275,HEIGHT/2-150, null);
            pnt2.setTextSize(25);
            pnt2.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            canvas.drawText("PRESS TO RETRY", WIDTH/2-100, HEIGHT/2-80, pnt2);
            pnt2.setTextSize(20);
            canvas.drawText("GAME OVER!", WIDTH/2-50, HEIGHT/2 - 50, pnt2);
            canvas.drawText("Gems: "+ heroGems, WIDTH/2-50, HEIGHT/2 - 20, pnt2);
            canvas.drawText("Final Score: "+ best, WIDTH/2-50, HEIGHT/2 +10, pnt2);
        }


    }
}
