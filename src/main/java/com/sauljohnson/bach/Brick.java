package com.sauljohnson.bach;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a brick that can be displayed and moved in a {@link Designer}.
 * 
 * @author  Saul Johnson, Alex Mullen, Lee Oliver
 */
public abstract class Brick<T> {

    /**
     * The underlying instance that is being visually represented.
     */
    private final T modelObject;

    /**
     * The x-coordinate of the brick in the designer.
     */
    private int x;

    /**
     * The y-coordinate of the brick in the designer.
     */
    private int y;

    /**
     * A collection of bricks that are connected to this one.
     */
    private List<Brick> connections;

    /**
     * Initialises a new instance of a brick that can be displayed and moved in a {@link Designer}.
     * 
     * @param modelObject   the underlying instance
     * @param x             the initial x-coordinate in pixels
     * @param y             the initial y-coordinate in pixels
     */
    public Brick(T modelObject, int x, int y) {
        this.modelObject = modelObject;
        this.x = x;
        this.y = y;
        connections = new ArrayList<>();
    }

    /**
     * Gets the x-coordinate of the brick in the designer in pixels.
     *
     * @return  the x-coordinate of the brick in the designer in pixels
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the x-coordinate of the brick in the designer in pixels.
     *
     * @param x the new x-coordinate of the brick in the designer in pixels
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Gets the y-coordinate of the brick in the designer in pixels.
     *
     * @return  the y-coordinate of the brick in the designer in pixels
     */
    public int getY() {
        return y;
    }

    /**
     * Gets the y-coordinate of the brick in the designer in pixels.
     *
     * @param y the new y-coordinate of the brick in the designer in pixels
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Gets the name of the brick type.
     *
     * @return  the name of the brick type
     */
    public abstract String getTypeName();

    /**
     * Gets the width of the brick in pixels.
     *
     * @return  the width of the brick in pixels
     */
    public abstract int getWidth();

    /**
     * Gets the height of the brick in pixels.
     *
     * @return  the height of the brick in pixels
     */
    public abstract int getHeight();

    /**
     * Gets the image that represents the brick.
     *
     * @return  the image that represents the brick
     */
    public abstract Image getImage();

    /**
     * Gets the popup menu for the brick.
     *
     * @return  the popup menu for the brick
     */
    public abstract JPopupMenu getContextMenu();

    /**
     * Gets the list of supported connection types for the brick.
     *
     * @return  the list of supported connection types for the brick
     */
    public abstract List<Class> getSupportedConnectionTypes();

    /**
     * Gets the list of bricks this brick is connected to.
     *
     * @return  the list of bricks this brick is connected to
     */
    public List<Brick> getConnections() {
        return Collections.unmodifiableList(connections);
    }

    /**
     * Returns true if this brick can connect to the one given, otherwise returns false.
     *
     * @param brick the brick to check
     * @return      true if this brick can connect to the one given, otherwise false
     */
    public boolean canAddConnection(Brick brick) {
        return getSupportedConnectionTypes().contains(brick.getClass())
                && brick.getSupportedConnectionTypes().contains(getClass());
    }

    /**
     * Adds a connection to the brick.
     *
     * @param brick the brick to add a connection to
     */
    public void addConnection(Brick brick) {
        if (canAddConnection(brick) && brick.canAddConnection(this)) {
            if (hasConnection(brick) || brick.hasConnection(this)) {
                throw new IllegalArgumentException("You may not add multiple links between the same bricks.");
            } else {
                connections.add(brick);
                brick.addConnection(brick);
            }
        } else {
            throw new IllegalArgumentException("You may not connect a brick of type '" + getTypeName()
                    + "' to one of type '" + brick.getTypeName() + "'.");
        }
    }

    /**
     * Returns true if this brick is connected to the one given, otherwise returns false.
     *
     * @param brick the brick to check
     * @return      true if this brick is connected to the one given, otherwise false
     */
    public boolean hasConnection(Brick brick) {
        return connections.contains(brick);
    }

    /**
     * Removes a connection from the brick.
     *
     * @param brick the brick to remove the connection to
     */
    public void removeConnection(Brick brick) {
        connections.remove(brick);
        brick.connections.remove(brick);
    }

    /**
     * Removes all connections from the brick.
     */
    public void removeAllConnections() {
        while (connections.size() > 0) {
            removeConnection(connections.get(0));
        }
    }

    /**
     * Returns the Rectangle instance that represents the size and position of this brick.
     * 
     * @return the Rectangle instance
     */
    public Rectangle getBounds() {
        return new Rectangle(x, y, getWidth(), getHeight());
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
