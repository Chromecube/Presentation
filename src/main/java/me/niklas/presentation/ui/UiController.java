package me.niklas.presentation.ui;

import me.niklas.presentation.images.ResourceManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Niklas on 21.01.2020 in Presentation
 */
public class UiController {

    private final ResourceManager resources = new ResourceManager();
    private boolean locked = false;
    private JFrame frame;
    private ImageView fullImage;
    private Blackscreen blackscreen;

    public UiController() {
        setupUI();
        loadImage();
        reset();
    }

    public void lastImage() {
        if (resources.hasNoImages()) {
            fullImage.setImage(null);
            return;
        } else resources.lastImage();

        loadImage();
    }

    public void nextImage() {
        if (resources.hasNoImages()) {
            fullImage.setImage(null);
            return;
        } else resources.nextImage();

        loadImage();
    }

    private void loadImage() {
        BufferedImage img = resources.getCurrentImage();

        fullImage.setImage(img);
        fullImage.setDescription(resources.getCurrentDescription());
    }

    private void setupUI() {
        blackscreen = new Blackscreen();

        frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setTitle("");
        frame.setLocation(0, 0);
        frame.setUndecorated(true);
        frame.getContentPane().setBackground(Color.BLACK);
        frame.setCursor(UiConstants.BLANK_CURSOR);

        fullImage = new ImageView(this);
        fullImage.setLogo(resources.getLogo());
        frame.add(fullImage);
        //frame.add(scroll);

        KeyListener keyEvents = new KeyListener(this);
        blackscreen.addHandler(keyEvents);
        frame.addKeyListener(keyEvents);
    }

    private void reset() {
        frame.setVisible(true);
        frame.requestFocus(FocusEvent.Cause.MOUSE_EVENT);
    }

    public ImageView getFullImage() {
        return fullImage;
    }

    public void setIndex(int index) {
        resources.setIndex(index);
        loadImage();
    }

    public JFrame getFrame() {
        return frame;
    }

    public Blackscreen getBlackscreen() {
        return blackscreen;
    }

    public boolean isLocked() {
        return locked;
    }

    public void toggleLocked() {
        locked = !locked;
    }

    public void openDirectory() {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().open(new File(System.getProperty("user.dir")));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
