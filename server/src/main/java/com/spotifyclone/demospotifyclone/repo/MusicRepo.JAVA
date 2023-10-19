package com.spotifyclone.demospotifyclone.repo;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Repository
public class MusicRepo {

    @Autowired
    private AmazonS3 s3Client;

    public String uploadFileToS3(MultipartFile file) {
        String bucketName = "spotify-clone-mason";
        try {
            // Convert MultipartFile to File
            File fileObj = convertMultiPartFileToFile(file);
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            
            // Upload the file
            s3Client.putObject(new PutObjectRequest(bucketName, fileName, fileObj));
            
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
