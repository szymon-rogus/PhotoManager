package controller.albums;

import app.AppManager;
import controller.AppController;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableBooleanValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
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
import util.Common;

import javax.swing.*;
import java.io.IOException;
import java.util.Date;
import java.util.List;


@NoArgsConstructor
public class AlbumViewController {

    private AppController appController;

    @FXML
    private TableView<Album> albumsTable;

    @FXML
    private TableColumn<Album, String> nameColumn;

    @FXML
    private TableColumn<Album, Date> creationDateColumn;

    @FXML
    private TableColumn<Album, Date> modificationDateColumn;

    @FXML
    private MenuItem createAlbumButton;

    @FXML
    private MenuItem renameAlbumButton;

    @FXML
    private MenuItem deleteAlbumButton;

    @FXML
    private MenuItem changeEmailButton;

    @FXML
    private MenuItem logoutButton;

    @FXML
    private MenuItem exitButton;

    public void setAppController(AppController appController) {
        this.appController = appController;
    }

    @FXML
    private void initialize() {
        setColumnsName();
        setIcons();
        albumsTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        albumsTable.setOnMouseClicked(this::openAlbum);
        deleteAlbumButton.disableProperty().bind(Bindings.isEmpty(albumsTable.getSelectionModel().getSelectedItems()));
        renameAlbumButton.disableProperty().bind(anyOrManySelection());

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

    private void setIcons() {
        Common.setIconForElement(createAlbumButton, "icons/add.png");
        Common.setIconForElement(deleteAlbumButton, "icons/remove.png");
        Common.setIconForElement(changeEmailButton, "icons/emailChange.png");
        Common.setIconForElement(renameAlbumButton, "icons/rename.png");
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

    private ObservableBooleanValue anyOrManySelection() {
        return Bindings.createBooleanBinding(() -> albumsTable.getSelectionModel().getSelectedItems().size() != 1,
                albumsTable.getSelectionModel().getSelectedItems());
    }

    private void reload() {
        albumsTable.setItems(getAlbums());
        albumsTable.refresh();
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
    private void handleRenameAlbumAction(ActionEvent event) throws IOException {
        String oldName = albumsTable.getSelectionModel().getSelectedItems().stream()
                .findFirst()
                .map(Album::getName)
                .get();
        appController.showRenameAlbumDialog(oldName);
        reload();
    }

    @FXML
    private void handleDeleteAlbumAction(ActionEvent event) {
        int response = getRemoveConfirmation();
        if (response == JOptionPane.NO_OPTION) {
            return;
        }
        Session session = AppManager.getSessionFactory().openSession();
        removeAlbums(albumsTable.getSelectionModel().getSelectedItems(), session);

        Transaction tx = session.beginTransaction();
        session.update(AppManager.getSessionUser());
        tx.commit();
        session.close();
        reload();
    }

    private int getRemoveConfirmation() {
        return JOptionPane.showConfirmDialog(null,
                "Are you sure you want to remove this album?", "Remove album",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
    }

    private void removeAlbums(List<Album> albumsToRemove, Session session) {
        for (Album album : albumsToRemove) {
            for (User user : album.getUsers()) {
                user.removeFromUser(album);
            }
            session.remove(album);
        }
    }

    @FXML
    private void handleChangeEmail(ActionEvent event) throws IOException {
        appController.showChangeEmailDialog();
    }

    @FXML
    private void handleLogout(ActionEvent event) throws IOException {
        appController.initRootLayout();
    }

    @FXML
    private void handleExit() {
        AppManager.exit();
    }
}
