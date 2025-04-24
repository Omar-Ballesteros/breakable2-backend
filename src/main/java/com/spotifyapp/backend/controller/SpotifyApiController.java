package com.spotifyapp.backend.controller;

import com.spotifyapp.backend.service.SpotifyApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SpotifyApiController {
    private final SpotifyApiService spotifyApiService;

    @GetMapping("me/top/artists")
    public String getTopArtists(@RequestParam String userId) {
        return spotifyApiService.getUserTopArtists(userId);
    }
}


