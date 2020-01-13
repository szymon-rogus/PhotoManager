package controller;

import app.AppManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Album;
import model.Photo;
import model.Tag;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.AlbumChange;
import util.TagParser;

import java.io.*;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class EditPhotoDialogController {

    private Stage dialogStage;

    private Photo photo;

    private Album album;

    private Session session;

    @FXML
    private ImageView imageView;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private TextField localizationTextField;

    @FXML
    private TextField dateTextField;

    @FXML
    private TextField tagsTextField;

    @FXML
    private Button okButton;

    @FXML
    private Button cancelButton;

    @FXML
    private void initialize() {
        this.session = AppManager.getSessionFactory().getCurrentSession();
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;

        imageView.setImage(new Image(new ByteArrayInputStream(photo.getImage())));
        if(photo.getDescription() != null) {
            this.descriptionTextArea.setText(photo.getDescription());
        }
        if(photo.getDate() != null) {
            this.dateTextField.setText((new SimpleDateFormat("yyyy-MM-dd")).format(photo.getDate()));
        }
        if(photo.getLocalization() != null) {
            this.localizationTextField.setText(photo.getLocalization());
        }

        StringBuilder tags = new StringBuilder();
        for(Tag tag : photo.getTags()) {
            tags.append(tag.getName()).append(" ");
        }
        this.tagsTextField.setText(tags.toString());
    }

    @FXML
    private void handleCancelAction(ActionEvent event) {
        dialogStage.close();
    }

    @FXML
    public void handleOkAction(ActionEvent event) throws ParseException {
        photo.setDescription(descriptionTextArea.getText());
        photo.setLocalization(localizationTextField.getText());
        photo.setTags(TagParser.parse(tagsTextField.getText()));
        photo.setDate(new SimpleDateFormat("yyyy-MM-dd").parse(dateTextField.getText()));

        final Transaction tx = session.beginTransaction();
        session.update(photo);
        tx.commit();
        dialogStage.close();
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }
}