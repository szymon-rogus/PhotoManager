package controller.albums;

import app.AppManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import lombok.NoArgsConstructor;
import model.Album;
import org.hibernate.Session;
import org.hibernate.Transaction;


@NoArgsConstructor
public class CreateAlbumDialog extends AbstractChangeAlbumDialog {

    @FXML
    @Override
    protected void handleOkAction(ActionEvent event) {
        Session session = AppManager.getSessionFactory().getCurrentSession();
        final Album album = new Album(textField.getText());
        final Transaction tx = session.beginTransaction();
        session.save(album);
        AppManager.getSessionUser().addAlbum(album);
        tx.commit();
        session.close();
        dialogStage.close();
    }
}
