# Getting Started with Photos60

## Quick Start Guide (5 minutes)

### Step 1: Prerequisites
- Java Development Kit (JDK) 21 installed
- JavaFX SDK 21 downloaded (see JAVAFX_SETUP.md)
- IDE or command-line compiler

### Step 2: Project Setup
1. Clone or access the cs213-photos repository
2. Ensure directory structure is present:
   ```
   cs213-photos/
   ├── src/ or root with .java files
   ├── gui/
   ├── photos/
   ├── users/
   ├── data/
   │   ├── photos/
   │   │   └── stock/
   │   └── users/ (created at runtime)
   └── docs/
   ```

### Step 3: Compile the Project
```bash
# On Windows (PowerShell)
javac --module-path "C:\javafx-sdk-21\lib" --add-modules javafx.controls,javafx.fxml -d bin src/**/*.java

# On macOS/Linux
javac --module-path /opt/javafx-sdk-21/lib --add-modules javafx.controls,javafx.fxml -d bin src/**/*.java
```

Replace the path with your actual JavaFX SDK location.

### Step 4: Run the Application
```bash
# On Windows (PowerShell)
java --module-path "C:\javafx-sdk-21\lib" --add-modules javafx.controls,javafx.fxml -cp bin Photos60

# On macOS/Linux
java --module-path /opt/javafx-sdk-21/lib --add-modules javafx.controls,javafx.fxml -cp bin Photos60
```

### Step 5: Login
Default accounts:
- **Username**: stock, **Password**: stock (to view stock photos)
- **Username**: admin, **Password**: admin (to manage users)
- **Username**: (any text), **Password**: (any text) for new regular users

## File Structure Explanation

### Root Level Files
- **Photos60.java** - Main application entry point (contains `main()` method)
- **README.md** - Project overview
- **IMPLEMENTATION_GUIDE.md** - Development guidance and next steps
- **JAVAFX_SETUP.md** - Detailed JavaFX SDK setup instructions
- **QUICK_REFERENCE.md** - API reference for quick lookup
- **SUMMARY.md** - Implementation status summary
- **COMPLETION_STATUS.md** - Detailed completion checklist

### /gui Directory
**Controllers** (Handle user interaction)
- LoginController.java - Login screen logic
- AdminController.java - Admin user management
- AlbumListController.java - Album listing and management
- PhotoViewController.java - Photo display and navigation
- SearchController.java - Photo search functionality
- UserInterface.java - UI utility class

**FXML Files** (Define UI layout)
- LoginScene.fxml - Login screen design
- AdminScene.fxml - Admin panel design
- AlbumListScene.fxml - Album list design
- PhotoViewScene.fxml - Photo viewer design
- SearchScene.fxml - Search interface design

### /photos Directory
**Model Classes** (Data and logic)
- Photo.java - Photo data model
- Album.java - Album data model
- Tag.java - Tag data model
- PhotoManager.java - Photo utility functions
- SearchPhotos.java - Search functionality

### /users Directory
**Model & Persistence** (User management)
- User.java - User data model
- UserManager.java - User persistence layer
- Admin.java - Admin user class
- Stock.java - Stock user class
- ManagePhotos.java - Interface for photo operations

### /data Directory
**Application Data** (Grows at runtime)
- photos/stock/ - Stock photo files (add 5-10 images here)
- users/ - User data files (created automatically)

### /docs Directory
**Generated Documentation** (Create after implementation)
- Javadoc HTML files (generate with: javadoc command)

## Feature Walkthrough

### 1. Login Screen
- Enter username (try "stock" or "admin")
- Enter password
- Click Login

### 2. Stock User (View Photos)
- Login with stock/stock
- Browse pre-loaded stock photos
- Add tags and captions
- Create albums

### 3. Admin User (Manage Users)
- Login with admin/admin
- View list of all users
- Create new users
- Delete existing users

### 4. Regular User (Full Features)
- Create albums
- Add photos from your computer
- Tag and caption photos
- Search by date or tags
- Organize photos in albums

## Development Workflow

### For Adding Features
1. **Edit Java files** in gui/, photos/, or users/ directories
2. **Compile** using javac command above
3. **Test** by running the application
4. **Commit** changes to GitHub

### For Modifying UI
1. **Edit FXML files** (use text editor or Scene Builder)
2. **Modify corresponding Controller** class methods
3. **Compile** and test
4. **Commit** changes

### For Fixing Issues
1. **Check compilation** - Fix any compiler errors
2. **Check runtime** - Look for runtime exceptions in console
3. **Validate logic** - Verify expected behavior
4. **Test edge cases** - Try unusual inputs
5. **Commit** fixes

## Common Tasks

### Add Stock Photos
1. Find 5-10 photos (JPEG, PNG, GIF, or BMP format)
2. Copy them to `data/photos/stock/` directory
3. Run the application
4. Login with stock/stock to see photos

### Create New User
1. Login with admin/admin
2. Click "Create User" button
3. Enter username
4. User is created with blank password

### Add Photo to Album
1. Login with regular user account
2. Create or select an album
3. Click "Add Photo" button
4. Browse and select image from your computer
5. Photo is added to album

### Search Photos
1. Login with user account
2. Click "Search" button
3. Choose search type (Date or Tags)
4. Enter search criteria
5. Click "Search"
6. View results
7. Optionally create album from results

### Tag a Photo
1. Open an album
2. Click on a photo
3. Click "Add Tag" button
4. Enter tag type (e.g., "location") and value (e.g., "Beach")
5. Tag is added to photo

## Troubleshooting

### Compilation Issues

**"package javafx.* does not exist"**
- Verify JavaFX SDK path in command
- Check that --module-path points to lib directory
- Ensure JavaFX SDK is extracted properly

**"cannot find symbol"**
- Check all Java files are in correct package directories
- Verify file names match class names
- Ensure all imports are correct

### Runtime Issues

**"No application was found with the specified class"**
- Verify Photos60.java has no package declaration (stays in root)
- Ensure main() method exists and is public static

**"FXML file not found"**
- Check LoginScene.fxml exists in gui/ directory
- Verify file names exactly match what's in controller

**"Image not displaying"**
- Use absolute file paths for photos
- Check photo file format is supported (jpg, png, gif, bmp)
- Verify file permissions allow reading

### Data Issues

**"No such file or directory: data/users"**
- This is normal - directory is created automatically
- Application will create it on first user save

**"User data not persisting"**
- Check that logout was called (data is saved on logout)
- Verify data/users/ directory has write permissions
- Look for exceptions in console output

## Integration with IDE

### IntelliJ IDEA
1. File → Open → select cs213-photos folder
2. Configure JavaFX SDK (see JAVAFX_SETUP.md)
3. Right-click Photos60.java → Run
4. Application should launch

### Eclipse
1. File → Import → Existing Projects into Workspace
2. Select cs213-photos folder
3. Configure JavaFX (see JAVAFX_SETUP.md)
4. Right-click Photos60.java → Run As → Java Application

### VS Code
1. Open cs213-photos folder
2. Install Extension Pack for Java
3. Configure .classpath (see JAVAFX_SETUP.md)
4. Create Run Configuration in .vscode/launch.json
5. Press F5 to run

## Testing Checklist

Basic tests to verify installation:
- [ ] Application starts without errors
- [ ] Login screen appears
- [ ] Can login with stock/stock
- [ ] Can login with admin/admin
- [ ] Admin can list users
- [ ] Data persists after logout/login

## Next Steps

1. **Complete Controller Implementations** (See IMPLEMENTATION_GUIDE.md)
   - Add dialog logic for user creation
   - Add album CRUD operations
   - Add photo display and management

2. **Add Stock Photos**
   - Find and add 5-10 images to data/photos/stock/

3. **Comprehensive Testing**
   - Test all features with various scenarios
   - Test with large photo collections
   - Test error handling

4. **Generate Documentation**
   - Run: `javadoc -d docs -sourcepath src -subpackages . -author -version`

5. **Commit to GitHub**
   - Make regular commits as features are completed
   - Each partner should make commits
   - Ensure grader has access before deadline

## Helpful Resources

- **JavaFX Official Docs**: https://openjfx.io/
- **FXML Documentation**: https://openjfx.io/javadoc/21/
- **Scene Builder**: https://gluonhq.com/products/scene-builder/
- **Java Serialization**: https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/io/Serializable.html

## Important Dates

- **First Individual Commit**: Friday, October 31, 11 PM
- **Final Submission**: Monday, November 17, 11 PM
- **Grader Access Required**: By November 17, 11 PM

## Contact & Support

For questions about:
- **JavaFX Setup**: See JAVAFX_SETUP.md
- **API Methods**: See QUICK_REFERENCE.md
- **Implementation Details**: See IMPLEMENTATION_GUIDE.md
- **Project Status**: See COMPLETION_STATUS.md

---

**Ready to start?** Begin with JAVAFX_SETUP.md to configure your development environment.
