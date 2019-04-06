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
 * A generic drag-and-drop designer.
 *
 * @author  Saul Johnson, Alex Mullen, Lee Oliver
 */
public class Designer extends JComponent {

    /**
     * The dash to use for creating a dash effect for the selection box border.
     */
    private final float[] selectionBoundingBoxDash = new float[] {2.0f};

    /**
     * The stroke to use for rendering the selection box around a selected brick.
     */
    private final BasicStroke selectionBoundingBoxStroke = new BasicStroke(1.0f,
            BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10.0f, selectionBoundingBoxDash, 0.0f);

    /**
     * The number of pixels each grid line is separated by.
     */
    private int gridSpacing;

    /**
     * The background colour of the designer.
     */
    private Color backgroundColor;

    /**
     * The colour of each grid line.
     */
    private Color gridLineColor;

    /**
     * The colour of the selection box that appears around the selected brick.
     */
    private Color selectionBoundingBoxColor;

    /**
     * The colour of the links between bricks.
     */
    private Color brickLinkColor;

    /**
     * The colour of the line shown when unlinking bricks.
     */
    private Color brickUnlinkingColor;

    /**
     * The colour of the linking tag border.
     */
    private Color linkingTagBorderColor;

    /**
     * The colour of the linking tag background.
     */
    private Color linkingTagBackgroundColor;

    /**
     * The size of the linking tag.
     */
    private int linkingTagSize;

    /**
     * The bounding box of the linking tag attached to the selected control.
     */
    private Rectangle linkingTagBounds;

    /**
     * The pop-up menu that appears when some empty space is right-clicked on. 
     */
    private JPopupMenu emptySpacePopup;

    /**
     * Whether or not the grid (and snap-to-grid features) are currently enabled.
     */
    private boolean showGrid = true;

    /**
     * The list of Brick instances currently on this designer.
     */
    private final List<Brick> bricks;

    /**
     * The current brick that is selected or null if none are selected.
     */
    private Brick selectedBrick;
    
    /**
     * The list of DesignerEventListeners that are observing this designer.
     */
    private final List<DesignerEventListener> designerEventListeners;

    /**
     * The current state this designer is in.
     */
    private DesignerState state;

    /**
     * The last right-click position so we know where to place bricks in the designer on creation of one.
     */
    private Point lastMenuClickPosition;

    /**
     * The offset of the cursor relative to the initial click on a component before dragging.
     */
    private Point selectedComponentDragOffset;

    /**
     * Creates a new Designer instance for designing and configuring a network of bricks.
     *
     * @param emptySpacePopup   the pop-up menu that appears when some empty space is right-clicked on
     */
    public Designer(JPopupMenu emptySpacePopup) {
        super();

        // Set default sizes.
        gridSpacing = 15;
        linkingTagSize = 10;

        // Set default colours.
        backgroundColor = Color.WHITE;
        gridLineColor = new Color(0, 0, 0, 25);
        selectionBoundingBoxColor = new Color(0, 0, 0, 100);
        brickLinkColor = Color.BLACK;
        brickUnlinkingColor = Color.RED;
        linkingTagBorderColor = Color.BLACK;
        linkingTagBackgroundColor = Color.WHITE;

        // Initialise collections.
        bricks = new ArrayList<>();
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
     * Gets the number of pixels each grid line is separated by.
     *
     * @return  the number of pixels
     */
    @SuppressWarnings({"unused", "WeakerAccess"}) // API method, also used internally.
    public int getGridSpacing() {
        return gridSpacing;
    }

    /**
     * Sets the number of pixels each grid line is separated by.
     *
     * @param gridSpacing   the number of pixels
     */
    @SuppressWarnings("unused") // API method.
    public void setGridSpacing(int gridSpacing) {
        this.gridSpacing = gridSpacing;
        repaint();
    }

    /**
     * Gets the background colour of the designer.
     *
     * @return  the background colour
     */
    @SuppressWarnings("unused") // API method.
    public Color getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * Sets the background colour of the designer.
     *
     * @param backgroundColor   the background colour
     */
    @SuppressWarnings("unused") // API method.
    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    /**
     * Gets the colour of each grid line.
     *
     * @return  the colour
     */
    @SuppressWarnings("unused") // API method.
    public Color getGridLineColor() {
        return gridLineColor;
    }

    /**
     * Sets the colour of each grid line.
     *
     * @param gridLineColor the colour
     */
    @SuppressWarnings("unused") // API method.
    public void setGridLineColor(Color gridLineColor) {
        this.gridLineColor = gridLineColor;
        repaint();
    }

    /**
     * Gets the colour of the selection box that appears around the selected brick.
     *
     * @return  the colour
     */
    @SuppressWarnings("unused") // API method.
    public Color getSelectionBoundingBoxColor() {
        return selectionBoundingBoxColor;
    }

    /**
     * Sets the colour of the selection box that appears around the selected brick.
     *
     * @param selectionBoundingBoxColor the colour
     */
    @SuppressWarnings("unused") // API method.
    public void setSelectionBoundingBoxColor(Color selectionBoundingBoxColor) {
        this.selectionBoundingBoxColor = selectionBoundingBoxColor;
        repaint();
    }

    /**
     * Gets the colour of the links between bricks.
     *
     * @return  the colour
     */
    @SuppressWarnings("unused") // API method.
    public Color getBrickLinkColor() {
        return brickLinkColor;
    }

    /**
     * Sets the colour of the links between bricks.
     *
     * @param brickLinkColor    the colour
     */
    @SuppressWarnings("unused") // API method.
    public void setBrickLinkColor(Color brickLinkColor) {
        this.brickLinkColor = brickLinkColor;
    }

    /**
     * Gets the colour of the line shown when unlinking bricks.
     *
     * @return  the colour
     */
    @SuppressWarnings("unused") // API method.
    public Color getBrickUnlinkingColor() {
        return brickUnlinkingColor;
    }

    /**
     * Sets the colour of the line shown when unlinking bricks.
     *
     * @param brickUnlinkingColor   the colour
     */
    @SuppressWarnings("unused") // API method.
    public void setBrickUnlinkingColor(Color brickUnlinkingColor) {
        this.brickUnlinkingColor = brickUnlinkingColor;
    }

    /**
     * Gets the colour of the linking tag border.
     *
     * @return  the colour
     */
    @SuppressWarnings("unused") // API method.
    public Color getLinkingTagBorderColor() {
        return linkingTagBorderColor;
    }

    /**
     * Sets the colour of the linking tag border.
     *
     * @param linkingTagBorderColor the colour
     */
    @SuppressWarnings("unused") // API method.
    public void setLinkingTagBorderColor(Color linkingTagBorderColor) {
        this.linkingTagBorderColor = linkingTagBorderColor;
    }

    /**
     * Gets the colour of the linking tag background.
     *
     * @return  the colour
     */
    @SuppressWarnings("unused") // API method.
    public Color getLinkingTagBackgroundColor() {
        return linkingTagBackgroundColor;
    }

    /**
     * Sets the colour of the linking tag background.
     *
     * @param linkingTagBackgroundColor the colour
     */
    @SuppressWarnings("unused") // API method.
    public void setLinkingTagBackgroundColor(Color linkingTagBackgroundColor) {
        this.linkingTagBackgroundColor = linkingTagBackgroundColor;
    }

    /**
     * Checks whether or not the given point lies within the linking tag bounding box.
     *
     * @param point the point to check
     * @return  true if the point is inside the bounding box of the linking tag, otherwise false
     */
    boolean isInLinkingTagBounds(Point point) {
        return linkingTagBounds != null && linkingTagBounds.contains(point);
    }

    /**
     * Gets the pop-up menu that appears when some empty space is right-clicked on.
     *
     * @return  the pop-up menu
     */
    @SuppressWarnings({"unused", "WeakerAccess"}) // API method, also used internally.
    public JPopupMenu getEmptySpacePopup() {
        return emptySpacePopup;
    }

    /**
     * Sets the pop-up menu that appears when some empty space is right-clicked on.
     *
     * @param emptySpacePopup   the pop-up menu
     */
    @SuppressWarnings("unused") // API method.
    public void setEmptySpacePopup(JPopupMenu emptySpacePopup) {
        this.emptySpacePopup = emptySpacePopup;
    }

    /**
     * Returns true if the designer has an empty space popup configured, otherwise returns false.
     *
     * @return  true if the designer has an empty space popup configured, otherwise false
     */
    boolean hasEmptySpacePopup() {
        return emptySpacePopup != null;
    }

    /**
     * Gets whether or not the grid (and snap-to-grid features) are currently enabled in the designer.
     *
     * @return true if the grid is set to showDialog otherwise false.
     */
    @SuppressWarnings({"unused", "WeakerAccess"}) // API method, also used internally.
    public boolean isShowGrid() {
        return showGrid;
    }

    /**
     * Sets whether or not the grid (and snap-to-grid features) are currently enabled in the designer.
     *
     * @param showGrid Whether or not to showDialog the grid and enable snap-to-grid features.
     */
    @SuppressWarnings("unused") // API method.
    public void setShowGrid(boolean showGrid) {
        this.showGrid = showGrid;
        repaint();
    }

    /**
     * Returns the unmodifiable list of Brick objects that visually represent the bricks in the designer.
     *
     * @return  the unmodifiable list of Brick objects
     */
    @SuppressWarnings("unused") // API method.
    public List<Brick> getBricks() {
        return Collections.unmodifiableList(bricks);
    }

    /**
     * Adds a brick to the designer.
     *
     * @param brick the brick to add
     */
    @SuppressWarnings("unused") // API method.
    public void addBrick(Brick brick) {
        bricks.add(brick);
        repaint();

        // Inform observers of addition event.
        for (DesignerEventListener currentListener : designerEventListeners) {
            currentListener.brickAdded(brick);
        }
    }

    /**
     * Removes the specified brick from the designer.
     *
     * @param brick the brick to remove
     * @throws IllegalArgumentException if the specified brick is not present in the designer
     */
    @SuppressWarnings({"unused", "WeakerAccess"}) // API method, also used internally.
    public void removeBrick(Brick brick) {

        // Check that brick is in the designer.
        if (!bricks.contains(brick)) {
            throw new IllegalArgumentException("Specified brick is not present in the designer so cannot be removed.");
        }

        // Remove all connections from brick and delete from designer.
        brick.removeAllConnections();
        bricks.remove(brick);

        // Clear selection if the selected brick is being deleted.
        if (selectedBrick == brick) {
            clearSelection();
        }

        // Repaint control.
        repaint();

        // Inform observers of deletion event.
        for (DesignerEventListener currentListener : designerEventListeners) {
            currentListener.brickRemoved(brick);
        }
    }

    /**
     * Gets the current brick that is selected or null if none are selected.
     *
     * @return  the selected brick or null if none are selected
     */
    @SuppressWarnings({"unused", "WeakerAccess"}) // API method, also used internally.
    public Brick getSelectedBrick() {
        return selectedBrick;
    }

    /**
     * Removes the currently selected brick if one is selected.
     */
    @SuppressWarnings("unused") // API method.
    public void removeSelectedBrickIfAny() {

        // Remove selected brick if there is one.
        if (selectedBrick != null) {
            removeBrick(selectedBrick);
        }
    }

    /**
     * Sets the current brick that is selected.
     *
     * @param selectedBrick the brick
     * @throws IllegalArgumentException if the specified brick is not present in the designer
     */
    @SuppressWarnings({"unused", "WeakerAccess"}) // API method, also used internally.
    public void setSelectedBrick(Brick selectedBrick) {

        // Check that brick is in the designer.
        if (!bricks.contains(selectedBrick)) {
            throw new IllegalArgumentException("Specified brick is not present in the designer so cannot be selected.");
        }

        // Select brick.
        this.selectedBrick = selectedBrick;
        repaint();

        // Inform observers of selection event.
        for (DesignerEventListener currentListener : designerEventListeners) {
            currentListener.brickSelected(selectedBrick);
        }
    }

    /**
     * Clears the current selected brick so that nothing is selected.
     */
    @SuppressWarnings({"unused", "WeakerAccess"}) // API method, also used internally.
    public void clearSelection() {

        // Deselect selected brick.
        selectedBrick = null;
        repaint();

        // Inform observers of selection clear event.
        for (DesignerEventListener currentListener : designerEventListeners) {
            currentListener.selectionCleared();
        }
    }

    /**
     * Returns the top-most brick (if any) at a particular position.
     *
     * @param point the position to check
     * @return      the brick at a that position or null if there is no brick at that position
     */
    @SuppressWarnings({"unused", "WeakerAccess"}) // API method, also used internally.
    public Brick getBrickAt(Point point) {

        // Start at the end of the array since the bricks at the end will be the ones that are rendered last (on top).
        for (int i = (bricks.size() - 1); i >= 0; i--) {
            if (bricks.get(i).getBounds().contains(point)) {
                return bricks.get(i);
            }
        }
        return null;
    }

    /**
     * Gets all currently registered designer event listeners.
     *
     * @return  an unmodifiable list of designer event listeners
     */
    List<DesignerEventListener> getDesignerEventListeners() {
        return Collections.unmodifiableList(designerEventListeners);
    }

    /**
     * Registers a listener class to the designer, which receives all design events triggered by the user.
     *
     * @param listener  the listener to add
     */
    @SuppressWarnings("unused") // API method.
    public void registerDesignerEventListener(DesignerEventListener listener) {
        designerEventListeners.add(listener);
    }

    /**
     * Unregisters a listener class from the designer.
     *
     * @param listener  the listener to remove
     */
    @SuppressWarnings("unused") // API method.
    public void unregisterDesignerEventListener(DesignerEventListener listener) {
        designerEventListeners.remove(listener);
    }

    /**
     * Sets the current designer state.
     *
     * @param state the designer state
     */
    void setState(DesignerState state) {
        this.state = state;
        repaint();
    }

    /**
     * Gets the last menu click position for the designer.
     *
     * @return  the position
     */
    @SuppressWarnings("unused") // API method.
    public Point getLastMenuClickPosition() {
        return lastMenuClickPosition;
    }

    /**
     * Sets the last menu click position for the designer.
     *
     * @param lastMenuClickPosition the position
     */
    void setLastMenuClickPosition(Point lastMenuClickPosition) {
        this.lastMenuClickPosition = lastMenuClickPosition;
    }

    /**
     * Gets the offset of the cursor relative to the initial click on a component before dragging.
     *
     * @return  the offset
     */
    Point getSelectedComponentDragOffset() {
        return selectedComponentDragOffset;
    }

    /**
     * Sets the offset of the cursor relative to the initial click on a component before dragging.
     *
     * @param selectedComponentDragOffset   the offset
     */
    void setSelectedComponentDragOffset(Point selectedComponentDragOffset) {
        this.selectedComponentDragOffset = selectedComponentDragOffset;
    }

    /**
     * Gets whether or not the designer is locked.
     *
     * @return  true if the designer is locked, otherwise false
     */
    @SuppressWarnings("unused") // API method.
    public boolean isLocked() {
        return state.getType() == DesignerStateType.LOCKED;
    }

    /**
     * Sets whether or not the designer is locked.
     *
     * @param locked    true to lock the designer, false to unlock
     */
    @SuppressWarnings("unused") // API method.
    public void setLocked(boolean locked) {
        state = locked ? new LockedState(this) : new DefaultState(this);
        repaint();
    }

    /**
     * Performs a repaint of this designer component.
     * 
     * @param g The graphics context we are going to paint to.
     */
    private void paintComponent(Graphics2D g) {

        // Fill in background.
        g.setColor(backgroundColor);
        g.fillRect(0, 0, getWidth(), getHeight());

        // Draw grid.
        if (showGrid) {
            g.setColor(gridLineColor);

            // Draw vertical grid lines.
            for (int x = 0; x < getWidth(); x += gridSpacing) {
                g.drawLine(x, 0, x, getHeight());
            }

            // Draw horizontal grid lines.
            for (int y = 0; y < getHeight(); y += gridSpacing) {
                g.drawLine(0, y, getWidth(), y);
            }
        }

        // Are we dragging a linking/unlinking line around? If so, draw link to cursor.
        if (state.getType() == DesignerStateType.BRICK_LINKING
                || state.getType() == DesignerStateType.BRICK_UNLINKING) {
            final int x = selectedBrick.getBounds().x + (selectedBrick.getBounds().width / 2);
            final int y = selectedBrick.getBounds().y + (selectedBrick.getBounds().height / 2);
            g.setColor(state.getType() == DesignerStateType.BRICK_LINKING ? brickLinkColor : brickUnlinkingColor);
            g.drawLine(x, y, getMousePosition().x, getMousePosition().y);
        }

        // Render brick links.
        for (Brick currentBrick : bricks) {
            final int sourceX = (currentBrick.getBounds().x + (currentBrick.getBounds().width / 2));
            final int sourceY = (currentBrick.getBounds().y + (currentBrick.getBounds().height / 2));
            for (Object otherBrick : currentBrick.getConnections()) {
                Brick connectedBrick = (Brick) otherBrick;
                final int destX = (connectedBrick.getBounds().x + (connectedBrick.getBounds().width / 2));
                final int destY = (connectedBrick.getBounds().y + (connectedBrick.getBounds().height / 2));
                g.setColor(brickLinkColor);
                g.drawLine(sourceX, sourceY, destX, destY);
            }
        }

        // Render brick images.
        for (Brick currentBrick : bricks) {
            g.drawImage(currentBrick.getImage(), currentBrick.getX(), currentBrick.getY(), null);
        }

        // Is a brick selected?
        if (selectedBrick != null) {
            final Rectangle brickBounds = selectedBrick.getBounds();

            // Link tag on selected brick.
            g.setColor(linkingTagBackgroundColor);
            linkingTagBounds = new Rectangle((int) brickBounds.getX() - linkingTagSize - 1,
                    (int) brickBounds.getY() - linkingTagSize - 1, linkingTagSize, linkingTagSize);
            g.fillRect(linkingTagBounds.x, linkingTagBounds.y, linkingTagBounds.width, linkingTagBounds.height);
            g.setColor(linkingTagBorderColor);
            g.drawRect(linkingTagBounds.x, linkingTagBounds.y, linkingTagBounds.width, linkingTagBounds.height);

            // Dotted bounding box on selected brick.
            g.setColor(selectionBoundingBoxColor);
            g.setStroke(selectionBoundingBoxStroke);
            g.drawRect((int) brickBounds.getX() - 1, (int) brickBounds.getY() - 1, (int) brickBounds.getWidth() + 1,
                    (int) brickBounds.getHeight() + 1);
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // We need Graphics2D for advanced drawing methods.
        paintComponent((Graphics2D) g);
    }
}
