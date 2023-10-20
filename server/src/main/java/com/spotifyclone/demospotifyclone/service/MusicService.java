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



@Service
public class MusicService {

    @Autowired
    private S3Client s3Client;

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private AlbumRepository albumRepository;

    public String uploadSongToDB(String id, String title, String artist, Integer duration, String filePath, String albumCoverUrl, String albumId, String albumTitle) {
        Song song = new Song();
        song.setId(id != null ? id : UUID.randomUUID().toString());
        song.setTitle(title);
        song.setArtist(artist);
        song.setDuration(duration);
        song.setFilePath(filePath);
        song.setAlbumCoverUrl(albumCoverUrl);
    
      // Check if the album already exists
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
    
        // Note: The song will be saved automatically due to the CascadeType.ALL setting in the Album entity
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
}