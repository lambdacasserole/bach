package com.sauljohnson.bach;

import javax.swing.*;
import java.awt.event.MouseEvent;

/**
 * Represents a state in which a {@link Brick} is currently being linked to another in a {@link Designer}.
 *
 * @author  Saul Johnson, Alex Mullen, Lee Oliver
 */
public class BrickLinkingState extends BrickSelectedDesignerState {

    /**
     * Initialises a new instance of a state in which a brick is currently being linked to another in a designer.
     *
     * @param designer  the designer this state relates to
     */
    public BrickLinkingState(Designer designer){
        super(designer);
    }

    @Override
    protected void handleMouseClicked(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            if (getSelectedBrick() != null) {
                designer.setSelectedAgentTryingToLink(false);
                designer.selectAgent(getSelectedBrick());
            } else {
                designer.setSelectedAgentTryingToLink(false);
                designer.clearSelection();
            }
        }
    }

    @Override
    protected void handleMouseDragged(MouseEvent e) {
        designer.repaint();
    }

    @Override
    protected void handleMouseMoved(MouseEvent e) {
        designer.repaint();
    }

    @Override
    protected void handleMousePressed(MouseEvent e) {
        handleMouseClicked(e);
    }
}
