package controller;

import app.AppManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.NoArgsConstructor;
import model.Album;

import java.io.IOException;


@NoArgsConstructor
public class AppController {

    private static final String TITLE = "PhotoManager";

    private static final String CREATE_ALBUM_DIALOG_TITLE = "Stwórz album";

    private static final String ADD_PHOTO_DIALOG_TITLE = "Dodaj zdjęcie";

    private Stage primaryStage;

    private Scene primaryScene;

    public AppController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void initRootLayout() throws IOException {
        this.primaryStage.setTitle(TITLE);

        final FXMLLoader loader = new FXMLLoader();
        loader.setLocation(AppManager.class.getResource("../view/AlbumView.fxml"));
        final BorderPane rootLayout = loader.load();

        final AlbumViewController albumViewController = loader.getController();
        albumViewController.setAppController(this);

        primaryScene = new Scene(rootLayout);
        primaryStage.setScene(primaryScene);
        primaryStage.show();
    }

    public void showPhotoView(Album album) throws IOException {
        final FXMLLoader loader = new FXMLLoader();
        loader.setLocation(AppManager.class.getResource("/view/PhotoView.fxml"));
        final BorderPane photoView = loader.load();

        final PhotoViewController photoViewController = loader.getController();
        photoViewController.setAppController(this);
        photoViewController.setAlbum(album);

        final Scene scene = new Scene(photoView);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void showCreateAlbumDialog() throws IOException {
        final FXMLLoader loader = new FXMLLoader();
        loader.setLocation(app.AppManager.class.getResource("/view/CreateAlbumDialog.fxml"));
        final BorderPane page = loader.load();

        final Stage dialogStage = new Stage();
        dialogStage.setTitle(CREATE_ALBUM_DIALOG_TITLE);
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(primaryStage);
        final Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        final CreateAlbumDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);

        dialogStage.setResizable(false);
        dialogStage.showAndWait();
    }

    public void showAddPhotoDialog(Album album) throws IOException {
        final FXMLLoader loader = new FXMLLoader();
        loader.setLocation(app.AppManager.class.getResource("/view/AddPhotoDialog.fxml"));
        final BorderPane page = loader.load();

        final Stage dialogStage = new Stage();
        dialogStage.setTitle(ADD_PHOTO_DIALOG_TITLE);
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(primaryStage);
        final Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        final AddPhotoDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setAlbum(album);

        dialogStage.setResizable(false);
        dialogStage.showAndWait();
    }

    public void backToPrimaryScene() {
        primaryStage.setScene(primaryScene);
        primaryStage.show();
    }
}