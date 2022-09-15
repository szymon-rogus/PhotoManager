package controller;

import app.AppManager;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableBooleanValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import lombok.NoArgsConstructor;
import model.Album;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.Common;

import java.util.List;
import java.util.stream.Collectors;


@NoArgsConstructor
public class CreateAlbumDialogController {

    private Stage dialogStage;

    private Session session;

    @FXML
    private TextField nameTextField;

    @FXML
    private Button cancelButton;

    @FXML
    private Button okButton;

    private List<String> albumNames;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    private void initialize() {
        this.session = AppManager.getSessionFactory().getCurrentSession();
        getAlbumNamesTaken();
        nameTextField.setTextFormatter(new TextFormatter<>(Common.validationOperator));
        okButton.disableProperty().bind(Bindings.isEmpty(nameTextField.textProperty()).or(albumNameExists()));
    }

    private void getAlbumNamesTaken() {
        albumNames = AlbumViewController.getAlbums().stream()
                .map(Album::getName)
                .collect(Collectors.toList());
    }

    private ObservableBooleanValue albumNameExists() {
        return Bindings.createBooleanBinding(() -> albumNames.contains(nameTextField.getText()),
                nameTextField.textProperty());
    }

    @FXML
    private void handleOkAction(ActionEvent event) {
        final Album album = new Album(nameTextField.getText());
        final Transaction tx = session.beginTransaction();
        session.save(album);
        tx.commit();
        dialogStage.close();
    }

    @FXML
    private void handleCancelAction(ActionEvent event) {
        dialogStage.close();
    }
}
