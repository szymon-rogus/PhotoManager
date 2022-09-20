package controller.albums;

import app.AppManager;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableBooleanValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextFormatter;
import lombok.NoArgsConstructor;
import model.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.Common;


@NoArgsConstructor
public class ChangeEmailDialog extends AbstractChangeDialog {

    @FXML
    private void initialize() {
        textField.setTextFormatter(new TextFormatter<>(Common.validationOperator));
        okButton.disableProperty().bind(wrongMailFormat());
    }

    private ObservableBooleanValue wrongMailFormat() {
        return Bindings.createBooleanBinding(() -> textField.getText().isEmpty()
                        || !textField.getText().matches(".*@.*\\..*"), textField.textProperty());
    }

    @FXML
    @Override
    protected void handleOkAction(ActionEvent event) {
        final User user = AppManager.getSessionUser();
        final Session session = AppManager.getSessionFactory().getCurrentSession();
        final Transaction tx = session.beginTransaction();
        user.setEmail(textField.getText());
        session.update(user);
        tx.commit();
        dialogStage.close();
    }
}
