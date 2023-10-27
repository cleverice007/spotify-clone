package com.spotifyclone.demospotifyclone.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class LambdaHandlerService {

    @Autowired
    private MusicService musicService;

    /**
     * 處理上傳歌曲的Lambda請求
     */
    public String handleUploadSong(MultipartFile audioFile, MultipartFile coverFile, String albumTitle, String songTitle) {
        return musicService.uploadFileToS3(audioFile, coverFile, albumTitle, songTitle);
    }

    /**
     * 處理獲取所有專輯列表的Lambda請求
     */
    public List<Album> handleGetAllAlbums() {
        return musicService.getAllAlbums();
    }
}
