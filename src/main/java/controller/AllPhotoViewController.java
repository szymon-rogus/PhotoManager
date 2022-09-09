package controller;

import app.AppManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lombok.NoArgsConstructor;
import model.Album;
import model.Photo;
import model.Tag;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@NoArgsConstructor
public class AllPhotoViewController {

    private AppController appController;

    private Session session;

    ObservableList<Photo> allPhotoList;

    @FXML
    private Button backButton;

    @FXML
    private Button showMostPopularTags;

    @FXML
    private TextField searchField;

    @FXML
    private Button searchName;

    @FXML
    private Button searchLocalization;

    @FXML
    private Button searchDate;

    @FXML
    private Button showAll;

    @FXML
    private Button searchTags;

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

        photosTable.setOnMouseClicked(event -> {
            photosTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

            if(event.getClickCount() == 2){
                try {
                    appController.showPhotoDialog(photosTable.getSelectionModel().getSelectedItem());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        reload();
    }

    @FXML
    private void showAll(ActionEvent event) {
        reload();
    }

    public void showMostPopularTagFrame(String tag) {
        JDialog.setDefaultLookAndFeelDecorated(true);
        Frame frame = new Frame("Most popular Tag");
        if(tag == null) {
            JOptionPane.showMessageDialog(frame, "Found 0 tags");
        }
        else {
            JOptionPane.showMessageDialog(frame, "The most popular Tag is ' " + tag + " '");
        }
    }

    @FXML
    public void handleSortByTags(ActionEvent event) {
        ObservableList<Tag> TagList = FXCollections.observableArrayList();
        final ObservableList<Photo> photoList = FXCollections.observableArrayList();
        this.session = AppManager.getSessionFactory().getCurrentSession();
        final Transaction tx = session.beginTransaction();

        for(Photo photo : allPhotoList) {
            TagList.addAll(photo.getTags());
        }

        String mostCommonTag = TagList.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(Tag::getName, Collectors.counting()))
                .entrySet().stream().max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey).orElse(null);

        for(Photo photo : allPhotoList) {
            for(Tag tag : photo.getTags()) {
                if(tag.getName().equals(mostCommonTag)){
                    photoList.add(photo);

                }
            }
        }

        tx.commit();
        photosTable.setItems(photoList);

        showMostPopularTagFrame(mostCommonTag);
    }

    @FXML
    private void handleSearchNameAction(ActionEvent event) {
        final ObservableList<Photo> photoList = FXCollections.observableArrayList();
        this.session = AppManager.getSessionFactory().getCurrentSession();
        final Transaction tx = session.beginTransaction();

        String data = searchField.getText();
        for(Photo photo : allPhotoList) {
            if(photo.getName().contains(data) && !photoList.contains(photo))
                photoList.add(photo);
        }

        tx.commit();
        photosTable.setItems(photoList);
    }

    @FXML
    private void handleSearchLocalizationAction(ActionEvent event) {
        final ObservableList<Photo> photoList = FXCollections.observableArrayList();
        this.session = AppManager.getSessionFactory().getCurrentSession();
        final Transaction tx = session.beginTransaction();

        String data = searchField.getText();
        for(Photo photo : allPhotoList) {
            if(photo.getLocalization().contains(data) && !photoList.contains(photo))
                photoList.add(photo);
        }

        tx.commit();
        photosTable.setItems(photoList);
    }

    @FXML
    private void handleSearchTagsAction(ActionEvent event) {
        final ObservableList<Photo> photoList = FXCollections.observableArrayList();
        this.session = AppManager.getSessionFactory().getCurrentSession();
        final Transaction tx = session.beginTransaction();

        String data = searchField.getText();
        for(Photo photo : allPhotoList) {
            if(data.equals("")) {
                photoList.add(photo);
            } else {
                for (Tag tag : photo.getTags()) {
                    if (tag.getName().contains(data) && !photoList.contains(photo)) {
                        photoList.add(photo);
                    }
                }
            }
        }

        tx.commit();
        photosTable.setItems(photoList);
    }

    @FXML
    private void handleSearchDateAction(ActionEvent event) {
        final ObservableList<Photo> photoList = FXCollections.observableArrayList();
        this.session = AppManager.getSessionFactory().getCurrentSession();
        final Transaction tx = session.beginTransaction();

        String data = searchField.getText();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        for(Photo photo : allPhotoList) {
            try {
                if(data.equals("")) {
                    photoList.add(photo);
                }
                else if(photo.getDate() != null && dateFormat.format(photo.getDate()).equals(data)) {
                    photoList.add(photo);
                }
            } catch ( IllegalArgumentException e) {
                e.getStackTrace();
            }
        }

        tx.commit();
        photosTable.setItems(photoList);
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

    public void reload() {
        photosTable.setItems(getPhotos());
    }

    public void setAppController(AppController appController) {
        this.appController = appController;
    }

    private ObservableList<Photo> getPhotos() {
        allPhotoList = FXCollections.observableArrayList();
        this.session = AppManager.getSessionFactory().getCurrentSession();
        final Transaction tx = session.beginTransaction();
        for(Album album : AppManager.getSessionUser().getAlbums()) {
            allPhotoList.addAll(album.getPhotoList());
        }
        tx.commit();

        return allPhotoList;
    }
}