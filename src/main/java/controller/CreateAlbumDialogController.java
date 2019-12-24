package controller;

import app.AppManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.NoArgsConstructor;
import model.Album;
import org.hibernate.Session;
import org.hibernate.Transaction;


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

    @FXML
    private void initialize() {
        this.session = AppManager.getSessionFactory().getCurrentSession();
    }

    @FXML
    private void handleCancelAction(ActionEvent event) {
        dialogStage.close();
    }

    @FXML
    private void handleOkAction(ActionEvent event) {
        if (!nameTextField.getText().isEmpty()) {
            final Album album = new Album(nameTextField.getText());
            final Transaction tx = session.beginTransaction();
            session.save(album);
            tx.commit();
            dialogStage.close();
        }
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
}
