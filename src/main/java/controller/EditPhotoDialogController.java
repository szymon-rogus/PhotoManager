package controller;

import app.AppManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.Album;
import model.Photo;
import model.Tag;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.TagParser;

import java.io.ByteArrayInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

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
        try {
            photo.setDate(new SimpleDateFormat("yyyy-MM-dd").parse(dateTextField.getText()));
        }
        catch(ParseException e) {

        }

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