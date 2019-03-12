package com.sauljohnson.bach;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * A class that represents a state where an agent has been selected.
 */
public class BrickSelectedState extends DesignerState {

    public BrickSelectedState(Designer designer){
        super(designer);
    }

    @Override
    protected void handleMouseClicked(MouseEvent e) {
        final Brick agentClicked = designer.getBrickAt(e.getPoint());
        if (SwingUtilities.isLeftMouseButton(e)) {
            if (agentClicked != null) {
                designer.selectAgent(agentClicked);
            } else {
                designer.clearSelection();
            }
        } else if (SwingUtilities.isRightMouseButton(e)) {
            if (agentClicked != null) {
                designer.selectAgent(agentClicked);
                designer.setLastMenuClickPosition(e.getPoint());
            } else {
                designer.clearSelection();
                designer.setLastMenuClickPosition(e.getPoint());
                designer.getEmptySpacePopup().show(e.getComponent(), e.getX(), e.getY());
            }
        }
    }

    @Override
    protected void handleMousePressed(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            final Brick agentClicked = designer.getBrickAt(e.getPoint());
            if (agentClicked != null) {
                designer.setAgentSelected(agentClicked);
                designer.setState(new BrickDraggingState(designer));
                designer.setSelectedComponentDragOffset(new Point(e.getX() - agentClicked.getX(), e.getY() - agentClicked.getY()));
                designer.repaint();
            }
        }
    }

    @Override
    protected void handleMouseReleased(MouseEvent e) {
        handleMouseClicked(e);
    }
}
