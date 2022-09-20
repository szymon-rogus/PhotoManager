package controller.albums;

import app.AppManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import model.Album;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class ChangeAlbumNameDialog extends AbstractChangeAlbumDialog {

    private String oldName;

    public void setOldName(String oldName) {
        this.oldName = oldName;
        textField.setText(oldName);
    }

    @FXML
    @Override
    protected void handleOkAction(ActionEvent event) {
        final Session session = AppManager.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();

        Query<Album> query = session.createQuery("SELECT a FROM Album a WHERE a.name=:name", Album.class);
        query.setParameter("name", oldName);
        Album album = query.uniqueResult();

        album.setName(textField.getText());
        session.update(album);
        AppManager.getSessionUser().renameAlbum(oldName, textField.getText());
        tx.commit();
        session.close();
        dialogStage.close();
    }
}
