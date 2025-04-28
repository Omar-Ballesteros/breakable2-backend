# Breakable Toy II - Spotify Integration Backend

Backend de app fullstack that integrates the Spotify API using Java, Spring Boot, and Gradle.
Exposes RESTful endpoints to retrieve data of artists, albums, playlists, and tracks.

## Technologies

- Java
- Spring Boot
- Gradle
- Spotify Web API
- RestTemplate

## Features

- Authenticate users with Spotify login using OAuth 2.0
- Automatic access token refresh
- Fetch user's top artists
- Retrieve artist information
- Retrieve album information
- Search for artist, album, and track
- Basic unit and integration tests

## Project Structure

```text
src/main/java/com/spotifyapp/backend
├── controller/         // REST API controllers
├── service/             // Service logic
├── model/               // Data model
├── config/              // Configuration classes
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
2. Set the required environment variables in `application.properties`:

```properties
spotify.client-id=your-client-id
spotify.client-secret=your-client-secret
# Replace it with your frontend redirect URI
spotify.redirect-uri=http://127.0.0.1:3000
```

3. Run the application:

```bash
./gradlew bootRun
```

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
   

### API Endpoints

| Method | Endpoint             | Description                                  | Request Body     |
|--------|----------------------|----------------------------------------------|------------------|
| POST   | /auth/spotify        | Handles Spotify OAuth flow and returns tokens | code (Spotify authorization code) |
| POST   | /auth/ spotify/refresh | Refreshes access token using refresh token | refresh_token (Spotify refresh token) |
| GET    | /me/top/artists	    | Get the top artists of the authenticated user | None            |
| GET	   | /artists/{id}	      | Get detailed information of an artist	       | None            |
| GET	   | /albums/{id}	        |Get details of an album	                     | None            |
| GET	   | /search	            |Search for artists, albums, or songs	         | Query parameters (q, type) |
