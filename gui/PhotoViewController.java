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
import javafx.stage.FileChooser;
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
     * Handles the add photo button action.
     * Opens a file chooser to select an image file.
     */
    @FXML
    private void handleAddPhoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Photo");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp")
        );
        
        java.io.File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            try {
                // Get file modification date
                java.nio.file.Path path = selectedFile.toPath();
                java.time.LocalDateTime fileDate = java.time.LocalDateTime.ofInstant(
                    java.nio.file.Files.getLastModifiedTime(path).toInstant(),
                    java.time.ZoneId.systemDefault()
                );
                
                // Create photo
                Photo newPhoto = new Photo(selectedFile.getAbsolutePath(), fileDate);
                
                // Check if photo already exists in album
                if (currentAlbum.containsPhoto(newPhoto)) {
                    showError("Duplicate Photo", "This photo is already in the album");
                    return;
                }
                
                currentAlbum.addPhoto(newPhoto);
                UserManager.saveUser(user);
                
                // Display the newly added photo
                displayPhoto(currentAlbum.getPhotoCount() - 1);
                showInfo("Success", "Photo added successfully");
            } catch (Exception e) {
                showError("Error Adding Photo", e.getMessage());
            }
        }
    }

    /**
     * Handles the edit caption button action.
     */
    @FXML
    private void handleEditCaption() {
        if (currentAlbum == null || currentAlbum.getPhotos().isEmpty()) {
            showError("No Photo", "No photo to edit");
            return;
        }

        Photo photo = currentAlbum.getPhotoAt(currentPhotoIndex);
        
        TextInputDialog dialog = new TextInputDialog(photo.getCaption());
        dialog.setTitle("Edit Caption");
        dialog.setHeaderText("Edit Photo Caption");
        dialog.setContentText("Caption:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(caption -> {
            try {
                photo.setCaption(caption.trim());
                UserManager.saveUser(user);
                displayPhoto(currentPhotoIndex);
                showInfo("Success", "Caption updated successfully");
            } catch (IOException e) {
                showError("Error Updating Caption", e.getMessage());
            }
        });
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
     * Handles the delete tag button action.
     */
    @FXML
    private void handleDeleteTag() {
        if (currentAlbum == null || currentAlbum.getPhotos().isEmpty()) {
            showError("No Photo", "No photo selected");
            return;
        }

        Photo photo = currentAlbum.getPhotoAt(currentPhotoIndex);
        
        if (photo.getTags().isEmpty()) {
            showError("No Tags", "This photo has no tags to delete");
            return;
        }

        // Create a dialog to select tag to delete
        ComboBox<String> tagCombo = new ComboBox<>();
        for (Tag tag : photo.getTags()) {
            tagCombo.getItems().add(tag.toString());
        }
        tagCombo.getSelectionModel().selectFirst();

        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
        dialog.setTitle("Delete Tag");
        dialog.setHeaderText("Select Tag to Delete");
        dialog.getDialogPane().setContent(tagCombo);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK && tagCombo.getValue() != null) {
            String selectedTag = tagCombo.getValue();
            
            // Parse tag string back to Tag object
            for (Tag tag : photo.getTags()) {
                if (tag.toString().equals(selectedTag)) {
                    try {
                        photo.removeTag(tag);
                        UserManager.saveUser(user);
                        displayPhoto(currentPhotoIndex);
                        showInfo("Success", "Tag deleted successfully");
                    } catch (IOException e) {
                        showError("Error Deleting Tag", e.getMessage());
                    }
                    break;
                }
            }
        }
    }

    /**
     * Handles the copy photo button action.
     * Copies the current photo to another album.
     */
    @FXML
    private void handleCopyPhoto() {
        if (currentAlbum == null || currentAlbum.getPhotos().isEmpty()) {
            showError("No Photo", "No photo to copy");
            return;
        }

        Photo photo = currentAlbum.getPhotoAt(currentPhotoIndex);
        
        // Get list of other albums
        ComboBox<String> albumCombo = new ComboBox<>();
        for (Album album : user.getAlbums()) {
            if (!album.getName().equals(currentAlbum.getName())) {
                albumCombo.getItems().add(album.getName());
            }
        }
        
        if (albumCombo.getItems().isEmpty()) {
            showError("No Albums", "No other albums available. Create another album first.");
            return;
        }
        
        albumCombo.getSelectionModel().selectFirst();

        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
        dialog.setTitle("Copy Photo");
        dialog.setHeaderText("Select Destination Album");
        dialog.getDialogPane().setContent(albumCombo);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK && albumCombo.getValue() != null) {
            String destAlbumName = albumCombo.getValue();
            Album destAlbum = user.getAlbum(destAlbumName);
            
            if (destAlbum != null) {
                try {
                    if (destAlbum.containsPhoto(photo)) {
                        showError("Duplicate Photo", "Photo already exists in album '" + destAlbumName + "'");
                        return;
                    }
                    
                    destAlbum.addPhoto(photo);
                    UserManager.saveUser(user);
                    showInfo("Success", "Photo copied to album '" + destAlbumName + "'");
                } catch (IOException e) {
                    showError("Error Copying Photo", e.getMessage());
                }
            }
        }
    }

    /**
     * Handles the move photo button action.
     * Moves the current photo to another album.
     */
    @FXML
    private void handleMovePhoto() {
        if (currentAlbum == null || currentAlbum.getPhotos().isEmpty()) {
            showError("No Photo", "No photo to move");
            return;
        }

        Photo photo = currentAlbum.getPhotoAt(currentPhotoIndex);
        
        // Get list of other albums
        ComboBox<String> albumCombo = new ComboBox<>();
        for (Album album : user.getAlbums()) {
            if (!album.getName().equals(currentAlbum.getName())) {
                albumCombo.getItems().add(album.getName());
            }
        }
        
        if (albumCombo.getItems().isEmpty()) {
            showError("No Albums", "No other albums available. Create another album first.");
            return;
        }
        
        albumCombo.getSelectionModel().selectFirst();

        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
        dialog.setTitle("Move Photo");
        dialog.setHeaderText("Select Destination Album");
        dialog.getDialogPane().setContent(albumCombo);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK && albumCombo.getValue() != null) {
            String destAlbumName = albumCombo.getValue();
            Album destAlbum = user.getAlbum(destAlbumName);
            
            if (destAlbum != null) {
                try {
                    if (destAlbum.containsPhoto(photo)) {
                        showError("Duplicate Photo", "Photo already exists in album '" + destAlbumName + "'");
                        return;
                    }
                    
                    destAlbum.addPhoto(photo);
                    currentAlbum.removePhoto(photo);
                    UserManager.saveUser(user);
                    
                    // Update display
                    if (currentAlbum.getPhotoCount() == 0) {
                        showInfo("Success", "Photo moved. Album is now empty.");
                        handleBack();
                    } else {
                        if (currentPhotoIndex >= currentAlbum.getPhotoCount()) {
                            currentPhotoIndex = currentAlbum.getPhotoCount() - 1;
                        }
                        displayPhoto(currentPhotoIndex);
                        showInfo("Success", "Photo moved to album '" + destAlbumName + "'");
                    }
                } catch (IOException e) {
                    showError("Error Moving Photo", e.getMessage());
                }
            }
        }
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
