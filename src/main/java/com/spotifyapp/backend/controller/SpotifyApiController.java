package com.spotifyapp.backend.controller;

import com.spotifyapp.backend.service.SpotifyApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SpotifyApiController {
    private final SpotifyApiService spotifyApiService;

    @GetMapping("me/top/artists")
    public String getTopArtists(@RequestParam String userId) {
        return spotifyApiService.getUserTopArtists(userId);
    }

    @GetMapping("/artists/{id}")
    public String getArtist(@PathVariable String id, @RequestParam String userId) {
        return spotifyApiService.getArtist(id, userId);
    }
}


