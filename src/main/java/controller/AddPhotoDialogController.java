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
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.AlbumChange;
import util.TagParser;

import java.io.*;
import java.nio.file.Files;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class AddPhotoDialogController {

    private static final String TITLE = "Wybierz zdjęcie";

    private static final String FILE_CHOOSER_DESCRIPTION = "Image files";

    private static final List<String> EXTENSIONS = Arrays.asList("*.png", "*.jpg");

    private Stage dialogStage;

    private File uploadedPhoto;

    private Album album;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField localizationTextField;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private TextField tagsTextField;

    @FXML
    private Button cancelButton;

    @FXML
    private Button okButton;

    @FXML
    private Button uploadPhotoButton;

    @FXML
    private void initialize() {
        TextFields.bindAutoCompletion(localizationTextField, t -> {
            Set<String> localizations = new HashSet<>();
            for(Album album : AppManager.getSessionUser().getAlbums()) {
                for(Photo photo : album.getPhotoList()){
                    if(photo.getLocalization().length() > 0) {
                        localizations.add(photo.getLocalization());
                    }
                }
            }
            return localizations.stream().filter(elem ->
            {
                if(t.getUserText().length() > 0 && !elem.equalsIgnoreCase(t.getUserText())) {
                    return elem.toLowerCase().startsWith(t.getUserText().toLowerCase());
                }
                return false;
            }).collect(Collectors.toSet());
        });

        AtomicReference<String> currentTagsWithoutLastOne = new AtomicReference<>();
        AutoCompletionBinding<String> tagsAutoCompletion = TextFields.bindAutoCompletion(tagsTextField, t -> {
            String tagsString = TagParser.parseAsString(t.getUserText());
            String lastTag;
            if(tagsString.lastIndexOf(" ") != -1) {
                currentTagsWithoutLastOne.set(tagsString.
                        substring(0, tagsString.lastIndexOf(" ")) + " ");
                lastTag = TagParser.parseAsStringGetLast(t.getUserText());
            }
            else {
                currentTagsWithoutLastOne.set("");
                lastTag = tagsString;
            }

            Set<String> tags = new HashSet<>();
            for(Album album : AppManager.getSessionUser().getAlbums()) {
                for(Photo photo : album.getPhotoList()){
                    for(Tag tag : photo.getTags()) {
                        tags.add(tag.getName());
                    }
                }
            }

            return tags.stream().filter(elem ->
            {
                if(t.getUserText().length() > 0 && !elem.equalsIgnoreCase(lastTag)) {
                    return elem.toLowerCase().startsWith(lastTag.toLowerCase());
                }
                return false;
            }).collect(Collectors.toSet());
        });

        tagsAutoCompletion.setOnAutoCompleted(e -> {
            tagsTextField.setText(currentTagsWithoutLastOne + e.getCompletion() + " ");
            tagsTextField.end();
        });
    }

    @FXML
    private void handleCancelAction() {
        dialogStage.close();
    }

    @FXML
    private void handleOkAction() {
        if (uploadedPhoto != null && album != null) {
            Photo photo;

            List<Tag> tags = new ArrayList<>();
            if (tagsTextField.getText() != null && !tagsTextField.getText().equals("")) {
                tags = TagParser.parse(tagsTextField.getText());
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
                final Session session = AppManager.getSessionFactory().getCurrentSession();
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