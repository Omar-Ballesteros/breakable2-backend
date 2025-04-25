package com.spotifyapp.backend.service;

import com.spotifyapp.backend.model.SpotifyToken;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
    public void testGetUserTopArtists() {

        SpotifyToken mockToken = new SpotifyToken();
        mockToken.setAccessToken("mockAccessToken");

        when(tokenService.getValidAccessToken(any(String.class))).thenReturn(mockToken);

        String expectedResponse = "{\"items\":[]}";
        ResponseEntity<String> responseEntity = new ResponseEntity<>(expectedResponse, HttpStatus.OK);

        when(restTemplate.exchange(
                eq("https://api.spotify.com/v1/me/top/artists"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(String.class)
        )).thenReturn(responseEntity);

        String actualResponse = spotifyApiService.getUserTopArtists("someUserId");

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void testGetArtist() {
        SpotifyToken mockToken = new SpotifyToken();
        mockToken.setAccessToken("mockAccessToken");

        when(tokenService.getValidAccessToken(any(String.class))).thenReturn(mockToken);

        String artistId = "123";
        String expectedResponse = "{\"name\":\"Mock Artist\"}";
        ResponseEntity<String> responseEntity = new ResponseEntity<>(expectedResponse, HttpStatus.OK);

        when(restTemplate.exchange(
                eq("https://api.spotify.com/v1/artists/" + artistId),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(String.class)
        )).thenReturn(responseEntity);

        String actualResponse = spotifyApiService.getArtist(artistId, "someUserId");

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void testGetAlbum() {
        SpotifyToken mockToken = new SpotifyToken();
        mockToken.setAccessToken("mockAccessToken");

        when(tokenService.getValidAccessToken(any(String.class))).thenReturn(mockToken);

        String albumId = "456";
        String expectedResponse = "{\"name\":\"Mock Album\"}";
        ResponseEntity<String> responseEntity = new ResponseEntity<>(expectedResponse, HttpStatus.OK);

        when(restTemplate.exchange(
                eq("https://api.spotify.com/v1/albums/" + albumId),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(String.class)
        )).thenReturn(responseEntity);

        String actualResponse = spotifyApiService.getAlbum(albumId, "someUserId");

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void testSearch() {
        SpotifyToken mockToken = new SpotifyToken();
        mockToken.setAccessToken("mockAccessToken");

        when(tokenService.getValidAccessToken(any(String.class))).thenReturn(mockToken);

        String query = "Muse";
        String type = "artist";
        String encodedQuery = "Muse"; // No necesita encoding en este caso

        String expectedUrl = "https://api.spotify.com/v1/search?q=" + encodedQuery + "&type=" + type;
        String expectedResponse = "{\"artists\":{\"items\":[]}}";
        ResponseEntity<String> responseEntity = new ResponseEntity<>(expectedResponse, HttpStatus.OK);

        when(restTemplate.exchange(
                eq(expectedUrl),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(String.class)
        )).thenReturn(responseEntity);

        String actualResponse = spotifyApiService.search(query, type, "someUserId");

        assertEquals(expectedResponse, actualResponse);
    }
}