package controller.albums;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableBooleanValue;
import javafx.fxml.FXML;
import javafx.scene.control.TextFormatter;
import model.Album;
import util.Common;

import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractChangeAlbumDialog extends AbstractChangeDialog {

    protected List<String> albumNames;

    @FXML
    protected void initialize() {
        getAlbumNamesTaken();
        textField.setTextFormatter(new TextFormatter<>(Common.validationOperator));
        okButton.disableProperty().bind(Bindings.isEmpty(textField.textProperty()).or(albumNameExists()));
    }

    private void getAlbumNamesTaken() {
        albumNames = AlbumViewController.getAlbums().stream()
                .map(Album::getName)
                .collect(Collectors.toList());
    }

    private ObservableBooleanValue albumNameExists() {
        return Bindings.createBooleanBinding(() -> albumNames.contains(textField.getText()),
                textField.textProperty());
    }

}
