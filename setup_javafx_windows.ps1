<#
setup_javafx_windows.ps1

Purpose:
- Download JavaFX SDK 21 for Windows x64 (best-effort URL)
- Extract into `lib\javafx-sdk-21` inside the project
- Compile the project using `javac` with the module-path set to the downloaded SDK
- Run the application using `java` with the module-path set to the downloaded SDK

Usage examples:
# Download JavaFX SDK only
.\setup_javafx_windows.ps1 -Action download

# Compile the project (requires JDK 21 on PATH)
.\setup_javafx_windows.ps1 -Action compile

# Compile then run the application
.\setup_javafx_windows.ps1 -Action run

# Clean downloaded SDK and `out` folder
.\setup_javafx_windows.ps1 -Action clean

# If JavaFX SDK is already present in C:\javafx-sdk-21, you can skip download by setting -SkipDownload

#> 

param(
    [ValidateSet("download","compile","run","clean","all")]
    [string]$Action = "all",
    [switch]$SkipDownload,
    [string]$JavaHome = "",
    [string]$JavafxPath = ""
)

$ErrorActionPreference = 'Stop'
$projectRoot = Split-Path -Parent $MyInvocation.MyCommand.Path
$javafxDir = if ($JavafxPath -and (Test-Path $JavafxPath)) { $JavafxPath } else { Join-Path $projectRoot "lib\javafx-sdk-21" }
$javafxLib = Join-Path $javafxDir "lib"
$outDir = Join-Path $projectRoot "out"

# Best-effort download URL for OpenJFX 21 Windows x64 SDK (may change). If this fails, the script instructs manual download.
$javafxUrl = 'https://download2.gluonhq.com/openjfx/21.0.0/openjfx-21.0.0_windows-x64_bin-sdk.zip'
$zipPath = Join-Path $projectRoot "openjfx-21-windows-sdk.zip"

function Get-JavaCmds {
    # Determine java and javac commands, optionally using provided JavaHome
    if ($JavaHome -and (Test-Path $JavaHome)) {
        $javaCmd = Join-Path $JavaHome 'bin\\java.exe'
        $javacCmd = Join-Path $JavaHome 'bin\\javac.exe'
    } else {
        $javaCmd = 'java'
        $javacCmd = 'javac'
    }
    return @{ java = $javaCmd; javac = $javacCmd }
}

function Check-Java21 {
    $cmds = Get-JavaCmds
    try {
        $v = & $cmds.java -version 2>&1
    } catch {
        return $false
    }
    if ($v -match 'version "21') { return $true }
    return $false
}

function Download-JavaFX {
    if (Test-Path $javafxLib) {
        Write-Host "JavaFX SDK already present at $javafxLib" -ForegroundColor Green
        return
    }

    Write-Host "Downloading JavaFX SDK 21 (Windows x64)..." -ForegroundColor Yellow
    try {
        Invoke-WebRequest -Uri $javafxUrl -OutFile $zipPath -UseBasicParsing
    } catch {
        Write-Host "Automatic download failed. Please download JavaFX 21 SDK (Windows x64) manually from https://gluonhq.com/products/javafx/ and place the SDK folder at $javafxDir" -ForegroundColor Red
        throw
    }

    Write-Host "Extracting JavaFX SDK..." -ForegroundColor Yellow
    # Use Expand-Archive which is robust and available in PowerShell 5+
    try {
        Expand-Archive -Path $zipPath -DestinationPath (Join-Path $projectRoot 'lib') -Force
    } catch {
        Write-Host "Failed to extract the archive using Expand-Archive: $_" -ForegroundColor Red
        throw
    }

    # Extraction may create a directory like openjfx-21.0.0-sdk or javafx-sdk-21, find it and normalize name to javafx-sdk-21
    $extracted = Get-ChildItem -Directory (Join-Path $projectRoot 'lib') | Where-Object { $_.Name -match 'openjfx|javafx' } | Select-Object -First 1
    if ($extracted) {
        $target = Join-Path $projectRoot 'lib\javafx-sdk-21'
        if (-Not (Test-Path $target)) {
            try {
                Rename-Item -Path $extracted.FullName -NewName 'javafx-sdk-21'
            } catch {
                # Fallback to Move-Item if Rename-Item fails
                Move-Item -Path $extracted.FullName -Destination $target
            }
        }
    }

    # Cleanup
    Remove-Item $zipPath -ErrorAction SilentlyContinue
    Write-Host "JavaFX SDK installed to $javafxDir" -ForegroundColor Green
}

function Compile-Project {
    if (-Not (Check-Java21)) {
        Write-Host "Java 21 not found (on PATH or provided via -JavaHome). Please install JDK 21 or provide -JavaHome path." -ForegroundColor Red
        return
    }

    if (-Not (Test-Path $javafxLib)) {
        Write-Host "JavaFX SDK not found at $javafxLib" -ForegroundColor Yellow
        Write-Host "You can run this script with -Action download to attempt automatic download, or install JavaFX SDK 21 manually and place it at $javafxDir" -ForegroundColor Yellow
        return
    }

    # Remove old out dir
    if (Test-Path $outDir) { Remove-Item -Recurse -Force $outDir }
    New-Item -ItemType Directory -Path $outDir | Out-Null

    # Collect .java files from project root and subfolders
    $javaFiles = Get-ChildItem -Path $projectRoot -Recurse -Include *.java | Where-Object { $_.FullName -notmatch '\\target\\|\\out\\' } | ForEach-Object { $_.FullName }
    if ($javaFiles.Count -eq 0) {
        Write-Host "No Java source files found in project root." -ForegroundColor Red
        return
    }

    $modulePath = "`"$javafxLib`""

    # Build the javac command using specified javac if provided
    $cmds = Get-JavaCmds
    $filesArg = $javaFiles -join ' '
    Write-Host "Compiling ${($javaFiles.Count)} Java files using $($cmds.javac) ..." -ForegroundColor Yellow

    $cmd = "`"$($cmds.javac)`" --module-path $modulePath --add-modules javafx.controls,javafx.fxml -d `"$outDir`" $filesArg"
    Write-Host $cmd

    & cmd /c $cmd
    if ($LASTEXITCODE -eq 0) {
        Write-Host "Compilation successful. Classes written to $outDir" -ForegroundColor Green
    } else {
        Write-Host "Compilation failed. See messages above." -ForegroundColor Red
    }
}

function Run-Project {
    if (-Not (Check-Java21)) {
        Write-Host "Java 21 not found (on PATH or via -JavaHome). Please install JDK 21 or provide -JavaHome." -ForegroundColor Red
        return
    }

    if (-Not (Test-Path $outDir)) {
        Write-Host "Output directory not found. Please run -Action compile first." -ForegroundColor Yellow
        return
    }

    if (-Not (Test-Path $javafxLib)) {
        Write-Host "JavaFX SDK not found at $javafxLib" -ForegroundColor Red
        return
    }

    $modulePath = "`"$javafxLib`""
    $cmds = Get-JavaCmds
    $cmd = "`"$($cmds.java)`" --module-path $modulePath --add-modules javafx.controls,javafx.fxml -cp `"$outDir`" Photos60"
    Write-Host $cmd
    & cmd /c $cmd
}

function Clean-All {
    if (Test-Path $outDir) { Remove-Item -Recurse -Force $outDir }
    if (Test-Path $javafxDir) { Remove-Item -Recurse -Force $javafxDir }
    Write-Host "Clean complete." -ForegroundColor Green
}

try {
    switch ($Action) {
        'download' {
            if ($SkipDownload) { Write-Host "Skipping download as requested."; break }
            Download-JavaFX
        }
        'compile' {
            if (-Not $SkipDownload) { if (-Not (Test-Path $javafxLib)) { Download-JavaFX } }
            Compile-Project
        }
        'run' {
            if (-Not $SkipDownload) { if (-Not (Test-Path $javafxLib)) { Download-JavaFX } }
            Compile-Project
            Run-Project
        }
        'clean' { Clean-All }
        'all' {
            if (-Not $SkipDownload) { Download-JavaFX }
            Compile-Project
            Run-Project
        }
    }
} catch {
    Write-Host "Script failed: $_" -ForegroundColor Red
}
