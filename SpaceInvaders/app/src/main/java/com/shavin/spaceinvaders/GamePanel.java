package com.shavin.spaceinvaders;

import android.content.Context;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

// Implements this interface to receive information about changes to the surface.
public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

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
    public void surfaceCreated(@NonNull SurfaceHolder holder) {

    }

    /**
     * Called after any structural changes (format or size) have been made to the surface.
     * @param holder
     * @param format
     * @param width
     * @param height
     */
    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    /**
     * Called immediately before surface is destroyed. Access to this surface will not be allowed
     * after call is returned.
     * @param holder
     */
    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

    }

    /**
     * onTouchEvent allows a check to see if user touches the screen or not
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event){
        return super.onTouchEvent(event);
    }
}
