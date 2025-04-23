package com.spotifyapp.backend.service;

import com.spotifyapp.backend.model.SpotifyToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class SpotifyApiServiceTest {

    @Mock
    private SpotifyTokenService tokenService;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private SpotifyApiService spotifyApiService;

    @Test
    public void testGetUserTopTracks() {
        // Crear un token simulado
        SpotifyToken mockToken = new SpotifyToken();
        mockToken.setAccessToken("mockAccessToken");

        // Simular el comportamiento del tokenService
        when(tokenService.getValidAccessToken(any(String.class))).thenReturn(mockToken);

        // Simular respuesta de la API de Spotify
        String expectedResponse = "{\"items\":[]}";
        ResponseEntity<String> responseEntity = new ResponseEntity<>(expectedResponse, HttpStatus.OK);

        when(restTemplate.exchange(
                eq("https://api.spotify.com/v1/me/top/tracks"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(String.class)
        )).thenReturn(responseEntity);

        // Ejecutar método
        String actualResponse = spotifyApiService.getUserTopTracks("someUserId");

        // Verificar resultado
        assertEquals(expectedResponse, actualResponse);
    }
}