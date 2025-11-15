# JavaFX SDK Configuration - Complete Setup Guide

## Overview
The Photos60 application uses JavaFX 21 SDK for its user interface. This guide provides step-by-step instructions to configure JavaFX in your development environment.

## Prerequisites
- JDK 21 installed and configured
- Maven 3.8.0 or higher (recommended) OR manual JavaFX SDK setup
- Windows 10/11 (or Mac/Linux for cross-platform support)

## Option 1: Using Maven (Recommended)

Maven automatically handles JavaFX SDK dependencies through the `pom.xml` file.

### Steps:
1. **Ensure Maven is installed:**
   ```powershell
   mvn --version
   ```

2. **Build the project:**
   ```powershell
   mvn clean compile
   ```
   Maven will automatically download JavaFX SDK 21 from Maven Central.

3. **Run the application:**
   ```powershell
   mvn javafx:run
   ```

4. **Package as JAR:**
   ```powershell
   mvn package
   ```
   Creates `photos60.jar` (fat JAR with all dependencies included)

5. **Run the packaged JAR:**
   ```powershell
   java -jar target/photos60.jar
   ```

## Option 2: Manual JavaFX SDK Setup (Without Maven)

If you prefer not to use Maven, follow these steps:

### Step 1: Download JavaFX SDK 21
1. Go to https://gluonhq.com/products/javafx/
2. Download **JavaFX SDK 21** for your operating system (Windows 64-bit)
3. Extract to a location, e.g., `C:\javafx-sdk-21`

### Step 2: Configure IDE (VS Code with Extension Pack for Java)

#### For VS Code:
1. Install the "Extension Pack for Java" extension (Microsoft)
2. Create `.vscode/settings.json` in your project root:
   ```json
   {
       "java.project.sourcePaths": ["."],
       "java.project.outputPath": "bin",
       "java.project.referencedLibraries": [
           "C:/javafx-sdk-21/lib/*"
       ]
   }
   ```

### Step 3: Compile with Module Path
```powershell
# Compile from project root
javac --module-path "C:\javafx-sdk-21\lib" `
      --add-modules javafx.controls,javafx.fxml `
      -d . `
      *.java `
      gui/*.java `
      photos/*.java `
      users/*.java `
      module-info.java
```

### Step 4: Run the Application
```powershell
java --module-path "C:\javafx-sdk-21\lib" `
     --add-modules javafx.controls,javafx.fxml `
     Photos60
```

## File Structure After Setup

```
cs213-photos/
├── pom.xml                 # Maven configuration (all dependencies)
├── module-info.java        # Java 9+ module definition
├── Photos60.java           # Main application entry point
├── gui/                    # UI controllers and FXML files
│   ├── LoginController.java
│   ├── AdminController.java
│   ├── AlbumListController.java
│   ├── PhotoViewController.java
│   ├── SearchController.java
│   └── *.fxml              # FXML scene files
├── photos/                 # Model classes
│   ├── Photo.java
│   ├── Album.java
│   ├── Tag.java
│   ├── PhotoManager.java
│   └── SearchPhotos.java
├── users/                  # User management
│   ├── User.java
│   ├── Admin.java
│   ├── Stock.java
│   └── UserManager.java
├── data/                   # Data directory
│   ├── photos/
│   │   └── stock/          # Stock photos (5 images)
│   └── users/              # User data (auto-created)
└── target/                 # Build output (created by Maven)
    ├── classes/            # Compiled classes
    └── photos60.jar        # Executable JAR
```

## Verification

### Using Maven:
```powershell
# Check Maven recognizes JavaFX
mvn dependency:tree

# Should show javafx-controls, javafx-fxml, etc. in the dependency tree
```

### Manual Setup:
```powershell
# Verify files compile without errors
javac --version      # Should be 21.x.x
java --version       # Should be 21.x.x
```

## Troubleshooting

### Issue: "Cannot find symbol: class Application"
**Solution:** Ensure `javafx-controls` is in your classpath/module-path

### Issue: "Exception in Application start method"
**Solution:** 
- Check that `LoginScene.fxml` exists in `gui/` directory
- Verify FXML file paths in code match actual locations
- Check file permissions on `data/photos/stock/` directory

### Issue: "Module not found: javafx.controls"
**Solution:** Ensure you're using `--module-path` with correct JavaFX SDK path

### Issue: "Cannot find resource gui/LoginScene.fxml"
**Solution:**
- Ensure working directory is the project root
- Verify FXML files are copied to the output directory
- For Maven: FXML files should be in `src/main/resources/gui/`

## IDE Configuration

### IntelliJ IDEA
1. Go to File → Project Structure → Libraries
2. Click `+` and select JavaFX SDK path
3. Modules: Mark `javafx.controls`, `javafx.fxml` as dependencies
4. Language level: Set to 21

### Eclipse
1. Project → Properties → Java Build Path → Libraries
2. Add External JARs from JavaFX SDK `lib/` directory
3. Configure VM arguments: `--module-path /path/to/javafx-sdk-21/lib --add-modules javafx.controls,javafx.fxml`

### VS Code
See Option 2, Step 2 above for settings.json configuration

## Running Tests

If you have test files:
```powershell
mvn test
```

## Building Documentation

Generate Javadoc:
```powershell
mvn javadoc:generate
```
Output: `target/site/apidocs/index.html`

## Key Configuration Files

### pom.xml
- Defines all Maven dependencies including JavaFX 21
- Configures build plugins for compilation and execution
- Sets Java version to 21 (matching assignment requirements)

### module-info.java
- Declares required JavaFX modules
- Opens `gui` package to FXML loader (required for JavaFX reflection)
- Exports packages for external access

## Next Steps

1. **First-time setup:** Run `mvn clean compile` to download all dependencies
2. **Run application:** Use `mvn javafx:run` for development
3. **Package:** Run `mvn package` to create executable JAR
4. **Share:** The `target/photos60.jar` can be distributed and run with `java -jar photos60.jar`

## Additional Resources

- [JavaFX Documentation](https://openjfx.io/)
- [Maven JavaFX Plugin](https://github.com/openjfx/javafx-maven-plugin)
- [Java 21 Module System](https://docs.oracle.com/en/java/javase/21/docs/api/)
- [FXML Documentation](https://openjfx.io/javadoc/21/)

---
For assignment requirements, ensure:
- ✅ JavaFX 21 is configured
- ✅ JDK 21 is used
- ✅ Java serialization for persistence ✓
- ✅ FXML UI design ✓
- ✅ Single-user application ✓
