# Photos60 API Quick Reference

## Model Classes Quick Reference

### Photo Class
```java
Photo(String filePath, LocalDateTime date)
String getFilePath()
LocalDateTime getDate()
String getCaption() / void setCaption(String caption)
List<Tag> getTags()
boolean addTag(Tag tag)
boolean removeTag(Tag tag)
boolean hasTag(Tag tag)
String getFileName()
```

### Album Class
```java
Album(String name)
String getName() / void setName(String name)
List<Photo> getPhotos()
int getPhotoCount()
boolean addPhoto(Photo photo)
boolean removePhoto(Photo photo)
LocalDateTime getEarliestDate()
LocalDateTime getLatestDate()
```

### Tag Class
```java
Tag(String type, String value)
String getType() / void setType(String type)
String getValue() / void setValue(String value)
```

### User Class
```java
User(String username, String password)
String getUsername()
String getPassword()
List<Album> getAlbums()
Album getAlbum(String albumName)
boolean createAlbum(String albumName)
boolean deleteAlbum(String albumName)
boolean renameAlbum(String oldName, String newName)
```

## Utility Classes Quick Reference

### UserManager
```java
User loadUser(String username) throws IOException, ClassNotFoundException
void saveUser(User user) throws IOException
boolean deleteUser(String username) throws IOException
boolean userExists(String username)
List<String> getAllUsers() throws IOException
User authenticate(String username, String password)
User createUser(String username, String password) throws IOException
void initializeStockUserIfNeeded() throws IOException
```

### PhotoManager
```java
Photo createPhotoFromFile(String filePath)
boolean isValidPhotoFormat(String filePath)
String getFileExtension(String filePath)
String getFileName(String filePath)
```

### SearchPhotos
```java
List<Photo> searchByDateRange(List<Photo> photos, 
                               LocalDateTime startDate, 
                               LocalDateTime endDate)
List<Photo> searchByTag(List<Photo> photos, 
                        String tagType, String tagValue)
List<Photo> searchByTagsAnd(List<Photo> photos, List<Tag> tags)
List<Photo> searchByTagsOr(List<Photo> photos, List<Tag> tags)
List<Photo> searchByTwoTagsAnd(List<Photo> photos, 
                                String tag1Type, String tag1Value,
                                String tag2Type, String tag2Value)
List<Photo> searchByTwoTagsOr(List<Photo> photos, 
                               String tag1Type, String tag1Value,
                               String tag2Type, String tag2Value)
List<String> getAllTagTypes(List<Photo> photos)
List<String> getTagValues(List<Photo> photos, String tagType)
```

## Controller Interface Quick Reference

### LoginController
```java
@FXML void handleLogin()
@FXML void handleQuit()
```

### AdminController
```java
void setStage(Stage stage)
@FXML void initialize()
@FXML void handleCreateUser()
@FXML void handleDeleteUser()
@FXML void handleBack()
```

### AlbumListController
```java
void setStage(Stage stage)
void setUser(User user)
@FXML void initialize()
@FXML void handleCreateAlbum()
@FXML void handleSearch()
@FXML void handleLogout()
```

### PhotoViewController
```java
void setStage(Stage stage)
void setUserAndAlbum(User user, Album album)
@FXML void initialize()
@FXML void handlePrevious()
@FXML void handleNext()
@FXML void handleAddTag()
@FXML void handleRemovePhoto()
@FXML void handleBack()
```

### SearchController
```java
void setStage(Stage stage)
void setUser(User user)
@FXML void initialize()
@FXML void handleSearchTypeChange()
@FXML void handleTagTypeChange()
@FXML void handleSearch()
@FXML void handleCreateAlbum()
@FXML void handleBack()
```

## Common Usage Patterns

### Create and Save a User
```java
User user = UserManager.createUser("john", "password");
user.createAlbum("Vacation");
UserManager.saveUser(user);
```

### Load User and Access Data
```java
User user = UserManager.authenticate("john", "password");
if (user != null) {
    Album album = user.getAlbum("Vacation");
    List<Photo> photos = album.getPhotos();
}
```

### Add Photo with Tags
```java
Photo photo = PhotoManager.createPhotoFromFile("path/to/image.jpg");
photo.setCaption("Beautiful sunset");
photo.addTag(new Tag("location", "Beach"));
photo.addTag(new Tag("person", "Alice"));
album.addPhoto(photo);
```

### Search Photos
```java
List<Photo> allPhotos = getAllPhotos(user); // Helper to get all user photos
List<Photo> results = SearchPhotos.searchByTag(allPhotos, "location", "Beach");
```

### Search by Date Range
```java
LocalDateTime start = LocalDate.of(2024, 1, 1).atStartOfDay();
LocalDateTime end = LocalDate.of(2024, 12, 31).atTime(23, 59, 59);
List<Photo> results = SearchPhotos.searchByDateRange(allPhotos, start, end);
```

### Search with AND Logic
```java
List<Photo> results = SearchPhotos.searchByTwoTagsAnd(
    allPhotos, 
    "location", "Beach",
    "person", "Alice"
);
```

### Search with OR Logic
```java
List<Photo> results = SearchPhotos.searchByTwoTagsOr(
    allPhotos,
    "person", "Alice",
    "person", "Bob"
);
```

## File Structure Quick Reference

**Data Storage:**
- User data: `data/users/{username}.dat`
- Stock photos: `data/photos/stock/*.{jpg,png,gif,bmp}`
- Generated docs: `docs/index.html`

**Configuration:**
- JavaFX module-path: `<javafx-sdk-21>/lib`
- JDK version: 21
- Target: JavaFX 21

## Default Users

| Username | Password | Purpose |
|----------|----------|---------|
| admin | admin | User administration |
| stock | stock | Pre-loaded stock photos |
| (any other) | (any) | Regular user |

## Supported Photo Formats
- BMP
- GIF
- JPEG / JPG
- PNG

## FXML File Locations

- `gui/LoginScene.fxml` - Login screen
- `gui/AdminScene.fxml` - Admin panel
- `gui/AlbumListScene.fxml` - Album list
- `gui/PhotoViewScene.fxml` - Photo display
- `gui/SearchScene.fxml` - Search interface

## Common Compile Command

```bash
javac --module-path /path/to/javafx-sdk-21/lib \
      --add-modules javafx.controls,javafx.fxml \
      -d bin -r src src/**/*.java
```

## Common Run Command

```bash
java --module-path /path/to/javafx-sdk-21/lib \
     --add-modules javafx.controls,javafx.fxml \
     -cp bin Photos60
```

## Troubleshooting Quick Fixes

| Problem | Solution |
|---------|----------|
| "package javafx.* does not exist" | Set `--module-path` to JavaFX SDK lib directory |
| "Error loading FXML" | Verify FXML files exist in gui/ directory |
| "ClassNotFoundException: User" | Ensure all classes are compiled to bin directory |
| "Photo image not displaying" | Use absolute path in `createPhotoFromFile()` |
| Data not persisting | Call `UserManager.saveUser()` before logout |
| "Could not find or load main class" | Ensure Photos60.java has no package declaration |
