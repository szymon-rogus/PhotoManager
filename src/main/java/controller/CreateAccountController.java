package controller;

import app.AppManager;
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
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;

@NoArgsConstructor
public class CreateAccountController {

    private Stage dialogStage;

    @FXML
    private TextField nameTextField;

    @FXML
    private PasswordField passwordTextField;

    @FXML
    private PasswordField repeatPasswordTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField repeatEmailTextField;

    @FXML
    private Button cancelButton;

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
    private void handleCancelAction() {
        dialogStage.close();
    }

    @FXML
    private void handleCreateAccountAction() throws NoSuchAlgorithmException, InvalidKeySpecException {
        final boolean isValid = validate();
        if (isValid) {
            final SecureRandom random = new SecureRandom();
            final byte[] salt = new byte[16];
            random.nextBytes(salt);
            final KeySpec spec = new PBEKeySpec(passwordTextField.getText().toCharArray(), salt, 65536, 128);
            final SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            final byte[] hash = factory.generateSecret(spec).getEncoded();
            final Session session = AppManager.getSessionFactory().getCurrentSession();
            final Transaction tx = session.beginTransaction();
            final User user = User.builder()
                    .name(nameTextField.getText())
                    .password(hash)
                    .email(emailTextField.getText().isEmpty() ? null : emailTextField.getText())
                    .albums(new ArrayList<>())
                    .salt(salt)
                    .build();
            session.save(user);
            tx.commit();
            dialogStage.close();
        }
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    private boolean validate() {
        if (nameTextField.getText().isEmpty()) {
            errorLabel.setText("Brak nazwy użytkownika!");
            errorLabel.setVisible(true);
            return false;
        }
        if (passwordTextField.getText().isEmpty()) {
            errorLabel.setText("Brak hasła!");
            errorLabel.setVisible(true);
            return false;
        }
        if (!passwordTextField.getText().equals(repeatPasswordTextField.getText())) {
            errorLabel.setText("Powtórzone hasło się nie zgadza!");
            errorLabel.setVisible(true);
            return false;
        }
        if (!emailTextField.getText().isEmpty()) {
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
        }
        final Session session = AppManager.getSessionFactory().getCurrentSession();
        final Transaction tx = session.beginTransaction();
        final Query query = session.createQuery("SELECT u FROM User u WHERE u.name=:name", User.class);
        query.setParameter("name", nameTextField.getText());
        final User user = (User) query.uniqueResult();
        tx.commit();
        if (user != null) {
            errorLabel.setText("Użytkownik o podanej nazwie istnieje!");
            errorLabel.setVisible(true);
            return false;
        }
        return true;
    }
}
