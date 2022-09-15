package controller.albums;

import app.AppManager;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableBooleanValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import model.Album;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import util.Common;

import java.util.List;
import java.util.stream.Collectors;

public class ChangeAlbumNameDialog extends AbstractChangeDialog {

    @FXML
    private TextField nameTextField;

    private String oldName;

    public void setOldName(String oldName) {
        this.oldName = oldName;
    }

    @FXML
    private void initialize() {
        nameTextField.setTextFormatter(new TextFormatter<>(Common.validationOperator));
        List<String> takenNames = getExistingAlbumNames();
        okButton.disableProperty().bind(forbiddenValues(takenNames));
    }

    private List<String> getExistingAlbumNames() {
        final Session session = AppManager.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        List<Album> albums = session.createQuery("SELECT a from Album a", Album.class).list();
        tx.commit();
        session.close();
        return albums.stream().map(Album::getName).collect(Collectors.toList());
    }

    private ObservableBooleanValue forbiddenValues(List<String> takenNames) {
        return Bindings.createBooleanBinding(() -> nameTextField.getText().isEmpty()
                || takenNames.contains(nameTextField.getText()), nameTextField.textProperty());
    }

    @Override
    protected void handleOkAction(ActionEvent event) {
        final Session session = AppManager.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();

        Query<Album> query = session.createQuery("SELECT a FROM Album a WHERE a.name=:name", Album.class);
        query.setParameter("name", oldName);
        Album album = query.uniqueResult();

        album.setName(nameTextField.getText());
        session.update(album);
        AppManager.getSessionUser().renameAlbum(oldName, nameTextField.getText());
        tx.commit();
        session.close();
        dialogStage.close();
    }
}
