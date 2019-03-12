package com.sauljohnson.bach;

import java.awt.event.MouseEvent;

/**
 * A class that represents a state where an agent is currently being dragged
 * in the designer.
 */
public class BrickDraggingState extends DesignerState {

    public BrickDraggingState(Designer designer) {
        super(designer);
    }

    @Override
    protected void handleMouseDragged(MouseEvent e) {
        int newX = (e.getX() - designer.getSelectedComponentDragOffset().x);
        int newY = (e.getY() - designer.getSelectedComponentDragOffset().y);

        if (designer.isShowGrid()) {
            newX = newX - (newX % designer.getGridSpacing());
            newY = newY - (newY % designer.getGridSpacing());
        }

        designer.getAgentSelected().setX(newX);
        designer.getAgentSelected().setY(newY);
        designer.repaint();

        for (DesignerEventListener currentListener : designer.getDesignerEventListeners()) {
            currentListener.agentMoved(designer.getAgentSelected());
        }
    }

    @Override
    protected void handleMouseReleased(MouseEvent e) {
        designer.setState(new BrickSelectedState(designer));
    }
}
