@echo off
cd /d E:\GITHUB\Wiyadama\WiyadamaExpenseTracker
echo Current directory:
cd
echo.
echo Starting Gradle build...
echo.
call gradlew.bat clean assembleDebug --console=plain --no-daemon
echo.
if exist "app\build\outputs\apk\debug\app-debug.apk" (
    echo.
    echo ========================================
    echo BUILD SUCCESSFUL!
    echo ========================================
    echo.
    echo APK Location: app\build\outputs\apk\debug\app-debug.apk
    dir "app\build\outputs\apk\debug\app-debug.apk"
) else (
    echo.
    echo ========================================
    echo BUILD FAILED or APK not found!
    echo ========================================
)
pause
