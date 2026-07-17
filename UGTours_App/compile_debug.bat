@echo off
echo Compiling Kotlin files to get detailed error messages...
cd /d "c:\Users\batzt\Desktop\b7b\UGTours_App"
call gradlew :app:compileDebugKotlin --stacktrace 2>&1
pause
