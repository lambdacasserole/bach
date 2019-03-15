package com.sauljohnson.bach;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Represents a state in which a {@link Brick} is currently being dragged in a {@link Designer}.
 *
 * @author  Saul Johnson, Alex Mullen, Lee Oliver
 */
class BrickDraggingState extends BrickSelectedDesignerState {

    /**
     * Initialises a new instance of a state in which a brick is currently being dragged in a designer.
     *
     * @param designer  the designer this state relates to
     */
    BrickDraggingState(Designer designer) {
        super(designer);
    }

    @Override
    DesignerStateType getType() {
        return DesignerStateType.BRICK_DRAGGING;
    }

    @Override
    void handleMouseDragged(MouseEvent e) {

        // Compute new location.
        int newX = e.getX() - designer.getSelectedComponentDragOffset().x;
        int newY = e.getY() - designer.getSelectedComponentDragOffset().y;

        // Snap to grid?
        if (designer.isShowGrid()) {
            newX = newX - (newX % designer.getGridSpacing());
            newY = newY - (newY % designer.getGridSpacing());
        }

        // Move brick.
        getSelectedBrick().setX(newX);
        getSelectedBrick().setY(newY);
        designer.repaint();

        // Raise events with observers.
        for (DesignerEventListener currentListener : designer.getDesignerEventListeners()) {
            currentListener.brickMoved(getSelectedBrick(), new Point(newX, newY));
        }
    }

    @Override
    void handleMouseReleased(MouseEvent e) {

        // Brick released, revert state.
        designer.setState(new DefaultState(designer));
    }
}
