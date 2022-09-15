package controller.mainView;

import app.AppManager;
import controller.AppController;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import lombok.NoArgsConstructor;
import model.Album;
import model.User;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.swing.*;
import java.io.IOException;
import java.util.Date;
import java.util.List;


@NoArgsConstructor
public class AlbumViewController {

    private AppController appController;

    private Session session;

    @FXML
    private TableView<Album> albumsTable;

    @FXML
    private TableColumn<Album, String> nameColumn;

    @FXML
    private TableColumn<Album, Date> creationDateColumn;

    @FXML
    private TableColumn<Album, Date> modificationDateColumn;

    @FXML
    private Button createAlbumButton;

    @FXML
    private Button deleteAlbumButton;

    @FXML
    private Button allPhotoViewButton;

    @FXML
    private Button changeEmailButton;

    @FXML
    private Button logoutButton;

    public void setAppController(AppController appController) {
        this.appController = appController;
    }

    @FXML
    private void initialize() {
        this.session = AppManager.getSessionFactory().getCurrentSession();
        setColumnsName();
        albumsTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        albumsTable.setOnMouseClicked(this::openAlbum);
        deleteAlbumButton.disableProperty().bind(Bindings.isEmpty(albumsTable.getSelectionModel().getSelectedItems()));

        reload();
        albumsTable.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            if (e.getButton() == MouseButton.SECONDARY) {
                albumsTable.getSelectionModel().clearSelection();
            }
        });
    }

    private void setColumnsName() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        creationDateColumn.setCellValueFactory(new PropertyValueFactory<>("creationDate"));
        modificationDateColumn.setCellValueFactory(new PropertyValueFactory<>("modificationDate"));
    }

    private void openAlbum(MouseEvent event) {
        if(event.getClickCount() == 2){
            try {
                appController.showPhotoView(albumsTable.getSelectionModel().getSelectedItem());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void reload() {
        albumsTable.setItems(getAlbums());
    }

    public static ObservableList<Album> getAlbums() {
        final ObservableList<Album> albumList = FXCollections.observableArrayList();
        albumList.addAll(AppManager.getSessionUser().getAlbums());

        return albumList;
    }

    @FXML
    private void handleCreateAlbumAction(ActionEvent event) throws IOException {
        appController.showCreateAlbumDialog();
        reload();
    }

    @FXML
    private void handleDeleteAlbumAction(ActionEvent event) {
        int response = getRemoveConfirmation();
        if (response == JOptionPane.NO_OPTION) {
            return;
        }
        session = AppManager.getSessionFactory().openSession();
        removeAlbums(albumsTable.getSelectionModel().getSelectedItems());

        Transaction tx = session.beginTransaction();
        session.update(AppManager.getSessionUser());
        tx.commit();
        reload();
    }

    private int getRemoveConfirmation() {
        return JOptionPane.showConfirmDialog(null,
                "Are you sure you want to remove this album?", "Remove album",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
    }

    private void removeAlbums(List<Album> albumsToRemove) {
        for (Album album : albumsToRemove) {
            for (User user : album.getUsers()) {
                user.removeFromUser(album);
            }
            session.remove(album);
        }
    }

    @FXML
    private void handleAllPhotoViewAction(ActionEvent event) throws IOException {
        appController.showAllPhotoView();
    }

    @FXML
    private void handleChangeEmail(ActionEvent event) throws IOException {
        appController.showChangeEmailDialog();
    }

    @FXML
    private void handleLogout(ActionEvent event) throws IOException {
        appController.initRootLayout();
    }
}
