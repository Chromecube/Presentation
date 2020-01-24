package me.niklas.presentation;

import me.niklas.presentation.images.Description;

/**
 * Created by Niklas on 6/15/19 in Presentation
 */
public class Utils {

    public static java.awt.Color invertColor(java.awt.Color color) {
        return new java.awt.Color(255 - color.getRed(), 255 - color.getGreen(), 255 - color.getBlue(), color.getAlpha());
    }

    public static boolean isInteger(String in) {
        try {
            Integer.parseInt(in);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static Description getDescription(String name) {
        name = name.substring(0, name.lastIndexOf("."));

        return Description.parse(name);
    }

    public static int tryParseInt(String input, int defaultValue) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
