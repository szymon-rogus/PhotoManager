package controller.validation;

import app.AppManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import util.Common;

public abstract class AbstractValidationController {

    protected Stage dialogStage;

    @FXML
    protected TextField nameTextField;

    @FXML
    protected PasswordField passwordTextField;

    @FXML
    protected Label errorLabel;

    @FXML
    protected void initialize() {
        errorLabel.setVisible(false);
        errorLabel.setTextFill(Color.RED);
        nameTextField.setTextFormatter(new TextFormatter<>(Common.validationOperator));
        passwordTextField.setTextFormatter(new TextFormatter<>(Common.validationOperator));
    }

    protected User getUserFromData() {
        final Session session = AppManager.getSessionFactory().getCurrentSession();
        final Transaction tx = session.beginTransaction();
        final Query<User> query = session.createQuery("SELECT u FROM User u WHERE u.name=:name", User.class);
        query.setParameter("name", nameTextField.getText());
        final User user = query.uniqueResult();
        tx.commit();

        return user;
    }
}
