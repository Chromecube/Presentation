package me.niklas.presentation.ui;

import me.niklas.presentation.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

import static java.awt.event.KeyEvent.*;

/**
 * Created by Niklas on 6/14/19 in Presentation
 */
class KeyListener implements java.awt.event.KeyListener {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final UiController controller;
    private final String[] keys = new String[]{"0", "0"};
    private final Map<Integer, Runnable> keymap = new HashMap<>();
    private boolean scaleFullscreen = true;

    public KeyListener(UiController controller) {
        this.controller = controller;
        loadKeymap();
    }

    private void loadKeymap() {
        keymap.clear();

        keymap.put(VK_A, controller::lastImage);
        keymap.put(VK_D, controller::nextImage);
        keymap.put(VK_E, controller::openDirectory);
        keymap.put(VK_L, () -> controller.getFullImage().toggleLogo());
        keymap.put(VK_ESCAPE, () -> {
            controller.getFrame().setVisible(false);
            System.exit(0);
        });
        keymap.put(VK_U, () -> controller.getFullImage().toggleBg());
        keymap.put(VK_F, () -> controller.getFullImage().toggleFullscreen());
        keymap.put(VK_B, () -> {
            controller.getBlackscreen().toggleVisibility();
            controller.getFrame().requestFocus(FocusEvent.Cause.MOUSE_EVENT);
        });
        keymap.put(VK_R, () -> controller.getFrame().repaint());
        keymap.put(VK_I, () -> controller.getFullImage().toggleShowMain());
        keymap.put(VK_ADD, () -> {
            if (scaleFullscreen) controller.getFullImage().increaseScale();
            else controller.getFullImage().increaseLogoScaleFactor();
        });
        keymap.put(VK_SUBTRACT, () -> {
            if (scaleFullscreen) controller.getFullImage().decreaseScale();
            else controller.getFullImage().decreaseLogoScaleFactor();
        });
        keymap.put(VK_S, () -> scaleFullscreen = !scaleFullscreen);
        keymap.put(VK_ENTER, () -> {
            int result = Integer.parseInt(keys[0] + keys[1]);
            keys[0] = "0";
            keys[1] = "1";
            controller.setIndex(result);
        });
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
            controller.toggleLocked();
            controller.getBlackscreen().setLocked(controller.isLocked());
        }

        logger.debug("Key pressed: " + KeyEvent.getKeyText(e.getKeyCode()));

        if (controller.isLocked()) return;

        if (keymap.containsKey(e.getKeyCode())) {
            keymap.get(e.getKeyCode()).run();
            controller.getFrame().repaint();
        }

        if (Utils.isInteger(e.getKeyChar() + "")) {
            keys[0] = keys[1];
            keys[1] = e.getKeyChar() + "";
        }
    }

}
