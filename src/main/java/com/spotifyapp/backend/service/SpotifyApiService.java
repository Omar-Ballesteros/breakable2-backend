package com.spotifyapp.backend.service;

import com.spotifyapp.backend.model.SpotifyToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SpotifyApiService {

    private final SpotifyTokenService tokenService;
    private final RestTemplate restTemplate;

    public String getUserTopArtists(String userId) {
        SpotifyToken token = tokenService.getValidAccessToken(userId);
        return makeGetRequest("https://api.spotify.com/v1/me/top/artists", token);
    }

    public String getArtist(String artistId, String userId) {
        SpotifyToken token = tokenService.getValidAccessToken(userId);
        return makeGetRequest("https://api.spotify.com/v1/artists/" + artistId, token);
    }

    public String getArtistTopTracks(String artistId, String userId) {
        SpotifyToken token = tokenService.getValidAccessToken(userId);
        return makeGetRequest("https://api.spotify.com/v1/artists/" + artistId + "/top-tracks", token);
    }

    public String getArtistAlbums(String artistId, String userId) {
        SpotifyToken token = tokenService.getValidAccessToken(userId);
        return makeGetRequest("https://api.spotify.com/v1/artists/" + artistId + "/albums", token);
    }

    public String getAlbum(String albumId, String userId) {
        SpotifyToken token = tokenService.getValidAccessToken(userId);
        return makeGetRequest("https://api.spotify.com/v1/albums/" + albumId, token);
    }

    public String search(String query, String type, String userId) {
        SpotifyToken token = tokenService.getValidAccessToken(userId);
        String encodedQuery = UriUtils.encodeQuery(query, StandardCharsets.UTF_8);
        String url = "https://api.spotify.com/v1/search?q=" + encodedQuery + "&type=" + type;
        return makeGetRequest(url, token);
    }

    private String makeGetRequest(String url, SpotifyToken token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token.getAccessToken());
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    String.class
            );
            return response.getBody();
        } catch (HttpClientErrorException.TooManyRequests e) {
            String retryAfterHeader = Objects.requireNonNull(e.getResponseHeaders()).getFirst("Retry-After");
            if (retryAfterHeader != null) {
                int retryAfterSeconds = Integer.parseInt(retryAfterHeader);
                try {
                    Thread.sleep(retryAfterSeconds * 1000L);
                    ResponseEntity<String> retryResponse = restTemplate.exchange(
                            url,
                            HttpMethod.GET,
                            entity,
                            String.class
                    );
                    return retryResponse.getBody();
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Thread interrupted while handling rate limit", ie);
                }
            } else {
                throw e;
            }
        }
    }
}
