package com.spotifyapp.backend.controller;

import com.spotifyapp.backend.AbstractTest;
import com.spotifyapp.backend.model.SpotifyToken;
import com.spotifyapp.backend.repository.SpotifyTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SpotifyApiControllerTest extends AbstractTest {

    @Value("${spotify.test.user-id}")
    private String testUserId;

    @Value("${spotify.test.access-token}")
    private String testAccessToken;

    @Value("${spotify.test.refresh-token}")
    private String testRefreshToken;

    @Autowired
    private SpotifyTokenRepository tokenRepository;

    @BeforeEach
    public void setUpTest() {
        super.setUp();

        SpotifyToken token = SpotifyToken.builder()
                .userId(testUserId)
                .accessToken(testAccessToken)
                .refreshToken(testRefreshToken)
                .expiresAt(Instant.now().plusSeconds(3600))
                .build();

        tokenRepository.save(token);
    }

    @Test
    public void getTopArtistsShouldReturn200() throws Exception {
        String uri = "/api/me/top/artists?userId=" + testUserId;

        MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        int status = result.getResponse().getStatus();
        assertEquals(200, status);
    }

    @Test
    public void getArtistsShouldReturn200() throws Exception {
        String uri = "/api/artists/4mN0qcMxWX8oToqfDPM5yV?userId=" + testUserId;

        MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        int status = result.getResponse().getStatus();
        assertEquals(200, status);
    }

    @Test
    public void getAlbumsShouldReturn200() throws Exception {
        String uri = "/api/albums/4aawyAB9vmqN3uQ7FjRGTy?userId=" + testUserId;

        MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        int status = result.getResponse().getStatus();
        assertEquals(200, status);
    }

    @Test
    public void searchShouldReturn200() throws Exception {
        String uri = "/api/search?query=coldplay&type=artist&userId=" + testUserId;

        MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        int status = result.getResponse().getStatus();
        assertEquals(200, status);
    }
}