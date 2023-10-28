package com.spotifyclone.demospotifyclone.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CommonPrefix;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Object;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import java.time.Duration;


import java.util.ArrayList;
import java.util.Optional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

import com.spotifyclone.demospotifyclone.model.Song;
import com.spotifyclone.demospotifyclone.model.Album;
import com.spotifyclone.demospotifyclone.repo.AlbumRepository;
import com.spotifyclone.demospotifyclone.repo.SongRepository;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import java.util.Base64;
import software.amazon.awssdk.core.sync.RequestBody;


@Service
public class MusicService {

    @Autowired
    private S3Client s3Client;

    @Autowired
    @Value("${aws.s3.bucket-name}")
    private String bucketName;



    @Autowired
    private SongRepository songRepository;

    @Autowired
    private AlbumRepository albumRepository;

   // 獲取預先簽名的URL以供上傳
   public String getPresignedUrlForSongUpload(String albumTitle, String songTitle) {
    String s3Path = albumTitle + "/songs/" + songTitle + ".mp3";
    
    return generatePresignedUrl(s3Path);
}
public String getPresignedUrlForCoverUpload(String albumTitle) {
    String s3Path = albumTitle + "/cover.jpg";
    
    return generatePresignedUrl(s3Path);
}

private String generatePresignedUrl(String s3Path) {
    PutObjectRequest putObjectRequest = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(s3Path)
            .build();

    S3Presigner presigner = S3Presigner.builder().build();

    PresignedPutObjectRequest presignedRequest = presigner.presignPutObject(b -> b.putObjectRequest(putObjectRequest).signatureDuration(Duration.ofMinutes(15)));

    String presignedUrl = presignedRequest.url().toString();

    presigner.close(); // 關閉presigner資源

    return presignedUrl;
}


// 上傳歌曲信息到數據庫
public String uploadSongToDB(String id, String title, String artist, String filePath, String albumCoverUrl, String albumTitle) {
    Song song = new Song();
    song.setId(id != null ? id : UUID.randomUUID().toString());
    song.setTitle(title);
    song.setArtist(artist);
    
    // 使用getMusicDuration計算歌曲的持續時間
    int duration = getMusicDuration(filePath);
    if (duration == -1) {
        return "Error occurred while calculating song duration.";
    }
    song.setDuration(duration);
    
    song.setFilePath(filePath);
    song.setAlbumCoverUrl(albumCoverUrl);

    // 檢查專輯是否已存在
    Optional<Album> existingAlbum = albumRepository.findByTitle(albumTitle);
    Album album;
    if (existingAlbum.isPresent()) {
        album = existingAlbum.get();
    } else {
        album = new Album();
        album.setId(UUID.randomUUID().toString());
        album.setTitle(albumTitle);
    }
    album.addSong(song);
    albumRepository.save(album);

    return "Song uploaded to DB with ID: " + song.getId();
}


        // 獲得音樂長度
    private int getMusicDuration(String videoPath) {
    try (FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(videoPath)) {
        grabber.start();
        int lengthInSeconds = (int) (grabber.getLengthInTime() / (1000 * 1000));
        grabber.stop();
        return lengthInSeconds;
    } catch (Exception e) {
        e.printStackTrace();
        return -1; // 或其他表示錯誤的值
    }
}

public List<Album> getAllAlbums() {
    List<Album> albums = albumRepository.findAll();
    for (Album album : albums) {
        album.getSongs().size();  
    }
    return albums;
}


}