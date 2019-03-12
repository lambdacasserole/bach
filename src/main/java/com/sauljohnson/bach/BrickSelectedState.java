package com.sauljohnson.bach;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * A class that represents a state where an agent has been selected.
 */
public class BrickSelectedState extends BrickSelectedDesignerState {

    public BrickSelectedState(Designer designer){
        super(designer);
    }

    @Override
    protected void handleMouseClicked(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            if (designer.getLinkBoxBounds().contains(e.getPoint())) {
                designer.setSelectedAgentTryingToLink(true);
                designer.setState(new BrickLinkingState(designer));
                designer.repaint();
            } else {
                designer.clearSelection();
            }
        } else if (SwingUtilities.isRightMouseButton(e)) {
            if (getSelectedBrick() != null) {
                designer.selectAgent(getSelectedBrick());
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
            if (getSelectedBrick() != null) {
                designer.setSelectedBrick(getSelectedBrick());
                designer.setState(new BrickDraggingState(designer));
                designer.setSelectedComponentDragOffset(new Point(e.getX() - getSelectedBrick().getX(), e.getY() - getSelectedBrick().getY()));
                designer.repaint();
            }
        }
    }

    @Override
    protected void handleMouseReleased(MouseEvent e) {
        handleMouseClicked(e);
    }
}
