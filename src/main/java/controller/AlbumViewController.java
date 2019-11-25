package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import lombok.NoArgsConstructor;
import model.Album;
import org.hibernate.Transaction;
import java.time.format.DateTimeFormatter;
import java.util.Date;


@NoArgsConstructor
public class AlbumViewController {

    private Stage primaryStage;

    private CreateAlbumDialogController createAlbumDialogController;

    private AppController appController;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm");

    @FXML
    private TableView<Album> albumsTable;

    @FXML
    private TableColumn<Album, String> nameColumn;

    @FXML
    private TableColumn<Album, Date> creationDateColumn;

    @FXML
    private TableColumn<Album, Date> modificationDateColumn;

    @FXML
    private TableColumn<Album, String> tagsColumn;

    @FXML
    private Button createAlbumButton;

    @FXML
    private void handleCreateAlbumAction(ActionEvent event) {
        appController.showDialogWindow();
        reload();
    }

    @FXML
    private void initialize(){
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        creationDateColumn.setCellValueFactory(new PropertyValueFactory<>("creationDate"));
        modificationDateColumn.setCellValueFactory(new PropertyValueFactory<>("modificationDate"));
        //tagsColumn.setCellValueFactory((new PropertyValueFactory<Album, String>("tagMap"))); // 5 najliczniejszych, TODO
        reload();
    }

    public void reload() {
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
