package me.niklas.presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

/**
 * Created by Niklas on 6/14/19 in Presentation
 */
public class ImageView extends JPanel {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Main main;

    private int xLimit; //The horizontal limit
    private int yLimit; //The vertical limit
    private float scaleFactor = 1; //Scale Factor for the whole screen

    private BufferedImage raw; //The raw, unscaled image
    private Image image; //The image scaled so it fits in the middle of the screen
    private Image fullscreen; //The image to show in fullscreen view.

    //The start position of the displayed image (calculated based on the limits and the size of the image)
    private int posX = 0;
    private int posY = 0;

    private float logoScaleFactor = 1; //Scaling factor of the logo
    private BufferedImage rawLogo; //The raw, unscaled logo
    private Image logo; //The logo shown in the upper left corner (already scaled)

    private int maxX; //The maximum x scale for an image (normal centered)
    private int maxY; //The maximum y scale for an image (normal centered)

    private boolean logoEnabled = true; //Whether the logo is enabled
    private boolean isFullscreen = false; //Whether fullscreen mode is active
    private boolean showMain = true; //Whether the main content should be shown

    private Color bgColor = Color.white; //The background color
    private Description description = new Description("", ""); //The description of an image

    public ImageView(Main main) {
        this.main = main;
        reload();
    }

    private void reload() {
        scaleFactor = scaleFactor > 1f ? 1f : scaleFactor < 0.2f ? 0.2f : scaleFactor;

        xLimit = (int) (Constants.SCREEN_SIZE.width * scaleFactor);
        yLimit = (int) (Constants.SCREEN_SIZE.height * scaleFactor);

        main.getFrame().setSize(xLimit, yLimit);
        main.getFrame().setLocation((Constants.SCREEN_SIZE.width - xLimit) / 2, (Constants.SCREEN_SIZE.height - yLimit) / 2);

        maxX = (int) (xLimit / 2.0);
        maxY = (int) (yLimit / 1.1);

        setLogo(rawLogo);
        setImage(raw);

        main.getFrame().repaint();
    }


    @Override
    protected void paintComponent(Graphics orig) {
        super.paintComponent(orig);

        Graphics2D g = (Graphics2D) orig;

        g.setColor(bgColor);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (main.isLocked()) {
            g.setColor(Color.red);
            g.fillRect(getWidth() - 2, getHeight() - 2, 2, 2);
        }

        g.setFont(new Font("Arial", Font.PLAIN, (int) (36 * scaleFactor)));
        g.setColor(Utils.invertColor(bgColor));


        if (!isFullscreen && showMain) {
            drawText(g, description.getName(), xLimit / 12, (yLimit / 2) - (g.getFontMetrics().getHeight() / 2));
            drawText(g, description.getNumber(), (int) (xLimit * 0.88f), (yLimit / 2) - (g.getFontMetrics().getHeight() / 2));
        }

        if (logoEnabled && logo != null && !isFullscreen) {
            g.drawImage(logo, 0, 0, this);
        }

        if (!showMain) return;


        if (image == null) {
            logger.error("Image in ImageView is null");
            return;
        }

        /* Deprecated: Scrolling
        if(index > limit || index < 0) {
            dir *= -1;
            index+=dir;
        }

        Image temp = image.getScaledInstance(xLimit/2, image.getHeight(this), TYPE_INT_ARGB);


        g.drawImage(temp, 0, 0, xLimit/2, yLimit,
                0, index, xLimit/2, yLimit+index, this);
        //g.drawImage(scroll, xLimit, 0, this);

        index+=dir;*/


        if (!isFullscreen) {
            g.drawImage(image, posX, posY, this);
        } else {
            g.drawImage(fullscreen, (xLimit - fullscreen.getWidth(this)) / 2, (yLimit - fullscreen.getHeight(this)) / 2, this);
        }
    }

    private void drawText(Graphics2D g, String text, int x, int y) {
        String[] lines = text.split(";n");

        double multi = lines.length / 2.0;

        y -= g.getFontMetrics().getHeight() * multi;
        for (String s : lines) {
            g.drawString(s.trim(), x, y += g.getFontMetrics().getHeight());
        }
    }

    public Image getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.raw = image;
        if (image == null) return;

        float ratio = Math.min((float) maxX / image.getWidth(), (float) maxY / image.getHeight());

        logger.debug(String.format("Image scaling ratio is %f", ratio));

        BufferedImage temp = new BufferedImage((int) (image.getWidth(this) * ratio), (int) (image.getHeight(this) * ratio), TYPE_INT_ARGB);

        logger.debug(String.format("Scaled size is [%d,%d]", temp.getWidth(), temp.getHeight()));

        AffineTransform at = new AffineTransform();
        at.scale(ratio, ratio);

        AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);

        this.image = scaleOp.filter(image, temp);

        calculateFullscreen();

        posX = (xLimit - this.image.getWidth(this)) / 2;
        posY = (yLimit - this.image.getHeight(this)) / 2;

        main.getFrame().repaint();
    }

    private void calculateFullscreen() {
        float ratio = Math.min((float) xLimit / raw.getWidth(), (float) yLimit / raw.getHeight());

        logger.debug(String.format("Image fullscreen scaling ratio is %f .", ratio));

        BufferedImage temp = new BufferedImage((int) (raw.getWidth() * ratio), (int) (raw.getHeight() * ratio), TYPE_INT_ARGB);

        logger.debug(String.format("Scaled fullscreen image size is [%d,%d]", temp.getWidth(), temp.getHeight()));

        AffineTransform at = new AffineTransform();
        at.scale(ratio, ratio);

        AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);


        this.fullscreen = scaleOp.filter(raw, temp);

        main.getFrame().repaint();
    }

    public void setDescription(Description desc) {
        this.description = desc;
        if (description == null) description = Description.EMPTY;
        main.getFrame().repaint();
    }

    public Image getLogo() {
        return logo;
    }

    public void setLogo(BufferedImage logo) {
        this.rawLogo = logo;

        logoScaleFactor = logoScaleFactor > 2f ? 2f : logoScaleFactor < 0.1f ? 0.1f : logoScaleFactor;
        if (logo == null) {
            this.logo = null;
            logger.error("Logo is null");
            return;
        }
        float scaleFactor = (float) (xLimit / 5) / logo.getWidth(this);
        scaleFactor *= this.logoScaleFactor; //Add public scale factor
        logger.debug(String.format("Global logo scaling scaling ratio is %f, logo image scaling ratio is %f.", logoScaleFactor, scaleFactor));

        BufferedImage temp = new BufferedImage((int) (logo.getWidth() * scaleFactor), (int) (logo.getHeight() * scaleFactor), TYPE_INT_ARGB);

        logger.debug(String.format("Scaled logo image size is [%d,%d]", temp.getWidth(), temp.getHeight()));

        AffineTransform at = new AffineTransform();
        at.scale(scaleFactor, scaleFactor);

        AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);

        this.logo = scaleOp.filter(logo, temp);
        main.getFrame().repaint();
    }

    public boolean isLogoEnabled() {
        return logoEnabled;
    }

    public void setLogoEnabled(boolean logoEnabled) {
        this.logoEnabled = logoEnabled;
    }

    public void toggleLogo() {
        logoEnabled = !logoEnabled;
    }

    public Color getBgColor() {
        return bgColor;
    }

    public void setBgColor(Color bgColor) {
        this.bgColor = bgColor;
    }

    public void toggleBg() {
        bgColor = Utils.invertColor(bgColor);
    }

    public void toggleFullscreen() {
        isFullscreen = !isFullscreen;
    }

    public void increaseLogoScaleFactor() {
        logoScaleFactor += 0.1f;
        setLogo(rawLogo);
    }

    public void decreaseLogoScaleFactor() {
        logoScaleFactor -= 0.1f;
        setLogo(rawLogo);
    }

    public void toggleShowMain() {
        showMain = !showMain;
    }

    public void increaseScale() {
        scaleFactor += 0.1f;
        reload();
    }

    public void decreaseScale() {
        scaleFactor -= 0.1f;
        reload();
    }
}
