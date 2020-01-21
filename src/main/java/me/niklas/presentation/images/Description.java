package me.niklas.presentation.images;

/**
 * Created by Niklas on 6/15/19 in Presentation
 */
public class Description {

    public static final Description EMPTY = new Description("", "");
    private final String number;
    private final String name;

    public Description(String number, String name) {
        this.number = number;
        this.name = name;
    }

    public static Description parse(String s) {
        String[] parts = s.split("-");
        String num = parts[0];
        String other = s.substring(parts[0].length() + 1);
        return new Description(num, other);
    }

    public String getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }
}
