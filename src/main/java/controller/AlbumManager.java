package controller;

import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.PhotoAlbum;

import java.util.List;

public class AlbumManager {

    private List<PhotoAlbum> albumList;

    @FXML
    private TableView<PhotoAlbum> albumsTable;

    @FXML
    private TableColumn<PhotoAlbum, String> name;

    private void initialize(){
        albumsTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        name.setCellValueFactory(dataValue -> dataValue.getValue().getNameProperty());
    }

}
