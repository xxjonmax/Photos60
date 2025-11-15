import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import users.UserManager;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Main entry point for the Photos60 application.
 * A single-user photo management application built with JavaFX.
 * 
 * Features:
 * - User login and authentication (admin, stock, or regular users)
 * - Album management (create, delete, rename albums)
 * - Photo management (add, remove, tag, caption photos)
 * - Photo search (by date range or tags)
 * - Stock photos pre-loaded with the application
 * 
 * @author Group XX
 */
public class Photos60 extends Application {
    public static final String APP_TITLE = "Photos60";
    
    @Override
    public void start(Stage primaryStage) {
        try {
            // Initialize the stock user if needed
            UserManager.initializeStockUserIfNeeded();
            
            // Load the login scene from classpath
            URL fxmlUrl = Photos60.class.getResource("/gui/LoginScene.fxml");
            if (fxmlUrl == null) {
                throw new IOException("Cannot find /gui/LoginScene.fxml on classpath");
            }
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Scene scene = new Scene(loader.load());
            
            primaryStage.setTitle(APP_TITLE);
            primaryStage.setScene(scene);
            primaryStage.setWidth(1000);
            primaryStage.setHeight(700);
            primaryStage.show();
            
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to load application");
            alert.setContentText("Could not load the login interface: " + e.getMessage());
            alert.showAndWait();
        }
    }

    /**
     * Launches the JavaFX application.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        launch(args);
    }
}
