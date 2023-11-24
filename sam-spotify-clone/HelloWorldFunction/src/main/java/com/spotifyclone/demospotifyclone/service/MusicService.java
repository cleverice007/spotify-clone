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
import com.spotifyclone.demospotifyclone.dao.AlbumDaoImpl;
import com.spotifyclone.demospotifyclone.dao.AlbumDao;


import org.bytedeco.javacv.FFmpegFrameGrabber;
import java.util.Base64;
import software.amazon.awssdk.core.sync.RequestBody;


@Service
public class MusicService {

    private S3Client s3Client;
    private String bucketName;
    private AlbumDao albumDao;

    public MusicService() {
        this.bucketName = System.getenv("BUCKET_NAME");  
        this.s3Client = S3Client.create();
        this.albumDao = new AlbumDaoImpl();
    }


    public String getPresignedUrlForSongUpload(String albumTitle, String songTitle) {
        String s3Path = albumTitle + "/songs/" + songTitle + ".mp3";
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(s3Path)
                .build();

        S3Presigner presigner = S3Presigner.builder().build();
        PresignedPutObjectRequest presignedRequest = presigner.presignPutObject(b -> b.putObjectRequest(putObjectRequest).signatureDuration(Duration.ofMinutes(15)));
        String presignedUrl = presignedRequest.url().toString();
        presigner.close();

        return presignedUrl;
    }

    public String getPresignedUrlForCoverUpload(String albumTitle) {
        String s3Path = albumTitle + "/cover.jpg";
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(s3Path)
                .build();

        S3Presigner presigner = S3Presigner.builder().build();
        PresignedPutObjectRequest presignedRequest = presigner.presignPutObject(b -> b.putObjectRequest(putObjectRequest).signatureDuration(Duration.ofMinutes(15)));
        String presignedUrl = presignedRequest.url().toString();
        presigner.close();

        return presignedUrl;
    }

    public String uploadSongToDB(String id, String title, String artist, String filePath, String albumCoverUrl, String albumTitle) {
        Song song = new Song();
        song.setId(id != null ? id : UUID.randomUUID().toString());
        song.setTitle(title);
        song.setArtist(artist);
        song.setDuration(getMusicDuration(filePath));
        song.setFilePath(filePath);
        song.setAlbumCoverUrl(albumCoverUrl);

        Optional<Album> existingAlbum = albumDao.findByTitle(albumTitle);
        Album album = existingAlbum.orElseGet(() -> {
            Album newAlbum = new Album();
            newAlbum.setId(UUID.randomUUID().toString());
            newAlbum.setTitle(albumTitle);
            return newAlbum;
        });

        album.addSong(song);
        albumDao.save(album);

        return "Song uploaded to DB with ID: " + song.getId();
    }

    private int getMusicDuration(String videoPath) {
        try (FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(videoPath)) {
            grabber.start();
            int lengthInSeconds = (int) (grabber.getLengthInTime() / (1000 * 1000));
            grabber.stop();
            return lengthInSeconds;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public List<Album> getAllAlbums() {
        return albumDao.findAll();
    }



}