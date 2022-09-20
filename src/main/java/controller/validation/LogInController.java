package controller.validation;

import app.AppManager;
import controller.AppController;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import lombok.NoArgsConstructor;
import model.User;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;

@NoArgsConstructor
public class LogInController extends AbstractValidationController {

    private AppController appController;

    @FXML
    private Button exitButton;

    @FXML
    private Button logInButton;

    @FXML
    private Button createAccountButton;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setAppController(AppController appController) {
        this.appController = appController;
    }

    @FXML
    @Override
    protected void initialize() {
        super.initialize();
        nameTextField.setPromptText("Enter name...");

        logInButton.disableProperty()
                .bind(Bindings.or(nameTextField.textProperty().isEmpty(), passwordTextField.textProperty().isEmpty()));
    }

    @FXML
    private void handleLogInAction() throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        final User user = getUserFromData();
        if (user == null) {
            loginError();
            return;
        }

        final byte[] hash = generateHashedPassword(user);
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

    private byte[] generateHashedPassword(User user) throws InvalidKeySpecException, NoSuchAlgorithmException {
        final KeySpec spec = new PBEKeySpec(passwordTextField.getText().toCharArray(), user.getSalt(), 65536, 128);
        final SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        return factory.generateSecret(spec).getEncoded();
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
