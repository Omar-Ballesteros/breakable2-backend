package com.spotifyapp.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpotifyToken {
    @Id
    private String userId;

    private String accessToken;
    private String refreshToken;
    private Instant expiresAt;
}
