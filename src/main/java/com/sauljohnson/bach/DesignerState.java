package com.sauljohnson.bach;

import java.awt.event.MouseEvent;

/**
 * A class that represents a state that a Designer object can be in.
 *
 * @author  Saul Johnson, Alex Mullen, Lee Oliver
 */
public abstract class DesignerState {

    protected Designer designer;

    protected DesignerState(Designer designer) {
        this.designer = designer;
    }

    /**
     * Invoked when the mouse button has been clicked (pressed and released)
     * on the designer.
     *
     * @param e     event data
     */
    protected void handleMouseClicked(MouseEvent e) { }

    /**
     * Invoked when a mouse button is pressed on the designer and then
     * dragged.
     *
     * @param e     event data
     */
    protected void handleMouseDragged(MouseEvent e) { }

    /**
     * Invoked when the mouse cursor has been moved over the designer area
     * but no buttons have been pushed.
     *
     * @param e     event data
     */
    protected void handleMouseMoved(MouseEvent e) { }

    /**
     * Invoked when a mouse button has been pressed on the designer area.
     *
     * @param e     event data
     */
    protected void handleMousePressed(MouseEvent e) { }

    /**
     * Invoked when a mouse button has been released on the designer area.
     *
     * @param e     event data
     */
    protected void handleMouseReleased(MouseEvent e) { }

    /**
     * Invoked when the mouse enters the designer area.
     *
     * @param e     event data
     */
    protected void handleMouseEntered(MouseEvent e) { }

    /**
     * Invoked when the mouse exits the designer area.
     *
     * @param e     event data
     */
    protected void handleMouseExited(MouseEvent e) { }
}
    