package com.sauljohnson.bach;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RawDataConnectionBrick extends Brick<Object> {

    private static BufferedImage image;

    /**
     * Initialises a new instance of a brick.
     *
     * @param modelObject the underlying instance
     * @param x
     * @param y
     */
    public RawDataConnectionBrick(Object modelObject, int x, int y) {
        super(modelObject, x, y);
    }

    @Override
    public String getTypeName() {
        return "RawDataConnectionBrick";
    }

    @Override
    public int getWidth() {
        return 258;
    }

    @Override
    public int getHeight() {
        return 298;
    }

    @Override
    public Image getImage() {
        try {
            if (image == null) {
                image = ImageIO.read(new File("C:/Users/u0031620/Desktop/th.png"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    @Override
    public JPopupMenu getContextMenu() {
        return null;
    }

    @Override
    public List<ConnectionCapacity> getConnectionCapacities() {
        ArrayList<ConnectionCapacity> ss = new ArrayList<>();
        ss.add(new ConnectionCapacity(1, RawDataConnectionBrick.class));
        return ss;
    }
}
