package controller;

import app.AppManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import lombok.NoArgsConstructor;
import model.Album;
import model.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

@NoArgsConstructor
public class ShareAlbumDialogController {

    private Album album;

    private Stage dialogStage;

    @FXML
    private Label errorLabel;

    @FXML
    private Button shareButton;

    @FXML
    private Button cancelButton;

    @FXML
    private TextField nameTextField;

    @FXML
    private void initialize() {
        this.errorLabel.setVisible(false);
        this.errorLabel.setTextFill(Color.RED);
    }

    @FXML
    private void handleShareAction(ActionEvent event) {
        final boolean isValid = validate();
        if (isValid) {
            final Session session = AppManager.getSessionFactory().getCurrentSession();
            final Transaction tx = session.beginTransaction();
            final Query query = session.createQuery("SELECT u FROM User u WHERE u.name=:name", User.class);
            query.setParameter("name", nameTextField.getText());
            final User user = (User) query.uniqueResult();
            user.addAlbum(album);
            album.getUsers().add(user);
            session.merge(album);
            tx.commit();
            dialogStage.close();
        }
    }

    @FXML
    private void handleCancelAction(ActionEvent event) {
        dialogStage.close();
    }

    private boolean validate() {
        if (nameTextField.getText().isEmpty()) {
            errorLabel.setText("Brak nazwy użytkownika!");
            errorLabel.setVisible(true);
            return false;
        }
        final Session session = AppManager.getSessionFactory().getCurrentSession();
        final Transaction tx = session.beginTransaction();
        final Query query = session.createQuery("SELECT u FROM User u WHERE u.name=:name", User.class);
        query.setParameter("name", nameTextField.getText());
        final User user = (User) query.uniqueResult();
        tx.commit();
        if (user == null) {
            errorLabel.setText("Użytkownik nie istnieje!");
            errorLabel.setVisible(true);
            return false;
        }
        if(user.getAlbums().contains(album) || user == AppManager.getSessionUser()) {
            errorLabel.setText("Użytkownik posiada już ten album!");
            errorLabel.setVisible(true);
            return false;
        }
        return true;
    }

    public void setDialogStage(Stage stage) {
        this.dialogStage = stage;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

}
