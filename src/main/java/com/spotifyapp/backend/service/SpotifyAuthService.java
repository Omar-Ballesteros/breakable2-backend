package com.spotifyapp.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.Map;

@Service
public class SpotifyAuthService {

    private final String clientId;
    private final String clientSecret;
    private final String redirectUri;
    private final RestTemplate restTemplate;

    public SpotifyAuthService(
            @Value("${spotify.client-id}") String clientId,
            @Value("${spotify.client-secret}") String clientSecret,
            @Value("${spotify.redirect-uri}") String redirectUri,
            RestTemplate restTemplate
    ) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
        this.restTemplate = restTemplate;
    }

    public Map<String, Object> exchangeCodeForTokens(String code) {
        String authUrl = "https://accounts.spotify.com/api/token";

        // Headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String basicAuth = Base64.getEncoder()
                .encodeToString((clientId + ":" + clientSecret).getBytes());
        headers.set("Authorization", "Basic " + basicAuth);

        // Body
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("code", code);
        body.add("redirect_uri", redirectUri);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    authUrl,
                    HttpMethod.POST,
                    request,
                    new ParameterizedTypeReference<>() {
                    }
            );
            return response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            System.err.println("Error al cambiar el codigo por tokens:");
            System.err.println("Status: " + ex.getStatusCode());
            System.err.println("Response: " + ex.getResponseBodyAsString());
            throw ex;
        }
    }

    public Map<String, Object> refreshAccessToken(String refreshToken) {
        String authUrl = "https://accounts.spotify.com/api/token";
        // Headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String basicAuth = Base64.getEncoder()
            .encodeToString((clientId + ":" + clientSecret).getBytes());
        headers.set("Authorization", "Basic " + basicAuth);

        // Body
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "refresh_token");
        body.add("refresh_token", refreshToken);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                authUrl,
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<>() {
                }
        );
        return response.getBody();
    }
}