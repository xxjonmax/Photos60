package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
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
 * @author Group 60
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
                // Create formatted string with album name, photo count, and date range
                StringBuilder albumInfo = new StringBuilder(album.getName());
                albumInfo.append(" (").append(album.getPhotoCount()).append(" photos)");
                
                if (album.getPhotoCount() > 0) {
                    java.time.LocalDateTime earliest = album.getEarliestDate();
                    java.time.LocalDateTime latest = album.getLatestDate();
                    java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("MM/dd/yyyy");
                    albumInfo.append(" [").append(earliest.format(formatter));
                    if (!earliest.equals(latest)) {
                        albumInfo.append(" - ").append(latest.format(formatter));
                    }
                    albumInfo.append("]");
                }
                
                albumListView.getItems().add(albumInfo.toString());
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
     * Handles the open album button action.
     * Opens the selected album to view its photos.
     */
    @FXML
    private void handleOpenAlbum() {
        String selected = albumListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("No Selection", "Please select an album to open");
            return;
        }

        // Extract album name from formatted string (everything before " (")
        String albumName = selected.contains(" (") ? selected.substring(0, selected.indexOf(" (")) : selected;
        Album album = user.getAlbum(albumName);
        if (album == null) {
            showError("Error", "Album not found");
            return;
        }

        try {
            Stage photoStage = (Stage) titleLabel.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/PhotoViewScene.fxml"));
            Scene scene = new Scene(loader.load());
            
            PhotoViewController controller = loader.getController();
            controller.setStage(photoStage);
            controller.setUserAndAlbum(user, album);
            
            photoStage.setScene(scene);
        } catch (IOException e) {
            showError("Error", "Failed to open album: " + e.getMessage());
        }
    }

    /**
     * Handles the rename album button action.
     */
    @FXML
    private void handleRenameAlbum() {
        String selected = albumListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("No Selection", "Please select an album to rename");
            return;
        }

        // Extract album name from formatted string (everything before " (")
        String albumName = selected.contains(" (") ? selected.substring(0, selected.indexOf(" (")) : selected;
        Album album = user.getAlbum(albumName);
        if (album == null) {
            showError("Error", "Album not found");
            return;
        }

        TextInputDialog dialog = new TextInputDialog(albumName);
        dialog.setTitle("Rename Album");
        dialog.setHeaderText("Rename Album");
        dialog.setContentText("New Name:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(newName -> {
            if (newName.trim().isEmpty()) {
                showError("Invalid Input", "Album name cannot be empty");
                return;
            }

            if (newName.equals(selected)) {
                return; // No change
            }

            try {
                if (user.getAlbum(newName) != null) {
                    showError("Album Exists", "Album '" + newName + "' already exists");
                    return;
                }

                album.setName(newName);
                UserManager.saveUser(user);
                loadAlbums();
                showInfo("Success", "Album renamed to '" + newName + "'");
            } catch (IOException e) {
                showError("Error Renaming Album", e.getMessage());
            }
        });
    }

    /**
     * Handles the delete album button action.
     */
    @FXML
    private void handleDeleteAlbum() {
        String selected = albumListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("No Selection", "Please select an album to delete");
            return;
        }

        // Extract album name from formatted string (everything before " (")
        String albumName = selected.contains(" (") ? selected.substring(0, selected.indexOf(" (")) : selected;
        Album album = user.getAlbum(albumName);
        if (album == null) {
            showError("Error", "Album not found");
            return;
        }

        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirm Delete");
        confirmDialog.setHeaderText("Delete Album?");
        confirmDialog.setContentText("Are you sure you want to delete album '" + albumName + "'?\nThis cannot be undone.");

        Optional<ButtonType> result = confirmDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                user.deleteAlbum(albumName);
                UserManager.saveUser(user);
                loadAlbums();
                showInfo("Success", "Album '" + albumName + "' deleted successfully");
            } catch (IOException e) {
                showError("Error Deleting Album", e.getMessage());
            }
        }
    }

    /**
     * Handles the search button action.
     */
    @FXML
    private void handleSearch() {
        try {
            Stage searchStage = (Stage) titleLabel.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/SearchScene.fxml"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/LoginScene.fxml"));
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
