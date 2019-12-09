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
import model.Photo;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.IOException;
import java.util.Date;

@NoArgsConstructor
public class PhotoViewController {

    private AppController appController;

    private Album album;

    private Session session;

    @FXML
    Button addPhotoButton;

    @FXML
    Button backButton;

    @FXML
    private TableView<Photo> photosTable;

    @FXML
    private TableColumn<Photo, String> nameColumn;

    @FXML
    private TableColumn<Photo, String> localizationColumn;

    @FXML
    private TableColumn<Photo, Date> dateColumn;

    @FXML
    private TableColumn<Photo, String> tagsColumn;


    @FXML
    private void handleAddPhotoAction(ActionEvent event) throws IOException {
        appController.showAddPhotoDialog(album);
        reload();
    }

    @FXML
    private void handleBackAction(ActionEvent event) throws IOException{
        appController.initRootLayout();
    }

    @FXML
    private void handlePhotoClickedAction(MouseEvent event) throws IOException {
        if (event.getClickCount() == 2) {
            appController.showPhotoDialog(photosTable.getSelectionModel().getSelectedItem());
        }
    }

    @FXML
    private void initialize(){
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        localizationColumn.setCellValueFactory(new PropertyValueFactory<>("localization"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
//        tagsColumn.setCellValueFactory(new PropertyValueFactory<>("tags")); //TODO
    }

    public void reload() {
        photosTable.setItems(getPhotos());
    }

    public void setAlbum(Album album) {
        this.album = album;
        reload();
    }

    public void setAppController(AppController appController){
        this.appController = appController;
    }

    private ObservableList<Photo> getPhotos() {
        final ObservableList<Photo> photoList = FXCollections.observableArrayList();
        this.session = AppManager.getSessionFactory().getCurrentSession();
        final Transaction tx = session.beginTransaction();

        photoList.addAll(album.getPhotoList());
        //photoList.addAll(session.createQuery("FROM Photo",Photo.class).list());
        tx.commit();

        return photoList;
    }
}
