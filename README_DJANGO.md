# Shopping List App - Django Backend

This is the Django backend for the Shopping List application, rewritten from Java Spring Boot.

## Version
1.1.0

## Requirements

- Python 3.12+
- MySQL database
- AWS DynamoDB
- AWS credentials configured

## Installation

1. Install dependencies:
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
export REMEMBER_ME_KEY=your_secret_key
```

3. Update the database table name in `api/models.py`:
   - Change `db_table = 'ReplaceWithYourTableName'` to your actual MySQL table name

4. Run migrations:
```bash
python manage.py makemigrations
python manage.py migrate
```

5. Create a superuser (optional):
```bash
python manage.py createsuperuser
```

6. Run the development server:
```bash
python manage.py runserver 8080
```

The application will be available at http://localhost:8080

## Features

- User authentication with session management
- Shopping list item management (add, toggle, fetch)
- Admin user creation
- DynamoDB integration for shopping list items
- MySQL integration for user management
- CSRF protection
- Static file serving for frontend assets

## API Endpoints

### Public Endpoints
- `GET /login` - Login page
- `POST /login/` - Login authentication
- `POST /logout/` - Logout

### Authenticated Endpoints
- `GET /` - Main shopping list page
- `GET /csrf-token` - Get CSRF token
- `POST /app-api/groceries/add` - Add a new item
- `POST /app-api/groceries/toggle` - Toggle item checked status
- `GET /app-api/groceries/fetch` - Fetch all unchecked items

### Admin Endpoints
- `GET /admin-portal/new-user` - Create user page
- `POST /admin-api/admin/new-user` - Create a new user

## Database Schema

### User Model (MySQL)
- `user_id` - Primary key (e.g., USER-001)
- `username` - Unique username
- `name` - User's full name
- `role` - User role (USER or ADMIN)
- `password` - Hashed password
- `is_active` - Whether the user is active
- `is_staff` - Whether the user can access admin panel

### Shopping List Items (DynamoDB)
- `id` - UUID primary key
- `name` - Item name
- `created_date` - Creation timestamp
- `created_user` - Username who created the item
- `location` - Item location
- `checked` - Whether item is checked (true/false)
- `checked_user` - Username who checked the item
- `checked_date` - When the item was checked

## Project Structure

```
.
├── manage.py                    # Django management script
├── requirements.txt             # Python dependencies
├── shopping_list_backend/       # Main project directory
│   ├── settings.py             # Django settings
│   ├── urls.py                 # URL routing
│   ├── wsgi.py                 # WSGI configuration
│   └── asgi.py                 # ASGI configuration
├── api/                        # API application
│   ├── models.py              # User model
│   ├── views.py               # API views
│   ├── urls.py                # API URL routing
│   ├── services.py            # Business logic services
│   ├── utils.py               # Utility functions
│   ├── dynamodb_handler.py    # DynamoDB integration
│   ├── auth_backend.py        # Custom authentication backend
│   └── admin.py               # Admin panel configuration
└── Shopping-List-App/          # Frontend assets (from original Java app)
    └── src/main/resources/
        ├── templates/          # HTML templates
        └── static/            # CSS, JavaScript, images
```

## Migration from Spring Boot

This Django backend replaces the following Spring Boot components:

1. **Controllers** → Django Views:
   - `AppAPIController` → `api/views.py` (add_grocery, toggle_grocery, fetch_groceries)
   - `AdminAPIController` → `api/views.py` (create_new_user)
   - `CsrfController` → `api/views.py` (csrf_token)

2. **Services** → Django Services:
   - `ItemService` → `api/services.py` (ItemService)
   - `DisplayService` → `api/services.py` (DisplayService)
   - `AdminService` → User creation logic in views

3. **Security** → Django Authentication:
   - Spring Security → Django auth system with custom User model
   - BCrypt passwords → Django's built-in password hashing
   - Remember-me → Django sessions

4. **Database Access**:
   - Spring Data JPA → Django ORM for MySQL
   - Custom DynamoDB handler → `api/dynamodb_handler.py`

## Notes

- The frontend templates and static files are reused from the original Spring Boot application
- Database configurations should be updated for production use
- AWS credentials should be configured through environment variables or AWS CLI
- The SECRET_KEY in settings.py should be changed for production

## License

© 2025 Ravindu Wijesundara
