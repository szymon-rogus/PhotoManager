package controller;

import app.AppManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lombok.NoArgsConstructor;
import model.Album;
import model.Photo;
import model.Tag;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
public class PhotoViewController {

    private AppController appController;

    private Album album;

    private Session session;

    @FXML
    private Button addPhotoButton;

    @FXML
    private Button shareAlbumButton;

    @FXML
    private Button backButton;

    @FXML
    private TableView<Photo> photosTable;

    @FXML
    private TableColumn<Photo, String> nameColumn;

    @FXML
    private TableColumn<Photo, String> localizationColumn;

    @FXML
    private TableColumn<Photo, Date> dateColumn;

    @FXML
    private TableColumn<Photo, List<Tag>> tagsColumn;


    @FXML
    private void handleAddPhotoAction(ActionEvent event) throws IOException {
        appController.showAddPhotoDialog(album);
        reload();
    }

    @FXML
    private void handleBackAction(ActionEvent event) throws IOException {
        appController.showAlbumView();
    }

    @FXML
    private void handlePhotoClickedAction(MouseEvent event) throws IOException {
        if (event.getClickCount() == 2 && photosTable.getSelectionModel().getSelectedItem() != null) {
            appController.showPhotoDialog(photosTable.getSelectionModel().getSelectedItem());
        }
    }

    @FXML
    private void handleShareAlbum(ActionEvent event) throws IOException {
        appController.showShareAlbumDialog(album);
    }

    @FXML
    private void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        localizationColumn.setCellValueFactory(new PropertyValueFactory<>("localization"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        final PropertyValueFactory<Photo, List<Tag>> tagValueFactory = new PropertyValueFactory<>("tags");
        tagsColumn.setCellValueFactory(tagValueFactory);
        tagsColumn.setCellFactory(col -> new TableCell<Photo, List<Tag>>() {
            @Override
            public void updateItem(List<Tag> tags, boolean empty) {
                super.updateItem(tags, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(tags.stream().map(Tag::getName).collect(Collectors.joining(", ")));
                }
            }
        });
    }

    public void reload() {
        photosTable.setItems(getPhotos());
    }

    public void setAlbum(Album album) {
        this.album = album;
        reload();
    }

    public void setAppController(AppController appController) {
        this.appController = appController;
    }

    private ObservableList<Photo> getPhotos() {
        final ObservableList<Photo> photoList = FXCollections.observableArrayList();
        this.session = AppManager.getSessionFactory().getCurrentSession();
        final Transaction tx = session.beginTransaction();
        photoList.addAll(album.getPhotoList());
        tx.commit();

        return photoList;
    }
}
