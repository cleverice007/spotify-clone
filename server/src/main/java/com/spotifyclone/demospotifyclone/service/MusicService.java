package com.spotifyclone.demospotifyclone.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CommonPrefix;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Object;

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


@Service
public class MusicService {

    @Autowired
    private S3Client s3Client;

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private AlbumRepository albumRepository;

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

    
    
public String uploadFileToS3(MultipartFile audioFile, MultipartFile coverFile, String albumTitle, String songTitle) {
    String bucketName = "spotify-clone-mason";

    // Upload audio file
    String audioPath = albumTitle + "/songs/" + songTitle + ".mp3";
    String audioFileName = uploadSingleFileToS3(audioFile, bucketName, audioPath);

    // Upload cover image
    String coverPath = albumTitle + "/cover/cover.jpg";
    String coverFileName = uploadSingleFileToS3(coverFile, bucketName, coverPath);

    return "Audio uploaded as: " + audioFileName + ", Cover uploaded as: " + coverFileName;
}

private String uploadSingleFileToS3(MultipartFile file, String bucketName, String s3Path) {
    try {
        File fileObj = convertMultiPartFileToFile(file);
        Path filePath = Paths.get(fileObj.getAbsolutePath());

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(s3Path)
                .build();

        s3Client.putObject(putObjectRequest, filePath);

        fileObj.delete();
        return s3Path;  // 返回完整的S3路徑
    } catch (Exception e) {
        e.printStackTrace();
        return "Error occurred while uploading file.";
    }
}

private File convertMultiPartFileToFile(MultipartFile file) {
    File fileObj = new File(file.getOriginalFilename());
    try (FileOutputStream outputStream = new FileOutputStream(fileObj)) {
        outputStream.write(file.getBytes());
    } catch (IOException e) {
        e.printStackTrace();
    }
    return fileObj;
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

public List<Album> getAllAlbumsFromS3() {
    List<Album> albums = new ArrayList<>();

    ListObjectsRequest listObjects = ListObjectsRequest.builder()
        .bucket("spotify-clone-mason")
        .prefix("albums/") 
        .delimiter("/") // 使用delimiter來獲取專輯資料夾
        .build();

    ListObjectsResponse res = s3Client.listObjects(listObjects);
    for (CommonPrefix prefix : res.commonPrefixes()) { // 使用commonPrefixes獲取所有的專輯資料夾
        Album album = getAlbumDetailsFromS3(prefix.prefix());
        albums.add(album);
    }
    return albums;
}

private Album getAlbumDetailsFromS3(String albumPrefix) {
    List<String> songUrls = new ArrayList<>();
    String coverUrl = null;

    ListObjectsRequest listObjects = ListObjectsRequest.builder()
        .bucket("spotify-clone-mason")
        .prefix(albumPrefix)
        .build();

    ListObjectsResponse res = s3Client.listObjects(listObjects);
    for (S3Object content : res.contents()) {
        String key = content.key();
        if (key.endsWith(".mp3")) {
            songUrls.add("https://s3-region.amazonaws.com/spotify-clone-mason/" + key); // 此URL可能需要調整
        } else if (key.endsWith("cover/cover.jpg")) {
            coverUrl = "https://s3-region.amazonaws.com/spotify-clone-mason/" + key;
        }
    }

    Album album = new Album();
    album.setSongs(songUrls);
    album.setCoverUrl(coverUrl);
    // 你可以在這裡根據 albumPrefix 解析專輯名稱

    return album;
}

}