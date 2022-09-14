package controller.validation;

import app.AppManager;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import lombok.NoArgsConstructor;
import model.User;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.Objects;

@NoArgsConstructor
public class CreateAccountController extends AbstractValidationController {

    @FXML
    private PasswordField repeatPasswordTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private Button cancelButton;

    @FXML
    private Button createAccountButton;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    @Override
    protected void initialize() {
        super.initialize();
        repeatPasswordTextField.setTextFormatter(new TextFormatter<>(validationOperator));
        emailTextField.setTextFormatter(new TextFormatter<>(validationOperator));

        passwordTextField.textProperty().addListener((observable, oldValue, newValue)
                -> checkForPasswordsMatch(newValue, repeatPasswordTextField.getText()));
        repeatPasswordTextField.textProperty().addListener((observable, oldValue, newValue)
                -> checkForPasswordsMatch(newValue, passwordTextField.getText()));

        createAccountButton.disableProperty()
                .bind(Bindings.notEqual(passwordTextField.textProperty(), repeatPasswordTextField.textProperty())
                        .or(nameTextField.textProperty().isEmpty())
                        .or(passwordTextField.textProperty().isEmpty())
                        .or(repeatPasswordTextField.textProperty().isEmpty())
                        .or(emailTextField.textProperty().isEmpty()));
    }

    private void checkForPasswordsMatch(String password, String repeatedPassword) {
        if (password.trim().isEmpty() && repeatedPassword.trim().isEmpty()) {
            repeatPasswordTextField.setStyle("-fx-border-color: transparent;");
        } else {
            repeatPasswordTextField.setStyle(Objects.equals(password, repeatedPassword)
                    ? "-fx-border-color: green": "-fx-border-color: red");
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
        final User user = getUserFromData();
        return user == null || showError("Login already exists!", nameTextField);
    }
}
