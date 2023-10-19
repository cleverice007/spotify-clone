package com.spotifyclone.demospotifyclone.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Repository
public class MusicRepo {

    @Autowired
    private S3Client s3Client;

    public String uploadFileToS3(MultipartFile file) {
        String bucketName = "spotify-clone-mason";
        try {
            // Convert MultipartFile to File
            File fileObj = convertMultiPartFileToFile(file);
            Path filePath = Paths.get(fileObj.getAbsolutePath());

            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

            // Upload the file
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();

            PutObjectResponse response = s3Client.putObject(putObjectRequest, filePath);

            fileObj.delete();
            return "File uploaded : " + fileName;
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