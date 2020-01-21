package me.niklas.presentation.ui;

import me.niklas.presentation.Utils;
import me.niklas.presentation.images.Description;
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
    private final UiController controller;

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

    public ImageView(UiController controller) {
        this.controller = controller;
        reload();
    }

    private void reload() {
        scaleFactor = scaleFactor > 1f ? 1f : Math.max(scaleFactor, 0.2f);

        xLimit = (int) (UiConstants.SCREEN_SIZE.width * scaleFactor);
        yLimit = (int) (UiConstants.SCREEN_SIZE.height * scaleFactor);

        controller.getFrame().setSize(xLimit, yLimit);
        controller.getFrame().setLocation((UiConstants.SCREEN_SIZE.width - xLimit) / 2, (UiConstants.SCREEN_SIZE.height - yLimit) / 2);

        maxX = (int) (xLimit / 2.0);
        maxY = (int) (yLimit / 1.1);

        setLogo(rawLogo);
        setImage(raw);

        controller.getFrame().repaint();
    }


    @Override
    protected void paintComponent(Graphics orig) {
        super.paintComponent(orig);

        Graphics2D g = (Graphics2D) orig;

        //Fill background
        g.setColor(bgColor);
        g.fillRect(0, 0, getWidth(), getHeight());
        //Enable fancy rendering
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        //Set up font family & color
        g.setFont(new Font("Arial", Font.PLAIN, (int) (36 * scaleFactor)));
        g.setColor(Utils.invertColor(bgColor));


        if (!isFullscreen && showMain) { //Display description if required
            drawText(g, description.getName(), xLimit / 12, (yLimit / 2) - (g.getFontMetrics().getHeight() / 2));
            drawText(g, description.getNumber(), (int) (xLimit * 0.88f), (yLimit / 2) - (g.getFontMetrics().getHeight() / 2));
        }

        if (logoEnabled && logo != null && !isFullscreen) { //Display logo if required (top left corner)
            g.drawImage(logo, 0, 0, this);
        }

        if (!showMain) return; //Option to only display the background color


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


        if (!isFullscreen) { //Draw image in the normal aspect ratio
            g.drawImage(image, posX, posY, this);
        } else { //Calculate maximum size and draw the image
            g.drawImage(fullscreen, (xLimit - fullscreen.getWidth(this)) / 2, (yLimit - fullscreen.getHeight(this)) / 2, this);
        }
    }

    private void drawText(Graphics2D g, String text, int x, int y) {
        String[] lines = text.split(";n"); //Manual line breaks

        double multi = lines.length / 2.0;

        y -= g.getFontMetrics().getHeight() * multi; //Use the font height in the calculation
        for (String s : lines) {
            g.drawString(s.trim(), x, y += g.getFontMetrics().getHeight()); //Draw lines from top to bottom
        }
    }

    public void setImage(BufferedImage image) {
        this.raw = image;
        if (image == null) return;
        //Calculate the required scaling factor to make it fit into the box
        float ratio = Math.min((float) maxX / image.getWidth(), (float) maxY / image.getHeight());

        logger.debug(String.format("Image scaling ratio is %f", ratio));
        //Create temporary buffered image with recommended ratio
        BufferedImage temp = new BufferedImage((int) (image.getWidth(this) * ratio), (int) (image.getHeight(this) * ratio), TYPE_INT_ARGB);

        logger.debug(String.format("Scaled size is [%d,%d]", temp.getWidth(), temp.getHeight()));
        //Perform scaling operation (bilinear scaling)
        AffineTransform at = new AffineTransform();
        at.scale(ratio, ratio);

        AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);

        this.image = scaleOp.filter(image, temp);

        calculateFullscreen();

        posX = (xLimit - this.image.getWidth(this)) / 2;
        posY = (yLimit - this.image.getHeight(this)) / 2;

        controller.getFrame().repaint();
    }

    private void calculateFullscreen() {
        //Calculate required scaling factor
        float ratio = Math.min((float) xLimit / raw.getWidth(), (float) yLimit / raw.getHeight());

        logger.debug(String.format("Image fullscreen scaling ratio is %f .", ratio));
        //Create temporary buffered image with recommended ratio
        BufferedImage temp = new BufferedImage((int) (raw.getWidth() * ratio), (int) (raw.getHeight() * ratio), TYPE_INT_ARGB);

        logger.debug(String.format("Scaled fullscreen image size is [%d,%d]", temp.getWidth(), temp.getHeight()));
        //Perform scaling operation (bilinear scaling)
        AffineTransform at = new AffineTransform();
        at.scale(ratio, ratio);

        AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);

        this.fullscreen = scaleOp.filter(raw, temp);

        controller.getFrame().repaint();
    }

    public void setDescription(Description desc) {
        this.description = desc;
        if (description == null) description = Description.EMPTY;
        controller.getFrame().repaint();
    }


    public void setLogo(BufferedImage logo) {
        this.rawLogo = logo;

        logoScaleFactor = logoScaleFactor > 2f ? 2f : Math.max(logoScaleFactor, 0.1f); //Limit the scaling factor (0.1 ... 2.0 times allowed)
        if (logo == null) {
            this.logo = null;
            logger.error("Logo is null");
            return;
        }

        float scaleFactor = (float) (xLimit / 5) / logo.getWidth(this); //Logo has to be quadratic! Calculate the real scale factor
        scaleFactor *= this.logoScaleFactor; //Add general scale factor
        logger.debug(String.format("Global logo scaling scaling ratio is %f, logo image scaling ratio is %f.", logoScaleFactor, scaleFactor));
        //Create temporary buffered image with required size
        BufferedImage temp = new BufferedImage((int) (logo.getWidth() * scaleFactor), (int) (logo.getHeight() * scaleFactor), TYPE_INT_ARGB);

        logger.debug(String.format("Scaled logo image size is [%d,%d]", temp.getWidth(), temp.getHeight()));
        //Perform scaling operation (bilinear scaling)
        AffineTransform at = new AffineTransform();
        at.scale(scaleFactor, scaleFactor);

        AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);

        this.logo = scaleOp.filter(logo, temp);
        controller.getFrame().repaint();
    }

    public void toggleLogo() {
        logoEnabled = !logoEnabled;
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
