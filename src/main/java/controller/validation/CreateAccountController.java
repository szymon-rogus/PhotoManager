package controller.validation;

import app.AppManager;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableStringValue;
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
import util.Common;

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
        repeatPasswordTextField.setTextFormatter(new TextFormatter<>(Common.validationOperator));
        emailTextField.setTextFormatter(new TextFormatter<>(Common.validationOperator));

        createPasswordStyleProperty();
        createDisableProperty();
    }

    private void createPasswordStyleProperty() {
        repeatPasswordTextField.styleProperty().bind(passwordsMatch());
    }

    private ObservableStringValue passwordsMatch() {
        return Bindings.createStringBinding(() -> {
            if (Objects.equals(passwordTextField.getText(), repeatPasswordTextField.getText())) {
                if (passwordTextField.getText().isEmpty()) {
                    return "-fx-border-color: transparent;";
                }
                return "-fx-border-color: green";
            }
            return "-fx-border-color: red";
        }, repeatPasswordTextField.textProperty(), passwordTextField.textProperty());
    }

    private void createDisableProperty() {
        createAccountButton.disableProperty()
                .bind(Bindings.notEqual(passwordTextField.textProperty(), repeatPasswordTextField.textProperty())
                        .or(nameTextField.textProperty().isEmpty())
                        .or(passwordTextField.textProperty().isEmpty())
                        .or(repeatPasswordTextField.textProperty().isEmpty())
                        .or(emailTextField.textProperty().isEmpty()));
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
            final User user = createUser(hash, salt);
            session.save(user);
            tx.commit();
            dialogStage.close();
        }
    }

    private void resetBordersStyle() {
        nameTextField.setStyle("-fx-border-color: transparent;");
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

    private User createUser(byte[] hash, byte[] salt) {
        return User.builder()
                .name(nameTextField.getText())
                .password(hash)
                .email(emailTextField.getText())
                .albums(new ArrayList<>())
                .salt(salt)
                .build();
    }
}
