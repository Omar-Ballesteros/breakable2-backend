package com.spotifyapp.backend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SpotifyAuthServiceTest {

    private RestTemplate restTemplate;
    private SpotifyAuthService authService;

    @BeforeEach
    void setUp() {
        restTemplate = mock(RestTemplate.class);
        authService = new SpotifyAuthService(
                "fakeClientId",
                "fakeClientSecret",
                "http://localhost/callback",
                restTemplate
        );
    }

    @Test
    void testExchangeCodeForTokens() {
        // Arrange
        String code = "authCode123";
        Map<String, Object> mockResponse = new HashMap<>();
        mockResponse.put("access_token", "mockAccessToken");
        mockResponse.put("refresh_token", "mockRefreshToken");

        ResponseEntity<Map<String, Object>> responseEntity =
                new ResponseEntity<>(mockResponse, HttpStatus.OK);

        when(restTemplate.exchange(
                eq("https://accounts.spotify.com/api/token"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class)
        )).thenReturn(responseEntity);

        // Act
        Map<String, Object> result = authService.exchangeCodeForTokens(code);

        // Assert
        assertNotNull(result);
        assertEquals("mockAccessToken", result.get("access_token"));
        assertEquals("mockRefreshToken", result.get("refresh_token"));

        verify(restTemplate, times(1)).exchange(
                eq("https://accounts.spotify.com/api/token"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class)
        );
    }
    @Test
    void testRefreshAccessToken() {
        // Arrange
        String refreshToken = "mockRefreshToken123";
        Map<String, Object> mockResponse = new HashMap<>();
        mockResponse.put("access_token", "newAccessToken");

        ResponseEntity<Map<String, Object>> responseEntity =
                new ResponseEntity<>(mockResponse, HttpStatus.OK);

        when(restTemplate.exchange(
                eq("https://accounts.spotify.com/api/token"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class)
        )).thenReturn(responseEntity);

        // Act
        Map<String, Object> result = authService.refreshAccessToken(refreshToken);

        // Assert
        assertNotNull(result);
        assertEquals("newAccessToken", result.get("access_token"));

        verify(restTemplate, times(1)).exchange(
                eq("https://accounts.spotify.com/api/token"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class)
        );
    }
}