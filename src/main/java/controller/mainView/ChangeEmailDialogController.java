package controller.mainView;

import app.AppManager;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableBooleanValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.NoArgsConstructor;
import model.User;
import org.hibernate.Session;
import org.hibernate.Transaction;


@NoArgsConstructor
public class ChangeEmailDialogController extends AbstractChangeDialog {

    @FXML
    private TextField emailTextField;

    public void setDialogStage(Stage stage) {
        this.dialogStage = stage;
    }

    @FXML
    private void initialize() {
        okButton.disableProperty().bind(wrongMailFormat());
    }

    private ObservableBooleanValue wrongMailFormat() {
        return Bindings.createBooleanBinding(() -> emailTextField.getText().isEmpty()
                        || !emailTextField.getText().matches(".*@.*\\..*"), emailTextField.textProperty());
    }

    @FXML
    protected void handleOkAction(ActionEvent event) {
        final User user = AppManager.getSessionUser();
        final Session session = AppManager.getSessionFactory().getCurrentSession();
        final Transaction tx = session.beginTransaction();
        user.setEmail(emailTextField.getText());
        session.update(user);
        tx.commit();
        dialogStage.close();
    }
}
