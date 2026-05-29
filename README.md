# User Management Application

A full-stack Kotlin web application built with Spring Boot, Vaadin, and PostgreSQL for managing users with role-based access control.

## 🎯 Overview

This application provides a secure user management system with the following features:
- User authentication with role-based access control (User/Admin roles)
- CRUD operations for user management (Admin only)
- Search and filter users by name or email
- Sort users by name, email, creation date, and update date
- Responsive Vaadin UI built with KaribuDSL
- Automatic database migration withs Flyway
- Seeded with 500 test users on startup

## 🛠 Technology Stack

- **Language:** Kotlin 1.9.25
- **Framework:** Spring Boot 3.5.14
- **UI Framework:** Vaadin 24.10.4
- **UI DSL:** KaribuDSL 2.1.3
- **Database:** PostgreSQL 16
- **ORM:** Spring Data JPA
- **Migration:** Flyway
- **Security:** Spring Security with BCrypt password encoding
- **Build Tool:** Gradle with Kotlin DSL
- **Containerization:** Docker & Docker Compose

## 📋 Prerequisites

- Docker and Docker Compose installed
- (Optional) JDK 21 for local development
- (Optional) Gradle 8.14.3 for local development

## 🚀 Quick Start

### Using Docker Compose (Recommended)

1. Clone the repository:
```bash
git clone <repository-url>
cd usersapp
```

2. Start the application:
```bash
docker-compose up
```

3. Access the application:
- Open your browser and navigate to: http://localhost:8080

4. Stop the application:
```bash
docker-compose down
```

### Local Development (IDEA)

**Option 1: Using Docker for Database Only**

1. Start only the database:
```bash
docker-compose up usersdb
```

2. Run the application from IDEA:
   - Open the project in IntelliJ IDEA
   - Set the active profile to `local` in Run Configuration: `-Dspring.profiles.active=local`
   - Or set environment variable: `SPRING_PROFILES_ACTIVE=local`
   - Run the main class `com.test.users.app.Application.kt`

3. Access the application at: http://localhost:8080

**Option 2: Using Local PostgreSQL**

1. Ensure PostgreSQL is running on localhost:5432 with database `usersdb`

2. Create the database and user:
```sql
CREATE DATABASE usersdb;
CREATE USER postgres WITH PASSWORD 'postgres';
GRANT ALL PRIVILEGES ON DATABASE usersdb TO postgres;
```

3. Run the application from IDEA:
   - Open the project in IntelliJ IDEA
   - Set the active profile to `local` in Run Configuration: `-Dspring.profiles.active=local`
   - Or set environment variable: `SPRING_PROFILES_ACTIVE=local`
   - Run the main class `com.test.users.app.Application.kt`

4. Access the application at: http://localhost:8080

**Note:** The `local` profile uses hardcoded database credentials for development. For production, use the Docker Compose setup or set environment variables.

## 🔐 Default Credentials

### Admin Account
- **Email:** admin@example.com
- **Password:** admin
- **Permissions:** Full CRUD access to users

### User Account
- **Email:** user@example.com
- **Password:** user
- **Permissions:** Read-only access to user list

## 📁 Project Structure

```
src/main/kotlin/com/test/users/app/
├── Application.kt              # Main application entry point
├── config/
│   ├── GlobalExceptionHandler.kt # Global exception handler
│   └── SecurityConfig.kt       # Spring Security configuration
├── controller/
│   ├── DashboardController.kt  # REST API endpoint
│   └── RoleController.kt       # Role REST API endpoint
├── dto/
│   ├── UserDto.kt             # Data transfer object for users
│   └── UserFormData.kt        # Form data with validation
├── entity/
│   ├── User.kt                # User entity
│   └── Role.kt                # Role entity
├── exception/
│   ├── GlobalExceptionHandler.kt # Global exception handler
│   ├── RoleNotFoundException.kt
│   ├── UserAlreadyExistsException.kt
│   └── UserNotFoundException.kt
├── repository/
│   ├── UserRepository.kt      # User data access
│   └── RoleRepository.kt      # Role data access
├── service/
│   ├── UserService.kt         # Business logic
│   └── CustomUserDetailsService.kt  # Spring Security user details
└── ui/
    ├── common/
    │   └── SafeExecutor.kt    # Error handling utility
    ├── component/
    │   ├── UserFormDialog.kt  # User creation/edit dialog
    │   ├── UserGrid.kt        # User list with pagination
    │   └── UserDataProvider.kt  # Pagination data provider
    └── view/
        ├── LoginView.kt       # Login page
        └── DashboardView.kt   # Main dashboard

src/main/resources/
├── application.properties      # Application configuration
├── application-local.properties # Local development configuration
└── db/migration/
    ├── V1__init_schema.sql    # Database schema
    └── V2__seed_users.sql     # Seed data (500 users)
```

## 🔧 Configuration

### Environment Variables

The application supports the following environment variables:

- `SPRING_DATASOURCE_URL` - Database JDBC URL (default: `jdbc:postgresql://127.0.0.1:5432/usersdb`)
- `SPRING_DATASOURCE_USERNAME` - Database username (default: `postgres`)
- `SPRING_DATASOURCE_PASSWORD` - Database password (default: `postgres`)

### Database Configuration

The application automatically:
- Runs Flyway migrations on startup
- Creates the database schema
- Seeds 500 test users
- Creates default admin and user accounts

## 👥 User Roles

### Admin Role
- View all users
- Create new users
- Edit existing users
- Delete users
- Search and filter users
- Sort users by any column

### User Role
- View all users
- Search and filter users
- Sort users by any column
- Read-only access (no modifications)

## 🔒 Security Features

- BCrypt password encoding
- Role-based access control
- Form-based authentication
- CSRF protection (handled by Vaadin)
- Session management
- Logout functionality

## 📊 Database Schema

### Users Table
- `id` - Primary key
- `name` - User name (indexed)
- `email` - User email (unique, indexed)
- `password` - Encrypted password
- `role_id` - Foreign key to roles table
- `created_at` - Creation timestamp (indexed)
- `updated_at` - Last update timestamp (indexed)

### Roles Table
- `id` - Primary key
- `name` - Role name (ADMIN, USER)

## 🐳 Docker Details

### Services

#### usersdb
- PostgreSQL 16
- Port: 5432
- Health check enabled
- Automatic restart on failure

#### usersapp
- Built from Dockerfile
- Port: 8080
- Waits for database to be healthy before starting
- Automatic restart on failure

### Dockerfile

The Dockerfile uses a multi-stage build:
1. Build stage: Gradle build with dependencies
2. Runtime stage: Minimal JRE image with application JAR

## 📝 Assumptions and Trade-offs

### Assumptions
1. The application will be deployed in a trusted network environment
2. Database credentials are managed securely in production (environment variables)
3. Password complexity is enforced at the application level (minimum 8 characters)
4. Email format validation is sufficient for user identification
5. Pagination is sufficient for handling large datasets (default page size: 20)

### Trade-offs
1. **No unit/integration tests** - Time constraint for this assignment; tests should be added for production
2. **No audit logging** - Not required for this assignment; should be added for production compliance
3. **No password complexity rules beyond length** - Simplified for this assignment
4. **No account lockout** - Not required for this assignment; should be added for production security
5. **No API documentation** - REST endpoint is minimal; OpenAPI/Swagger could be added
6. **Manual timestamp management** - Database trigger handles updates; could use @PreUpdate instead
7. **EAGER fetching for roles** - Simplifies queries but could cause N+1 issues; LAZY with JOIN FETCH preferred for production

## 🚧 Known Limitations

1. **No pagination UI controls** - Pagination is implemented in backend but Vaadin Grid handles it automatically
2. **No loading states** - Operations complete quickly; loading indicators could be added for better UX
3. **No accessibility features** - ARIA labels and keyboard navigation could be improved
4. **No responsive design optimizations** - Basic responsive layout; could be enhanced for mobile
5. **No real-time form validation** - Validation occurs on submit; could add real-time feedback
6. **Hardcoded test credentials** - Password hashes in migration files; should use environment variables in production

## 🔮 Future Enhancements

- [ ] Add unit and integration tests
- [ ] Implement audit logging for user changes
- [ ] Add password complexity requirements (uppercase, lowercase, numbers, special characters)
- [ ] Implement account lockout after failed login attempts
- [ ] Add session timeout configuration
- [ ] Implement OpenAPI/Swagger documentation for REST API
- [ ] Add CI/CD pipeline (GitHub Actions)
- [ ] Implement bulk operations (delete multiple users)
- [ ] Add export to CSV functionality
- [ ] Add user profile page for self-service updates
- [ ] Implement email verification for new users
- [ ] Add password reset functionality
- [ ] Implement two-factor authentication
- [ ] Add activity log for user actions
- [ ] Improve accessibility (ARIA labels, keyboard navigation)
- [ ] Enhance responsive design for mobile devices

## 📄 License

This project is created for assessment purposes.

## 👤 Author

Developed as a full-stack Kotlin developer assessment task.

## 📞 Support

For issues or questions, please refer to the assignment documentation or contact the assessment team.
