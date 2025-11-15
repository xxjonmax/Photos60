$javafxLib='C:\openjfx-21.0.9_windows-x64_bin-sdk\javafx-sdk-21.0.9\lib'
Write-Host "Starting compile..."
Remove-Item -Recurse -Force out -ErrorAction SilentlyContinue
New-Item -ItemType Directory out | Out-Null
$files = Get-ChildItem -Recurse -Filter *.java | ForEach-Object FullName
Write-Host "Found $($files.Count) Java files"
& javac --release 21 --module-path $javafxLib --add-modules javafx.controls,javafx.fxml -d out $files
if ($LASTEXITCODE -eq 0) {
    Write-Host "[OK] Compilation successful"
    Remove-Item -Recurse -Force out\gui -ErrorAction SilentlyContinue
    New-Item -ItemType Directory out\gui | Out-Null
    Copy-Item -Path gui\*.fxml -Destination out\gui -Force
    Write-Host "[OK] FXML files copied"
    Write-Host "Starting application..."
    & java --enable-native-access=javafx.graphics --module-path $javafxLib --add-modules javafx.controls,javafx.fxml -cp out Photos60
    Write-Host "Application exited with code: $LASTEXITCODE"
} else {
    Write-Host "[ERROR] Compilation failed with exit code: $LASTEXITCODE"
}
