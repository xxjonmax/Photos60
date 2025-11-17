Photos60 - Photo Management Application
Group 60
CS 213 Assignment 3
========================================

PROJECT INFORMATION:
-------------------
Main Class: Photos (Photos.java)
Entry Point: Photos.main()

REQUIRED DIRECTORY STRUCTURE (VERIFIED):
-----------------------------------------
Photos60/
├── Photos.java          ← Main class with main method
├── gui/                 ← Controllers (AdminController, LoginController, etc.) + FXML files
├── photos/              ← Model classes (Photo, Album, Tag, SearchPhotos)
├── users/               ← User classes (User, Admin, Stock, UserManager)
├── data/                ← Required by assignment
│   └── photos/stock/    ← Stock photos (bunny.jpg, capybara.jpg, guinea_pig.jpg, hyrax.jpg, paca.jpg)
├── docs/                ← Required by assignment - Javadoc HTML documentation
└── module-info.java

HOW TO RUN FOR GRADERS:
-----------------------
**IMPORTANT**: This application requires JavaFX.

If your testing environment has JavaFX configured with module path:
   
   javac --module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.fxml -d out Photos.java gui/*.java photos/*.java users/*.java
   
   java --module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.fxml -cp out Photos

For Windows with build script (if JavaFX SDK available):
   
   .\build.ps1 run

DEFAULT LOGIN CREDENTIALS:
--------------------------
Admin:
   Username: admin
   Password: admin
   
Stock User (with 5 pre-loaded photos):
   Username: stock
   Password: stock

ASSIGNMENT REQUIREMENTS MET:
-----------------------------
✓ Main class named "Photos" with main method
✓ Model classes in separate packages (photos/, users/)
✓ View/Controller in gui/ package  
✓ Data persistence using java.io.Serializable
✓ ObjectOutputStream/ObjectInputStream for save/load
✓ JavaFX + FXML for GUI (no Swing, no external libraries)
✓ Stock photos in data/ directory
✓ Javadoc HTML documentation in docs/ directory
✓ All classes documented with Javadoc tags and @author Group 60

FEATURES IMPLEMENTED (ALL 190 POINTS):
--------------------------------------
Admin Subsystem:
  ✓ Create new users
  ✓ Delete users (except admin/stock)
  ✓ List all users
  ✓ Logout

Non-Admin User Subsystem:
  ✓ Display albums with name, photo count, date range
  ✓ Create albums
  ✓ Delete albums
  ✓ Rename albums
  ✓ Open album to view photos
  ✓ Add photos (FileChooser with BMP/GIF/JPEG/PNG support)
  ✓ Remove photos from album
  ✓ Caption/recaption photos
  ✓ Display photo with caption, date, tags
  ✓ Add tags (type-value pairs)
  ✓ Delete tags
  ✓ Copy photo to another album
  ✓ Move photo to another album
  ✓ Manual slideshow (Previous/Next buttons)
  ✓ Search by date range
  ✓ Search by single tag
  ✓ Search by two tags with AND logic
  ✓ Search by two tags with OR logic
  ✓ Create album from search results
  ✓ Logout and save

Data Persistence:
  ✓ Users saved to data/users/{username}.dat
  ✓ All albums and photos serialized per user
  ✓ Stock user auto-initialized with stock photos
  ✓ Same photo in multiple albums = same physical photo object
  ✓ Changes to photo (caption/tags) reflected in all albums

SCALABILITY:
------------
✓ ListView for albums (handles many albums)
✓ ListView for photos (handles many photos)
✓ Slideshow navigation for photo viewing
✓ Search results in ListView
✓ Efficient serialization per user

NOTES FOR GRADING:
------------------
- No duplicate users allowed
- No duplicate photos in same album  
- No duplicate albums per user
- Admin cannot manage photos/albums
- Photo dates from file modification time (not editable)
- Tags allow multiple values for same type per photo
- Copy/move maintains photo reference (same object)
- Stock photos automatically loaded for all new non-admin users
