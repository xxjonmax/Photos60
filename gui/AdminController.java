package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import users.UserManager;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Controller for the admin screen.
 * Handles admin operations: list users, create users, delete users.
 * 
 * @author Group XX
 */
public class AdminController {
    @FXML
    private ListView<String> userListView;
    
    private Stage stage;

    /**
     * Sets the stage for this controller.
     *
     * @param stage the primary stage
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Initializes the controller after FXML loading.
     */
    @FXML
    public void initialize() {
        loadUsers();
    }

    /**
     * Loads all users from the system.
     */
    private void loadUsers() {
        userListView.getItems().clear();
        try {
            List<String> users = UserManager.getAllUsers();
            userListView.getItems().addAll(users);
        } catch (IOException e) {
            showError("Error Loading Users", e.getMessage());
        }
    }

    /**
     * Handles the create user button action.
     */
    @FXML
    private void handleCreateUser() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Create User");
        dialog.setHeaderText("Create New User");
        dialog.setContentText("Username:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(username -> {
            if (username.trim().isEmpty()) {
                showError("Invalid Input", "Username cannot be empty");
                return;
            }

            try {
                if (UserManager.userExists(username)) {
                    showError("User Exists", "User '" + username + "' already exists");
                    return;
                }

                UserManager.createUser(username, "");
                loadUsers();
                showInfo("Success", "User '" + username + "' created successfully");
            } catch (IOException e) {
                showError("Error Creating User", e.getMessage());
            }
        });
    }

    /**
     * Handles the delete user button action.
     */
    @FXML
    private void handleDeleteUser() {
        String selected = userListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("No Selection", "Please select a user to delete");
            return;
        }

        if (selected.equals("admin") || selected.equals("stock")) {
            showError("Cannot Delete", "Cannot delete the '" + selected + "' user");
            return;
        }

        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirm Delete");
        confirmDialog.setHeaderText("Delete User?");
        confirmDialog.setContentText("Are you sure you want to delete user '" + selected + "'?\nThis cannot be undone.");

        Optional<ButtonType> result = confirmDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                UserManager.deleteUser(selected);
                loadUsers();
                showInfo("Success", "User '" + selected + "' deleted successfully");
            } catch (IOException e) {
                showError("Error Deleting User", e.getMessage());
            }
        }
    }

    /**
     * Handles the back button action.
     * Returns to the login screen.
     */
    @FXML
    private void handleBack() {
        try {
            Stage primaryStage = (Stage) userListView.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/LoginScene.fxml"));
            Scene scene = new Scene(loader.load());
            primaryStage.setScene(scene);
        } catch (IOException e) {
            showError("Error", "Failed to load login screen: " + e.getMessage());
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

