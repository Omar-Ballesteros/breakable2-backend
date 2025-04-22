package com.spotifyapp.backend.repository;

import com.spotifyapp.backend.model.SpotifyToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpotifyTokenRepository extends JpaRepository<SpotifyToken, String> {
}
