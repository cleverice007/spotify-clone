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
     *
     * @param audioFile    音頻文件
     * @param coverFile    封面圖片
     * @param albumTitle   專輯名稱
     * @param songTitle    歌曲標題
     * @return             上傳結果
     */
    public String handleUploadSong(MultipartFile audioFile, MultipartFile coverFile, String albumTitle, String songTitle) {
        // 使用MusicService上傳音樂和封面到S3
        String uploadResult = musicService.uploadFileToS3(audioFile, coverFile, albumTitle, songTitle);
        if (uploadResult.contains("Error")) {
            return uploadResult;
        }

        // 構造S3的檔案路徑
        String audioPath = albumTitle + "/songs/" + songTitle + ".mp3";
        String coverPath = albumTitle + "/cover/cover.jpg";
        
        // 使用MusicService將歌曲資料上傳到數據庫
        String dbResult = musicService.uploadSongToDB(null, songTitle, "Unknown Artist", audioPath, coverPath, albumTitle);

        return dbResult;
    }

    /**
     * 處理獲取所有專輯列表的Lambda請求
     *
     * @return 專輯列表
     */
    public List<Album> handleGetAllAlbums() {
        return musicService.getAllAlbums();
    }
}
