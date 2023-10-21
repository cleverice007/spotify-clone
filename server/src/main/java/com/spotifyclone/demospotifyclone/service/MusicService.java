package com.spotifyclone.demospotifyclone.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import java.util.Optional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

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

    public String uploadSongToDB(String id, String title, String artist, String filePath, String albumCoverUrl, String albumId, String albumTitle) {
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
        Optional<Album> existingAlbum = albumRepository.findById(albumId);
        Album album;
        if (existingAlbum.isPresent()) {
            album = existingAlbum.get();
        } else {
            album = new Album();
            album.setId(albumId != null ? albumId : UUID.randomUUID().toString());
            album.setTitle(albumTitle);
        }
        album.addSong(song);
        albumRepository.save(album);
    
        return "Song uploaded to DB with ID: " + song.getId();
    }
    
    
    public String uploadFileToS3(MultipartFile audioFile, MultipartFile coverFile) {
        String bucketName = "spotify-clone-mason";

        // Upload audio file
        String audioFileName = uploadSingleFileToS3(audioFile, bucketName);

        // Upload cover image
        String coverFileName = uploadSingleFileToS3(coverFile, bucketName);

        return "Audio uploaded as: " + audioFileName + ", Cover uploaded as: " + coverFileName;
    }

    private String uploadSingleFileToS3(MultipartFile file, String bucketName) {
        try {
            File fileObj = convertMultiPartFileToFile(file);
            Path filePath = Paths.get(fileObj.getAbsolutePath());

            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();

            s3Client.putObject(putObjectRequest, filePath);

            fileObj.delete();
            return fileName;
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

}
public List<Album> getAllAlbumsFromS3() {
    List<Album> albums = new ArrayList<>();
    
    ListObjectsRequest listObjects = ListObjectsRequest.builder()
        .bucket("spotify-clone-mason")
        .prefix("albums/") // 如果你在S3中有使用資料夾結構來存儲專輯
        .build();

    ListObjectsResponse res = s3Client.listObjects(listObjects);
    for (S3Object content : res.contents()) {
        // 在這裡，你需要有一個方法將S3的object轉化為你的Album對象
        Album album = convertS3ObjectToAlbum(content);
        albums.add(album);
    }
    return albums;
}

private Album convertS3ObjectToAlbum(S3Object s3Object) {
    // 你的邏輯來將S3 object轉化為Album對象
    Album album = new Album();
    // 例如: album.setId(s3Object.key());
    // 更多的轉化邏輯...
    return album;
}
