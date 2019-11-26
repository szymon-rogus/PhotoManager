package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.NoArgsConstructor;
import model.Album;
import model.Photo;
import org.hibernate.Transaction;

import java.io.IOException;
import java.util.ArrayList;


@NoArgsConstructor
public class CreateAlbumDialogController {
    private Stage dialogStage;

    @FXML
    private TextField nameTextField;

    @FXML
    private Button cancelButton;

    @FXML
    private Button okButton;

    @FXML
    private void handleCancelAction(ActionEvent event) {
        dialogStage.close();
    }

    @FXML
    private void handleOkAction(ActionEvent event) {
        if(!nameTextField.getText().isEmpty()) {
            Transaction tx = app.AppManager.getSessionFactory().getCurrentSession().
                    beginTransaction();
            app.AppManager.getSessionFactory().getCurrentSession().
                    save(new Album(nameTextField.getText(), new ArrayList<Photo>()));
            tx.commit();
            dialogStage.close();
        }
    }

    public void showDialogWindow(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(app.AppManager.class
                    .getResource("/view/CreateAlbumDialog.fxml"));
            BorderPane page = loader.load();

            dialogStage = new Stage();
            dialogStage.setTitle("Stwórz album");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
