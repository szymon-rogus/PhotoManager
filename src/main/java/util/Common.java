package util;

import javafx.scene.control.MenuItem;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.text.SimpleDateFormat;
import java.util.function.UnaryOperator;

public class Common {

    public static UnaryOperator<TextFormatter.Change> validationOperator = change -> {
        if (change.getText().equals(" ")) {
            change.setText("");
        }
        return change;
    };

    public static final SimpleDateFormat globalFormat = new SimpleDateFormat("yyyy-MM-dd, HH:mm:ss");

    public static void setIconForElement(MenuItem item, String path) {
        ImageView imageView = new ImageView(new Image(ClassLoader.getSystemResourceAsStream(path)));
        imageView.setFitHeight(20);
        imageView.setFitWidth(20);
        item.setGraphic(imageView);
    }
}
