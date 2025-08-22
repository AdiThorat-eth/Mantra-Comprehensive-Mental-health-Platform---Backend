# Mantra - Mental Health Support Platform

A comprehensive mental health support platform built with Spring Boot, featuring AI-powered chatbot, meditation resources, therapy booking, and community support.

## 🚀 Features

- **JWT Authentication** - Secure user registration and login
- **AI Chatbot** - Gemini API integration with rule-based fallbacks
- **Meditation Library** - Guided meditation resources
- **Therapy Booking** - Connect with professional therapists
- **Community Forum** - Peer support and discussions
- **Contact System** - Get in touch with support
- **Admin Dashboard** - System management and analytics

## 🛠️ Technology Stack

- **Backend**: Spring Boot 3.x, Java 17
- **Database**: MySQL
- **Security**: Spring Security with JWT
- **AI Integration**: Google Gemini API
- **Documentation**: Swagger/OpenAPI
- **Build Tool**: Maven

## 📋 Prerequisites

- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+
- IDE (IntelliJ IDEA, Eclipse, VS Code)

## 🔧 Setup Instructions

### 1. Clone the Repository
```bash
git clone <repository-url>
cd mantra-backend
```

### 2. Database Setup
```sql
CREATE DATABASE mantra_db;
```

Update `src/main/resources/application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/mantra_db?createDatabaseIfNotExist=true&serverTimezone=UTC
    username: your_mysql_username
    password: your_mysql_password
```

### 3. Gemini API Configuration
Get your Gemini API key from [Google AI Studio](https://makersuite.google.com/app/apikey)

Update `application.yml`:
```yaml
gemini:
  api:
    key: YOUR_GEMINI_API_KEY_HERE
```

### 4. Build and Run
```bash
# Build the project
mvn clean compile

# Run the application
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## 📖 API Documentation

Once running, access Swagger UI at:
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API Docs**: http://localhost:8080/api-docs

## 🔐 Demo Credentials

The application includes demo data for testing:

### Admin Account
- **Email**: admin@mantra.com
- **Password**: password

### User Accounts
- **Email**: sanket@example.com, **Password**: password
- **Email**: adi@example.com, **Password**: password

## 🔧 API Endpoints

### Authentication
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - User login
- `POST /api/auth/logout` - User logout
- `GET /api/users/me` - Get current user profile

### Meditations
- `GET /api/meditations` - List all meditations
- `GET /api/meditations/{id}` - Get meditation by ID
- `POST /api/meditations` - Create meditation (Admin only)
- `PUT /api/meditations/{id}` - Update meditation (Admin only)
- `DELETE /api/meditations/{id}` - Delete meditation (Admin only)

### Therapy
- `GET /api/therapists` - List available therapists
- `POST /api/bookings` - Book therapy session
- `GET /api/bookings/me` - Get user's bookings
- `GET /api/bookings/all` - Get all bookings (Admin only)

### Community
- `GET /api/posts` - List all posts
- `POST /api/posts` - Create new post
- `GET /api/posts/{id}` - Get post with replies
- `POST /api/posts/{id}/replies` - Reply to post

### Chatbot
- `POST /api/chatbot/message` - Send message to AI chatbot

### Contact
- `POST /api/contact` - Send contact message
- `GET /api/contact` - Get all messages (Admin only)

### Admin
- `GET /api/admin/stats` - System statistics
- `GET /api/admin/users` - List all users

## 🗂️ Project Structure

```
src/main/java/com/mantra/
├── MantraApplication.java
├── auth/                    # Authentication & User Management
├── chatbot/                 # AI Chatbot Service
├── meditation/              # Meditation Resources
├── therapy/                 # Therapy Booking System
├── community/               # Community Forum
├── contact/                 # Contact Management
├── admin/                   # Admin Dashboard
├── config/                  # Security & Configuration
├── shared/                  # Common DTOs & Utilities
└── utils/                   # Utility Classes
```

## 🔒 Security Features

- JWT-based authentication
- Role-based authorization (USER, ADMIN)
- Password encryption with BCrypt
- CORS configuration for frontend integration
- Input validation and error handling

## 🤖 Chatbot Features

- **Rule-based responses** for common mental health queries
- **Gemini AI integration** for complex conversations
- **Conversation history** stored per user
- **Fallback mechanisms** when AI is unavailable

## 📊 Demo Data

The application automatically seeds demo data including:
- Sample users (admin and regular users)
- Meditation content with YouTube links
- Professional therapists
- Community forum posts and replies
- Sample bookings and contact messages
- Chat conversation history

## 🚀 Production Deployment

For production deployment:

1. Update `application.yml` with production database credentials
2. Set strong JWT secret key
3. Configure proper CORS origins
4. Set up proper logging configuration
5. Use environment variables for sensitive data

## 🤝 Contributing

1. Fork the repository
2. Create feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 📞 Support

For support and queries:
- Create an issue in the repository
- Contact: support@mantra.com

---

Built with ❤️ for mental health awareness and support.