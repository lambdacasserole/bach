package com.sauljohnson.bach;

/**
 * Represents an event raised by a {@link Designer} control.
 *
 * @author  Saul Johnson, Alex Mullen, Lee Oliver
 */
public class DesignerEvent {

    /**
     * The brick that triggered the event.
     */
    private final Brick source;
    
    /**
     * Initialises a new instance of a designer event.
     * 
     * @param source    the brick that triggered the event
     */
    public DesignerEvent(Brick source) {
        this.source = source;
    }
        
    /**
     * Gets the brick that triggered the event in the designer.
     * 
     * @return  the brick that triggered the event
     */
    public Brick getSource() {
        return source;
    }
}
