# Microservice app for tests

Two microservices:
- **auth-service** 
- **notification-service** 
## Quick Start (Docker)

1. Set admin email in auth-service/src/main/java/com/krainet/auth/security/DataInitializer.java

2. Build and run:
   ```bash
   docker-compose up --build
   ```
3. Services:
   - auth-service: http://localhost:8081
   - notification-service: http://localhost:8082
   - MySQL: localhost:3306 auth_db/notification_db

## API



Below you can see all URL's u need. I'll provide you with json templates you need to use with them, and how to use auth down below. 

## URL's
- `POST http://localhost:8081/api/auth/register` — register new user 
- `POST http://localhost:8081/api/auth/login` — returns JWT that u need to copy, u will need it later
- `GET http://localhost:8081/api/users/me` — current user profile
- `GET http://localhost:8081/api/users/{id}` — see user with specific id (admins only)
- `GET http://localhost:8081/api/users/all` — all registered users (admins only)
- `POST http://localhost:8081/api/users/create/by/admin` — create user as admin (admins only)
- `PUT http://localhost:8081/api/users/{id}` — update user info (user its self or admins)
- `DELETE http://localhost:8081/api/users/{id}` — delete user from db (user its self or admin)

## Test Data

Flyway seeds:
- Admin: username `admin`, password `admin123` (bcrypt), role `ADMIN`, email `urs email`
- User: username `user`, password `user123` (bcrypt), role `USER`, email `user@example.com`


## JSON templates
POST http://localhost:8081/api/auth/login:
```json
{ "username": "admin", "password": "admin123" }
```

POST http://localhost:8081/api/auth/register:
```json
{
"username": "alex",
"password": "12345",
"email": "alex@example.com",
"firstName": "Alex",
"lastName": "Maksimovich",
"role": "USER"
}
```
GET http://localhost:8081/api/users/me:

For this one u need to take JWT token u got in console after login.
Then u need to set up ur auth:

If ur using PostmanAPI go to Authorization tab, click on the type, and select bearer Token. Ull see a Token field where u need to paste the token u copied. It should look like this :
```
eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJrb2tvIiwicm9sZSI6IlVTRVIiLCJpYXQiOjE3NTY2NjEwMDgsImV4cCI6MTc1Njc0NzQwOH0.8xdIak6PrqKzAKoiLozCedwmJG42UwhYIpUlFqXPTRA
```

GET http://localhost:8081/api/users/{id}:

Same logic as in http://localhost:8081/api/users/me, but u need to login as admin

GET http://localhost:8081/api/users/all:

Same logic as in http://localhost:8081/api/users/me, but u need to login as admin

POST http://localhost:8081/api/users/create/by/admin:

auth logic same as in http://localhost:8081/api/users/me need to login as admin
```json
{
"username": "alex",
"password": "12345",
"email": "alex@example.com",
"firstName": "Alex",
"lastName": "Maksimovich",
"role": "USER"
}
```

PUT http://localhost:8081/api/users/{id}:

Same logic as in http://localhost:8081/api/users/me, but u need to login as admin, or user its self
```json
{
"email": "kokos@example.com",
"firstName": "Alex",
"lastName": "Maksimovcih",
"password": "12345"
}
```

DELETE http://localhost:8081/api/users/{id}:

Same logic as in http://localhost:8081/api/users/me, but u need to login as admin, or user its self
