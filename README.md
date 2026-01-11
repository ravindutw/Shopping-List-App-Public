# Shopping List App

A simple shopping list application with support for multiple users and roles.

**Version:** 1.1.0  
**© 2025 Ravindu Wijesundara**

## Overview

This repository contains a Shopping List application that now supports **two backend implementations**:

1. **Java Spring Boot Backend** (Original) - Located in `Shopping-List-App/`
2. **Python Django Backend** (New) - Root directory files

Both backends:
- Share the same frontend (HTML/CSS/JavaScript)
- Use MySQL for user management
- Use AWS DynamoDB for shopping list items
- Provide identical API endpoints
- Support authentication and authorization

## Architecture

### Frontend
- **Templates**: HTML with embedded JavaScript
- **Styling**: Custom CSS with dark theme
- **JavaScript**: Vanilla JS for API interactions
- **Static Assets**: Favicon, fonts

### Backend Options

#### 1. Java Spring Boot Backend
- **Framework**: Spring Boot 3.5.3
- **Language**: Java 21
- **Build Tool**: Maven
- **ORM**: Spring Data JPA with Hibernate
- **Security**: Spring Security with BCrypt

#### 2. Python Django Backend
- **Framework**: Django 6.0.1
- **Language**: Python 3.12+
- **Package Manager**: pip
- **ORM**: Django ORM
- **Security**: Django Authentication with custom User model

### Databases
- **MySQL**: User authentication and management
- **AWS DynamoDB**: Shopping list items storage

## Features

### User Management
- User registration (admin only)
- Login/Logout with session management
- Role-based access control (USER, ADMIN)
- Remember-me functionality

### Shopping List
- Add new items to the list
- Mark items as checked/completed
- View all unchecked items
- Track who created and checked items
- Timestamp tracking for all operations

### Admin Features
- Create new users
- Assign roles (USER or ADMIN)
- Access to admin portal

## Getting Started

Choose one of the backend implementations to run:

### Option 1: Run with Django Backend (Python)

#### Prerequisites
- Python 3.12 or higher
- MySQL server
- AWS account with DynamoDB access
- AWS credentials configured

#### Installation

1. Install Python dependencies:
```bash
pip install -r requirements.txt
```

2. Configure environment variables:
```bash
export DB_NAME=your_database_name
export DB_USER=your_database_user
export DB_PASSWORD=your_database_password
export DB_HOST=localhost
export DB_PORT=3306
export AWS_REGION=ap-southeast-1
export DYNAMODB_TABLE_NAME=your_dynamodb_table_name
```

3. Update database table name in `api/models.py`:
   - Change `db_table = 'ReplaceWithYourTableName'` to your actual table name

4. Run migrations:
```bash
python manage.py makemigrations
python manage.py migrate
```

5. Create a superuser (optional):
```bash
python manage.py createsuperuser
```

6. Start the server:
```bash
python manage.py runserver 8080
```

Or use the startup script:
```bash
./start_django.sh
```

7. Access the application at: `http://localhost:8080`

#### Django-specific Commands
```bash
# Check configuration
python manage.py check

# Create migrations
python manage.py makemigrations

# Apply migrations
python manage.py migrate

# Create admin user
python manage.py createsuperuser

# Access admin panel
# http://localhost:8080/admin/
```

### Option 2: Run with Spring Boot Backend (Java)

#### Prerequisites
- Java 21 or higher
- Maven
- MySQL server
- AWS account with DynamoDB access
- AWS credentials configured

#### Installation

1. Navigate to the Spring Boot directory:
```bash
cd Shopping-List-App
```

2. Configure `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/your_database
spring.datasource.username=your_username
spring.datasource.password=your_password
```

3. Update configuration files:
   - In `entity/User.java`: Change table name
   - In `service/ItemService.java`: Change DynamoDB table name
   - In `service/DisplayService.java`: Change DynamoDB table name

4. Build and run:
```bash
mvn spring-boot:run
```

Or build a JAR:
```bash
mvn clean package
java -jar target/Shopping-List-App-1.1.0.jar
```

5. Access the application at: `http://localhost:8080`

## API Endpoints

Both backends provide the same API:

### Authentication
- `GET /login` - Login page
- `POST /login/` - Authenticate user
- `POST /logout/` - Logout user

### Shopping List (Requires Authentication)
- `GET /` - Main application page
- `GET /csrf-token` - Get CSRF token
- `POST /app-api/groceries/add` - Add new item
  - Body: `{"name": "item name"}`
- `POST /app-api/groceries/toggle` - Toggle item status
  - Body: `{"id": "item_id"}`
- `GET /app-api/groceries/fetch` - Get all unchecked items

### Admin (Requires ADMIN role)
- `GET /admin-portal/new-user` - User creation page
- `POST /admin-api/admin/new-user` - Create new user
  - Body: `{"userName": "...", "name": "...", "role": "USER|ADMIN", "password": "..."}`

## Database Schema

### Users Table (MySQL)
```sql
CREATE TABLE YourTableName (
    user_id VARCHAR(50) PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL DEFAULT 'USER',
    password VARCHAR(255) NOT NULL
);
```

### Shopping Items (DynamoDB)
- Partition Key: `id` (String) - UUID
- Attributes:
  - `name` (String) - Item name
  - `created_date` (String) - ISO timestamp
  - `created_user` (String) - Username
  - `location` (String) - Item location
  - `checked` (String) - "true" or "false"
  - `checked_user` (String) - Username who checked
  - `checked_date` (String) - When checked

## Project Structure

```
.
├── manage.py                           # Django management script
├── requirements.txt                    # Python dependencies
├── start_django.sh                     # Django startup script
├── README.md                          # This file
├── README_DJANGO.md                   # Django-specific documentation
├── MIGRATION_GUIDE.md                 # Spring Boot to Django migration guide
│
├── shopping_list_backend/              # Django project settings
│   ├── settings.py                    # Django configuration
│   ├── urls.py                        # URL routing
│   ├── wsgi.py                        # WSGI configuration
│   └── asgi.py                        # ASGI configuration
│
├── api/                               # Django application
│   ├── models.py                      # User model
│   ├── views.py                       # API views/controllers
│   ├── urls.py                        # API URL routing
│   ├── services.py                    # Business logic
│   ├── utils.py                       # Utility functions
│   ├── dynamodb_handler.py            # DynamoDB integration
│   ├── auth_backend.py                # Custom authentication
│   ├── admin.py                       # Admin configuration
│   └── migrations/                    # Database migrations
│
├── templates/                         # Django templates
│   ├── index.html                     # Main app page
│   ├── login.html                     # Login page
│   ├── create-user.html              # User creation page
│   └── error.html                     # Error page
│
└── Shopping-List-App/                 # Spring Boot backend (original)
    ├── pom.xml                        # Maven configuration
    └── src/
        ├── main/
        │   ├── java/                  # Java source files
        │   └── resources/
        │       ├── application.properties
        │       ├── templates/         # Thymeleaf templates
        │       └── static/            # CSS, JS, images
        └── test/                      # Tests
```

## Development

### Django Development
```bash
# Run development server with auto-reload
python manage.py runserver 8080

# Create a new app
python manage.py startapp appname

# Django shell
python manage.py shell

# Collect static files (for production)
python manage.py collectstatic
```

### Spring Boot Development
```bash
# Run with Maven
mvn spring-boot:run

# Run tests
mvn test

# Create production JAR
mvn clean package
```

## Comparison: Django vs Spring Boot

| Feature | Django | Spring Boot |
|---------|--------|-------------|
| Lines of Code | ~1000 | ~1500 |
| Setup Time | 5 minutes | 10 minutes |
| Learning Curve | Gentle | Moderate |
| Built-in Admin | Yes | No |
| ORM Syntax | Simple, Pythonic | More verbose |
| Configuration | One settings file | Multiple files |
| Dependencies | 5 packages | 9+ packages |

## Security Notes

1. Change `SECRET_KEY` in Django settings for production
2. Set `DEBUG = False` in production
3. Configure proper `ALLOWED_HOSTS`
4. Use environment variables for sensitive data
5. Keep dependencies updated
6. Use HTTPS in production
7. Configure proper CORS settings

## AWS Configuration

### DynamoDB Setup
1. Create a DynamoDB table with:
   - Partition key: `id` (String)
   - No sort key needed

2. Configure AWS credentials:
```bash
aws configure
# OR set environment variables:
export AWS_ACCESS_KEY_ID=your_key
export AWS_SECRET_ACCESS_KEY=your_secret
export AWS_DEFAULT_REGION=ap-southeast-1
```

### Secrets Manager (Optional)
Store sensitive configuration in AWS Secrets Manager:
- Database passwords
- Remember-me keys
- API keys

## Troubleshooting

### Django Issues

**Port already in use:**
```bash
# Find and kill the process
lsof -i :8080
kill -9 <PID>
```

**Database connection error:**
- Verify MySQL is running
- Check credentials in environment variables
- Ensure database exists

**Missing dependencies:**
```bash
pip install -r requirements.txt
```

**Migration errors:**
```bash
# Reset migrations (development only)
python manage.py migrate --fake api zero
python manage.py migrate
```

### Spring Boot Issues

**Build failures:**
```bash
mvn clean install -U
```

**Port conflict:**
Change port in `application.properties`:
```properties
server.port=8081
```

## Documentation

- [Django Backend README](README_DJANGO.md) - Detailed Django documentation
- [Migration Guide](MIGRATION_GUIDE.md) - Spring Boot to Django migration
- [Django Documentation](https://docs.djangoproject.com/)
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)

## License

© 2025 Ravindu Wijesundara. All rights reserved.

## Support

For issues, questions, or contributions, please refer to the project documentation or contact the maintainer.
