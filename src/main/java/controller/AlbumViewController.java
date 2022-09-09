package controller;

import app.AppManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.NoArgsConstructor;
import model.Album;
import model.User;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.swing.*;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;


@NoArgsConstructor
public class AlbumViewController {

    private AppController appController;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm");

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

    @FXML
    private void initialize() {
        this.session = AppManager.getSessionFactory().getCurrentSession();
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        creationDateColumn.setCellValueFactory(new PropertyValueFactory<>("creationDate"));
        modificationDateColumn.setCellValueFactory(new PropertyValueFactory<>("modificationDate"));

        albumsTable.setOnMouseClicked(event -> {
            albumsTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

            if(event.getClickCount() == 2){
                try {
                    appController.showPhotoView(albumsTable.getSelectionModel().getSelectedItem());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        reload();
    }

    @FXML
    private void handleCreateAlbumAction(ActionEvent event) throws IOException {
        appController.showCreateAlbumDialog();
        reload();
    }

    @FXML
    private void handleDeleteAlbumAction(ActionEvent event) {
        if(albumsTable.getSelectionModel().getSelectedItem() != null) {
            JDialog.setDefaultLookAndFeelDecorated(true);
            int response = JOptionPane.showConfirmDialog(null, "Jeste� pewien?", "Potwierd�",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (response == JOptionPane.YES_OPTION) {
                session = AppManager.getSessionFactory().openSession();
                List<Album> albumsToRemove = albumsTable.getSelectionModel().getSelectedItems();
                for (Album album : albumsToRemove) {
                    for (User user : album.getUsers()) {
                        user.removeFromUser(album);
                    }
                    session.remove(album);
                }

                Transaction tx = session.beginTransaction();
                session.update(AppManager.getSessionUser());
                tx.commit();
                reload();
            }
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

    public void reload() {
        albumsTable.setItems(getAllAlbums());
    }

    public void setAppController(AppController appController) {
        this.appController = appController;
    }

    public ObservableList<Album> getAllAlbums() {
        final ObservableList<Album> albumList = FXCollections.observableArrayList();
        albumList.addAll(AppManager.getSessionUser().getAlbums());

        return albumList;
    }
}
