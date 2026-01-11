#!/bin/bash
# Startup script for Django Shopping List App

echo "Shopping List App - Django Backend"
echo "Version 1.1.0"
echo "© 2025 Ravindu Wijesundara"
echo ""

# Check if virtual environment exists
if [ ! -d "venv" ]; then
    echo "Creating virtual environment..."
    python3 -m venv venv
fi

# Activate virtual environment
echo "Activating virtual environment..."
source venv/bin/activate

# Install dependencies
echo "Installing dependencies..."
pip install -r requirements.txt

# Check Django configuration
echo "Checking Django configuration..."
python manage.py check

# Run migrations
echo "Running migrations..."
python manage.py migrate

# Start the server
echo ""
echo "Starting Django development server on port 8080..."
python manage.py runserver 8080
