package app;

import controller.AppController;
import javafx.application.Application;
import javafx.stage.Stage;
import model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import util.AlbumChangeListener;

import java.io.IOException;

public class AppManager extends Application {

    private static User sessionUser;

    private static SessionFactory sessionFactory = null;

    private AppController appController;

    private static AlbumChangeListener albumChangeListener;

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setOnCloseRequest(e -> handleExit());
        this.appController = new AppController(primaryStage);
        this.appController.initRootLayout();
        albumChangeListener = new AlbumChangeListener();
    }

    public static void main(String[] args) {
        sessionFactory = getSessionFactory();
        Session session = sessionFactory.openSession();

        Application.launch(args);

        session.close();
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            Configuration configuration = new Configuration();
            sessionFactory = configuration.configure().buildSessionFactory();
        }
        return sessionFactory;
    }

    public static User getSessionUser() {
        return sessionUser;
    }

    public static void setSessionUser(User sessionUser) {
        AppManager.sessionUser = sessionUser;
    }

    public static AlbumChangeListener getAlbumChangeListener() {
        return albumChangeListener;
    }

    private void handleExit() {
        try {
            final Stage sendEmailsStage = appController.showSendEmailsView();
            albumChangeListener.sendEmails();
            sendEmailsStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}