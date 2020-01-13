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

    @FXML
    private void initialize() {
        this.errorLabel.setVisible(false);
        this.errorLabel.setTextFill(Color.RED);
    }

    @FXML
    private void handleLogInAction() throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        if (nameTextField.getText().isEmpty()) {
            errorLabel.setText("Brak nazwy użytkownika!");
            errorLabel.setVisible(true);
            return;
        }
        if (passwordTextField.getText().isEmpty()) {
            errorLabel.setText("Brak hasła!");
            errorLabel.setVisible(true);
            return;
        }
        final Session session = AppManager.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        final Query query = session.createQuery("SELECT u FROM User u WHERE u.name=:name", User.class);
        query.setParameter("name", nameTextField.getText());
        final User user = (User) query.uniqueResult();
        tx.commit();
        if (user == null) {
            errorLabel.setText("Użytkownik nie istnieje!");
            errorLabel.setVisible(true);
            return;
        }
        final KeySpec spec = new PBEKeySpec(passwordTextField.getText().toCharArray(), user.getSalt(), 65536, 128);
        final SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        final byte[] hash = factory.generateSecret(spec).getEncoded();
        if (!Arrays.equals(hash, user.getPassword())) {
            errorLabel.setText("Hasło niepoprawne!");
            errorLabel.setVisible(true);
            return;
        }
        AppManager.setSessionUser(user);
        appController.showAlbumView();
    }

    @FXML
    private void handleExitAction() {
        dialogStage.close();
    }

    @FXML
    private void handleCreateAccountAction(ActionEvent event) throws IOException {
        appController.showCreateAccountDialog();
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setAppController(AppController appController) {
        this.appController = appController;
    }
}
