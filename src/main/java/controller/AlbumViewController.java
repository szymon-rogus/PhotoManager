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
import model.Album;
import model.Tag;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class AlbumViewController {

    private Stage primaryStage;

    private CreateAlbumDialogController createAlbumDialogController;

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

    public AlbumViewController(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.createAlbumDialogController = new CreateAlbumDialogController();
    }

    public void initRootLayout() {
        try {
            this.primaryStage.setTitle("PhotoManager");

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(app.AppManager.class
                    .getResource("../view/AlbumView.fxml"));
            BorderPane rootLayout = loader.load();

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void handleCreateAlbumAction(ActionEvent event) {
        createAlbumDialogController.showDialogWindow(primaryStage);
    }

    @FXML
    private void initialize(){
        nameColumn.setCellValueFactory(new PropertyValueFactory<Album, String>("name"));
        creationDateColumn.setCellValueFactory(new PropertyValueFactory<Album, LocalDate>("creationDate"));
        modificationDateColumn.setCellValueFactory(new PropertyValueFactory<Album, LocalDate>("modificationDate"));
        tagsColumn.setCellValueFactory((new PropertyValueFactory<Album, String>("tagMap"))); // 5 najliczniejszych, TODO
        albumsTable.setItems(getAllAlbums());
    }

    public ObservableList<Album> getAllAlbums(){
        ObservableList<Album> albumList = FXCollections.observableArrayList();
        albumList.addAll(app.AppManager.getSessionFactory().getCurrentSession().createCriteria(Album.class).list());

        return albumList;
    }
}
