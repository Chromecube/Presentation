package me.niklas.presentation.ui;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Niklas on 6/14/19 in Presentation
 */
class UiConstants {

    public static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();

    public static final Cursor BLANK_CURSOR = Toolkit.getDefaultToolkit().createCustomCursor(
            new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "Blank"
    );
}
