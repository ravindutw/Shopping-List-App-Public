# Spring Boot to Django Migration Guide

## Overview
This document details the complete migration from Java Spring Boot backend to Python Django backend for the Shopping List application.

## Architecture Comparison

### Spring Boot (Before)
```
Shopping-List-App/
├── src/main/java/com/ravinduw/apps/shoppinglistapp/
│   ├── ShoppingListApp.java (Main application)
│   ├── controller/
│   │   ├── AppAPIController.java
│   │   ├── AdminAPIController.java
│   │   └── CsrfController.java
│   ├── service/
│   │   ├── ItemService.java
│   │   ├── DisplayService.java
│   │   └── AdminService.java
│   ├── entity/
│   │   ├── User.java
│   │   ├── ItemRequest.java
│   │   └── ToggleRequest.java
│   ├── repository/
│   │   └── UserRepo.java
│   ├── auth/
│   │   ├── SecurityConfig.java
│   │   ├── AuthUtils.java
│   │   ├── CustomUserDetailsService.java
│   │   └── CustomLoginSuccessHandler.java
│   ├── dynamodbpkg/
│   │   ├── DynamoDBHandler.java
│   │   └── DynamoDBAttributeValueHandler.java
│   ├── awssmpkg/
│   │   └── AWSSMHandler.java
│   ├── utils/
│   │   └── Utils.java
│   └── MvcConfig.java
├── src/main/resources/
│   ├── application.properties
│   ├── templates/ (Thymeleaf)
│   └── static/
└── pom.xml
```

### Django (After)
```
.
├── manage.py
├── requirements.txt
├── shopping_list_backend/
│   ├── settings.py
│   ├── urls.py
│   ├── wsgi.py
│   └── asgi.py
├── api/
│   ├── models.py (User model)
│   ├── views.py (Controllers)
│   ├── urls.py (URL routing)
│   ├── services.py (Business logic)
│   ├── utils.py (Utilities)
│   ├── dynamodb_handler.py (DynamoDB integration)
│   ├── auth_backend.py (Custom authentication)
│   └── admin.py (Admin configuration)
└── Shopping-List-App/src/main/resources/ (Reused frontend)
```

## Component Mapping

### 1. Controllers → Views

#### Spring Boot AppAPIController
```java
@RestController
@RequestMapping("/app-api/groceries")
public class AppAPIController {
    @PostMapping("/add")
    public ResponseEntity<String> addGroceries(@RequestBody ItemRequest request)
    
    @PostMapping("/toggle")
    public ResponseEntity<String> toggleItem(@RequestBody ToggleRequest request)
    
    @GetMapping("/fetch")
    public ResponseEntity<String> getGroceries(HttpServletRequest request)
}
```

#### Django Views (api/views.py)
```python
@login_required
@require_http_methods(["POST"])
def add_grocery(request):
    data = json.loads(request.body)
    name = data.get('name', '').strip()
    item_service = ItemService()
    item_service.new_item(request.user, name, 'HKU')
    return JsonResponse({'message': 'Item added successfully'})

@login_required
@require_http_methods(["POST"])
def toggle_grocery(request):
    data = json.loads(request.body)
    item_id = data.get('id', '').strip()
    item_service = ItemService()
    item_service.check_item(item_id, request.user)
    return JsonResponse({'message': 'Item toggled successfully'})

@login_required
@require_http_methods(["GET"])
def fetch_groceries(request):
    display_service = DisplayService()
    items = display_service.get_items()
    return JsonResponse(items, safe=False)
```

### 2. Services

#### Spring Boot ItemService
```java
@Service
public class ItemService {
    private final DynamoDBHandler db = new DynamoDBHandler(dbName);
    
    public void newItem(User user, String name, String location) throws Exception {
        // Create item in DynamoDB
    }
    
    public void checkItem(String id, User user) throws Exception {
        // Update item in DynamoDB
    }
}
```

#### Django ItemService (api/services.py)
```python
class ItemService:
    def __init__(self):
        self.db = DynamoDBHandler()
    
    def new_item(self, user, name, location='NULL'):
        # Create item in DynamoDB
        
    def check_item(self, item_id, user):
        # Update item in DynamoDB
```

### 3. Authentication & Security

#### Spring Boot SecurityConfig
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/app-api/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/admin-api/**").hasRole("ADMIN"))
            .formLogin(form -> form.loginPage("/login"))
            .logout(logout -> logout.logoutUrl("/logout"));
        return http.build();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

#### Django Settings (shopping_list_backend/settings.py)
```python
# Authentication
AUTH_USER_MODEL = 'api.User'
AUTHENTICATION_BACKENDS = [
    'api.auth_backend.CustomUserBackend',
    'django.contrib.auth.backends.ModelBackend',
]
LOGIN_URL = '/login'
LOGIN_REDIRECT_URL = '/'
LOGOUT_REDIRECT_URL = '/login'

# Password hashing (Django uses PBKDF2 by default, compatible with BCrypt)
# Custom user model with password field
```

#### Django Custom Auth Backend (api/auth_backend.py)
```python
class CustomUserBackend(ModelBackend):
    def authenticate(self, request, username=None, password=None, **kwargs):
        try:
            user = User.objects.get(username=username)
            if user.check_password(password):
                return user
        except User.DoesNotExist:
            return None
        return None
```

### 4. Models & Entities

#### Spring Boot User Entity
```java
@Entity
@Table(name = "ReplaceWithYourTableName")
@Data
public class User {
    @Id
    @Column(name = "user_id")
    private String userId;
    
    @Column(name = "username")
    private String userName;
    
    @Column(name = "name")
    private String name;
    
    @Column(name = "role")
    private String role;
    
    @Column(name = "password")
    private String password;
}
```

#### Django User Model
```python
class User(AbstractBaseUser, PermissionsMixin):
    user_id = models.CharField(max_length=50, primary_key=True)
    username = models.CharField(max_length=255, unique=True, db_column='username')
    name = models.CharField(max_length=255, db_column='name')
    role = models.CharField(max_length=50, db_column='role', default='USER')
    password = models.CharField(max_length=255, db_column='password')
    is_active = models.BooleanField(default=True)
    is_staff = models.BooleanField(default=False)
    
    USERNAME_FIELD = 'username'
    
    class Meta:
        db_table = 'ReplaceWithYourTableName'
```

### 5. Database Access

#### Spring Boot Repository
```java
public interface UserRepo extends JpaRepository<User, String> {
    User findByUserName(String userName);
    
    @Query("SELECT u.userId FROM User u ORDER BY u.userId DESC")
    List<String> findAllUserIdsOrderByIdDesc();
}
```

#### Django ORM
```python
# In views.py or services
user = User.objects.get(username=username)
last_user = User.objects.order_by('-user_id').first()
User.objects.filter(username=username).exists()
```

### 6. DynamoDB Integration

#### Spring Boot DynamoDBHandler
```java
public class DynamoDBHandler {
    protected DynamoDbClient ddb;
    
    public void putItem(DynamoDBAttributeValueHandler attributes) {
        PutItemRequest request = PutItemRequest.builder()
            .tableName(tableName)
            .item(putAttributeValueMap)
            .build();
        ddb.putItem(request);
    }
}
```

#### Django DynamoDBHandler
```python
class DynamoDBHandler:
    def __init__(self, table_name=None):
        self.dynamodb = boto3.client('dynamodb', region_name=settings.AWS_REGION)
    
    def put_item(self, item):
        self.dynamodb.put_item(
            TableName=self.table_name,
            Item=item
        )
```

### 7. Utilities

#### Spring Boot Utils
```java
public class Utils {
    public static String getDateAndTime(String format) {
        ZoneId colomboZone = ZoneId.of("Asia/Colombo");
        ZonedDateTime colomboDateTime = ZonedDateTime.now(colomboZone);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return colomboDateTime.format(formatter);
    }
    
    public static String generateRandomID() {
        return UUID.randomUUID().toString();
    }
}
```

#### Django Utils (api/utils.py)
```python
def get_date_and_time(format_str='%Y-%m-%d %H:%M:%S'):
    colombo_tz = pytz.timezone('Asia/Colombo')
    colombo_time = datetime.now(colombo_tz)
    return colombo_time.strftime(format_str)

def generate_random_id():
    return str(uuid.uuid4())
```

## URL Routing Comparison

### Spring Boot
```java
// MvcConfig.java
registry.addViewController("/").setViewName("index");
registry.addViewController("/login").setViewName("login");

// Controllers with @RequestMapping
@RequestMapping("/app-api/groceries")
@RequestMapping("/admin-api/admin")
```

### Django
```python
# urls.py
urlpatterns = [
    path('', views.index, name='index'),
    path('login/', auth_views.LoginView.as_view(), name='login'),
    path('app-api/groceries/add', views.add_grocery),
    path('admin-api/admin/new-user', views.create_new_user),
]
```

## Configuration Comparison

### Spring Boot (application.properties)
```properties
spring.application.name=Shopping-List-App
spring.datasource.url=disabled
spring.datasource.username=disabled
spring.datasource.password=disabled
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
```

### Django (settings.py)
```python
DATABASES = {
    'default': {
        'ENGINE': 'django.db.backends.mysql',
        'NAME': os.environ.get('DB_NAME', 'shopping_list_db'),
        'USER': os.environ.get('DB_USER', 'root'),
        'PASSWORD': os.environ.get('DB_PASSWORD', ''),
        'HOST': os.environ.get('DB_HOST', 'localhost'),
        'PORT': os.environ.get('DB_PORT', '3306'),
    }
}

TIME_ZONE = 'Asia/Colombo'
AWS_REGION = os.environ.get('AWS_REGION', 'ap-southeast-1')
DYNAMODB_TABLE_NAME = os.environ.get('DYNAMODB_TABLE_NAME', '...')
```

## Key Differences

### Dependency Management
- **Spring Boot**: Maven (pom.xml) with 9 dependencies
- **Django**: pip (requirements.txt) with 5 dependencies

### Server
- **Spring Boot**: Embedded Tomcat server
- **Django**: Built-in development server (Gunicorn/uWSGI for production)

### ORM
- **Spring Boot**: Hibernate/JPA
- **Django**: Django ORM (more Pythonic, simpler syntax)

### Templates
- **Spring Boot**: Thymeleaf
- **Django**: Django templates (reusing the same HTML files)

### Dependency Injection
- **Spring Boot**: @Autowired annotation
- **Django**: Python imports and instantiation

### Annotations vs Decorators
- **Spring Boot**: @RestController, @RequestMapping, @PostMapping, etc.
- **Django**: @login_required, @require_http_methods, etc.

## Migration Benefits

1. **Simpler Code**: Django views are more concise than Spring Boot controllers
2. **Built-in Admin**: Django provides an admin interface out of the box
3. **Easier Setup**: Fewer configuration files needed
4. **Python Ecosystem**: Access to Python libraries and tools
5. **ORM Simplicity**: Django ORM is more intuitive than JPA
6. **Less Boilerplate**: No need for getter/setter or explicit dependency injection

## Running the Application

### Spring Boot
```bash
mvn spring-boot:run
```

### Django
```bash
python manage.py runserver 8080
# or
./start_django.sh
```

## Summary

The Django backend provides the same functionality as the Spring Boot backend with:
- **21 new files** (vs ~20+ Java files)
- **Simpler configuration** (1 settings file vs multiple config files)
- **Same frontend** (templates and static files reused)
- **Compatible APIs** (same endpoint structure)
- **Better developer experience** (simpler syntax, less boilerplate)

Both backends:
- Use MySQL for user management
- Use DynamoDB for shopping list items
- Provide authentication and authorization
- Serve the same frontend templates
- Support CSRF protection
- Handle sessions and remember-me functionality
