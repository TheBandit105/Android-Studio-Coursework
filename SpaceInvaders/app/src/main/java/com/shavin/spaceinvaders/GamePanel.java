package com.shavin.spaceinvaders;

import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

// Implements this interface to receive information about changes to the surface.
public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

    /*
    GamePanel allows access to application-specific resources and classes,
    as well as up-calls for application-level operations such as launching activities,
    broadcasting abd receiving intents, etc.
     */
    public GamePanel(Context c) {

        super(c);

        // getHolder() intercepts events.
        getHolder().addCallback(this);

        // setFocusable makes GamePanel focus on handling events.
        setFocusable(true);

    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

    }
}
