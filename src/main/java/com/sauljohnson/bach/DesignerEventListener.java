package com.sauljohnson.bach;

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
    public void brickAdded(Brick brick);
    
    /**
     * Raised when an brick is deleted from the designer.
     * 
     * @param brick The brick that was deleted.
     */
    public void brickDeleted(Brick brick);
    
    /**
     * Raised when an brick is selected in the designer.
     * 
     * @param brick The brick that was selected.
     */
    public void brickSelected(Brick brick);
    
    /**
     * Raised when an brick has been repositioned in the designer.
     * 
     * @param brick The brick that has been repositioned/moved.
     */
    public void brickMoved(Brick brick);

    /**
     * Raised when a previously selected brick is now not selected and nothing else is currently selected.
     */
    public void selectionCleared();
    
    /**
     * Raised when an brick is linked with another brick in the designer.
     * 
     * @param brick The link that was created.
     */
    public void linkCreated(Brick brick);
    
    /**
     * Raised when an brick link is broken then deleted in the designer.
     * 
     * @param brick The link that is and has been deleted.
     */
    public void linkDeleted(Brick brick);
    
}