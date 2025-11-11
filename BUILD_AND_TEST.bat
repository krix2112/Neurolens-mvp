@echo off
echo ============================================
echo   BUILDING APP FOR DEMO
echo ============================================
echo.

echo [1/3] Cleaning previous build...
call gradlew clean

echo.
echo [2/3] Building APK...
call gradlew assembleDebug

echo.
echo [3/3] Installing on device...
call gradlew installDebug

echo.
echo ============================================
echo   BUILD COMPLETE!
echo ============================================
echo.
echo Next steps:
echo 1. Open app on your device
echo 2. Click "Activate Mock Mode"
echo 3. Test with sample inputs
echo.
echo Sample test:
echo   "I'm stressed about my deadline"
echo.
echo Ready to record demo video!
echo.
pause
