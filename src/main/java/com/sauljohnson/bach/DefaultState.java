package com.sauljohnson.bach;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Represents a state in which neither a linking or dragging operation is taking place in a {@link Designer}.
 *
 * @author  Saul Johnson, Alex Mullen, Lee Oliver
 */
public class DefaultState extends DesignerState {

    /**
     * Initialises a new instance of a state in which neither a linking or dragging operation is taking place in a
     * designer.
     *
     * @param designer  the designer this state relates to
     */
    DefaultState(Designer designer){
        super(designer);
    }

    @Override
    DesignerStateType getType() {
        return DesignerStateType.DEFAULT;
    }

    @Override
    protected void handleMousePressed(MouseEvent e) {

        // Linking tag left-clicked, enter linking mode.
        if (designer.isInLinkingTagBounds(e.getPoint())) {
            if (SwingUtilities.isLeftMouseButton(e)) {
                designer.setState(new BrickLinkingState(designer));
            } else if (SwingUtilities.isRightMouseButton(e)) {
                designer.setState(new BrickUnlinkingState(designer));
            }
            return;
        }

        final Brick brickClicked = designer.getBrickAt(e.getPoint());
        if (brickClicked != null) {
            designer.setSelectedBrick(brickClicked); // Brick clicked, select it.
            designer.setSelectedComponentDragOffset(new Point(e.getX() - brickClicked.getX(),
                    e.getY() - brickClicked.getY()));

            // Which mouse button?
            if (SwingUtilities.isLeftMouseButton(e)) {

                // Left-click enters dragging mode.
                designer.setState(new BrickDraggingState(designer));
            } else if (SwingUtilities.isRightMouseButton(e) && brickClicked.hasContextMenu()) {

                // Brick right-clicked, show its context menu.
                designer.setLastMenuClickPosition(e.getPoint());
                brickClicked.getContextMenu().show(e.getComponent(), e.getX(), e.getY());
            }
        } else {

            // Deselect all bricks.
            designer.clearSelection();

            // Empty space right-clicked, show context menu for designer.
            if (SwingUtilities.isRightMouseButton(e) && designer.hasEmptySpacePopup()) {
                designer.setLastMenuClickPosition(e.getPoint());
                designer.getEmptySpacePopup().show(e.getComponent(), e.getX(), e.getY());
            }
        }
    }
}