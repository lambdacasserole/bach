package com.sauljohnson.bach;

/**
 * An enumeration of different types of designer state.
 *
 * @author Saul Johnson
 */
public enum DesignerStateType {

    /**
     * Represents the default (idle) designer state.
     */
    DEFAULT,

    /**
     * Represents the designer state in which a brick is being dragged.
     */
    BRICK_DRAGGING,

    /**
     * Represents the designer state in which a brick is being linked to another.
     */
    BRICK_LINKING
}
