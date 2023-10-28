package com.spotifyclone.demospotifyclone.Lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.spotifyclone.demospotifyclone.model.Album;
import com.spotifyclone.demospotifyclone.service.MusicService;
import java.util.List;
import java.util.Map;

 class LambdaHandlers {

    private static final MusicService musicService = new MusicService();

    // Lambda Handler 用於獲取預先簽名的URL
    public static class GetPresignedUrlLambdaHandler implements RequestHandler<Map<String, Object>, String> {
        
        @Override
        public String handleRequest(Map<String, Object> input, Context context) {
            String albumTitle = (String) input.get("albumTitle");
            String songTitle = (String) input.get("songTitle");
            
            return musicService.getPresignedUrlForUpload(albumTitle, songTitle);
        }
    }

    // Lambda Handler 用於將歌曲信息上傳到數據庫
    public static class SaveSongToDbLambdaHandler implements RequestHandler<Map<String, Object>, String> {

        @Override
        public String handleRequest(Map<String, Object> input, Context context) {
            String songId = (String) input.get("songId");
            String title = (String) input.get("title");
            String artist = (String) input.get("artist");
            String filePath = (String) input.get("filePath");  // 此filePath應該是S3中的文件路徑
            String albumCoverUrl = (String) input.get("albumCoverUrl");  // 若封面也上傳到S3，此處也應該是S3的URL
            String albumTitle = (String) input.get("albumTitle");
            
            return musicService.uploadSongToDB(songId, title, artist, filePath, albumCoverUrl, albumTitle);
        }
    }


    public static class GetAllAlbumsLambdaHandler implements RequestHandler<Map<String, Object>, String> {

        @Override
        public String handleRequest(Map<String, Object> input, Context context) {
            List<Album> albums = musicService.getAllAlbums();
            return albums.toString();
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