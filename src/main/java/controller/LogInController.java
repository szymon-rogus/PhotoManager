package controller;

import app.AppManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import lombok.NoArgsConstructor;
import model.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;

@NoArgsConstructor
public class LogInController {

    private Stage dialogStage;

    private AppController appController;

    @FXML
    private TextField nameTextField;

    @FXML
    private PasswordField passwordTextField;

    @FXML
    private Button exitButton;

    @FXML
    private Button logInButton;

    @FXML
    private Button createAccountButton;

    @FXML
    private Label errorLabel;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setAppController(AppController appController) {
        this.appController = appController;
    }

    @FXML
    private void initialize() {
        errorLabel.setVisible(false);
        errorLabel.setTextFill(Color.RED);
        logInButton.setDisable(true);

        nameTextField.textProperty()
                .addListener((observable, oldText, newText) -> setLogInButtonStatus(newText, passwordTextField));
        passwordTextField.textProperty()
                .addListener((observable, oldText, newText) -> setLogInButtonStatus(newText, nameTextField));
    }

    private void setLogInButtonStatus(String text, TextField otherTextField) {
        logInButton.setDisable(text.trim().isEmpty() || otherTextField.getText().trim().isEmpty());
    }

    @FXML
    private void handleLogInAction() throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        final Session session = AppManager.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        final Query<User> query = session.createQuery("SELECT u FROM User u WHERE u.name=:name", User.class);
        query.setParameter("name", nameTextField.getText());
        final User user = query.uniqueResult();
        tx.commit();
        if (user == null) {
            loginError();
            return;
        }
        final KeySpec spec = new PBEKeySpec(passwordTextField.getText().toCharArray(), user.getSalt(), 65536, 128);
        final SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        final byte[] hash = factory.generateSecret(spec).getEncoded();
        if (!Arrays.equals(hash, user.getPassword())) {
            loginError();
            return;
        }
        AppManager.setSessionUser(user);
        appController.showAlbumView();
    }

    private void loginError() {
        errorLabel.setText("Invalid login or password!");
        errorLabel.setVisible(true);
        nameTextField.setStyle("-fx-border-color: red;");
        passwordTextField.setStyle("-fx-border-color: red;");
    }

    @FXML
    private void handleExitAction() {
        dialogStage.close();
    }

    @FXML
    private void handleCreateAccountAction(ActionEvent event) throws IOException {
        appController.showCreateAccountDialog();
    }
}
