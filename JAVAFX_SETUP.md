# JavaFX SDK Setup Guide

This guide explains how to configure JavaFX SDK 21 for the Photos60 project.

## Download JavaFX SDK 21

1. Visit: https://gluonhq.com/products/javafx/
2. Download JavaFX SDK 21 for your operating system
3. Extract to a convenient location (e.g., `C:\javafx-sdk-21` or `/opt/javafx-sdk-21`)

## IDE Setup

### IntelliJ IDEA

1. **Open Project Settings**
   - File → Project Structure (Ctrl+Alt+Shift+S on Windows, Cmd+; on Mac)

2. **Configure JavaFX SDK**
   - Go to Platform Settings → SDKs
   - Click "+" and select "Add JDK"
   - Select your JDK 21 installation folder
   - Click "Apply"

3. **Add JavaFX Module Library**
   - In Project Settings → Modules
   - Select your module
   - Go to Dependencies tab
   - Click "+" → Library → Java
   - Click "New" and browse to `<javafx-sdk>/lib`
   - Select all JAR files in the lib folder
   - Click OK

4. **Configure VM Options**
   - Run → Edit Configurations
   - Add to VM options:
   ```
   --module-path <javafx-sdk-21>/lib --add-modules javafx.controls,javafx.fxml
   ```
   - Replace `<javafx-sdk-21>` with your actual path

5. **Build Configuration**
   - File → Project Structure → Modules
   - Dependencies tab
   - Ensure JavaFX library is listed

### Eclipse

1. **Install e(fx)clipse Plugin** (optional but recommended)
   - Help → Eclipse Marketplace
   - Search "e(fx)clipse"
   - Install and restart

2. **Configure JavaFX SDK**
   - Window → Preferences
   - Java → Build Path → User Libraries
   - Click "New..."
   - Name it "JavaFX21"
   - Click "Add External JARs..."
   - Navigate to `<javafx-sdk-21>/lib` and select all JAR files
   - Click "Add External Class Path Entries..."
   - Select `<javafx-sdk-21>/src.zip` for source
   - Click Finish

3. **Add to Project**
   - Right-click project → Build Path → Add Libraries
   - Select User Library → JavaFX21
   - Click Finish

4. **Configure VM Arguments**
   - Run → Run Configurations
   - Select your Java Application
   - Arguments tab
   - VM arguments:
   ```
   --module-path <javafx-sdk-21>/lib --add-modules javafx.controls,javafx.fxml
   ```

### Visual Studio Code

1. **Install Extensions**
   - Extension Pack for Java (Microsoft)
   - JavaFX Support (optional)

2. **Configure project**
   - Create `.classpath` file in project root:
   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <classpath>
       <classpathentry kind="src" path="src"/>
       <classpathentry kind="con" path="org.eclipse.jdt.launching.JRE_CONTAINER/org.eclipse.jdt.internal.debug.launcher.StandardVMType/JavaSE-21"/>
       <classpathentry kind="lib" path="/path/to/javafx-sdk-21/lib/javafx.base.jar"/>
       <classpathentry kind="lib" path="/path/to/javafx-sdk-21/lib/javafx.controls.jar"/>
       <classpathentry kind="lib" path="/path/to/javafx-sdk-21/lib/javafx.fxml.jar"/>
       <classpathentry kind="lib" path="/path/to/javafx-sdk-21/lib/javafx.graphics.jar"/>
       <classpathentry kind="output" path="bin"/>
   </classpath>
   ```

3. **Configure launch settings**
   - Create `.vscode/launch.json`:
   ```json
   {
       "version": "0.2.0",
       "configurations": [
           {
               "type": "java",
               "name": "Photos60",
               "request": "launch",
               "mainClass": "Photos60",
               "preLaunchTask": "java: build",
               "vmArgs": "--module-path /path/to/javafx-sdk-21/lib --add-modules javafx.controls,javafx.fxml"
           }
       ]
   }
   ```

## Command Line Setup

### Compile
```bash
# Windows (PowerShell)
javac --module-path "C:\javafx-sdk-21\lib" --add-modules javafx.controls,javafx.fxml -d bin -r src src\**\*.java

# macOS/Linux
javac --module-path /opt/javafx-sdk-21/lib --add-modules javafx.controls,javafx.fxml -d bin -r src src/**/*.java
```

### Run
```bash
# Windows (PowerShell)
java --module-path "C:\javafx-sdk-21\lib" --add-modules javafx.controls,javafx.fxml -cp bin Photos60

# macOS/Linux
java --module-path /opt/javafx-sdk-21/lib --add-modules javafx.controls,javafx.fxml -cp bin Photos60
```

## Troubleshooting

### Issue: "package javafx.* does not exist"
**Solution:** Make sure JavaFX SDK is properly configured in IDE classpath/modulepath

### Issue: "Error: Could not find or load main class Photos60"
**Solution:** Ensure:
1. Photos60.java is in the root package (no package declaration)
2. Compiled to correct bin directory
3. Running with correct classpath

### Issue: "RuntimeException: fx:root element does not have required attribute"
**Solution:** Ensure `fx:controller` attribute is present in FXML root element

### Issue: "Module javafx.controls not found"
**Solution:** Add `--add-modules javafx.controls,javafx.fxml` to VM arguments

### Issue: "ClassNotFoundException: com.sun.javafx..."
**Solution:** Module path is not set correctly. Verify `--module-path` points to `<javafx-sdk>/lib`

## Verify Installation

Create a simple test file `TestFX.java`:
```java
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class TestFX extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Label label = new Label("JavaFX is working!");
        StackPane root = new StackPane(label);
        Scene scene = new Scene(root, 300, 200);
        stage.setTitle("JavaFX Test");
        stage.setScene(scene);
        stage.show();
    }
}
```

Compile and run:
```bash
javac --module-path /path/to/javafx-sdk-21/lib --add-modules javafx.controls TestFX.java
java --module-path /path/to/javafx-sdk-21/lib --add-modules javafx.controls TestFX
```

If a window opens with "JavaFX is working!" text, your setup is correct.

## Environment Variables (Optional)

You can set environment variables to avoid typing the long paths:

### Windows (PowerShell)
```powershell
$env:JAVAFX_HOME = "C:\javafx-sdk-21"
$env:PATH += ";$env:JAVAFX_HOME\lib"
```

### macOS/Linux
```bash
export JAVAFX_HOME=/opt/javafx-sdk-21
export PATH=$PATH:$JAVAFX_HOME/lib
```

Then use shortcuts in commands:
```bash
javac --module-path $JAVAFX_HOME/lib --add-modules javafx.controls,javafx.fxml ...
```

## Additional Resources

- Official JavaFX Documentation: https://openjfx.io/
- JavaFX Maven/Gradle Guide: https://openjfx.io/openjfx-docs/
- Scene Builder (Visual FXML Editor): https://gluonhq.com/products/scene-builder/

## Notes

- This project uses FXML for UI design, which requires the `javafx.fxml` module
- All UI components are from `javafx.controls` module
- Image handling uses `javafx.scene.image` module
- All required modules are included when you add both `javafx.controls` and `javafx.fxml`
