package app;

import controller.AlbumViewController;
import javafx.application.Application;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class AppManager extends Application{

    private static SessionFactory sessionFactory = null;
    private Stage primaryStage;
    private AlbumViewController albumViewController;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        this.albumViewController = new AlbumViewController(primaryStage);
        this.albumViewController.initRootLayout();
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