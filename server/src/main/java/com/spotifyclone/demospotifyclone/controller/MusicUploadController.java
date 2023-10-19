package com.spotifyclone.demospotifyclone.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.spotifyclone.demospotifyclone.repo.MusicRepo;

@RestController
public class MusicUploadController {

    @Autowired
    MusicRepo musicRepo;

    @PostMapping("/upload")
    public String uploadMusic(@RequestParam("file") MultipartFile file) {
        return musicRepo.uploadFileToS3(file);
    }
}
