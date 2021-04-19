package com.shavin.spaceinvaders;

import android.graphics.Rect;

public abstract class GameObject {
    /**
     * All the common variables for the objects will be created here
     * (e.g. Hero, Enemy, etc...).
     */
    protected int x;
    protected int y;
    protected int dx;
    protected int dy;
    protected int width;
    protected int height;


    /*
    Setters and getters need to be set.
     */

    // Setter and getter methods for the x coordinate.
    public void setX(int x) {
        this.x = x;
    }

    public int getX() {
        return x;
    }

    // Setter and getter methods for the y coordinate.
    public void setY(int y) {
        this.y = y;
    }
    public int getY() {
        return y;
    }

    // Gets height and width of the objects used.
    public int getHeight() {
        return height;
    }
    public int getWidth() {
        return width;
    }

    //Rect creates a rectangle around the image, which will be used in collision checks.
    public Rect getRect(){
        return new Rect(x, y, x+width, y+height);
    }

}
