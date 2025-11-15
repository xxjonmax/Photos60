package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import photos.Album;
import photos.Photo;
import photos.SearchPhotos;
import photos.Tag;
import users.User;
import users.UserManager;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Controller for the photo search screen.
 * Provides search functionality by date range and tags.
 * 
 * @author Group XX
 */
public class SearchController {
    @FXML
    private ComboBox<String> searchTypeCombo;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private ComboBox<String> tagTypeCombo;
    @FXML
    private ComboBox<String> tagValueCombo;
    @FXML
    private ComboBox<String> tag2TypeCombo;
    @FXML
    private ComboBox<String> tag2ValueCombo;
    @FXML
    private ComboBox<String> logicCombo;
    @FXML
    private ListView<String> resultsListView;
    
    private Stage stage;
    private User user;
    private List<Photo> searchResults;

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
     * @param user the current user
     */
    public void setUser(User user) {
        this.user = user;
        loadTagTypes();
    }

    /**
     * Initializes the controller after FXML loading.
     */
    @FXML
    public void initialize() {
        // Set up search type options
        searchTypeCombo.getItems().addAll("Date Range", "Tags");
        searchTypeCombo.setValue("Date Range");
        
        // Set up logic options for tag-based searches
        logicCombo.getItems().addAll("AND", "OR");
        logicCombo.setValue("AND");
        
        // Handle search type changes
        searchTypeCombo.setOnAction(e -> handleSearchTypeChange());
        tagTypeCombo.setOnAction(e -> handleTagTypeChange());
    }

    /**
     * Loads all available tag types from user's photos.
     */
    private void loadTagTypes() {
        tagTypeCombo.getItems().clear();
        tag2TypeCombo.getItems().clear();
        
        List<Photo> allPhotos = getAllPhotos();
        List<String> tagTypes = SearchPhotos.getAllTagTypes(allPhotos);
        tagTypeCombo.getItems().addAll(tagTypes);
        tag2TypeCombo.getItems().addAll(tagTypes);
    }

    /**
     * Gets all photos from all user albums.
     *
     * @return list of all photos
     */
    private List<Photo> getAllPhotos() {
        List<Photo> allPhotos = new java.util.ArrayList<>();
        if (user != null) {
            for (Album album : user.getAlbums()) {
                for (Photo photo : album.getPhotos()) {
                    if (!allPhotos.contains(photo)) {
                        allPhotos.add(photo);
                    }
                }
            }
        }
        return allPhotos;
    }

    /**
     * Handles search type selection change.
     */
    @FXML
    private void handleSearchTypeChange() {
        String searchType = searchTypeCombo.getValue();
        boolean isDateRange = searchType.equals("Date Range");
        startDatePicker.setDisable(!isDateRange);
        endDatePicker.setDisable(!isDateRange);
        tagTypeCombo.setDisable(isDateRange);
        tagValueCombo.setDisable(isDateRange);
        tag2TypeCombo.setDisable(isDateRange);
        tag2ValueCombo.setDisable(isDateRange);
        logicCombo.setDisable(isDateRange);
    }

    /**
     * Handles tag type selection change.
     */
    @FXML
    private void handleTagTypeChange() {
        String tagType = tagTypeCombo.getValue();
        if (tagType == null || tagType.isEmpty()) {
            return;
        }

        tagValueCombo.getItems().clear();
        List<Photo> allPhotos = getAllPhotos();
        List<String> values = SearchPhotos.getTagValues(allPhotos, tagType);
        tagValueCombo.getItems().addAll(values);
    }

    /**
     * Handles the search button action.
     */
    @FXML
    private void handleSearch() {
        String searchType = searchTypeCombo.getValue();
        List<Photo> allPhotos = getAllPhotos();

        if (searchResults != null) {
            searchResults.clear();
        }

        if (searchType.equals("Date Range")) {
            performDateRangeSearch(allPhotos);
        } else {
            performTagSearch(allPhotos);
        }

        displayResults();
        
        if (searchResults == null || searchResults.isEmpty()) {
            showInfo("Search Results", "No photos found matching your search criteria");
        }
    }

    /**
     * Performs a date range search.
     *
     * @param allPhotos the list of all photos to search
     */
    private void performDateRangeSearch(List<Photo> allPhotos) {
        if (startDatePicker.getValue() == null || endDatePicker.getValue() == null) {
            showError("Invalid Input", "Please select both start and end dates");
            return;
        }

        LocalDateTime startDate = startDatePicker.getValue().atStartOfDay();
        LocalDateTime endDate = endDatePicker.getValue().atTime(23, 59, 59);

        searchResults = SearchPhotos.searchByDateRange(allPhotos, startDate, endDate);
    }

    /**
     * Performs a tag-based search.
     *
     * @param allPhotos the list of all photos to search
     */
    private void performTagSearch(List<Photo> allPhotos) {
        String tag1Type = tagTypeCombo.getValue();
        String tag1Value = tagValueCombo.getValue();

        if (tag1Type == null || tag1Value == null || tag1Type.isEmpty() || tag1Value.isEmpty()) {
            showError("Invalid Input", "Please select a tag type and value");
            return;
        }

        String tag2Type = tag2TypeCombo.getValue();
        String tag2Value = tag2ValueCombo.getValue();

        if (tag2Type == null || tag2Type.isEmpty()) {
            // Single tag search
            searchResults = SearchPhotos.searchByTag(allPhotos, tag1Type, tag1Value);
        } else if (tag2Value == null || tag2Value.isEmpty()) {
            // Single tag search
            searchResults = SearchPhotos.searchByTag(allPhotos, tag1Type, tag1Value);
        } else {
            // Two-tag search
            String logic = logicCombo.getValue();
            if (logic.equals("AND")) {
                searchResults = SearchPhotos.searchByTwoTagsAnd(allPhotos, tag1Type, tag1Value, tag2Type, tag2Value);
            } else {
                searchResults = SearchPhotos.searchByTwoTagsOr(allPhotos, tag1Type, tag1Value, tag2Type, tag2Value);
            }
        }
    }

    /**
     * Displays search results in the list view.
     */
    private void displayResults() {
        resultsListView.getItems().clear();
        if (searchResults != null && !searchResults.isEmpty()) {
            for (Photo photo : searchResults) {
                String display = photo.getFileName() + 
                    (photo.getCaption().isEmpty() ? "" : " [" + photo.getCaption() + "]");
                resultsListView.getItems().add(display);
            }
        }
    }

    /**
     * Handles the create album from results button.
     */
    @FXML
    private void handleCreateAlbum() {
        if (searchResults == null || searchResults.isEmpty()) {
            showError("No Results", "Please perform a search first");
            return;
        }

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Create Album");
        dialog.setHeaderText("Create Album from Search Results");
        dialog.setContentText("Album name:");

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

                // Create album and add search results
                user.createAlbum(albumName);
                Album newAlbum = user.getAlbum(albumName);
                for (Photo photo : searchResults) {
                    newAlbum.addPhoto(photo);
                }
                UserManager.saveUser(user);
                showInfo("Success", "Album '" + albumName + "' created with " + searchResults.size() + " photos");
            } catch (IOException e) {
                showError("Error Creating Album", e.getMessage());
            }
        });
    }

    /**
     * Handles the back button action.
     */
    @FXML
    private void handleBack() {
        try {
            Stage albumStage = (Stage) resultsListView.getScene().getWindow();
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
