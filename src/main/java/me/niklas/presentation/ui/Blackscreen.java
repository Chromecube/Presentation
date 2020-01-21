package me.niklas.presentation.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;

/**
 * Created by Niklas on 6/16/19 in Presentation
 */
public class Blackscreen extends JPanel {

    private final JFrame frame;
    private boolean locked = false;

    public Blackscreen() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.setTitle("");
        frame.setLocation(0, 0);
        frame.setAlwaysOnTop(true);
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.setUndecorated(true);
        frame.add(this);
        frame.setCursor(UiConstants.BLANK_CURSOR);
        frame.repaint();
    }

    public void toggleVisibility() {
        frame.repaint();
        frame.setVisible(!frame.isVisible());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.black);
        g.fillRect(0, 0, getWidth(), getHeight());

        if (locked) {
            g.setColor(Color.red);
            g.fillRect(getWidth() - 2, getHeight() - 2, 2, 2);
        }
    }

    public void addHandler(KeyListener listener) {
        frame.addKeyListener(listener);
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
        frame.repaint();
    }
}
