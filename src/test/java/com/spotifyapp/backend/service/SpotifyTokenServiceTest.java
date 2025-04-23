package com.spotifyapp.backend.service;

import com.spotifyapp.backend.model.SpotifyToken;
import com.spotifyapp.backend.repository.SpotifyTokenRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class SpotifyTokenServiceTest {

    private final SpotifyTokenRepository repository = mock(SpotifyTokenRepository.class);
    private final SpotifyAuthService authService = mock(SpotifyAuthService.class);

    private final SpotifyTokenService tokenService = new SpotifyTokenService(repository, authService);

    @Test
    //Regresa token válido si aún no expira
    public void shouldReturnValidToken_WhenTokenNotExpired() {
        SpotifyToken token = SpotifyToken.builder()
                .userId("user123")
                .accessToken("valid-token")
                .refreshToken("refresh-token")
                .expiresAt(Instant.now().plusSeconds(3600)) // aún no expira
                .build();

        when(repository.findById("user123")).thenReturn(Optional.of(token));

        SpotifyToken result = tokenService.getValidAccessToken("user123");

        assertEquals("valid-token", result.getAccessToken());
        verify(repository, times(1)).findById("user123");
        verify(authService, never()).refreshAccessToken(anyString());
    }

    @Test
    //Se refrescan los tokns una vez expirados
    public void shouldRefreshToken_WhenTokenExpired() {
        SpotifyToken expiredToken = SpotifyToken.builder()
                .userId("user123")
                .accessToken("old-token")
                .refreshToken("refresh-token")
                .expiresAt(Instant.now().minusSeconds(10)) // ya expiró
                .build();

        when(repository.findById("user123")).thenReturn(Optional.of(expiredToken));
        when(authService.refreshAccessToken("refresh-token")).thenReturn(Map.of(
                "access_token", "new-token",
                "expires_in", 3600
        ));

        SpotifyToken result = tokenService.getValidAccessToken("user123");

        assertEquals("new-token", result.getAccessToken());
        assertTrue(result.getExpiresAt().isAfter(Instant.now()));
        verify(repository).save(result);
    }

    @Test
    //Los tokens se guardan correctamente
    void testSaveTokens_SavesCorrectly() {
        String userId = "user123";
        String accessToken = "access";
        String refreshToken = "refresh";
        long expiresIn = 3600;

        tokenService.saveTokens(userId, accessToken, refreshToken, expiresIn);

        verify(repository).save(Mockito.argThat(token ->
                token.getUserId().equals(userId) &&
                        token.getAccessToken().equals(accessToken) &&
                        token.getRefreshToken().equals(refreshToken) &&
                        token.getExpiresAt().isAfter(Instant.now())
        ));
    }
}
