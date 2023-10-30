package com.spotifyclone.demospotifyclone.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3Config {

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Bean
    public S3Client s3Client() {
        return S3Client.builder().build();
    }

    @Bean
    public String bucketName() {
        return bucketName;
    }
}

