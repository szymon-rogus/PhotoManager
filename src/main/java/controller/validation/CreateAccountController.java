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
import java.util.Objects;

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
    private Button cancelButton;

    @FXML
    private Button createAccountButton;

    @FXML
    private Label errorLabel;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    private void initialize() {
        this.errorLabel.setVisible(false);
        this.errorLabel.setTextFill(Color.RED);

        passwordTextField.textProperty().addListener((observable, oldValue, newValue)
                -> checkForPasswordsMatch(newValue, repeatPasswordTextField));
        repeatPasswordTextField.textProperty().addListener((observable, oldValue, newValue)
                -> checkForPasswordsMatch(newValue, passwordTextField));
    }

    private void checkForPasswordsMatch(String value, PasswordField otherPasswordField) {
        if (value.trim().isEmpty() && otherPasswordField.getText().trim().isEmpty()) {
            repeatPasswordTextField.setStyle("-fx-border-color: transparent;");
        } else {
            repeatPasswordTextField.setStyle(Objects.equals(value, otherPasswordField.getText())
                    ? "-fx-border-color: green" : "-fx-border-color: red");
        }
    }

    @FXML
    private void handleCancelAction() {
        dialogStage.close();
    }

    @FXML
    private void handleCreateAccountAction() throws NoSuchAlgorithmException, InvalidKeySpecException {
        resetBordersStyle();
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

    private void resetBordersStyle() {
        nameTextField.setStyle("-fx-border-color: transparent;");
        passwordTextField.setStyle("-fx-border-color: transparent;");
        repeatPasswordTextField.setStyle("-fx-border-color: transparent;");
        emailTextField.setStyle("-fx-border-color: transparent;");
    }

    private boolean validate() {
        if (nameTextField.getText().isEmpty()) {
            return showError("Missing login!", nameTextField);
        }
        if (passwordTextField.getText().isEmpty()) {
            return showError("Missing password!", passwordTextField);
        }
        if (!passwordTextField.getText().equals(repeatPasswordTextField.getText())) {
            return showError("Passwords mismatch!", repeatPasswordTextField);
        }
        if (!emailTextField.getText().isEmpty()) {
            return showError("Missing mail!", emailTextField);
        }
        if (!emailTextField.getText().matches(".*@.*\\..*")) {
            return showError("Wrong mail format!", emailTextField);
        }

        return validateCorrectData();
    }

    private boolean showError(String errorMessage, TextField errorField) {
        errorLabel.setText(errorMessage);
        errorLabel.setVisible(true);
        errorField.setStyle("-fx-border-color: red");
        return false;
    }

    private boolean validateCorrectData() {
        final Session session = AppManager.getSessionFactory().getCurrentSession();
        final Transaction tx = session.beginTransaction();
        final Query<User> query = session.createQuery("SELECT u FROM User u WHERE u.name=:name", User.class);
        query.setParameter("name", nameTextField.getText());
        final User user = query.uniqueResult();
        tx.commit();

        return user == null || showError("Login already exists!", nameTextField);
    }
}
