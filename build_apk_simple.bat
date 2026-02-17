@echo off
cd /d "E:\GITHUB\Wiyadama\WiyadamaExpenseTracker"
call gradlew.bat assembleDebug
if %ERRORLEVEL% EQU 0 (
    echo.
    echo ================================
    echo BUILD SUCCESSFUL!
    echo ================================
    echo APK Location:
    dir /s /b app\build\outputs\apk\debug\*.apk
) else (
    echo.
    echo ================================
    echo BUILD FAILED!
    echo ================================
)
pause
