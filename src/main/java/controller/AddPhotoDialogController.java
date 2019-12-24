package controller;

import app.AppManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class AddPhotoDialogController {

    private static final String TITLE = "Wybierz zdjęcie";

    private static final String FILE_CHOOSER_DESCRIPTION = "Image files";

    private static final List<String> EXTENSIONS = Arrays.asList("*.png", "*.jpg");

    private Stage dialogStage;

    private File uploadedPhoto;

    private Album album;

    private Session session;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField localizationTextField;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private TextArea tagsTextArea;

    @FXML
    private Button cancelButton;

    @FXML
    private Button okButton;

    @FXML
    private Button uploadPhotoButton;

    @FXML
    private void initialize() {
        this.session = AppManager.getSessionFactory().getCurrentSession();
    }

    @FXML
    private void handleCancelAction(ActionEvent event) {
        dialogStage.close();
    }

    @FXML
    private void handleOkAction(ActionEvent event) {
        if (uploadedPhoto != null && album != null) {
            Photo photo;

            List<Tag> tags = new ArrayList<>();
            if (tagsTextArea.getText() != null && !tagsTextArea.getText().equals("")) {
                tags = TagParser.parse(tagsTextArea.getText());
            }

            Date date = null;
            if (datePicker.getValue() != null) {
                date = Date.from(datePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            }

            try {
                if (nameTextField.getText() != null && !nameTextField.getText().equals("")) {
                    photo = new Photo(nameTextField.getText(), Files.readAllBytes(uploadedPhoto.toPath()), localizationTextField.getText(), descriptionTextArea.getText(), date, tags);

                } else {
                    photo = new Photo(uploadedPhoto.getName(), Files.readAllBytes(uploadedPhoto.toPath()), localizationTextField.getText(), descriptionTextArea.getText(), date, tags);
                }

                album.addToAlbum(photo);
                final Transaction tx = session.beginTransaction();
                session.update(album);
                tx.commit();
                final AlbumChange albumChange = new AlbumChange(album);
                albumChange.addChangedPhoto(photo);
                AppManager.getAlbumChangeListener().addAlbumChange(albumChange);
                dialogStage.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleUploadPhotoAction(ActionEvent event) {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(TITLE);
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(FILE_CHOOSER_DESCRIPTION, EXTENSIONS));

        uploadedPhoto = fileChooser.showOpenDialog(dialogStage);
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }
}