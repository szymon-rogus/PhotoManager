package util;

import javafx.scene.control.TextFormatter;

import java.util.function.UnaryOperator;

public class Common {

    public static UnaryOperator<TextFormatter.Change> validationOperator = change -> {
        if (change.getText().equals(" ")) {
            change.setText("");
        }
        return change;
    };
}
