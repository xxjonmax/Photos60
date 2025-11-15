/**
 * Module definition for the Photos60 application.
 * Declares dependencies on JavaFX modules and required services.
 */
module photos60 {
    // JavaFX modules
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.swing;

    // Export packages for module access
    exports gui;
    exports photos;
    exports users;

    // Open packages for FXML reflection (required for JavaFX FXML loader)
    opens gui to javafx.fxml;
}
