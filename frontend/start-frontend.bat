@echo off
echo Starting User Management Frontend...
echo.

REM Check if Node.js is installed
node --version >nul 2>&1
if %errorlevel% neq 0 (
    echo Error: Node.js is not installed or not in PATH
    echo Please install Node.js from https://nodejs.org/
    pause
    exit /b 1
)

REM Check if npm is installed
npm --version >nul 2>&1
if %errorlevel% neq 0 (
    echo Error: npm is not installed or not in PATH
    pause
    exit /b 1
)

REM Install dependencies if node_modules doesn't exist
if not exist "node_modules" (
    echo Installing dependencies...
    npm install
    if %errorlevel% neq 0 (
        echo Error: Failed to install dependencies
        pause
        exit /b 1
    )
)

REM Create .env file if it doesn't exist
if not exist ".env" (
    echo Creating .env file...
    echo REACT_APP_API_URL=http://localhost:8080 > .env
    echo Environment file created with default API URL
)

echo.
echo Starting development server...
echo Frontend will be available at: http://localhost:3000
echo Backend API should be running at: http://localhost:8080
echo.
echo Press Ctrl+C to stop the server
echo.

npm start
