# Photos60 - Photo Management Application

A JavaFX-based single-user photo management application built with Java 21 and JavaFX SDK 21.

## Features

### User Management
- User login with optional password
- Special users: `admin` (for user management), `stock` (pre-loaded stock photos)
- Regular user accounts with persistent data

### Album Management
- Create, delete, and rename albums
- Display album statistics (photo count, date range)
- Album-based photo organization

### Photo Management
- Add photos from any location on the user's machine
- Remove photos from albums
- Add/edit photo captions
- Tag photos with custom attributes
- Display photos with metadata (date, caption, tags)
- Copy and move photos between albums
- Slideshow functionality (navigate photos sequentially)

### Search Functionality
- Search by date range
- Search by single tag (type:value)
- Search by tag combinations with AND logic
- Search by tag combinations with OR logic
- Create albums from search results

### Stock Photos
- Pre-loaded stock photos in the `data/photos/stock` directory
- Accessible via the `stock` user account
- 5-10 sample photos included

## Project Structure

```
cs213-photos/
├── src/
│   ├── Photos60.java                 # Main application entry point
│   ├── gui/
│   │   ├── LoginController.java      # Login screen controller
│   │   ├── AdminController.java      # Admin user management
│   │   ├── AlbumListController.java  # Album listing and management
│   │   ├── PhotoViewController.java  # Photo display and management
│   │   ├── SearchController.java     # Photo search
│   │   ├── LoginScene.fxml           # Login screen UI
│   │   ├── AdminScene.fxml           # Admin panel UI
│   │   ├── AlbumListScene.fxml       # Album list UI
│   │   ├── PhotoViewScene.fxml       # Photo view UI
│   │   └── SearchScene.fxml          # Search UI
│   ├── photos/
│   │   ├── Photo.java                # Photo model class
│   │   ├── Album.java                # Album model class
│   │   ├── Tag.java                  # Tag model class
│   │   ├── PhotoManager.java         # Photo utility methods
│   │   └── SearchPhotos.java         # Photo search utility
│   └── users/
│       ├── User.java                 # User model class
│       ├── UserManager.java          # User persistence and management
│       ├── Admin.java                # Admin user class
│       ├── Stock.java                # Stock user class
│       └── ManagePhotos.java         # Photo management interface
├── data/
│   ├── photos/
│   │   └── stock/                    # Stock photos directory
│   └── users/                        # User data storage (created at runtime)
├── docs/                             # Generated Javadoc documentation
└── README.md                          # This file
```

## Setup and Compilation

### Requirements
- JDK 21
- JavaFX SDK 21
- IDE or command-line compiler with JavaFX support

### Compilation
```bash
# Compile with JavaFX SDK
javac --module-path /path/to/javafx-sdk-21/lib --add-modules javafx.controls,javafx.fxml -d bin -r src src/**/*.java

# Or using IDE: Configure JavaFX SDK in your project settings
```

### Running
```bash
# Run the application
java --module-path /path/to/javafx-sdk-21/lib --add-modules javafx.controls,javafx.fxml -cp bin Photos60

# Or from IDE: Run as JavaFX Application
```

## Default Users

| Username | Password | Role | Notes |
|----------|----------|------|-------|
| admin | admin | Admin | Manages all users |
| stock | stock | User | Contains pre-loaded photos |
| (any other) | (any) | User | Regular user account |

## Usage

### First Launch
1. Login with username `stock` and password `stock` to see pre-loaded photos
2. Or login with username `admin` and password `admin` to manage users

### Creating a User Account
1. Login with admin account
2. Click "Create User" button
3. Enter a new username
4. New user account is created with no password

### Managing Albums
1. Login with a regular user account
2. Click "New Album" to create an album
3. Right-click on album to delete or rename
4. Click on album to open and view photos

### Managing Photos
1. Open an album
2. Click "Add Photo" to import photos from your computer
3. Click on a photo to view details
4. Add tags or caption the photo
5. Use "Copy" or "Move" to organize across albums

### Searching Photos
1. Click "Search" button
2. Choose search criteria (date range or tags)
3. View search results
4. Optionally create a new album with search results

## Serialization

All user data is automatically serialized to disk in the `data/users/` directory. Each user's data is stored in a separate file using Java's ObjectInputStream/ObjectOutputStream.

## Data Persistence

- User data (albums, photos, tags) is saved to disk on logout
- Stock photos are stored in `data/photos/stock/` directory
- User-imported photos are referenced by file path (not copied)
- All data persists between application sessions

## Javadoc Documentation

Generate Javadoc documentation:
```bash
javadoc -d docs -sourcepath src -subpackages . -doctitle "Photos60 API"
```

HTML documentation will be generated in the `docs/` directory.

## Notes

- Photos are identified by file path, allowing the same photo in multiple albums
- Modifying a photo (caption, tags) affects all albums containing that photo
- Password support is optional and primarily for admin security
- Application uses FXML for all UI design (except standard dialogs)

## Author

Group XX
