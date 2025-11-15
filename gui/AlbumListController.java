package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import photos.Album;
import users.User;
import users.UserManager;

import java.io.IOException;
import java.util.Optional;

/**
 * Controller for the album list screen.
 * Displays all albums for a user and handles album management operations.
 * 
 * @author Group XX
 */
public class AlbumListController {
    @FXML
    private Label titleLabel;
    @FXML
    private ListView<String> albumListView;
    
    private Stage stage;
    private User user;

    /**
     * Sets the stage for this controller.
     *
     * @param stage the primary stage
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Sets the current user.
     *
     * @param user the user whose albums are displayed
     */
    public void setUser(User user) {
        this.user = user;
        loadAlbums();
    }

    /**
     * Initializes the controller after FXML loading.
     */
    @FXML
    public void initialize() {
        // Initialization code here
    }

    /**
     * Loads all albums for the current user.
     */
    private void loadAlbums() {
        if (user != null) {
            titleLabel.setText("Albums for " + user.getUsername());
            albumListView.getItems().clear();
            for (Album album : user.getAlbums()) {
                albumListView.getItems().add(album.getName());
            }
        }
    }

    /**
     * Handles the create album button action.
     */
    @FXML
    private void handleCreateAlbum() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Create Album");
        dialog.setHeaderText("Create New Album");
        dialog.setContentText("Album Name:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(albumName -> {
            if (albumName.trim().isEmpty()) {
                showError("Invalid Input", "Album name cannot be empty");
                return;
            }

            try {
                if (user.getAlbum(albumName) != null) {
                    showError("Album Exists", "Album '" + albumName + "' already exists");
                    return;
                }

                user.createAlbum(albumName);
                UserManager.saveUser(user);
                loadAlbums();
                showInfo("Success", "Album '" + albumName + "' created successfully");
            } catch (IOException e) {
                showError("Error Creating Album", e.getMessage());
            }
        });
    }

    /**
     * Handles the search button action.
     */
    @FXML
    private void handleSearch() {
        try {
            Stage searchStage = (Stage) titleLabel.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SearchScene.fxml"));
            Scene scene = new Scene(loader.load());
            
            SearchController controller = loader.getController();
            controller.setStage(searchStage);
            controller.setUser(user);
            
            searchStage.setScene(scene);
        } catch (IOException e) {
            showError("Error", "Failed to load search screen: " + e.getMessage());
        }
    }

    /**
     * Handles the logout button action.
     * Returns to the login screen and saves user data.
     */
    @FXML
    private void handleLogout() {
        try {
            UserManager.saveUser(user);
            
            Stage loginStage = (Stage) titleLabel.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginScene.fxml"));
            Scene scene = new Scene(loader.load());
            loginStage.setScene(scene);
        } catch (IOException e) {
            showError("Error", "Failed to logout: " + e.getMessage());
        }
    }

    /**
     * Shows an information dialog.
     *
     * @param title the dialog title
     * @param message the dialog message
     */
    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Shows an error dialog.
     *
     * @param title the dialog title
     * @param message the error message
     */
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
