package com.spotifyapp.backend.service;

import com.spotifyapp.backend.model.SpotifyToken;
import com.spotifyapp.backend.repository.SpotifyTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SpotifyTokenService {

    private final SpotifyTokenRepository repository;
    private final SpotifyAuthService authService;


    public SpotifyToken getValidAccessToken(String userId) {
        SpotifyToken token = repository.findById(userId).orElseThrow();

        if (token.getExpiresAt().isBefore(Instant.now().plusSeconds(60))) {
            token = refreshAccessToken(token);
            repository.save(token);
        }

        return token;
    }

    public SpotifyToken refreshAccessToken(SpotifyToken oldToken) {
        Map<String, Object> response = authService.refreshAccessToken(oldToken.getRefreshToken());

        String newAccessToken = (String) response.get("access_token");
        Integer expiresIn = (Integer) response.get("expires_in");

        oldToken.setAccessToken(newAccessToken);
        oldToken.setExpiresAt(Instant.now().plusSeconds(expiresIn));

        return oldToken;
    }

    public void saveTokens(String userId, String accessToken, String refreshToken, long expiresIn) {
        SpotifyToken token = SpotifyToken.builder()
                .userId(userId)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiresAt(Instant.now().plusSeconds(expiresIn))
                .build();

        repository.save(token);
    }

}
