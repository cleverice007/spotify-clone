/* 
package com.spotifyclone.demospotifyclone.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.spotifyclone.demospotifyclone.service.MusicService;
import com.spotifyclone.demospotifyclone.model.Album;
import java.util.List;


@RestController
public class MusicUploadController {

    @Autowired
    MusicService musicService;

    @PostMapping("/upload")
    public String uploadMusic(
            @RequestParam("audioFile") MultipartFile audioFile,
            @RequestParam("coverFile") MultipartFile coverFile,
            @RequestParam("albumTitle") String albumTitle,
            @RequestParam("songTitle") String songTitle,
            @RequestParam("artist") String artist
    ) {
        // Upload to S3 with the album and song titles for structured organization
        String s3UploadStatus = musicService.uploadFileToS3(audioFile, coverFile, albumTitle, songTitle);
        
        // Upload to Database
        String dbUploadStatus = musicService.uploadSongToDB(null, songTitle, artist, null, null,  albumTitle);
        
        return s3UploadStatus + " and " + dbUploadStatus;
    }
    
    @GetMapping("/albums")
    public ResponseEntity<List<Album>> getAllAlbums() {
        List<Album> albums = musicService.getAllAlbums();
        return ResponseEntity.ok(albums);
    }
    
}

*/