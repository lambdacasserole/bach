package com.sauljohnson.bach;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class DefaultState extends DesignerState {

    public DefaultState(Designer designer){
        super(designer);
    }

    @Override
    protected void handleMousePressed(MouseEvent e) {
        System.out.println("Mouse pressed in default state.");

        // Linking tag left-clicked, enter linking mode.
        if (SwingUtilities.isLeftMouseButton(e) && designer.isInLinkingTagBounds(e.getPoint())) {
            designer.setState(new BrickLinkingState(designer));
            return;
        }

        final Brick brickClicked = designer.getBrickAt(e.getPoint());
        if (brickClicked != null) {
            designer.selectBrick(brickClicked); // Brick clicked, select it.
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

            // Empty space right-clicked, show context menu for designer.
            if (SwingUtilities.isRightMouseButton(e) && designer.hasEmptySpacePopup()) {
                designer.setLastMenuClickPosition(e.getPoint());
                designer.getEmptySpacePopup().show(e.getComponent(), e.getX(), e.getY());
            }
        }
    }
}