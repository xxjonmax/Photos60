# Photos60 - Complete Setup & Build Guide

## Quick Start (5 minutes)

### Prerequisites
- Windows 10/11
- JDK 21 installed
- Maven 3.8.0+ installed

### Run Application Immediately
```powershell
# Navigate to project directory
cd cs213-photos

# Run with Maven (downloads dependencies automatically)
mvn javafx:run
```

That's it! The application will:
1. Download JavaFX SDK 21 automatically
2. Compile all source files
3. Launch the Photos60 application

---

## Complete Setup Instructions

### Step 1: Install Java Development Kit (JDK) 21

**Windows:**
1. Download JDK 21 from [oracle.com](https://www.oracle.com/java/technologies/downloads/) or [adoptium.net](https://adoptium.net/)
2. Run the installer and follow the prompts
3. Verify installation:
   ```powershell
   java -version
   javac -version
   ```

### Step 2: Install Maven

**Windows:**
1. Download Maven from [maven.apache.org](https://maven.apache.org/download.cgi)
2. Extract to a folder (e.g., `C:\Maven`)
3. Add Maven `bin` folder to PATH:
   - Press `Win + X` → System
   - Click "Advanced system settings"
   - Click "Environment Variables"
   - Under "System variables", click "New"
   - Variable name: `MAVEN_HOME`
   - Variable value: `C:\Maven` (or your installation path)
   - Click OK
   - Edit `Path` variable and add `%MAVEN_HOME%\bin`
4. Verify installation:
   ```powershell
   mvn --version
   ```

### Step 3: Clone or Extract Project

```powershell
# Navigate to desired location
cd Documents

# Clone the repository (if using Git)
git clone https://github.com/xxjonmax/cs213-photos.git
cd cs213-photos

# OR extract the ZIP file and navigate to it
```

### Step 4: Build and Run

**First Time (downloads all dependencies):**
```powershell
# Clean and compile
mvn clean compile

# This will:
# - Download JavaFX SDK 21
# - Download other Maven dependencies
# - Compile all source files
# - Create bin/classes directory with compiled .class files
```

**Run the Application:**
```powershell
# Start the application
mvn javafx:run

# OR use the provided PowerShell script
.\build.ps1 -Action run
```

**Create Executable JAR:**
```powershell
# Package as standalone JAR
mvn package

# Run the JAR
java -jar target/photos60.jar
```

---

## Using the Provided Build Script

A PowerShell script (`build.ps1`) is provided for convenience:

```powershell
# Build only (compile, no run)
.\build.ps1 -Action build

# Run application
.\build.ps1 -Action run

# Clean build artifacts
.\build.ps1 -Action clean

# Create executable JAR
.\build.ps1 -Action package

# Generate Javadoc
.\build.ps1 -Action docs
```

If you get "execution policy" error:
```powershell
Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope CurrentUser
```

### Automatic JavaFX setup script (Windows)

There is a convenience PowerShell script `setup_javafx_windows.ps1` in the project root that can:
- Download JavaFX SDK 21 (Windows x64) into `lib\javafx-sdk-21` (best-effort),
- Compile the project with `javac` using the downloaded SDK, and
- Run the application.

Usage examples:
```powershell
# Download JavaFX SDK only
.\setup_javafx_windows.ps1 -Action download

# Compile the project (requires JDK 21 on PATH)
.\setup_javafx_windows.ps1 -Action compile

# Compile and run
.\setup_javafx_windows.ps1 -Action run

# Clean downloaded SDK and output
.\setup_javafx_windows.ps1 -Action clean
```

Notes:
- The script attempts a best-effort download from the OpenJFX/Gluon distribution. If the automatic download fails, it prints instructions to download the SDK manually and place it at `lib\javafx-sdk-21`.
- The script expects `java` and `javac` on PATH and verifies the Java version is 21. If you don't have JDK 21 installed, install it before running the script.
- This script is intended to make local configuration simple while still keeping the repository layout unchanged for autograder compatibility.
```

---

## Project Structure

```
cs213-photos/
├── pom.xml                          # Maven configuration (PRIMARY)
├── module-info.java                 # Java 9+ module definition
├── build.ps1                        # Build script for Windows
├── Photos60.java                    # Main application class
│
├── gui/                             # JavaFX UI Controllers
│   ├── LoginController.java
│   ├── AdminController.java
│   ├── AlbumListController.java
│   ├── PhotoViewController.java
│   ├── SearchController.java
│   ├── UserInterface.java
│   ├── Login.java
│   └── *.fxml                       # FXML scene files (5 files)
│
├── photos/                          # Data Model Classes
│   ├── Photo.java                   # Photo with tags & metadata
│   ├── Album.java                   # Album container for photos
│   ├── Tag.java                     # Tag model (type/value)
│   ├── PhotoManager.java            # Photo utility methods
│   └── SearchPhotos.java            # Search functionality (8 search types)
│
├── users/                           # User Management
│   ├── User.java                    # User model with albums
│   ├── Admin.java                   # Admin user (admin/admin)
│   ├── Stock.java                   # Stock user (stock/stock)
│   ├── UserManager.java             # User persistence & auth
│   └── ManagePhotos.java            # Interface (optional)
│
├── data/                            # Application Data
│   ├── photos/
│   │   └── stock/                   # Stock photos (5 images)
│   │       ├── bunny.jpg
│   │       ├── capybara.jpg
│   │       ├── guinea_pig.jpg
│   │       ├── hyrax.jpg
│   │       └── paca.jpg
│   └── users/                       # User data (auto-created)
│       ├── admin.dat
│       ├── stock.dat
│       └── [username].dat           # Serialized user data
│
├── target/                          # Build output
│   ├── classes/                     # Compiled .class files
│   └── photos60.jar                 # Executable JAR
│
└── [Documentation]
    ├── README.md
    ├── GETTING_STARTED.md
    ├── JAVAFX_SETUP.md
    ├── JAVAFX_CONFIGURATION.md
    ├── IMPLEMENTATION_GUIDE.md
    ├── QUICK_REFERENCE.md
    ├── SUMMARY.md
    ├── COMPLETION_STATUS.md
    ├── CHECKLIST.md
    └── CHANGELOG.md
```

---

## How JavaFX Configuration Works

### pom.xml (Maven Configuration)
- **Purpose:** Defines all dependencies and build configuration
- **Key Sections:**
  - `<dependency>` blocks for JavaFX SDK 21 modules
  - `<plugin>` blocks for compilation, execution, packaging
  - `<properties>` for Java version (21) and JavaFX version (21)

### module-info.java (Java Modules)
- **Purpose:** Declares module dependencies (Java 9+ feature)
- **Requires:** javafx.controls, javafx.fxml, javafx.graphics, javafx.swing
- **Opens:** gui package to javafx.fxml for reflection

### Maven Build Plugins
1. **maven-compiler-plugin** - Compiles Java code with JDK 21
2. **javafx-maven-plugin** - Runs JavaFX application (mvn javafx:run)
3. **maven-assembly-plugin** - Creates JAR with dependencies included
4. **maven-shade-plugin** - Builds fat JAR with JavaFX bundled
5. **maven-javadoc-plugin** - Generates API documentation

---

## Troubleshooting

### Issue: "mvn command not found"
**Cause:** Maven not in PATH  
**Fix:** 
1. Verify Maven installation path
2. Add `[Maven_Path]\bin` to system PATH environment variable
3. Restart terminal/IDE

### Issue: "Java 21 not found"
**Cause:** Wrong JDK version or not in PATH  
**Fix:**
1. Install JDK 21 from oracle.com or adoptium.net
2. Verify: `java -version` shows version 21.x.x
3. Verify: `javac -version` shows version 21.x.x

### Issue: "Cannot find module javafx.controls"
**Cause:** JavaFX SDK not downloaded yet  
**Fix:**
```powershell
# Clean and rebuild (downloads dependencies)
mvn clean compile

# This will download JavaFX 21 from Maven Central (~200MB)
```

### Issue: "Exception in Application start method"
**Cause:** Missing FXML files or incorrect paths  
**Fix:**
1. Ensure working directory is project root (where pom.xml is)
2. Verify FXML files exist in `gui/` directory
3. Check console error for exact file name/path that's missing

### Issue: "Cannot access LoginScene.fxml"
**Cause:** IDE not copying FXML to output directory  
**Fix:**
```powershell
# Maven automatically handles this, just use:
mvn clean compile javafx:run
```

### Issue: "Module not found: users" or "Module not found: photos"
**Cause:** module-info.java has dependency issues  
**Fix:**
1. Verify module-info.java is in project root (same level as pom.xml)
2. Check that packages exist (gui/, photos/, users/ directories)
3. Run: `mvn clean compile`

---

## Development Workflow

### Regular Development
```powershell
# Edit code, then run:
mvn javafx:run

# Make changes and run again (Maven detects changes)
mvn javafx:run
```

### Before Submitting/Sharing
```powershell
# Full clean build and package
mvn clean package

# Verify JAR runs standalone
java -jar target/photos60.jar
```

### Generate Documentation
```powershell
# Create Javadoc HTML
mvn javadoc:generate

# Opens in: target/site/apidocs/index.html
```

---

## Configuration Summary

| Component | Version | Configuration |
|-----------|---------|----------------|
| Java | 21 | `pom.xml` <version>21</version> |
| JavaFX | 21 | `pom.xml` javafx.version = 21 |
| Build System | Maven 3.8.0+ | `pom.xml` |
| Module System | Java 9+ | `module-info.java` |
| IDE | VS Code / IntelliJ / Eclipse | `.vscode/settings.json` |

---

## Key Files to Know

1. **pom.xml** - Edit here to change dependencies or build settings
2. **module-info.java** - Edit if adding/removing JavaFX modules
3. **build.ps1** - Use this to build/run (Windows)
4. **Photos60.java** - Main application entry point

---

## Next Steps

1. **First Run:** Execute `mvn javafx:run`
2. **Test Features:** 
   - Login as admin/admin
   - Create albums
   - Search photos
3. **Package:** Run `mvn package` for deployment
4. **Distribute:** Share `target/photos60.jar` with others

---

## Assignment Compliance

✅ **JDK 21** - Configured in pom.xml  
✅ **JavaFX 21** - All modules included in pom.xml  
✅ **Java Serialization** - Implemented in UserManager.java  
✅ **FXML UI** - 5 FXML files with CSS styling  
✅ **Single-User** - Login supports multiple users (admin, stock, regular)  
✅ **Persistence** - User data saved to data/users/ directory  
✅ **Stock Photos** - 5 images in data/photos/stock/  

---

For detailed JavaFX configuration, see **JAVAFX_CONFIGURATION.md**
