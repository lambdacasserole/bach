package com.sauljohnson.bach;

/**
 * Represents an abstract state in which a {@link Brick} is selected in a {@link Designer}.
 *
 * @author  Saul Johnson, Alex Mullen, Lee Oliver
 */
public abstract class BrickSelectedDesignerState extends DesignerState {

    protected BrickSelectedDesignerState(Designer designer) {
        super(designer);
    }

    /**
     * Gets the selected brick in the designer.
     *
     * @return  the selected brick in the designer
     */
    public Brick getSelectedBrick() {
        return designer.getSelectedBrick();
    }
}
