package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import photos.Album;
import photos.Photo;
import photos.Tag;
import users.User;
import users.UserManager;

import java.io.IOException;
import java.util.Optional;

/**
 * Controller for the photo view screen.
 * Displays photos from an album with details and allows photo management.
 * 
 * @author Group XX
 */
public class PhotoViewController {
    @FXML
    private Label albumTitleLabel;
    @FXML
    private ImageView photoImageView;
    @FXML
    private Label captionLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private Label tagsLabel;
    
    private Stage stage;
    private User user;
    private Album currentAlbum;
    private int currentPhotoIndex = 0;

    /**
     * Sets the stage for this controller.
     *
     * @param stage the primary stage
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Sets the current user and album.
     *
     * @param user the current user
     * @param album the album to display
     */
    public void setUserAndAlbum(User user, Album album) {
        this.user = user;
        this.currentAlbum = album;
        albumTitleLabel.setText(album.getName());
        displayPhoto(0);
    }

    /**
     * Initializes the controller after FXML loading.
     */
    @FXML
    public void initialize() {
        // Initialization code here
    }

    /**
     * Displays a photo at the specified index.
     *
     * @param index the index of the photo to display
     */
    private void displayPhoto(int index) {
        if (currentAlbum == null || currentAlbum.getPhotos().isEmpty()) {
            return;
        }

        if (index < 0 || index >= currentAlbum.getPhotoCount()) {
            return;
        }

        currentPhotoIndex = index;
        Photo photo = currentAlbum.getPhotoAt(index);
        
        // Display photo
        try {
            Image image = new Image("file:" + photo.getFilePath());
            photoImageView.setImage(image);
        } catch (Exception e) {
            showError("Error Loading Image", "Could not load image: " + photo.getFilePath());
        }

        // Display metadata
        captionLabel.setText("Caption: " + (photo.getCaption().isEmpty() ? "(none)" : photo.getCaption()));
        dateLabel.setText("Date: " + photo.getDate());
        
        StringBuilder tags = new StringBuilder("Tags: ");
        if (photo.getTags().isEmpty()) {
            tags.append("(none)");
        } else {
            for (Tag tag : photo.getTags()) {
                tags.append(tag.toString()).append(" ");
            }
        }
        tagsLabel.setText(tags.toString());
    }

    /**
     * Handles the previous button action.
     */
    @FXML
    private void handlePrevious() {
        if (currentPhotoIndex > 0) {
            displayPhoto(currentPhotoIndex - 1);
        }
    }

    /**
     * Handles the next button action.
     */
    @FXML
    private void handleNext() {
        if (currentPhotoIndex < currentAlbum.getPhotoCount() - 1) {
            displayPhoto(currentPhotoIndex + 1);
        }
    }

    /**
     * Handles the add tag button action.
     */
    @FXML
    private void handleAddTag() {
        Photo photo = currentAlbum.getPhotoAt(currentPhotoIndex);
        
        TextInputDialog typeDialog = new TextInputDialog();
        typeDialog.setTitle("Add Tag");
        typeDialog.setHeaderText("Enter Tag Type");
        typeDialog.setContentText("Tag Type (e.g., location, person, object):");

        Optional<String> typeResult = typeDialog.showAndWait();
        typeResult.ifPresent(type -> {
            if (type.trim().isEmpty()) {
                showError("Invalid Input", "Tag type cannot be empty");
                return;
            }

            TextInputDialog valueDialog = new TextInputDialog();
            valueDialog.setTitle("Add Tag");
            valueDialog.setHeaderText("Enter Tag Value");
            valueDialog.setContentText("Tag Value:");

            Optional<String> valueResult = valueDialog.showAndWait();
            valueResult.ifPresent(value -> {
                if (value.trim().isEmpty()) {
                    showError("Invalid Input", "Tag value cannot be empty");
                    return;
                }

                try {
                    Tag tag = new Tag(type.trim(), value.trim());
                    photo.addTag(tag);
                    UserManager.saveUser(user);
                    displayPhoto(currentPhotoIndex);
                    showInfo("Success", "Tag added successfully");
                } catch (IOException e) {
                    showError("Error Adding Tag", e.getMessage());
                }
            });
        });
    }

    /**
     * Handles the remove photo button action.
     */
    @FXML
    private void handleRemovePhoto() {
        if (currentAlbum == null || currentAlbum.getPhotos().isEmpty()) {
            showError("No Photo", "No photo to remove");
            return;
        }

        Photo photo = currentAlbum.getPhotoAt(currentPhotoIndex);
        
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirm Delete");
        confirmDialog.setHeaderText("Delete Photo?");
        confirmDialog.setContentText("Are you sure you want to remove this photo from the album?\nThis cannot be undone.");

        Optional<ButtonType> result = confirmDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
            currentAlbum.removePhoto(photo);
                UserManager.saveUser(user);
                
                if (currentAlbum.getPhotoCount() == 0) {
                    showInfo("Success", "Photo removed. Album is now empty.");
                    handleBack();
                } else {
                    if (currentPhotoIndex >= currentAlbum.getPhotoCount()) {
                        currentPhotoIndex = currentAlbum.getPhotoCount() - 1;
                    }
                    displayPhoto(currentPhotoIndex);
                    showInfo("Success", "Photo removed successfully");
                }
            } catch (IOException e) {
                showError("Error Removing Photo", e.getMessage());
            }
        }
    }

    /**
     * Handles the back button action.
     */
    @FXML
    private void handleBack() {
        try {
            Stage albumStage = (Stage) albumTitleLabel.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/AlbumListScene.fxml"));
            Scene scene = new Scene(loader.load());
            
            AlbumListController controller = loader.getController();
            controller.setStage(albumStage);
            controller.setUser(user);
            
            albumStage.setScene(scene);
        } catch (IOException e) {
            showError("Error", "Failed to load album list: " + e.getMessage());
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
