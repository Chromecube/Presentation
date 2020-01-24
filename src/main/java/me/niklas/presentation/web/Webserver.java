package me.niklas.presentation.web;

import me.niklas.presentation.Utils;
import me.niklas.presentation.images.ResourceManager;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Service;
import spark.resource.ExternalResource;
import spark.staticfiles.MimeType;
import spark.utils.IOUtils;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by Niklas on 22.01.2020 in Presentation
 */
public class Webserver implements Runnable {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final ResourceManager manager = new ResourceManager();

    public Webserver() {
        logger.info("This project depends on the following web technologies:");
        logger.info("Bootstrap 4: https://getbootstrap.com");
        logger.info("Jquery: https://jquery.com");
        logger.info("Axios: https://github.com/axios/axios");
    }

    @Override
    public void run() {
        Service service = Service.ignite().ipAddress("127.0.0.1").port(8998).staticFileLocation("web");

        Executors.newSingleThreadScheduledExecutor().schedule(() -> {
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(new URI("http://localhost:8998"));
                } catch (Exception e) {
                    logger.error("Can not open browser", e);
                }
            }
        }, 1, TimeUnit.SECONDS);

        service.before("*", (request, response) -> response.type("application/json"));

        service.get("/api/desc", (request, response) -> buildCurrentDataObject());

        service.get("/api/logo", (request, response) -> {
            File f = manager.getLogoFile();
            response.type(MimeType.fromResource(new ExternalResource(f.getAbsolutePath())));
            stream(response.raw().getOutputStream(), new FileInputStream(f));
            return "";
        });

        service.get("/api/img", (request, response) -> {
            File f = manager.getCurrentImageFile();
            response.type(MimeType.fromResource(new ExternalResource(f.getAbsolutePath())));
            stream(response.raw().getOutputStream(), new FileInputStream(f));
            return "";
        });

        service.get("/api/img/:index", (request, response) -> {
            if (!Utils.isInteger(request.params("index"))) return "{\"reponse\":\"INVALID\"}";

            File f = manager.getFileAtIndex(Utils.tryParseInt(request.params("index"), 0));
            response.type(MimeType.fromResource(new ExternalResource(f.getAbsolutePath())));
            stream(response.raw().getOutputStream(), new FileInputStream(f));

            return "{\"reponse\":\"OK\"}";
        });

        service.get("/api/prev", (request, response) -> {
            manager.lastImage();
            return buildCurrentDataObject();
        });

        service.get("/api/next", (request, response) -> {
            manager.nextImage();
            return buildCurrentDataObject();
        });

        service.get("/api/set/:index", (request, response) -> {
            if (!Utils.isInteger(request.params("index"))) return "{\"reponse\":\"INVALID\"}";
            manager.setIndex(Utils.tryParseInt(request.params("index"), 0));
            return buildCurrentDataObject();
        });

        service.get("/api/index", (request, response) -> {
            JSONObject obj = new JSONObject();
            obj.put("index", manager.getIndex());
            return obj;
        });

        service.notFound((request, response) -> {
            response.type("text/html");
            return "Not found";
        });

    }

    private void stream(OutputStream out, FileInputStream in) throws IOException {
        IOUtils.copy(in, out);
        out.close();
        in.close();
    }

    private JSONObject buildCurrentDataObject() {
        JSONObject obj = new JSONObject();
        obj.put("name", manager.getCurrentDescription().getWebName());
        obj.put("number", manager.getCurrentDescription().getWebNumber());
        obj.put("link", "/api/img/" + manager.getIndex());
        return obj;
    }
}
