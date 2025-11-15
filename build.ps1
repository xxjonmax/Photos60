# Photos60 - Build and Run Script
# PowerShell script for Maven-based compilation and execution

param(
    [Parameter(Mandatory=$false)]
    [ValidateSet("build", "run", "clean", "package", "test", "docs")]
    [string]$Action = "run"
)

# Colors for output
$green = "Green"
$red = "Red"
$yellow = "Yellow"

function Write-Header {
    param([string]$Message)
    Write-Host "`n========================================" -ForegroundColor $yellow
    Write-Host $Message -ForegroundColor $yellow
    Write-Host "========================================`n" -ForegroundColor $yellow
}

function Write-Success {
    param([string]$Message)
    Write-Host "✓ $Message" -ForegroundColor $green
}

function Write-Error {
    param([string]$Message)
    Write-Host "✗ $Message" -ForegroundColor $red
}

# Check if Maven is installed
Write-Header "Checking Prerequisites"
try {
    $mvnVersion = mvn --version
    Write-Success "Maven is installed"
    Write-Host $mvnVersion[0]
} catch {
    Write-Error "Maven is not installed or not in PATH"
    Write-Host "Download Maven from https://maven.apache.org/download.cgi"
    exit 1
}

# Check if JDK 21 is configured
try {
    $javaVersion = java -version 2>&1
    if ($javaVersion -match "21") {
        Write-Success "Java 21 is configured"
        Write-Host ($javaVersion | Select-Object -First 1)
    } else {
        Write-Host "Warning: Java version may not be 21" -ForegroundColor $yellow
        Write-Host ($javaVersion | Select-Object -First 1)
    }
} catch {
    Write-Error "Java is not installed or not in PATH"
    exit 1
}

# Execute requested action
Write-Header "Executing Action: $Action"

switch ($Action) {
    "build" {
        Write-Host "Building project with Maven..."
        mvn clean compile
        if ($LASTEXITCODE -eq 0) {
            Write-Success "Build completed successfully"
        } else {
            Write-Error "Build failed"
            exit 1
        }
    }
    
    "run" {
        Write-Host "Building and running application..."
        mvn clean compile javafx:run
        if ($LASTEXITCODE -eq 0) {
            Write-Success "Application exited normally"
        } else {
            Write-Error "Application failed to run"
            exit 1
        }
    }
    
    "clean" {
        Write-Host "Cleaning build artifacts..."
        mvn clean
        if ($LASTEXITCODE -eq 0) {
            Write-Success "Project cleaned successfully"
        } else {
            Write-Error "Clean failed"
            exit 1
        }
    }
    
    "package" {
        Write-Host "Packaging application as JAR..."
        mvn clean package
        if ($LASTEXITCODE -eq 0) {
            Write-Success "Package created: target/photos60.jar"
            Write-Host "Run with: java -jar target/photos60.jar"
        } else {
            Write-Error "Packaging failed"
            exit 1
        }
    }
    
    "test" {
        Write-Host "Running tests..."
        mvn test
        if ($LASTEXITCODE -eq 0) {
            Write-Success "All tests passed"
        } else {
            Write-Error "Some tests failed"
            exit 1
        }
    }
    
    "docs" {
        Write-Host "Generating Javadoc..."
        mvn javadoc:generate
        if ($LASTEXITCODE -eq 0) {
            Write-Success "Javadoc generated: target/site/apidocs/index.html"
        } else {
            Write-Error "Javadoc generation failed"
            exit 1
        }
    }
}

Write-Host "`n✓ Operation completed" -ForegroundColor $green
