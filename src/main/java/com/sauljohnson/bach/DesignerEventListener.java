package com.sauljohnson.bach;

import java.awt.*;

/**
 * A specialised event listener designed to listen for events raised by a {@link Designer} control.
 * 
 * @author  Saul Johnson, Alex Mullen, Lee Oliver
 */
public interface DesignerEventListener {

    /**
     * Raised when an brick is added into the designer.
     * 
     * @param brick The brick that was added.
     */
    void brickAdded(Brick brick);
    
    /**
     * Raised when an brick is deleted from the designer.
     * 
     * @param brick The brick that was deleted.
     */
    void brickRemoved(Brick brick);
    
    /**
     * Raised when an brick is selected in the designer.
     * 
     * @param brick The brick that was selected.
     */
    void brickSelected(Brick brick);
    
    /**
     * Raised when an brick has been repositioned in the designer.
     * 
     * @param brick the brick that has been repositioned/moved
     * @param point the new location of the brick
     */
    void brickMoved(Brick brick, Point point);

    /**
     * Raised when a previously selected brick is now not selected and nothing else is currently selected.
     */
    void selectionCleared();

    /**
     * Raised when a brick is linked with another brick in the designer.
     *
     * @param brick1 the first brick
     * @param brick2 the second brick
     */
    void linkCreated(Brick brick1, Brick brick2);

    /**
     * Raised when the designer refuses to link a brick with another brick in the designer.
     *
     * @param brick1 the first brick
     * @param brick2 the second brick
     */
    void linkRefused(Brick brick1, Brick brick2);

    /**
     * Raised when a brick is unlinked from another brick in the designer.
     *
     * @param brick1 the first brick
     * @param brick2 the second brick
     */
    void linkDeleted(Brick brick1, Brick brick2);
}