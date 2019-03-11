package com.sauljohnson.bach;

import javax.swing.*;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 * Represents a brick that can be displayed and moved in a designer.
 * 
 * @author  Saul Johnson, Alex Mullen, Lee Oliver
 * @see     Designer
 */
public abstract class Brick<T> {

    /**
     * The underlying instance that is being visually represented.
     */
    private final T modelObject;

    private int x;

    private int y;

    private int width;

    private int height;

    private BufferedImage image;

    /**
     * Initialises a new instance of a brick.
     * 
     * @param modelObject the underlying instance
     */
    public Brick(T modelObject, int x, int y, int width, int height, BufferedImage image) {
        this.modelObject = modelObject;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.image = image;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public BufferedImage getImage() {
        return image;
    }

    public abstract JPopupMenu getContextMenu();

    /**
     * Returns the Rectangle instance that represents the size and position of this brick.
     * 
     * @return the Rectangle instance
     */
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
    
    /**
     * Returns the underlying instance this brick is representing.
     * 
     * @return the instance
     */
    public T getModelObject() {
        return modelObject;
    }
}
