Photos60 - Grading / Autograder Instructions

This file provides exact commands the grader (or an autograder) can use with **JDK 21** and **JavaFX SDK 21** to compile and run the project without Maven.

Prerequisites on the grader machine:
- JDK 21 installed and on PATH
- JavaFX SDK 21 downloaded and extracted (example: `C:\javafx-sdk-21`)

From the project root (where `module-info.java` and `Photos60.java` live), run the following commands in PowerShell.

1) Compile all Java sources into the `out` directory:

```powershell
# Create output directory
Remove-Item -Recurse -Force out -ErrorAction SilentlyContinue; New-Item -ItemType Directory out

# Compile (module-aware compilation)
javac --module-path "C:\javafx-sdk-21\lib" --add-modules javafx.controls,javafx.fxml -d out \
    $(Get-ChildItem -Recurse -Filter *.java | ForEach-Object FullName)
```

2) Run the application:

```powershell
java --module-path "C:\javafx-sdk-21\lib" --add-modules javafx.controls,javafx.fxml -cp out Photos60
```

Notes for the autograder:
- Ensure working directory is project root.
- The `module-info.java` file declares required JavaFX modules and opens the `gui` package for FXML reflection.
- The project includes `gui/*.fxml` and `data/` resource files; these are not required on the classpath because `Photos60` loads FXML via `getResource("gui/LoginScene.fxml")` which reads from the working directory resources included in compilation output.

If the grader prefers Maven, the repository contains a `pom.xml` configured for Java 21 and JavaFX 21. With Maven installed, run:

```powershell
mvn clean compile
mvn javafx:run
```

Contact/Notes:
- If the autograder runs in a headless environment, GUI tests may fail — ensure display environment is available if running JavaFX UI tests.
- Using Java or JavaFX versions different from 21 may result in compile or runtime differences; grading will assume Java 21 + JavaFX 21.
