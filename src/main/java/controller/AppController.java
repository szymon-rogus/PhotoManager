package controller;

import app.AppManager;
import controller.validation.CreateAccountController;
import controller.validation.LogInController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.NoArgsConstructor;
import model.Album;
import model.Photo;

import java.io.IOException;


@NoArgsConstructor
public class AppController {

    private static final String TITLE = "PhotoManager";

    private static final String CREATE_ALBUM_DIALOG_TITLE = "Stwórz album";

    private static final String ADD_PHOTO_DIALOG_TITLE = "Dodaj zdjęcie";

    private static final String EDIT_PHOTO_DIALOG_TITLE = "Dodaj zdjęcie";

    private static final String CREATE_ACCOUNT_DIALOG_TITLE = "Stwórz konto";

    private static final String CHANGE_EMAIL_DIALOG_TITLE = "Zmień email";

    private static final String SHARE_ALBUM_DIALOG_TITLE = "Udostępnij album";

    private static final String SEND_EMAILS_VIEW_TITLE = "Wysyłanie emaili...";

    private Stage primaryStage;

    private Scene primaryScene;

    public AppController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void initRootLayout() throws IOException {
        this.primaryStage.setTitle(TITLE);

        final FXMLLoader loader = new FXMLLoader();
        loader.setLocation(AppManager.class.getResource("../view/LogInView.fxml"));
        final BorderPane rootLayout = loader.load();

        final LogInController logInController = loader.getController();
        logInController.setAppController(this);

        primaryScene = new Scene(rootLayout);
        primaryStage.setScene(primaryScene);
        logInController.setDialogStage(primaryStage);
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

    public void showAllPhotoView() throws IOException {
        final FXMLLoader loader = new FXMLLoader();
        loader.setLocation(AppManager.class.getResource("/view/AllPhotoView.fxml"));
        final BorderPane allPhotoView = loader.load();

        final AllPhotoViewController allPhotoViewController = loader.getController();
        allPhotoViewController.setAppController(this);
        final Scene scene = new Scene(allPhotoView);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void showAlbumView() throws IOException {
        final FXMLLoader loader = new FXMLLoader();
        loader.setLocation(AppManager.class.getResource("/view/AlbumView.fxml"));
        final BorderPane albumView = loader.load();

        final AlbumViewController albumViewController = loader.getController();
        albumViewController.setAppController(this);

        final Scene scene = new Scene(albumView);
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
        final VBox page = loader.load();

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

    public void showEditPhotoDialog(Photo photo, Album album) throws IOException {
        final FXMLLoader loader = new FXMLLoader();
        loader.setLocation(app.AppManager.class.getResource("/view/EditPhotoDialog.fxml"));
        final VBox page = loader.load();

        final Stage dialogStage = new Stage();
        dialogStage.setTitle(EDIT_PHOTO_DIALOG_TITLE);
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(primaryStage);
        final Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        final EditPhotoDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setPhoto(photo);
        controller.setAlbum(album);

        dialogStage.setResizable(false);
        dialogStage.showAndWait();
    }

    public void showPhotoDialog(Photo photo) throws IOException {
        final FXMLLoader loader = new FXMLLoader();
        loader.setLocation(app.AppManager.class.getResource("/view/ShowPhotoDialog.fxml"));
        final VBox page = loader.load();

        final Stage dialogStage = new Stage();
        dialogStage.setTitle(photo.getName());
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(primaryStage);
        final Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        final ShowPhotoDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setPhoto(photo);

        dialogStage.setResizable(false);
        dialogStage.showAndWait();
    }

    public void showCreateAccountDialog() throws IOException {
        final FXMLLoader loader = new FXMLLoader();
        loader.setLocation(app.AppManager.class.getResource("/view/CreateAccountView.fxml"));
        final BorderPane page = loader.load();

        final Stage dialogStage = new Stage();
        dialogStage.setTitle(CREATE_ACCOUNT_DIALOG_TITLE);
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(primaryStage);
        final Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        final CreateAccountController controller = loader.getController();
        controller.setDialogStage(dialogStage);

        dialogStage.setResizable(false);
        dialogStage.showAndWait();
    }

    public void showChangeEmailDialog() throws IOException {
        final FXMLLoader loader = new FXMLLoader();
        loader.setLocation(app.AppManager.class.getResource("/view/ChangeEmailDialog.fxml"));
        final BorderPane page = loader.load();

        final Stage dialogStage = new Stage();
        dialogStage.setTitle(CHANGE_EMAIL_DIALOG_TITLE);
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(primaryStage);
        final Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        final ChangeEmailDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);

        dialogStage.setResizable(false);
        dialogStage.showAndWait();
    }

    public void showShareAlbumDialog(Album album) throws IOException {
        final FXMLLoader loader = new FXMLLoader();
        loader.setLocation(app.AppManager.class.getResource("/view/ShareAlbumDialog.fxml"));
        final BorderPane page = loader.load();

        final Stage dialogStage = new Stage();
        dialogStage.setTitle(SHARE_ALBUM_DIALOG_TITLE);
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(primaryStage);
        final Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        final ShareAlbumDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setAlbum(album);

        dialogStage.setResizable(false);
        dialogStage.showAndWait();
    }

    public void backToPrimaryScene() {
        primaryStage.setScene(primaryScene);
        primaryStage.show();
    }

    public Stage showSendEmailsView() throws IOException {
        final FXMLLoader loader = new FXMLLoader();
        loader.setLocation(app.AppManager.class.getResource("/view/SendEmailsView.fxml"));
        final BorderPane page = loader.load();

        final Stage dialogStage = new Stage();
        dialogStage.setTitle(SEND_EMAILS_VIEW_TITLE);
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(primaryStage);
        final Scene scene = new Scene(page);
        dialogStage.setScene(scene);
        dialogStage.setResizable(false);
        dialogStage.show();
        return dialogStage;
    }
}