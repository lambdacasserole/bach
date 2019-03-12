package com.sauljohnson.bach;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * A class that represents the default state where nothing is really currently
 * happening. (Nothing selected, nothing getting dragged or nothing linking)
 */
public class DefaultState extends DesignerState {

    public DefaultState(Designer designer){
        super(designer);
    }

    @Override
    protected void handleMouseClicked(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            final Brick agentClicked = designer.getBrickAt(e.getPoint());
            if (agentClicked != null) {
                designer.selectAgent(agentClicked);
            }
        } else if (SwingUtilities.isRightMouseButton(e)) {
            designer.setLastMenuClickPosition(e.getPoint());
            designer.getEmptySpacePopup().show(e.getComponent(), e.getX(), e.getY());
        }
    }

    @Override
    protected void handleMousePressed(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            final Brick agentClicked = designer.getBrickAt(e.getPoint());
            if (agentClicked != null) {
                designer.setAgentSelected(agentClicked);
                designer.setState(new BrickDraggingState(designer));
                designer.setSelectedComponentDragOffset(new Point(e.getX() - designer.getAgentSelected().getX(), e.getY() - designer.getAgentSelected().getY()));
                designer.repaint();
            }
        }
    }

    @Override
    protected void handleMouseReleased(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) {
            final Brick agentClicked = designer.getBrickAt(e.getPoint());
            if (agentClicked != null) {
                designer.selectAgent(agentClicked);
                designer.setLastMenuClickPosition(e.getPoint());
            } else {
                designer.setLastMenuClickPosition(e.getPoint());
                designer.getEmptySpacePopup().show(e.getComponent(), e.getX(), e.getY());
            }
        }
    }
}