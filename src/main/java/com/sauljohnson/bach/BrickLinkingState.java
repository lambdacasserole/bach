package com.sauljohnson.bach;

import javax.swing.*;
import java.awt.event.MouseEvent;

/**
 * A class that represents a state where there is a link attached to the
 * selected agent with the other end attached to the mouse cursor position.
 */
public class BrickLinkingState extends DesignerState {

    public BrickLinkingState(Designer designer){
        super(designer);
    }

    @Override
    protected void handleMouseClicked(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            final Brick agentClicked = designer.getBrickAt(e.getPoint());
            if (agentClicked != null) {
                designer.setSelectedAgentTryingToLink(false);
                designer.selectAgent(agentClicked);
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
