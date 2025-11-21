#!/bin/bash
JAVAFX_PATH="/Users/NASD/bookstore/NewFolder/javafx-sdk-21.0.7"
MODULE_PATH="$JAVAFX_PATH/lib"
CP="lib/h2.jar:."

# Compile core files first
javac --module-path "$MODULE_PATH" --add-modules javafx.controls,javafx.fxml -cp "$CP" \
  DatabaseManager.java BankService.java 2>&1 | head -5

# Try to run if compilation succeeded
if [ $? -eq 0 ]; then
  echo "Compilation successful, attempting to run..."
  java --module-path "$MODULE_PATH" --add-modules javafx.controls,javafx.fxml \
    -cp "$CP:." FXLauncher
else
  echo "Compilation had errors. Checking what compiled..."
  find . -name "*.class" | head -10
fi
