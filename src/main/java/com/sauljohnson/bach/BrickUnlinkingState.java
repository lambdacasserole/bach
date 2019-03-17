package com.sauljohnson.bach;

import java.awt.event.MouseEvent;

/**
 * Represents a state in which a {@link Brick} is currently being unlinked from another in a {@link Designer}.
 *
 * @author  Saul Johnson, Alex Mullen, Lee Oliver
 */
class BrickUnlinkingState extends BrickSelectedDesignerState {

    /**
     * Initialises a new instance of a state in which a brick is currently being unlinked from another in a designer.
     *
     * @param designer  the designer this state relates to
     */
    BrickUnlinkingState(Designer designer){
        super(designer);
    }

    @Override
    DesignerStateType getType() {
        return DesignerStateType.BRICK_UNLINKING;
    }

    @Override
    void handleMouseMoved(MouseEvent e) {
        designer.repaint();
    }

    @Override
    void handleMousePressed(MouseEvent e) {

        // If a connected brick was clicked.
        final Brick brickClicked = designer.getBrickAt(e.getPoint());
        if (brickClicked != null && getSelectedBrick().hasConnection(brickClicked)) {

            // Remove link.
            getSelectedBrick().removeConnection(brickClicked);

            // Inform observers of unlink event.
            for (DesignerEventListener currentListener : designer.getDesignerEventListeners()) {
                currentListener.linkDeleted(getSelectedBrick(), brickClicked);
            }

            // Select brick that was clicked.
            designer.setSelectedBrick(brickClicked);
        } else {

            // Clear selection.
            designer.clearSelection();
        }

        // Revert state to default (i.e. leave linking state).
        designer.setState(new DefaultState(designer));
    }
}
