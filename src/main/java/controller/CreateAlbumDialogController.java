package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.NoArgsConstructor;
import model.Album;
import org.hibernate.Transaction;
import java.util.ArrayList;


@NoArgsConstructor
public class CreateAlbumDialogController {
    private Stage dialogStage;

    @FXML
    private TextField nameTextField;

    @FXML
    private Button cancelButton;

    @FXML
    private Button okButton;

    @FXML
    private void handleCancelAction(ActionEvent event) {
        dialogStage.close();
    }

    @FXML
    private void handleOkAction(ActionEvent event) {
        if(!nameTextField.getText().isEmpty()) {
            Transaction tx = app.AppManager.getSessionFactory().getCurrentSession().
                    beginTransaction();
            app.AppManager.getSessionFactory().getCurrentSession().
                    save(new Album(nameTextField.getText(), new ArrayList<>()));
            tx.commit();
            dialogStage.close();
        }
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
}
