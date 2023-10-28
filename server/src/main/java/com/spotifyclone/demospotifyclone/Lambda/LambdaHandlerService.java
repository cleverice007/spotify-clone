package com.spotifyclone.demospotifyclone.Lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.springframework.web.multipart.MultipartFile;
import com.spotifyclone.demospotifyclone.model.Album;
import com.spotifyclone.demospotifyclone.service.MusicService;

import java.util.List;
import java.util.Map;

class LambdaHandlers {

    private static MusicService musicService = new MusicService();

    public static class UploadSongLambdaHandler implements RequestHandler<Map<String, Object>, String> {

        @Override
        public String handleRequest(Map<String, Object> input, Context context) {
            // 從input中提取所需的參數，然後調用handleUploadSong方法
            MultipartFile audioFile = (MultipartFile) input.get("audioFile");
            MultipartFile coverFile = (MultipartFile) input.get("coverFile");
            String albumTitle = (String) input.get("albumTitle");
            String songTitle = (String) input.get("songTitle");
            return handleUploadSong(audioFile, coverFile, albumTitle, songTitle);
        }

        public String handleUploadSong(MultipartFile audioFile, MultipartFile coverFile, String albumTitle, String songTitle) {
            return musicService.uploadFileToS3(audioFile, coverFile, albumTitle, songTitle);
        }
    }

    public static class GetAllAlbumsLambdaHandler implements RequestHandler<Map<String, Object>, String> {

        @Override
        public String handleRequest(Map<String, Object> input, Context context) {
            List<Album> albums = handleGetAllAlbums();
            // 將結果轉換為JSON字符串或其他適當的格式返回
            return albums.toString();
        }

        public List<Album> handleGetAllAlbums() {
            return musicService.getAllAlbums();
        }
    }
}



/*
 * package com.spotifyclone.demospotifyclone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@SpringBootApplication
public class DemospotifyCloneApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemospotifyCloneApplication.class, args);
    }

    @Component
    public class DatabaseConnectionChecker implements ApplicationListener<ContextRefreshedEvent> {

        private final DataSource dataSource;

        public DatabaseConnectionChecker(DataSource dataSource) {
            this.dataSource = dataSource;
        }

        @Override
        public void onApplicationEvent(ContextRefreshedEvent event) {
            try (Connection connection = dataSource.getConnection()) {
                System.out.println("Successfully connected to PostgreSQL database!");
            } catch (SQLException e) {
                System.out.println("Failed to connect to PostgreSQL database.");
                e.printStackTrace();
            }
        }
    }
}

 */