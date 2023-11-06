package com.spotifyclone.demospotifyclone.Lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.spotifyclone.demospotifyclone.model.Album;
import com.spotifyclone.demospotifyclone.service.MusicService;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import com.google.gson.Gson;

public class LambdaHandlers {

public static class  GetPresignedUrlsLambdaHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private final MusicService musicService;

    public GetPresignedUrlsLambdaHandler() {
        this.musicService = new MusicService(); 
    }

    // 通過構造器注入MusicService的實例
    public GetPresignedUrlsLambdaHandler(MusicService musicService) {
        this.musicService = musicService;
    }

 @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        Map<String, String> formData = new Gson().fromJson(input.getBody(), Map.class);
        
        String albumTitle = formData.get("albumTitle");
        String songTitle = formData.get("title");

        String songPresignedUrl = musicService.getPresignedUrlForSongUpload(albumTitle, songTitle);
        String coverPresignedUrl = musicService.getPresignedUrlForCoverUpload(albumTitle);

        Map<String, String> presignedUrls = new HashMap<>();
        presignedUrls.put("songPresignedUrl", songPresignedUrl);
        presignedUrls.put("coverPresignedUrl", coverPresignedUrl);

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        response.setStatusCode(200);
        response.setBody(new Gson().toJson(presignedUrls));
        return response;
        }
    }

public static  class SaveSongToDbLambdaHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private final MusicService musicService;

    public SaveSongToDbLambdaHandler() {
        this.musicService = new MusicService(); 
    }

    // 通過構造器注入MusicService的實例
    public SaveSongToDbLambdaHandler (MusicService musicService) {
        this.musicService = musicService;
    }


        @Override
        public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        // 將請求主體的JSON解析為Map
        Map<String, String> formData = new Gson().fromJson(input.getBody(), Map.class);

        String songId = formData.get("songId");
        String title = formData.get("title");
        String artist = formData.get("artist");
        String filePath = formData.get("filePath");
        String albumCoverUrl = formData.get("albumCoverUrl");
        String albumTitle = formData.get("albumTitle");

            String result = musicService.uploadSongToDB(songId, title, artist, filePath, albumCoverUrl, albumTitle);
            APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
            response.setStatusCode(200);
            response.setBody(result);
            return response;
        }
    }

public static class GetAllAlbumsLambdaHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private final MusicService musicService;

      public GetAllAlbumsLambdaHandler() {
        this.musicService = new MusicService(); 
    }


    // 通過構造器注入MusicService的實例
    public GetAllAlbumsLambdaHandler(MusicService musicService) {
        this.musicService = musicService;
    }


        @Override
        public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
            List<Album> albums = musicService.getAllAlbums();
            
            APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
            response.setStatusCode(200);
            response.setBody(new Gson().toJson(albums));
            return response;
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