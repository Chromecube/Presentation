package me.niklas.presentation;

import me.niklas.presentation.ui.UiController;
import me.niklas.presentation.web.Webserver;

class Main {

    public static void main(String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("web")) {
            Thread t = new Thread(new Webserver());
            t.setName("Webserver");
            t.start();
        } else new UiController();
    }
}
