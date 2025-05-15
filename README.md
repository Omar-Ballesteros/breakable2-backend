# Breakable Toy II - Spotify Integration Backend

Backend of a fullstack app that integrates the Spotify API using Java, Spring Boot, and Gradle.
Exposes RESTful endpoints to retrieve data of artists, albums, playlists, and tracks.

## Technologies

- Java
- Spring Boot
- Gradle
- Spotify Web API
- RestTemplate
- H2 (in-memory database)
- JUnit 5

## Features

- Authenticate users with Spotify login using OAuth 2.0 Authorization code flow
- Automatic access token refresh
- Fetch user's top artists
- Retrieve artist information
- Retrieve album information
- Search for artist, album, and track
- Token storage in-memory (H2)
- Basic unit and integration tests

## Project Structure

```text
src/main/java/com/spotifyapp/backend
├── config/              // Configuration classes
├── controller/         // REST API controllers
├── model/               // Data model
├── service/             // Service logic
└── BackendApplication.java  // Main application entry point
```

```text
src/test/java/com/spotifyapp/backend
├── controller/         // Controller tests
├── service/             // Service tests
└── AbstractTest.java  // Shared test configuration
```

## How to run

1. Clone this repository
2. Set the required environment variables in `src/main/resources/application.properties`:

```properties
spotify.client-id=your-client-id
spotify.client-secret=your-client-secret
spotify.redirect-uri=http://127.0.0.1:3000  # Replace it with your frontend redirect URI
```

3. Run the application:

```bash
./gradlew bootRun
```

The app will start at:
`http://localhost:9090`


## Running tests

1 Enable the H2 console by adding the following to application.properties:

```bash
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
```

2. Perfom authentication flow manually in Postman to obtain:
   - userId
   - accessToken
   - refreshToken
  
3. Extract them from the H2 database and add to your application.properties:

```properties
spotify.test.user-id=your-test-client-id
spotify.test.access-token=your-test-access-token
spotify.test.refresh-token=your-test-refresh-token
```

4. Run your tests with:

```bash
./gradlew test
``` 


### CORS Configuration   

CORS is enabled to allow frontend requests from:
```properties
http://127.0.0.1:8080
```
You can modify this in the `SecurityConfig.java` file if needed.

### API Endpoints

| Method | Endpoint              | Description                                             | Request Body / Params                     |
|--------|-----------------------|---------------------------------------------------------|-------------------------------------------|
| GET    | /api/login            | Redirects user to Spotify's OAuth authorization page   | None                                      |
| POST   | /api/auth/spotify     | Handles Spotify OAuth flow and returns tokens          | code (Spotify authorization code)         |
| GET    | /api/access-token     | Returns a valid access token for a given user ID       | userId (user identifier)                  |
| GET    | /api/me/top/artists   | Get the top artists of the authenticated user          | userId (user identifier)                  |
| GET    | /api/artists/{id}     | Get detailed information of an artist                  | userId (user identifier)                  |
| GET    | /api/artists/{id}/top-tracks  | Get the top tracks of an artist                | userId (user identifier)                  |
| GET    | /api/artists/{id}/albums      | Get the albums of an artist                    | userId (user identifier)                  |
| GET    | /api/albums/{id}      | Get details of an album                                | userId (user identifier)                  |
| GET    | /api/search           | Search for artists, albums, or songs                   | query, type, userId (query parameters)    |
