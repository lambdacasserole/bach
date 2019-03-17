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
    @SuppressWarnings("WeakerAccess") // API method.
    public int getX() {
        return x;
    }

    /**
     * Gets the x-coordinate of the brick in the designer in pixels.
     *
     * @param x the new x-coordinate of the brick in the designer in pixels
     */
    @SuppressWarnings("WeakerAccess") // API method.
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Gets the y-coordinate of the brick in the designer in pixels.
     *
     * @return  the y-coordinate of the brick in the designer in pixels
     */
    @SuppressWarnings("WeakerAccess") // API method.
    public int getY() {
        return y;
    }

    /**
     * Gets the y-coordinate of the brick in the designer in pixels.
     *
     * @param y the new y-coordinate of the brick in the designer in pixels
     */
    @SuppressWarnings("WeakerAccess") // API method.
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Gets the name of the brick type.
     *
     * @return  the name of the brick type
     */
    @SuppressWarnings("WeakerAccess") // API method.
    public abstract String getTypeName();

    /**
     * Gets the width of the brick in pixels.
     *
     * @return  the width of the brick in pixels
     */
    @SuppressWarnings("WeakerAccess") // API method.
    public abstract int getWidth();

    /**
     * Gets the height of the brick in pixels.
     *
     * @return  the height of the brick in pixels
     */
    @SuppressWarnings("WeakerAccess") // API method.
    public abstract int getHeight();

    /**
     * Gets the image that represents the brick.
     *
     * @return  the image that represents the brick
     */
    @SuppressWarnings("WeakerAccess") // API method.
    public abstract Image getImage();

    /**
     * Gets the popup menu for the brick.
     *
     * @return  the popup menu for the brick
     */
    @SuppressWarnings("WeakerAccess") // API method.
    public abstract JPopupMenu getContextMenu();

    /**
     * Returns true if this brick has an associated context menu. Otherwise returns false.
     *
     * @return  true if this brick has an associated context menu, otherwise false
     */
    @SuppressWarnings("WeakerAccess") // API method.
    public boolean hasContextMenu() {
        return getContextMenu() != null;
    }

    /**
     * Gets the list of connection capacities for the brick.
     *
     * @return  the list of supported connection capacities for the brick
     */
    @SuppressWarnings("WeakerAccess") // API method.
    public abstract List<ConnectionCapacity> getConnectionCapacities();

    /**
     * Gets the list of bricks this brick is connected to.
     *
     * @return  the list of bricks this brick is connected to
     */
    @SuppressWarnings("WeakerAccess") // API method.
    public List<Brick> getConnections() {
        return Collections.unmodifiableList(connections);
    }

    /**
     * Returns true if this brick can connect to the type of the one given, otherwise returns false.
     *
     * @param brick the brick to check
     * @return      true if this brick can connect to the type of the one given, otherwise false
     */
    private boolean supportsConnection(Brick brick) {
        for (ConnectionCapacity capacity : getConnectionCapacities()) {
            if (capacity.getType() == brick.getClass()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Counts connections having the same type as the given brick.
     *
     * @param brick the brick for which to count connections of the same type
     * @return      the number of connections having the same type as the given brick
     */
    private int countConnectionsOfType(Brick brick) {
        int count = 0;
        for (Brick connection : connections) {
            if (connection.getClass() == brick.getClass()) {
                count++;
            }
        }
        return count;
    }

    /**
     * Gets the maximum connection capacity for connections of the same type as the given brick.
     *
     * @param brick the brick for which to get the maximum capacity for connections of the same type
     * @return      the maximum connection capacity for connections of the same type as the given brick
     */
    private int getMaximumConnectionCapacity(Brick brick) {
        for (ConnectionCapacity capacity : getConnectionCapacities()) {
            if (capacity.getType() == brick.getClass()) {
                return capacity.getCount();
            }
        }
        return 0;
    }

    /**
     * Returns true if this brick can connect to the one given, otherwise returns false.
     *
     * @param brick the brick to check
     * @return      true if this brick can connect to the one given, otherwise false
     */
    @SuppressWarnings("WeakerAccess") // API method.
    public boolean canConnect(Brick brick) {

        // Bidirectional support and bidirectional capacity required.
        return supportsConnection(brick) && brick.supportsConnection(this)
                && countConnectionsOfType(brick) < getMaximumConnectionCapacity(brick)
                && brick.countConnectionsOfType(this) < brick.getMaximumConnectionCapacity(this);
    }

    /**
     * Adds a connection to the brick without checking its validity.
     *
     * @param brick the brick to add a connection to
     */
    private void addConnectionUnchecked(Brick brick) {
        connections.add(brick);
    }

    /**
     * Adds a connection to the brick.
     *
     * @param brick the brick to add a connection to
     */
    @SuppressWarnings("WeakerAccess") // API method.
    public void addConnection(Brick brick) {
        if (canConnect(brick) && brick.canConnect(this)) {
            if (hasConnection(brick)) {
                throw new IllegalArgumentException("You may not add multiple links between the same bricks.");
            } else {
                addConnectionUnchecked(brick);
                brick.addConnectionUnchecked(this);
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
    @SuppressWarnings("WeakerAccess") // API method.
    public boolean hasConnection(Brick brick) {
        return connections.contains(brick);
    }

    /**
     * Removes a connection from the brick.
     *
     * @param brick the brick to remove the connection to
     */
    @SuppressWarnings("WeakerAccess") // API method.
    public void removeConnection(Brick brick) {
        connections.remove(brick);
        brick.connections.remove(this);
    }

    /**
     * Removes all connections from the brick.
     */
    @SuppressWarnings("WeakerAccess") // API method.
    public void removeAllConnections() {
        while (connections.size() > 0) {
            removeConnection(connections.get(0));
        }
    }

    /**
     * Returns the Rectangle instance that represents the size and position of this brick.
     * 
     * @return  the Rectangle instance
     */
    @SuppressWarnings("WeakerAccess") // API method.
    public Rectangle getBounds() {
        return new Rectangle(x, y, getWidth(), getHeight());
    }
    
    /**
     * Returns the underlying instance this brick is representing.
     * 
     * @return  the instance
     */
    @SuppressWarnings("unused") // API method.
    public T getModelObject() {
        return modelObject;
    }
}
