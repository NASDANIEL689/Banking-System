#!/bin/bash
# Compile and run Banking System with JavaFX

JAVAFX_PATH="/Users/NASD/bookstore/NewFolder/javafx-sdk-21.0.7"
MODULE_PATH="$JAVAFX_PATH/lib"
CP="lib/h2.jar:."

echo "Compiling Banking System..."
echo "This may take a moment..."

# Compile all Java files together
javac --module-path "$MODULE_PATH" \
      --add-modules javafx.controls,javafx.fxml \
      -cp "$CP" \
      -d out \
      $(find . -name "*.java" -not -path "./out/*" | tr '\n' ' ') \
      2>&1 | tee compile_errors.log

if [ ${PIPESTATUS[0]} -eq 0 ]; then
    echo ""
    echo "Compilation successful! Starting application..."
    echo ""
    java --module-path "$MODULE_PATH" \
         --add-modules javafx.controls,javafx.fxml \
         -cp "$CP:out" \
         FXLauncher
else
    echo ""
    echo "Compilation had errors. Check compile_errors.log for details."
    echo "Attempting to run anyway if main classes exist..."
    if [ -f "out/FXLauncher.class" ]; then
        java --module-path "$MODULE_PATH" \
             --add-modules javafx.controls,javafx.fxml \
             -cp "$CP:out" \
             FXLauncher
    fi
fi






