package com.sauljohnson.bach;

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
    protected void handleMouseMoved(MouseEvent e) {
        designer.repaint();
    }

    @Override
    protected void handleMousePressed(MouseEvent e) {

        // If brick was clicked.
        final Brick brickClicked = designer.getBrickAt(e.getPoint());
        if (brickClicked != null) {

            // Make link if supported and does not exist already.
            if (getSelectedBrick().canConnect(brickClicked)
                    && !getSelectedBrick().hasConnection(brickClicked)) {
                getSelectedBrick().addConnection(brickClicked);
            }
            designer.selectBrick(brickClicked);
        }

        // Revert state to default (i.e. leave linking state).
        designer.setState(new DefaultState(designer));
    }
}
