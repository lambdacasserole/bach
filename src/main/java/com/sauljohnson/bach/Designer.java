package com.sauljohnson.bach;

import centralcommand.designer.dialogs.AgentPropertiesDialog;
import centralcommand.designer.dialogs.CreateAgentDialog;
import centralcommand.designer.dialogs.AgentConnectionsDialog;
import centralcommand.designer.dialogs.AgentRoutingDialog;
import denobo.Agent;
import denobo.socket.SocketAgent;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

/**
 * A Multi-Agent-System network designer for Denobo.
 *
 * @author  Saul Johnson, Alex Mullen, Lee Oliver
 */
public class Designer extends JComponent implements ActionListener {
    
    // Grid line constants.
    
    /**
     * The number of pixels each grid line is separated by.
     */
    private final int gridSpacing = 15;
    
    /**
     * The colour of each grid line.
     */
    private final Color gridLineColor = new Color(0, 0, 0, 25);   
    
    
    // Selection line constants.
    
    /**
     * The colour of the selection box that appears around a selected agent.
     */
    private final Color selectionBoundingBoxColor = new Color(0, 0, 0, 100);
    
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
     * The list of BrickLink instances currently on this designer.
     */
    private final List<BrickLink> BrickLinks;
    
    /**
     * The list of DesignerEventListeners that are observing this designer.
     */
    private final List<DesignerEventListener> designerEventListeners;
    
    
    // State data to save in-between events.
    
    /**
     * The last right-click position so we know where to place agents in the
     * designer on creation of one.
     */
    private Point lastMenuClickPosition;
    
    /** 
     * The offset of the cursor relative to the initial click on a component 
     * before dragging.
     */
    private Point selectedComponentDragOffset;
    
    /**
     * The current state this designer is in.
     */
    private DesignerState state;
    
    /**
     * The current agent that is selected or null if none are selected.
     */
    private Brick agentSelected;
    
    /**
     * Holds whether the selected agent is currently in "link" mode where a link
     * is being dragged from it.
     */
    private boolean isSelectedAgentTryingToLink;
    
    /**
     * Whether or not the grid (and snap-to-grid features) are currently enabled.
     */
    private boolean showGrid = true;


    /**
     * Creates a new Designer instance for designing and configuring a 
     * network of agents.
     */
    @SuppressWarnings("LeakingThisInConstructor")
    public Designer() {

        super();
 
        this.setBackground(Color.WHITE);

        agents = new ArrayList<>();
        BrickLinks = new ArrayList<>();
        designerEventListeners = new ArrayList<>();
        
        agentSelectedPopup = new JPopupMenu();
        
        menuOptionLink = new JMenuItem("Link");
        menuOptionLink.addActionListener(this);
        agentSelectedPopup.add(menuOptionLink);
       
        agentSelectedPopup.addSeparator();
        
        menuOptionsConnections = new JMenuItem("Connections");
        menuOptionsConnections.addActionListener(this);
        agentSelectedPopup.add(menuOptionsConnections);
        
        menuOptionMonitor = new JMenuItem("Monitor");
        menuOptionMonitor.addActionListener(this);
        agentSelectedPopup.add(menuOptionMonitor);
        
        menuOptionDebugWindow = new JMenuItem("Debug Window");
        menuOptionDebugWindow.addActionListener(this);
        agentSelectedPopup.add(menuOptionDebugWindow);
        
        agentSelectedPopup.addSeparator();
        
        menuOptionDelete = new JMenuItem("Delete");
        menuOptionDelete.addActionListener(this);
        agentSelectedPopup.add(menuOptionDelete);
        
        menuOptionRouting = new JMenuItem("Routing");
        menuOptionRouting.addActionListener(this);
        agentSelectedPopup.add(menuOptionRouting);
        
        agentSelectedPopup.addSeparator();
        
        menuOptionProperties = new JMenuItem("Properties");
        menuOptionProperties.addActionListener(this);
        agentSelectedPopup.add(menuOptionProperties);
        
        emptySpacePopup = new JPopupMenu();
        
        menuOptionCreateAgent = new JMenuItem("Create Agent");
        menuOptionCreateAgent.addActionListener(this);
        emptySpacePopup.add(menuOptionCreateAgent);
        
        state = new DefaultState();
        
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
     * Sets whether or not the grid (and snap-to-grid features) are currently enabled in the designer.
     *
     * @param showGrid Whether or not to showDialog the grid and enable snap-to-grid features.
     */
    public void setShowGrid(boolean showGrid) {
    
        this.showGrid = showGrid;
        
    }
    
    /**
     * Gets whether or not the grid (and snap-to-grid features) are currently enabled in the designer.
     * 
     * @return true if the grid is set to showDialog otherwise false.
     */
    public boolean getShowGrid() {
    
        return showGrid;
        
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
     * Returns the list of BrickLink objects that visually represent the links
     * between agents.
     * 
     * @return The list of BrickLink objects in this designer.
     */
    public List<BrickLink> getBrickLinks() {
        
        return BrickLinks;
        
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
    
    /**
     * Returns the top-most agent (if any) at a particular position.
     * 
     * @param point     The position to check.
     * @return          The agent selected or null is returned if there is no
     *                  agent at that position.
     */
    private Brick getAgentAt(Point point) {
        
        /* 
         * Start at the end of the array since the agents at the end will be the
         * ones that are rendered last, thus are on top.
         */
        for (int i = (agents.size() - 1); i >= 0; i--) {
            if (agents.get(i).getBounds().contains(point)) {
                return agents.get(i);
            }
        }
        
        return null;
        
    }
    
    /**
     * Returns whether two Brick's are already linked in the designer.
     * 
     * @param agent1    the first agent
     * @param agent2    the second agent
     * @return          true if they are already linked, otherwise false
     */
    private boolean linkAlreadyExistsBetween(Brick agent1, Brick agent2) {
        
        for (BrickLink currentBrickLink : BrickLinks) {
            if (currentBrickLink.contains(agent1, agent2)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Links two Brick instances together in this Designer.
     * <p>
     * If the two agents are the same or the two agents are already linked, this
     * method will not link them.
     * 
     * @param agent1 the first agent to link
     * @param agent2 the second agent to link
     */
    private void linkAgents(Brick agent1, Brick agent2) {
        
        if ((agent1 == agent2) || (linkAlreadyExistsBetween(agent1, agent2))) {
            return;
        }
        
        final BrickLink link = new BrickLink(agent1, agent2);
        BrickLinks.add(link);
        
        for (DesignerEventListener currentListener : designerEventListeners) {
            currentListener.linkCreated(link);
        }
        
    }
    
    /**
     * Shutdown and removes the specified Brick from the network.
     * 
     * @param agent The agent to shutdown and remove.
     */
    public void removeAgent(Brick agent) {
        
        agent.getDebugWindow().dispose();
        agent.getMonitorDialog().dispose();
        
        agent.getModelObject().shutdown();
        
        agents.remove(agent);
        removeAnyLinksContaining(agent);
        
        if (agentSelected == agent) {
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
        
        if (agentSelected != null) {
            removeAgent(agentSelected);
        }
        
    }
    
    /**
     * Removes any BrickLink's containing the specified agent.
     * 
     * @param agent The agent to remove any links to.
     */
    public void removeAnyLinksContaining(Brick agent) {

        final Iterator<BrickLink> iter = BrickLinks.iterator();
        while (iter.hasNext()) {
            
            final BrickLink currentBrickLink = iter.next();
            if (currentBrickLink.contains(agent)) {
                
                currentBrickLink.breakLink();
                iter.remove();
                
                for (DesignerEventListener currentListener : designerEventListeners) {
                    currentListener.linkDeleted(currentBrickLink);
                }
                
            }
            
        }

        this.repaint();

    }
    
    /**
     * Removes any BrickLink's between the specified two agents.
     * 
     * @param agent1 The first agent.
     * @param agent2 The second agent.
     */
    public void removeAnyLinksContaining(Brick agent1, Brick agent2) {
        
        final Iterator<BrickLink> iter = BrickLinks.iterator();
        while (iter.hasNext()) {
            
            final BrickLink currentBrickLink = iter.next();
            if (currentBrickLink.contains(agent1, agent2)) {
                
                currentBrickLink.breakLink();
                iter.remove();
                
                for (DesignerEventListener currentListener : designerEventListeners) {
                    currentListener.linkDeleted(currentBrickLink);
                }
                
            }
            
        }

        this.repaint();

    }
    
    /**
     * Set's the specified agent as the one selected.
     * 
     * @param agent The agent to set as the selected agent.
     */
    public void selectAgent(Brick agent) {
        
        agentSelected = agent;
        state = new AgentSelectedState();
        this.repaint();
        
        for (DesignerEventListener currentListener : designerEventListeners) {
            currentListener.agentSelected(agent);
        }
        
    }
    
    /** 
     * Clears the current selected agent so that nothing is selected.
     */
    public void clearSelection() {

        agentSelected = null;
        state = new DefaultState();
        this.repaint();
        
        for (DesignerEventListener currentListener : designerEventListeners) {
            currentListener.selectionCleared();
        }
        
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        
        if (e.getSource() == menuOptionCreateAgent) {
            
            createAgentDialog = new CreateAgentDialog();
            final Agent agentToAdd = createAgentDialog.showDialog(lastMenuClickPosition);
            if (agentToAdd != null) {

                if (agentToAdd instanceof SocketAgent) {
                          
                    agents.add(new SocketBrick((SocketAgent) agentToAdd, 
                                lastMenuClickPosition.x - (Brick.WIDTH / 2), 
                                lastMenuClickPosition.y - (Brick.HEIGHT / 2)));
                
                } else {
                    
                    agents.add(new Brick(agentToAdd, 
                                lastMenuClickPosition.x - (Brick.WIDTH / 2), 
                                lastMenuClickPosition.y - (Brick.HEIGHT / 2)));

                }

                this.repaint();

            }
            
        } else if (e.getSource() == menuOptionsConnections) {
            
            agentConnectionsDialog.showDialog(agentSelected.getBounds().getLocation(), agentSelected);

        } else if (e.getSource() == menuOptionMonitor) {
            
            agentSelected.getMonitorDialog().showAt(agentSelected.getBounds().getLocation());
            
        } else if (e.getSource() == menuOptionDebugWindow) {
                    
            agentSelected.getDebugWindow().showAt(this, lastMenuClickPosition);
            
        } else if (e.getSource() == menuOptionDelete) {
            
            removeAgent(agentSelected);
            
        } else if (e.getSource() == menuOptionRouting) {
            
            new AgentRoutingDialog().show(agentSelected.getModelObject(), lastMenuClickPosition);
            
        } else if (e.getSource() == menuOptionProperties) {
            
            agentPropertiesDialog.showDialog(agentSelected.getBounds().getLocation(), agentSelected.getModelObject());

        } else if (e.getSource() == menuOptionLink) {

            isSelectedAgentTryingToLink = true;
            state = new BrickLinkingState();
            this.repaint();
            
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

        if (isSelectedAgentTryingToLink) {    // Are we dragging a link around?
            
            final int x = agentSelected.getBounds().x + (agentSelected.getBounds().width / 2);
            final int y = agentSelected.getBounds().y + (agentSelected.getBounds().height / 2);
            
            g.setColor(Color.black);
            g.drawLine(x, y, this.getMousePosition().x, this.getMousePosition().y);
            
        }
        
        for (BrickLink currentLink : BrickLinks) {
            currentLink.draw(g);
        }
        
        for (Brick currentAgent : agents) {
            if (currentAgent != agentSelected) {
                currentAgent.draw(g);
            }
        }
        
        if (agentSelected != null) { 
            
            agentSelected.draw(g);
            
            // Dotted bounding box on selected agent.
            final Rectangle agentBounds = agentSelected.getBounds();
            g.setColor(Color.WHITE);
            g.fillRect((int) agentBounds.getX() - 10, (int) agentBounds.getY() - 10, 10, 10);
            g.setColor(Color.BLACK);
            g.drawRect((int) agentBounds.getX() - 10, (int) agentBounds.getY() - 10, 10, 10);
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
    
    /**
     * A class that represents a state that a Designer object can be in.
     */
    private abstract class DesignerState {
        
        /**
         * Invoked when the mouse button has been clicked (pressed and released)
         * on the designer.
         * 
         * @param e     event data
         */
        protected void handleMouseClicked(MouseEvent e) { }
        
        /**
         * Invoked when a mouse button is pressed on the designer and then
         * dragged.
         * 
         * @param e     event data
         */
        protected void handleMouseDragged(MouseEvent e) { }
        
        /**
         * Invoked when the mouse cursor has been moved over the designer area
         * but no buttons have been pushed.
         * 
         * @param e     event data
         */
        protected void handleMouseMoved(MouseEvent e) { }
        
        /**
         * Invoked when a mouse button has been pressed on the designer area.
         * 
         * @param e     event data
         */
        protected void handleMousePressed(MouseEvent e) { }
        
        /**
         * Invoked when a mouse button has been released on the designer area.
         * 
         * @param e     event data
         */
        protected void handleMouseReleased(MouseEvent e) { }
        
        /**
         * Invoked when the mouse enters the designer area.
         * 
         * @param e     event data
         */
        protected void handleMouseEntered(MouseEvent e) { }
        
        /**
         * Invoked when the mouse exits the designer area.
         * 
         * @param e     event data
         */
        protected void handleMouseExited(MouseEvent e) { }
        
    }
    
    ////////////////////////////////////////////////////////////////////////////
    
    /**
     * A class that represents the default state where nothing is really currently 
     * happening. (Nothing selected, nothing getting dragged or nothing linking)
     */
    private class DefaultState extends DesignerState {

        @Override
        protected void handleMouseClicked(MouseEvent e) {

            if (SwingUtilities.isLeftMouseButton(e)) {
                
                final Brick agentClicked = getAgentAt(e.getPoint());
                
                if (agentClicked != null) {
                    selectAgent(agentClicked);
                }
                
            } else if (SwingUtilities.isRightMouseButton(e)) {
                
                lastMenuClickPosition = e.getPoint();
                emptySpacePopup.show(e.getComponent(), e.getX(), e.getY());
                
            }

        }

        @Override
        protected void handleMousePressed(MouseEvent e) {

            if (SwingUtilities.isLeftMouseButton(e)) {
                
                final Brick agentClicked = getAgentAt(e.getPoint());
                
                if (agentClicked != null) {
                    
                    agentSelected = agentClicked;
                    state = new AgentDraggingState();
                    selectedComponentDragOffset = new Point(e.getX() - agentSelected.getBounds().x, e.getY() - agentSelected.getBounds().y);
                    Designer.this.repaint();

                }               
                
            }

        }

        @Override
        protected void handleMouseReleased(MouseEvent e) {
            
            if (SwingUtilities.isRightMouseButton(e)) {
                
                final Brick agentClicked = getAgentAt(e.getPoint());
                
                if (agentClicked != null) {
                    
                    selectAgent(agentClicked);

                    lastMenuClickPosition = e.getPoint();
                    agentSelectedPopup.show(e.getComponent(), e.getX(), e.getY());
                    
                } else {
                    
                    lastMenuClickPosition = e.getPoint();
                    emptySpacePopup.show(e.getComponent(), e.getX(), e.getY());
                    
                }
  
            }

        }

    }

    ////////////////////////////////////////////////////////////////////////////
    
    /**
     * A class that represents a state where an agent has been selected.
     */
    private class AgentSelectedState extends DesignerState {

        @Override
        protected void handleMouseClicked(MouseEvent e) {

            final Brick agentClicked = getAgentAt(e.getPoint());
            
            if (SwingUtilities.isLeftMouseButton(e)) {
                
                if (agentClicked != null) {
                    selectAgent(agentClicked);
                } else {
                    clearSelection();
                }
                
            } else if (SwingUtilities.isRightMouseButton(e)) {
                
                if (agentClicked != null) {
                    
                    selectAgent(agentClicked);
                    
                    lastMenuClickPosition = e.getPoint();
                    agentSelectedPopup.show(e.getComponent(), e.getX(), e.getY());
                    
                } else {
                    
                    clearSelection();
                    
                    lastMenuClickPosition = e.getPoint();
                    emptySpacePopup.show(e.getComponent(), e.getX(), e.getY());
                    
                }
                
            }

        }

        @Override
        protected void handleMousePressed(MouseEvent e) {
            
            if (SwingUtilities.isLeftMouseButton(e)) {
                
                final Brick agentClicked = getAgentAt(e.getPoint());
                
                if (agentClicked != null) {
                    
                    agentSelected = agentClicked;
                    state = new AgentDraggingState();
                    selectedComponentDragOffset = new Point(e.getX() - agentSelected.getBounds().x, e.getY() - agentSelected.getBounds().y);
                    Designer.this.repaint();

                }               
                
            }
            
        }

        @Override
        protected void handleMouseReleased(MouseEvent e) {

            handleMouseClicked(e);

        }

    }
    
    ////////////////////////////////////////////////////////////////////////////
    
    /**
     * A class that represents a state where an agent is currently being dragged
     * in the designer.
     */
    private class AgentDraggingState extends DesignerState {

        @Override
        protected void handleMouseDragged(MouseEvent e) {
            
            int newX = (e.getX() - selectedComponentDragOffset.x);
            int newY = (e.getY() - selectedComponentDragOffset.y);

            if (showGrid) {
                newX = newX - (newX % gridSpacing);
                newY = newY - (newY % gridSpacing);
            }

            agentSelected.getBounds().x = newX;
            agentSelected.getBounds().y = newY;
            Designer.this.repaint();
            
            for (DesignerEventListener currentListener : designerEventListeners) {
                currentListener.agentMoved(agentSelected);
            }

        }
        
        @Override
        protected void handleMouseReleased(MouseEvent e) {
            
            state = new AgentSelectedState();
            
        }

    }
    
    ////////////////////////////////////////////////////////////////////////////
    
    /**
     * A class that represents a state where there is a link attached to the
     * selected agent with the other end attached to the mouse cursor position.
     */
    private class BrickLinkingState extends DesignerState {

        @Override
        protected void handleMouseClicked(MouseEvent e) {
            
            if (SwingUtilities.isLeftMouseButton(e)) {
                
                final Brick agentClicked = getAgentAt(e.getPoint());
                
                if (agentClicked != null) {
                    
                    linkAgents(agentSelected, agentClicked);
                    isSelectedAgentTryingToLink = false;
                    selectAgent(agentClicked);

                } else {
                    
                    isSelectedAgentTryingToLink = false;
                    clearSelection();
                    
                }
                
            }

        }

        @Override
        protected void handleMouseDragged(MouseEvent e) {

            Designer.this.repaint();
            
        }

        @Override
        protected void handleMouseMoved(MouseEvent e) {

            Designer.this.repaint();
            
        }

        @Override
        protected void handleMousePressed(MouseEvent e) {

            handleMouseClicked(e);
            
        }

    }

}
