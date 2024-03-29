package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.Photo;
import model.Tag;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;

public class ShowPhotoDialogController {
    private Stage dialogStage;

    private Photo photo;

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
    private void initialize() { }

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

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
}