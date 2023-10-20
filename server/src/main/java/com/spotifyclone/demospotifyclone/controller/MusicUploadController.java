package com.spotifyclone.demospotifyclone.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.spotifyclone.demospotifyclone.service.MusicService; 
@RestController
public class MusicUploadController {

    @Autowired
    MusicService musicService;
    @PostMapping("/upload")
    public String uploadMusic(@RequestParam("audioFile") MultipartFile audioFile, @RequestParam("coverFile") MultipartFile coverFile, /* 其他歌曲和專輯信息 */) {
        // 上傳到S3
        String s3UploadStatus = musicService.uploadFileToS3(audioFile, coverFile);

        // 上傳到資料庫
        Album album = new Album(); // 這裡您需要設置專輯的各種信息
        // 設置專輯信息，例如 album.setTitle(), album.setId() 等

        String dbUploadStatus = musicService.uploadSongToDB(null, "title", "artist", 300, "filePath", "coverUrl", album);

        return s3UploadStatus + " and " + dbUploadStatus;
    }
}
