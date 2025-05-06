package com.spotifyapp.backend.controller;

import com.spotifyapp.backend.model.SpotifyToken;
import com.spotifyapp.backend.service.SpotifyAuthService;
import com.spotifyapp.backend.service.SpotifyTokenService;
import org.slf4j.Logger;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.slf4j.LoggerFactory;

import java.util.Map;

@RestController
@RequestMapping("/api")

public class AuthController {

    private final SpotifyAuthService spotifyAuthService;
    private final SpotifyTokenService spotifyTokenService;
    private final Logger logger = LoggerFactory.getLogger(AuthController.class);

    public AuthController(SpotifyAuthService spotifyAuthService, SpotifyTokenService spotifyTokenService) {
        this.spotifyAuthService = spotifyAuthService;
        this.spotifyTokenService = spotifyTokenService;
    }

    @PostMapping("/auth/spotify")
    public ResponseEntity<?> authenticateWithSpotify(@RequestParam("code") String code) {
        try {


            Map<String, Object> tokens = spotifyAuthService.exchangeCodeForTokens(code);

            String accessToken = (String) tokens.get("access_token");
            String refreshToken = (String) tokens.get("refresh_token");
            Integer expiresIn = (Integer) tokens.get("expires_in");

            if (accessToken == null || refreshToken == null || expiresIn == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Invalid tokens"));
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);
            HttpEntity<?> request = new HttpEntity<>(headers);
            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    "https://api.spotify.com/v1/me",
                    HttpMethod.GET,
                    request,
                    new ParameterizedTypeReference<>() {
                    }
            );

            Map<String, Object> responseBody = response.getBody();
            if (responseBody == null || !responseBody.containsKey("id")) {
                return ResponseEntity.badRequest().body(Map.of("error", "Could not fetch user info"));
            }

            String userId = (String) responseBody.get("id");

            spotifyTokenService.saveTokens(userId, accessToken, refreshToken, expiresIn);

            return ResponseEntity.ok(Map.of("userId", userId, "accessToken", accessToken, "expiresIn", expiresIn));
        } catch (Exception e) {
            logger.error("An unexpected error occurred", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Unexpected error", "message", e.getMessage()));
        }
    }

    @GetMapping("/access-token")
    public ResponseEntity<Map<String, String>> getAccessToken(@RequestParam String userId) {
        SpotifyToken token = spotifyTokenService.getValidAccessToken(userId);
        return ResponseEntity.ok(Map.of("access_token", token.getAccessToken()));
    }
}
