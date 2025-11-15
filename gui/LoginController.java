package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import users.User;
import users.UserManager;

import java.io.IOException;

/**
 * Controller for the login screen.
 * Handles user authentication and navigation to appropriate subsystem (admin or user).
 * 
 * @author Group XX
 */
public class LoginController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorLabel;

    /**
     * Handles the login button action.
     * Authenticates the user and navigates to the appropriate subsystem.
     */
    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (username.isEmpty()) {
            errorLabel.setText("Please enter a username");
            return;
        }

        // Authenticate the user
        User user = UserManager.authenticate(username, password);

        if (user == null) {
            errorLabel.setText("Invalid username or password");
            return;
        }

        // Navigate based on user type
        try {
            Stage stage = (Stage) usernameField.getScene().getWindow();
            
            if (username.equals("admin")) {
                // Load admin scene
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/AdminScene.fxml"));
                Scene scene = new Scene(loader.load());
                AdminController controller = loader.getController();
                controller.setStage(stage);
                stage.setScene(scene);
            } else {
                // Load user scene
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/AlbumListScene.fxml"));
                Scene scene = new Scene(loader.load());
                AlbumListController controller = loader.getController();
                controller.setStage(stage);
                controller.setUser(user);
                stage.setScene(scene);
            }
        } catch (IOException e) {
            errorLabel.setText("Error loading scene: " + e.getMessage());
        }
    }

    /**
     * Handles the quit button action.
     * Exits the application.
     */
    @FXML
    private void handleQuit() {
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.close();
    }
}
