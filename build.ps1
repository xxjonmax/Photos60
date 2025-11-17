# Photos60 - Build and Run Script
# PowerShell script for direct compilation and execution with JavaFX SDK

param(
    [Parameter(Mandatory=$false)]
    [ValidateSet("compile", "run", "clean", "package")]
    [string]$Action = "run",
    
    [Parameter(Mandatory=$false)]
    [string]$JavaFXPath = "C:\openjfx-21.0.9_windows-x64_bin-sdk"
)

$green = "Green"; $red = "Red"; $yellow = "Yellow"

function Write-Header {
    param([string]$Message)
    Write-Host "`n======================================" -ForegroundColor $yellow
    Write-Host $Message -ForegroundColor $yellow
    Write-Host "======================================`n" -ForegroundColor $yellow
}

function Write-Success {
    param([string]$Message)
    Write-Host "[OK] $Message" -ForegroundColor $green
}

function Write-Err {
    param([string]$Message)
    Write-Host "[ERROR] $Message" -ForegroundColor $red
}

# Detect JavaFX lib directory
Write-Header "Detecting JavaFX SDK"
if (-Not (Test-Path $JavaFXPath)) {
    Write-Err "JavaFX SDK root not found at: $JavaFXPath"
    Write-Host "Please ensure the SDK is extracted and the path is correct."
    exit 1
}

$libCandidates = @(
    (Join-Path $JavaFXPath 'javafx-sdk-21.0.9\lib'),
    (Join-Path $JavaFXPath 'lib'),
    (Join-Path $JavaFXPath 'javafx-sdk-21\lib')
)

$javafxLib = $null
foreach ($candidate in $libCandidates) {
    if (Test-Path $candidate) {
        $javafxLib = $candidate
        break
    }
}

if (-Not $javafxLib) {
    $found = Get-ChildItem -Path $JavaFXPath -Recurse -Directory -Filter lib -ErrorAction SilentlyContinue | Select-Object -First 1
    if ($found) {
        $javafxLib = $found.FullName
    } else {
        Write-Err "Could not locate lib directory under: $JavaFXPath"
        exit 2
    }
}

Write-Success "Found JavaFX lib: $javafxLib"

try {
    $javaVersion = java -version 2>&1
    Write-Success "Java is installed"
    Write-Host ($javaVersion | Select-Object -First 1)
} catch {
    Write-Err "Java is not installed or not in PATH"
    exit 1
}

Write-Header "Executing Action: $Action"

switch ($Action) {
    "compile" {
        Write-Host "Compiling project..."
        Remove-Item -Recurse -Force out -ErrorAction SilentlyContinue
        New-Item -ItemType Directory out | Out-Null
        $files = Get-ChildItem -Recurse -Filter *.java | ForEach-Object FullName
        & javac --release 21 --module-path $javafxLib --add-modules javafx.controls,javafx.fxml -d out $files
        if ($LASTEXITCODE -eq 0) {
            Copy-Item -Path gui\*.fxml -Destination out\gui -Force
            Write-Success "Build completed successfully"
        } else {
            Write-Err "Compilation failed"
            exit 1
        }
    }
    "run" {
        Write-Host "Compiling and running application..."
        Remove-Item -Recurse -Force out -ErrorAction SilentlyContinue
        New-Item -ItemType Directory out | Out-Null
        $files = Get-ChildItem -Recurse -Filter *.java | ForEach-Object FullName
        & javac --release 21 --module-path $javafxLib --add-modules javafx.controls,javafx.fxml -d out $files
        if ($LASTEXITCODE -ne 0) {
            Write-Err "Compilation failed"
            exit 1
        }
        Copy-Item -Path gui\*.fxml -Destination out\gui -Force
        Write-Success "Compilation completed"
        Write-Host "`nStarting Photos..."
        & java --enable-native-access=javafx.graphics --module-path $javafxLib --add-modules javafx.controls,javafx.fxml -cp "out;." Photos
        if ($LASTEXITCODE -eq 0) {
            Write-Success "Application exited normally"
        } else {
            Write-Err "Application exited with code: $LASTEXITCODE"
            exit 1
        }
    }
    "clean" {
        Write-Host "Cleaning build artifacts..."
        Remove-Item -Recurse -Force out -ErrorAction SilentlyContinue
        if ($?) {
            Write-Success "Project cleaned successfully"
        } else {
            Write-Err "Clean failed"
            exit 1
        }
    }
    "package" {
        Write-Host "Creating executable JAR..."
        Remove-Item -Recurse -Force out -ErrorAction SilentlyContinue
        New-Item -ItemType Directory out | Out-Null
        $files = Get-ChildItem -Recurse -Filter *.java | ForEach-Object FullName
        & javac --release 21 --module-path $javafxLib --add-modules javafx.controls,javafx.fxml -d out $files
        if ($LASTEXITCODE -ne 0) {
            Write-Err "Compilation failed"
            exit 1
        }
        Remove-Item -Recurse -Force out\gui -ErrorAction SilentlyContinue
        New-Item -ItemType Directory out\gui | Out-Null
        Copy-Item -Path gui\*.fxml -Destination out\gui -Force
        Copy-Item -Path data -Destination out\data -Recurse -Force -ErrorAction SilentlyContinue
        New-Item -ItemType Directory target -ErrorAction SilentlyContinue | Out-Null
        & jar -cfe target\photos60.jar Photos -C out .
        if ($LASTEXITCODE -eq 0) {
            Write-Success "Package created: target\photos60.jar"
            Write-Host "Run with: java --module-path `"$javafxLib`" --add-modules javafx.controls,javafx.fxml -jar target\photos60.jar"
        } else {
            Write-Err "Packaging failed"
            exit 1
        }
    }
}

Write-Host "`n[OK] Operation completed" -ForegroundColor $green
