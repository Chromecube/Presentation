package me.niklas.presentation.images;

import me.niklas.presentation.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Niklas on 21.01.2020 in Presentation
 */
public class ResourceManager {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final List<File> files = new ArrayList<>();
    private final AtomicInteger index = new AtomicInteger();
    private ResourceLoader loader;

    public ResourceManager() {
        try { //Create directory if not present yet
            File f = new File("pics");
            if (!f.exists()) f.mkdir();
        } catch (Exception e) {
            logger.error("Can not create directory", e);
        }

        loadResources();
        loadFiles();
    }

    private void loadResources() {
        loader = new ResourceLoader();
    }

    private void loadFiles() {
        files.clear();

        File[] allFiles = new File("pics").listFiles(pathname ->
                pathname.getName().endsWith(".jpeg") || pathname.getName().endsWith("png") || pathname.getName().endsWith(".jpg"));

        if (allFiles == null) return;
        logger.info(String.format("Read in %d files.", allFiles.length));
        files.addAll(Arrays.asList(allFiles));
    }

    public File getCurrentImageFile() {
        int i = index.get();

        return i >= 0 && i < files.size() ? files.get(index.get()) : null;
    }

    public void lastImage() {
        index.decrementAndGet();

        if (index.get() >= files.size() || index.get() < 0) index.set(0);
    }

    public void nextImage() {
        index.incrementAndGet();

        if (index.get() >= files.size() || index.get() < 0) index.set(0);
    }

    public boolean hasNoImages() {
        return files.size() == 0;
    }

    public BufferedImage getCurrentImage() {
        File f = getCurrentImageFile();
        if (f == null) return new BufferedImage(0, 0, BufferedImage.TYPE_INT_ARGB);
        return getImage(f);
    }

    public void setIndex(int index) {
        this.index.set(index < 0 ? 0 : index >= files.size() ? files.size() - 1 : index);
    }

    private BufferedImage getImage(File f) {
        try {
            return ImageIO.read(f);
        } catch (Exception e) {
            logger.error("Can not read image", e);
            return null;
        }
    }

    public BufferedImage getLogo() {
        return loader.getLogo();
    }

    public File getLogoFile() {
        return loader.getLogoFile();
    }

    public Description getCurrentDescription() {
        return Utils.getDescription(getCurrentImageFile().getName());
    }

    public int getIndex() {
        return index.get();
    }

    public File getFileAtIndex(int index) {
        if (index >= files.size()) return files.get(files.size() - 1);
        if (index < 0) return files.get(0);
        return files.get(index);
    }
}
