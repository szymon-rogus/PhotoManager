package app;

import controller.AlbumViewController;
import controller.AppController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.IOException;

public class AppManager extends Application{

    private static SessionFactory sessionFactory = null;
    private Stage primaryStage;
    private AppController appController;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        this.appController = new AppController(primaryStage);
        this.appController.initRootLayout();
    }

    public static void main(String[]args) {
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
}