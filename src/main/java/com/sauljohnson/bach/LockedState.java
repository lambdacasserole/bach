package com.sauljohnson.bach;

/**
 * Represents a state in which a {@link Designer} is locked and cannot be modified.
 *
 * @author  Saul Johnson, Alex Mullen, Lee Oliver
 */
public class LockedState extends DesignerState {

    /**
     * Initialises a new instance of a state in which a designer is locked and cannot be modified.
     *
     * @param designer  the designer this state relates to
     */
    LockedState(Designer designer){
        super(designer);
    }

    @Override
    DesignerStateType getType() {
        return DesignerStateType.LOCKED;
    }
}