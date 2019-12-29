package controller;

import app.AppManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lombok.NoArgsConstructor;
import model.Album;
import model.Photo;
import model.Tag;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.swing.*;
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
    private Button removePhotoButton;

    @FXML
    private Button editPhotoButton;

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
    private void handleRemovePhotoAction(ActionEvent event) throws IOException {
        JDialog.setDefaultLookAndFeelDecorated(true);
        int response = JOptionPane.showConfirmDialog(null, "Are you sure?", "Confirm",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (response == JOptionPane.YES_OPTION) {
            session = AppManager.getSessionFactory().openSession();
            List<Photo> photosToRemove = photosTable.getSelectionModel().getSelectedItems();
            for(Photo photo : photosToRemove){
                album.removeFromAlbum(photo);
            }
            Transaction tx = session.beginTransaction();
            session.update(album);
            tx.commit();
            reload();
        }
    }

    @FXML
    private void handleEditButton(ActionEvent event) {

    }

    @FXML
    private void handleBackAction(ActionEvent event) throws IOException {
        appController.showAlbumView();
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

        photosTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                photosTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

                if(event.getClickCount() == 2){
                    try {
                        appController.showPhotoDialog(photosTable.getSelectionModel().getSelectedItem());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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
