package me.niklas.presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Main {

    private final List<File> files = new ArrayList<>();
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private boolean locked = false;
    private JFrame frame;
    private ImageView fullImage;
    private Blackscreen blackscreen;
    private int index = 0;
    private ResourceLoader loader;

    private Main() {

        try { //Create directory if not present yet
            File f = new File("pics");
            if (!f.exists()) f.mkdir();
        } catch (Exception e) {
            e.printStackTrace();
        }

        loadResources();
        loadFiles();
        setupUI();
        setIndex(0);
        reset();
    }

    public static void main(String[] args) {
        new Main();
    }

    private void loadResources() {
        loader = new ResourceLoader();
    }

    void nextImage() {
        index++;

        if (index >= files.size() || index < 0) index = 0;
        if (files.size() == 0) {
            fullImage.setImage(null);
            return;
        }

        loadImage();
    }

    private void loadImage() {
        File f = files.get(index);
        BufferedImage img = getImage(f);

        fullImage.setImage(img);
        fullImage.setDescription(Utils.getDescription(f.getName()));
    }

    public void lastImage() {
        index--;
        if (index < 0) index = files.size() - 1;

        if (files.size() == 0) {
            fullImage.setImage(null);
        }

        loadImage();
    }

    private void setupUI() {
        blackscreen = new Blackscreen();

        frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setTitle("");
        frame.setLocation(0, 0);
        frame.setUndecorated(true);
        frame.getContentPane().setBackground(Color.BLACK);
        frame.setCursor(Constants.BLANK_CURSOR);

        fullImage = new ImageView(this);
        fullImage.setLogo(loader.getLogo());
        frame.add(fullImage);
        //frame.add(scroll);

        Listener keyEvents = new Listener(this);
        blackscreen.addHandler(keyEvents);
        frame.addKeyListener(keyEvents);
    }

    private void loadFiles() {
        files.clear();

        File[] allFiles = new File("pics").listFiles(pathname ->
                pathname.getName().endsWith(".jpeg") || pathname.getName().endsWith("png") || pathname.getName().endsWith(".jpg"));

        if (allFiles == null) return;
        logger.info(String.format("Read in %d files.", allFiles.length));
        files.addAll(Arrays.asList(allFiles));
    }

    private void reset() {
        frame.setVisible(true);
        frame.requestFocus(FocusEvent.Cause.MOUSE_EVENT);
    }

    private BufferedImage getImage(File f) {

        try {
            return ImageIO.read(f);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ImageView getFullImage() {
        return fullImage;
    }

    public List<File> getFiles() {
        return files;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
        if (files.size() == 0) return;
        if (this.index < 0) this.index = 0;
        if (this.index >= files.size()) this.index = files.size() - 1;

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

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public void toggleLocked() {
        locked = !locked;
    }
}
