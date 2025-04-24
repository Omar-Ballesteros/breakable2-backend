package com.spotifyapp.backend.service;

import com.spotifyapp.backend.model.SpotifyToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class SpotifyApiService {

    private final SpotifyTokenService tokenService;
    private final RestTemplate restTemplate;

    public String getUserTopArtists(String userId) {
        SpotifyToken token = tokenService.getValidAccessToken(userId);
        return makeGetRequest("https://api.spotify.com/v1/me/top/artists", token);
    }

    private String makeGetRequest(String url, SpotifyToken token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token.getAccessToken());
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                String.class
        );
        return response.getBody();
    }
}
