package com.spotifyapp.backend.controller;

import com.spotifyapp.backend.service.SpotifyAuthService;
import com.spotifyapp.backend.service.SpotifyTokenService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/api")

public class AuthController {

    private final SpotifyAuthService spotifyAuthService;
    private final SpotifyTokenService spotifyTokenService;

    public AuthController(SpotifyAuthService spotifyAuthService, SpotifyTokenService spotifyTokenService) {
        this.spotifyAuthService = spotifyAuthService;
        this.spotifyTokenService = spotifyTokenService;
    }

    @PostMapping("/auth/spotify")
    public ResponseEntity<?> authenticateWithSpotify(@RequestParam("code") String code) {
        Map<String, Object> tokens = spotifyAuthService.exchangeCodeForTokens(code);

        String accessToken = (String) tokens.get("access_token");
        String refreshToken = (String) tokens.get("refresh_token");
        Integer expiresIn = (Integer) tokens.get("expires_in");

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<?> request = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Map> response = restTemplate.exchange(
                "https://api.spotify.com/v1/me",
                HttpMethod.GET,
                request,
                Map.class
        );

        String userId = (String) response.getBody().get("id");

        spotifyTokenService.saveTokens(userId, accessToken, refreshToken, expiresIn);

        return ResponseEntity.ok(Map.of("message", "Authenticated successfully", "userId", userId));
    }

    @PostMapping("/auth/spotify/refresh")
    public ResponseEntity<?> refreshSpotifyToken(@RequestParam("refresh_token") String refreshToken) {
        return ResponseEntity.ok(spotifyAuthService.refreshAccessToken(refreshToken));
    }
}
