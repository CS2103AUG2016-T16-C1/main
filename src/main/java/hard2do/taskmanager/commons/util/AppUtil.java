package hard2do.taskmanager.commons.util;

import hard2do.taskmanager.MainApp;
import javafx.scene.image.Image;

/**
 * A container for App specific utility functions
 */
public class AppUtil {

    public static Image getImage(String imagePath) {
        assert imagePath != null;
        return new Image(MainApp.class.getResourceAsStream(imagePath));
    }

}
