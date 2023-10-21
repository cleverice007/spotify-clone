package com.spotifyclone.demospotifyclone.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.spotifyclone.demospotifyclone.service.MusicService;
import com.spotifyclone.demospotifyclone.model.Album;

@RestController
public class MusicUploadController {

    @Autowired
    MusicService musicService;

    @PostMapping("/upload")
    public String uploadMusic(
            @RequestParam("audioFile") MultipartFile audioFile,
            @RequestParam("coverFile") MultipartFile coverFile,
            @RequestParam("albumTitle") String albumTitle,
            @RequestParam(value = "albumId", required = false) String albumId,
            @RequestParam("songTitle") String songTitle,
            @RequestParam("artist") String artist
    ) {
        // Upload to S3
        String s3UploadStatus = musicService.uploadFileToS3(audioFile, coverFile);
    
        // Upload to Database
        // 注意：我們移除了duration參數
        String dbUploadStatus = musicService.uploadSongToDB(null, songTitle, artist, null, null, albumId, albumTitle);
    
        return s3UploadStatus + " and " + dbUploadStatus;
    }    
}
