package com.sauljohnson.bach;

/**
 * Represents an event raised by a NetworkDesigner control.
 *
 * @author  Saul Johnson, Alex Mullen, Lee Oliver
 */
public class DesignerEvent {

    /**
     * The AgentDisplayable that triggered the event.
     */
    private final Brick source;
    
    /**
     * Initialises a new instance of a designer event.
     * 
     * @param source The AgentDisplayable that triggered the event.
     */
    public DesignerEvent(Brick source) {
    
        this.source = source;
    
    }
        
    /**
     * Gets the AgentDisplayable that triggered the event in the designer.
     * 
     * @return The AgentDisplayable that triggered the event.
     */
    public Brick getSource() {
        
        return source;
        
    }

}
