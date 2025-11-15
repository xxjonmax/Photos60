# Photos60 Implementation Guide

## Project Status

### ✅ Completed Components

#### Core Model Classes (photos/ package)
- **Photo.java** - Photo model with serialization support
  - Properties: filePath, date, caption, tags
  - Methods: tag management, metadata queries
  
- **Album.java** - Album model with photo collection
  - Properties: name, photos list
  - Methods: photo add/remove, date range queries
  
- **Tag.java** - Tag model for photo attributes
  - Properties: type (e.g., "location"), value (e.g., "Prague")
  - Methods: equality checks for duplicate prevention

- **PhotoManager.java** - Photo utility class
  - Methods: createPhotoFromFile(), isValidPhotoFormat(), getFileName()
  
- **SearchPhotos.java** - Search utility class
  - Methods: searchByDateRange(), searchByTag(), searchByTagsAnd(), searchByTagsOr()
  - Supports all required search criteria

#### User Management (users/ package)
- **User.java** - User model with album management
  - Properties: username, password, albums list
  - Methods: album CRUD operations
  
- **UserManager.java** - User persistence layer
  - Methods: loadUser(), saveUser(), authenticate(), getAllUsers()
  - Stores user data in data/users/ directory
  
- **Admin.java** - Admin user subclass
  - Default credentials: admin/admin
  
- **Stock.java** - Stock user subclass
  - Default credentials: stock/stock
  - Contains pre-loaded stock photos

- **ManagePhotos.java** - Interface for photo operations
  - Methods: addPhotoToAlbum(), removePhotoFromAlbum(), copyPhotoToAlbum(), movePhotoToAlbum()

#### GUI Components (gui/ package)
- **Photos60.java** (main) - Application entry point
  - Initializes JavaFX application
  - Loads LoginScene
  - Initializes stock user

- **LoginController.java** + **LoginScene.fxml**
  - Username/password authentication
  - Navigates to Admin or User subsystem
  
- **AdminController.java** + **AdminScene.fxml**
  - List all users
  - Create new users
  - Delete existing users
  
- **AlbumListController.java** + **AlbumListScene.fxml**
  - Display all user albums
  - Album statistics (count, date range)
  - Navigation to album management
  
- **PhotoViewController.java** + **PhotoViewScene.fxml**
  - Display individual photos
  - Show photo metadata (date, caption, tags)
  - Slideshow functionality (previous/next)
  - Photo operations (add/remove tags)
  
- **SearchController.java** + **SearchScene.fxml**
  - Date range search
  - Single tag search
  - AND/OR tag combinations
  - Create album from search results

### ⏳ Remaining Tasks

#### 1. Complete Controller Implementations
The controller classes contain TODO placeholders that need to be filled in:
- Implement handleCreateAlbum(), handleDeleteAlbum(), handleRenameAlbum()
- Implement photo caption editing dialogs
- Implement tag management dialogs
- Implement copy/move photo operations
- Add proper error handling and user feedback

#### 2. Stock Photos Setup
You need to:
1. Add 5-10 sample photos to `data/photos/stock/` directory
   - Supported formats: BMP, GIF, JPEG, PNG
   - Keep file sizes reasonable (they'll be in GitHub)
2. Create initialization code to load these into the stock user's stock album

#### 3. Data Binding and UI Updates
- Connect ListView items to actual Album/Photo objects
- Implement proper data binding for UI updates
- Add refresh mechanisms when data changes

#### 4. Error Handling
- Add try-catch blocks with user-friendly error dialogs
- Handle file I/O errors gracefully
- Validate user input before processing

#### 5. JavaFX Module Configuration
The project needs proper JavaFX SDK configuration:

**For IntelliJ IDEA:**
1. File → Project Structure → Modules
2. Add JavaFX SDK to Dependencies
3. Add VM options: `--module-path /path/to/javafx-sdk-21/lib --add-modules javafx.controls,javafx.fxml`

**For Eclipse:**
1. Project → Properties → Java Build Path
2. Add JavaFX SDK as Library
3. Configure module path in Run Configurations

**For Command Line:**
```bash
# Compile
javac --module-path /path/to/javafx-sdk-21/lib --add-modules javafx.controls,javafx.fxml -d bin src/**/*.java

# Run
java --module-path /path/to/javafx-sdk-21/lib --add-modules javafx.controls,javafx.fxml -cp bin Photos60
```

#### 6. Generate Javadoc Documentation
```bash
javadoc -d docs -sourcepath src -subpackages . -doctitle "Photos60 API" -author -version
```

## Key Implementation Notes

### Serialization
- Uses Java's ObjectInputStream/ObjectOutputStream
- User data files stored in `data/users/` directory
- Photos are referenced by file path, not copied
- Changes to photos are reflected across all albums containing them

### Photo Identification
- Photos are identified by file path
- Multiple albums can reference the same photo
- Deleting an album removes references, not the photo file
- Modifying photo metadata (caption, tags) affects all albums

### Album Management
- Each user can have multiple albums
- Album names must be unique per user
- Album names can be duplicated across users
- Deletion removes album and its photo references

### Search Implementation
- Date and tag searches are mutually exclusive (not combined)
- Tag searches support: single tag, two tags with AND, two tags with OR
- Search results can be saved as a new album
- Results are copies of references (not duplicates)

## File Structure

```
cs213-photos/
├── src/
│   ├── Photos60.java
│   ├── gui/
│   │   ├── *Controller.java (5 controllers)
│   │   └── *Scene.fxml (5 FXML files)
│   ├── photos/
│   │   ├── Photo.java
│   │   ├── Album.java
│   │   ├── Tag.java
│   │   ├── PhotoManager.java
│   │   └── SearchPhotos.java
│   └── users/
│       ├── User.java
│       ├── UserManager.java
│       ├── Admin.java
│       ├── Stock.java
│       └── ManagePhotos.java
├── data/
│   ├── photos/
│   │   └── stock/  (add 5-10 images here)
│   └── users/      (created at runtime)
├── docs/           (generated by Javadoc)
└── README.md
```

## Testing Checklist

- [ ] Application starts and shows login screen
- [ ] Login with stock user loads stock photos
- [ ] Login with admin user shows user management
- [ ] Create new user account
- [ ] Delete user account
- [ ] List all users in admin panel
- [ ] Create album as regular user
- [ ] Delete album
- [ ] Rename album
- [ ] Add photo to album
- [ ] Remove photo from album
- [ ] Add tag to photo
- [ ] Remove tag from photo
- [ ] Edit photo caption
- [ ] View photo with full metadata
- [ ] Slideshow previous/next buttons
- [ ] Search by date range
- [ ] Search by single tag
- [ ] Search by two tags with AND
- [ ] Search by two tags with OR
- [ ] Create album from search results
- [ ] Copy photo between albums
- [ ] Move photo between albums
- [ ] Logout and login again (data persists)
- [ ] Quit application safely
- [ ] User data saved to disk
- [ ] Multiple users can have different albums/photos

## Next Steps

1. **Set up JavaFX SDK** in your development environment
2. **Complete controller implementations** with proper UI logic
3. **Add sample photos** to data/photos/stock/
4. **Test thoroughly** with the checklist above
5. **Generate Javadoc** documentation
6. **Make regular GitHub commits** as you implement features
7. **Verify serialization** works across application restarts
