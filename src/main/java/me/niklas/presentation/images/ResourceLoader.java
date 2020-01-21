package me.niklas.presentation.images;

/*import com.google.gson.Gson;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.io.BufferedReader;*/

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;


/**
 * Created by Niklas on 6/14/19 in Presentation
 */
public class ResourceLoader {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private BufferedImage logo;
//    private Description[] indices;

    public ResourceLoader() {
        reload();
    }

    private void reload() {
        try {
            File resources = new File("res");
            if (!resources.exists()) resources.mkdir();

            File[] relevant = resources.listFiles((dir, name) -> name.startsWith("logo"));

            if (relevant == null || relevant.length == 0) {
                logger.error("No logo file found.");
                return;
            }

            logo = ImageIO.read(relevant[0]);

        } catch (Exception e) {
            e.printStackTrace();
        }

/*        try {
            File indices = new File("res/indices.json");

            StringBuilder text = new StringBuilder();
            String line;
            try(BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(indices),
                    StandardCharsets.UTF_8))) {

                while((line = reader.readLine()) != null) {
                    text.append(line).append("\n");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            Gson gson = new Gson();

            this.indices = gson.fromJson(text.toString().trim(), Description[].class);

        } catch (Exception e) {
            e.printStackTrace();
        }
        */
    }

    public BufferedImage getLogo() {
        return logo;
    }

    /*public Description getIndex(int index) {
        if (index >= 0 && index < indices.length) {
            return indices[index];
        }
        return Description.EMPTY;
    }*/
}
