package controller;

import app.AppManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lombok.NoArgsConstructor;
import model.Album;
import org.hibernate.Session;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Date;


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
    private Button changeEmailButton;

    @FXML
    private void handleCreateAlbumAction(ActionEvent event) throws IOException {
        appController.showCreateAlbumDialog();
        reload();
    }

    @FXML
    private void handleAlbumClickedAction(MouseEvent event) throws IOException {
        if (albumsTable.getSelectionModel().getSelectedItem() != null) {
            if (event.getClickCount() == 2) {
                appController.showPhotoView(albumsTable.getSelectionModel().getSelectedItem());
            }
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
    private void initialize() {
        this.session = AppManager.getSessionFactory().getCurrentSession();
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        creationDateColumn.setCellValueFactory(new PropertyValueFactory<>("creationDate"));
        modificationDateColumn.setCellValueFactory(new PropertyValueFactory<>("modificationDate"));
        reload();
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
