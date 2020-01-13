package controller;

import app.AppManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import lombok.NoArgsConstructor;
import model.User;
import org.hibernate.Session;
import org.hibernate.Transaction;


@NoArgsConstructor
public class ChangeEmailDialogController {

    private Stage dialogStage;

    private AppController appController;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField repeatEmailTextField;

    @FXML
    private Button okButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Label errorLabel;

    @FXML
    private void initialize() {
        this.errorLabel.setVisible(false);
        this.errorLabel.setTextFill(Color.RED);
    }

    @FXML
    private void handleCancelAction(ActionEvent event) {
        dialogStage.close();
    }

    @FXML
    private void handleOkAction(ActionEvent event) {
        final boolean isValid = validate();
        if (isValid) {
            final User user = AppManager.getSessionUser();
            final Session session = AppManager.getSessionFactory().getCurrentSession();
            final Transaction tx = session.beginTransaction();
            user.setEmail(emailTextField.getText());
            session.update(user);
            tx.commit();
            dialogStage.close();
        }
    }

    public void setDialogStage(Stage stage) {
        this.dialogStage = stage;
    }

    private boolean validate() {
        if (emailTextField.getText().isEmpty()) {
            errorLabel.setText("Brak emaila!");
            errorLabel.setVisible(true);
            return false;
        }
        if (!emailTextField.getText().matches(".*@.*\\..*")) {
            errorLabel.setText("Zły format emaila!");
            errorLabel.setVisible(true);
            return false;
        }
        if (!emailTextField.getText().equals(repeatEmailTextField.getText())) {
            errorLabel.setText("Powtórzony email się nie zgadza!");
            errorLabel.setVisible(true);
            return false;
        }
        return true;
    }
}
