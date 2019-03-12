package com.sauljohnson.bach;

import javax.swing.*;

public class QuickTest {
    public static void main(String[] args) {
        JFrame gg = new JFrame();
        Designer d = new Designer(null);
        gg.add(d);
        gg.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gg.show();
        RawDataConnectionBrick dd = new RawDataConnectionBrick(null,0, 0);
        d.addBrick(dd);
    }
}
