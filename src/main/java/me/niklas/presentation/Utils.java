package me.niklas.presentation;

/**
 * Created by Niklas on 6/15/19 in Presentation
 */
class Utils {

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
}
