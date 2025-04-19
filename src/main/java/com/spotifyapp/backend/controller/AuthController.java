package com.spotifyapp.backend.controller;

import com.spotifyapp.backend.service.SpotifyAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")

public class AuthController {

    private final SpotifyAuthService spotifyAuthService;

    public AuthController(SpotifyAuthService spotifyAuthService) {
        this.spotifyAuthService = spotifyAuthService;
    }

    @PostMapping("/auth/spotify")
    public ResponseEntity<?> authenticateWithSpotify(@RequestParam("code") String code) {
        return ResponseEntity.ok(spotifyAuthService.exchangeCodeForTokens(code));
    }

    @PostMapping("/auth/spotify/refresh")
    public ResponseEntity<?> refreshSpotifyToken(@RequestParam("refresh_token") String refreshToken) {
        return ResponseEntity.ok(spotifyAuthService.refreshAccessToken(refreshToken));
    }
}
