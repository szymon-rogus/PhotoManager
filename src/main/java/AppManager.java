import controller.AlbumManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class AppManager extends Application{

    private static SessionFactory sessionFactory = null;

    public static void main(String[]args) {
        sessionFactory = getSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        tx.commit();
        session.close();

        Application.launch(args);

    }
    private static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            Configuration configuration = new Configuration();
            sessionFactory = configuration.configure().buildSessionFactory();
        }
        return sessionFactory;
    }

    @Override
    public void start(Stage stage) throws Exception {
        // Create the FXMLLoader
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("view/AlbumView.fxml"));
        BorderPane root = (BorderPane) loader.load();

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}