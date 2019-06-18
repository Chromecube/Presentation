package me.niklas.presentation;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;

/**
 * Created by Niklas on 6/14/19 in Presentation
 */
class Listener implements KeyListener {

    private final Main main;
    private final String[] keys = new String[]{"0", "0"};
    private boolean scaleFullscreen = true;

    public Listener(Main main) {
        this.main = main;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_P) {
            main.toggleLocked();
            main.getBlackscreen().setLocked(main.isLocked());
        }

        if (main.isLocked()) return;

        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            main.getFrame().setVisible(false);
            System.exit(0);
        }
        if (e.getKeyCode() == KeyEvent.VK_D) {
            main.nextImage();
        } else if (e.getKeyCode() == KeyEvent.VK_A) {
            main.lastImage();
        } else if (e.getKeyCode() == KeyEvent.VK_E) {
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().open(new File(System.getProperty("user.dir")));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        } else if (e.getKeyCode() == KeyEvent.VK_L) {
            main.getFullImage().toggleLogo();
        } else if (e.getKeyCode() == KeyEvent.VK_U) {
            main.getFullImage().toggleBg();
        } else if (e.getKeyCode() == KeyEvent.VK_F) {
            main.getFullImage().toggleFullscreen();
        } else if (e.getKeyCode() == KeyEvent.VK_B) {
            main.getBlackscreen().toggleVisibility();
            main.getFrame().requestFocus(FocusEvent.Cause.MOUSE_EVENT);
        } else if (e.getKeyCode() == KeyEvent.VK_R) {
            main.getFrame().repaint();
        } else if (e.getKeyCode() == KeyEvent.VK_I) {
            main.getFullImage().toggleShowMain();
        } else if (e.getKeyCode() == KeyEvent.VK_ADD) {
            if (scaleFullscreen) main.getFullImage().increaseScale();
            else main.getFullImage().increaseLogoScaleFactor();
        } else if (e.getKeyCode() == KeyEvent.VK_SUBTRACT) {
            if (scaleFullscreen) main.getFullImage().decreaseScale();
            else main.getFullImage().decreaseLogoScaleFactor();
        } else if (e.getKeyCode() == KeyEvent.VK_S) {
            scaleFullscreen = !scaleFullscreen;
        } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            int result = Integer.parseInt(keys[0] + keys[1]);
            keys[0] = "0";
            keys[1] = "1";
            main.setIndex(result);
        }

        if (Utils.isInteger(e.getKeyChar() + "")) {
            keys[0] = keys[1];
            keys[1] = e.getKeyChar() + "";
        }

    }
}
