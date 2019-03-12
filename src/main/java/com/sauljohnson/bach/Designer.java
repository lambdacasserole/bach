package com.sauljohnson.bach;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;

/**
 * A Multi-Agent-System network designer for Denobo.
 *
 * @author  Saul Johnson, Alex Mullen, Lee Oliver
 */
public class Designer extends JComponent {

    /**
     * The number of pixels each grid line is separated by.
     */
    private int gridSpacing = 15;
    
    /**
     * The colour of each grid line.
     */
    private final Color gridLineColor = new Color(0, 0, 0, 25);   
    
    
    // Selection line constants.
    
    /**
     * The colour of the selection box that appears around a selected agent.
     */
    private final Color selectionBoundingBoxColor = new Color(0, 0, 0, 100);

    public Rectangle getLinkingTagBounds() {
        return linkingTagBounds;
    }

    private Rectangle linkingTagBounds;

    public boolean isInLinkingTagBounds(Point p) {
        return linkingTagBounds != null && linkingTagBounds.contains(p);
    }

    /**
     * The dash to use for creating a dash effect for the selection box border.
     */
    private final float[] selectionBoundingBoxDash = new float[] {2.0f};
    
    /**
     * The stroke to use for rendering the selection box around a selected agent.
     */
    private final BasicStroke selectionBoundingBoxStroke = new BasicStroke(1.0f, 
            BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10.0f, selectionBoundingBoxDash, 0.0f);
   
    
    // Menu for right-clicking on empty space.

    public JPopupMenu getEmptySpacePopup() {
        return emptySpacePopup;
    }

    /**
     * The pop-up menu that appears when some empty space is right-clicked on. 
     */
    private final JPopupMenu emptySpacePopup;

    // Collections to hold the data for this designer.
    
    /**
     * The list of Brick instances currently on this designer.
     */
    private final List<Brick> agents;
    
    /**
     * The list of DesignerEventListeners that are observing this designer.
     */
    private final List<DesignerEventListener> designerEventListeners;

    public List<DesignerEventListener> getDesignerEventListeners() {
        return Collections.unmodifiableList(designerEventListeners);
    }
    
    // State data to save in-between events.

    public Point getLastMenuClickPosition() {
        return lastMenuClickPosition;
    }

    public void setLastMenuClickPosition(Point lastMenuClickPosition) {
        this.lastMenuClickPosition = lastMenuClickPosition;
    }

    /**
     * The last right-click position so we know where to place agents in the
     * designer on creation of one.
     */
    private Point lastMenuClickPosition;

    public Point getSelectedComponentDragOffset() {
        return selectedComponentDragOffset;
    }

    public void setSelectedComponentDragOffset(Point selectedComponentDragOffset) {
        this.selectedComponentDragOffset = selectedComponentDragOffset;
    }

    /**
     * The offset of the cursor relative to the initial click on a component 
     * before dragging.
     */
    private Point selectedComponentDragOffset;

    public DesignerState getState() {
        return state;
    }

    public void setState(DesignerState state) {
        this.state = state;
        repaint();
    }

    /**
     * The current state this designer is in.
     */
    private DesignerState state;

    public Brick getSelectedBrick() {
        return selectedBrick;
    }

    public void setSelectedBrick(Brick selectedBrick) {
        this.selectedBrick = selectedBrick;
    }

    /**
     * The current agent that is selected or null if none are selected.
     */
    private Brick selectedBrick;

    public boolean isSelectedAgentTryingToLink() {
        return state instanceof BrickLinkingState;
    }

    public boolean hasEmptySpacePopup() {
        return emptySpacePopup != null;
    }

    /**
     * Whether or not the grid (and snap-to-grid features) are currently enabled.
     */
    private boolean showGrid = true;


    /**
     * Creates a new Designer instance for designing and configuring a network of agents.
     */
    public Designer(JPopupMenu emptySpacePopup) {

        super();
 
        this.setBackground(Color.WHITE);

        agents = new ArrayList<>();
        designerEventListeners = new ArrayList<>();

        // Set empty space popup.
        this.emptySpacePopup = emptySpacePopup;

        // Default state by default.
        state = new DefaultState(this);

        // Delegate mouse events to state.
        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                state.handleMouseClicked(e);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                state.handleMousePressed(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                state.handleMouseReleased(e);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                state.handleMouseEntered(e);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                state.handleMouseExited(e);
            }
        });

        // Delegate mouse motion events to state.
        this.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                state.handleMouseDragged(e);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                state.handleMouseMoved(e);
            }
        });
    }

    /**
     * Gets whether or not the grid (and snap-to-grid features) are currently enabled in the designer.
     *
     * @return true if the grid is set to showDialog otherwise false.
     */
    public boolean isShowGrid() {
        return showGrid;
    }

    /**
     * Sets whether or not the grid (and snap-to-grid features) are currently enabled in the designer.
     *
     * @param showGrid Whether or not to showDialog the grid and enable snap-to-grid features.
     */
    public void setShowGrid(boolean showGrid) {
        this.showGrid = showGrid;
        this.repaint();
    }

    public int getGridSpacing() {
        return gridSpacing;
    }

    public void setGridSpacing(int gridSpacing) {
        this.gridSpacing = gridSpacing;
        this.repaint();
    }

    /**
     * Adds a listener class to the designer, which receives all design events triggered by the user.
     *
     * @param listener The listener object to add.
     */
    public void addDesignerEventListener(DesignerEventListener listener) {
        designerEventListeners.add(listener);
    }

    /**
     * Returns the list of Brick objects that visually represent the
     * agents in the designer.
     * 
     * @return The list of Brick objects in this designer.
     */
    public List<Brick> getBricks() {
        return agents;
    }

    public void addBrick(Brick g) {
        agents.add(g);
        this.repaint();
    }

    /**
     * Returns the top-most agent (if any) at a particular position.
     * 
     * @param point     The position to check.
     * @return          The agent selected or null is returned if there is no
     *                  agent at that position.
     */
    public Brick getBrickAt(Point point) {
        /* Start at the end of the array since the agents at the end will be the ones that are rendered last, thus are
         * on top.
         */
        for (int i = (agents.size() - 1); i >= 0; i--) {
            if (agents.get(i).getBounds().contains(point)) {
                return agents.get(i);
            }
        }
        return null;
    }
    
    /**
     * Shutdown and removes the specified Brick from the network.
     * 
     * @param agent The agent to shutdown and remove.
     */
    public void removeAgent(Brick agent) {
        agent.removeAllConnections();
        agents.remove(agent);
        
        if (selectedBrick == agent) {
            clearSelection();
        }
        
        this.repaint();
        
        for (DesignerEventListener currentListener : designerEventListeners) {
            currentListener.agentDeleted(agent);
        }
        
    }
    
    /**
     * Shutdown and removes the current selected agent if one is selected.
     */
    public void removeSelectedAgent() {
        
        if (selectedBrick != null) {
            removeAgent(selectedBrick);
        }
        
    }
    
    /**
     * Sets the specified brick as the one selected.
     * 
     * @param brick The brick to set as the selected brick.
     */
    public void selectBrick(Brick brick) {
        selectedBrick = brick;
        this.repaint();
        
        for (DesignerEventListener currentListener : designerEventListeners) {
            currentListener.brickSelected(brick);
        }
    }

    /** 
     * Clears the current selected agent so that nothing is selected.
     */
    public void clearSelection() {
        selectedBrick = null;
        this.repaint();
        
        for (DesignerEventListener currentListener : designerEventListeners) {
            currentListener.selectionCleared();
        }
    }

    /**
     * Performs a repaint of this designer component.
     * 
     * @param g The graphics context we are going to paint to.
     */
    protected void paintComponent(Graphics2D g) {

        // Fill in background.
        g.setColor(this.getBackground());
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        
        if (showGrid) { // Are we drawing a grid?
        
            g.setColor(gridLineColor);

            for (int x = 0; x < this.getWidth(); x += gridSpacing) { // Draw vertical gridlines.
                g.drawLine(x, 0, x, this.getHeight());
            }
            
            for (int y = 0; y < this.getHeight(); y += gridSpacing) { // Draw horizontal gridlines.
                g.drawLine(0, y, this.getWidth(), y);
            }
            
        }

        if (isSelectedAgentTryingToLink()) {    // Are we dragging a link around?
            
            final int x = selectedBrick.getBounds().x + (selectedBrick.getBounds().width / 2);
            final int y = selectedBrick.getBounds().y + (selectedBrick.getBounds().height / 2);
            
            g.setColor(Color.black);
            g.drawLine(x, y, this.getMousePosition().x, this.getMousePosition().y);
            
        }
        
        for (Brick CurrentAgent : agents) {
            final int sourceX = (CurrentAgent.getBounds().x + (CurrentAgent.getBounds().width / 2));
            final int sourceY = (CurrentAgent.getBounds().y + (CurrentAgent.getBounds().height / 2));
            for (Object otherAgent : CurrentAgent.getConnections()) {
Brick gg = (Brick) otherAgent;
                final int destX = (gg.getBounds().x + (gg.getBounds().width / 2));
                final int destY = (gg.getBounds().y + (gg.getBounds().height / 2));

                g.setColor(Color.BLACK); // TODO: Break this out.
                g.drawLine(sourceX, sourceY, destX, destY);
            }
        }

        for (Brick currentAgent : agents) {
            g.drawImage(currentAgent.getImage(), currentAgent.getX(), currentAgent.getY(), null);
        }
        
        if (selectedBrick != null) {
            
//            selectedBrick.draw(g);
            
            // Dotted bounding box on selected agent.
            final Rectangle agentBounds = selectedBrick.getBounds();
            g.setColor(Color.WHITE);
            linkingTagBounds = new Rectangle((int) agentBounds.getX() - 10, (int) agentBounds.getY() - 10, 10, 10);
            g.fillRect(linkingTagBounds.x, linkingTagBounds.y, linkingTagBounds.width, linkingTagBounds.height);
            g.setColor(Color.BLACK);
            g.drawRect(linkingTagBounds.x, linkingTagBounds.y, linkingTagBounds.width, linkingTagBounds.height);
            g.setColor(selectionBoundingBoxColor);
            g.setStroke(selectionBoundingBoxStroke);
            g.draw(agentBounds);

        }
        
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        
        super.paintComponent(g);
        paintComponent((Graphics2D) g); // We need Graphics2D for advanced drawing methods.

    }
}
