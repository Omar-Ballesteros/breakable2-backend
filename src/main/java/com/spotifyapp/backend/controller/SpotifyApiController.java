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

    @GetMapping("/albums/{id}")
    public String getAlbum(@PathVariable String id, @RequestParam String userId) {
        return spotifyApiService.getAlbum(id, userId);
    }

    @GetMapping("/search")
    public String search(@RequestParam String query, @RequestParam String type, @RequestParam String userId) {
        return spotifyApiService.search(query, type, userId);
    }
}


