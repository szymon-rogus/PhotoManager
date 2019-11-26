package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import lombok.NoArgsConstructor;
import model.Album;
import model.Tag;
import org.hibernate.Transaction;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;


@NoArgsConstructor
public class AlbumViewController {

    private Stage primaryStage;

    private CreateAlbumDialogController createAlbumDialogController;

    private AppController appController;

    @FXML
    private TableView<Album> albumsTable;

    @FXML
    private TableColumn<Album, String> nameColumn;

    @FXML
    private TableColumn<Album, LocalDate> creationDateColumn;

    @FXML
    private TableColumn<Album, LocalDate> modificationDateColumn;

    @FXML
    private TableColumn<Album, String> tagsColumn;

    @FXML
    private Button createAlbumButton;

    @FXML
    private void handleCreateAlbumAction(ActionEvent event) {
        this.createAlbumDialogController = new CreateAlbumDialogController();
        createAlbumDialogController.showDialogWindow(primaryStage);
    }

    @FXML
    private void initialize(){
        nameColumn.setCellValueFactory(new PropertyValueFactory<Album, String>("name"));
        creationDateColumn.setCellValueFactory(new PropertyValueFactory<Album, LocalDate>("creationDate"));
        modificationDateColumn.setCellValueFactory(new PropertyValueFactory<Album, LocalDate>("modificationDate"));
        //tagsColumn.setCellValueFactory((new PropertyValueFactory<Album, String>("tagMap"))); // 5 najliczniejszych, TODO
        albumsTable.setItems(getAllAlbums());
    }

    public void setAppController(AppController appController){
        this.appController = appController;
    }

    public ObservableList<Album> getAllAlbums(){
        ObservableList<Album> albumList = FXCollections.observableArrayList();
        Transaction tx = app.AppManager.getSessionFactory().getCurrentSession().
                beginTransaction();
        albumList.addAll(app.AppManager.getSessionFactory().getCurrentSession().createCriteria(Album.class).list());
        tx.commit();

        return albumList;
    }
}
