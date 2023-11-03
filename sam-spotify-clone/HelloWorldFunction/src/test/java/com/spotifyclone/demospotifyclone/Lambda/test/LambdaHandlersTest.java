package com.spotifyclone.demospotifyclone.Lambda.test;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.spotifyclone.demospotifyclone.Lambda.LambdaHandlers.GetAllAlbumsLambdaHandler;
import com.spotifyclone.demospotifyclone.Lambda.LambdaHandlers.GetPresignedUrlsLambdaHandler;
import com.spotifyclone.demospotifyclone.Lambda.LambdaHandlers.SaveSongToDbLambdaHandler;
import com.spotifyclone.demospotifyclone.model.Album;
import com.spotifyclone.demospotifyclone.model.Song;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.spotifyclone.demospotifyclone.repo.AlbumRepository;
import com.spotifyclone.demospotifyclone.model.Album;


import static org.junit.jupiter.api.Assertions.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

class LambdaHandlersTest {

    private final GetPresignedUrlsLambdaHandler getUrlsHandler = new GetPresignedUrlsLambdaHandler();
    private final SaveSongToDbLambdaHandler saveSongHandler = new SaveSongToDbLambdaHandler();
    private final GetAllAlbumsLambdaHandler getAllAlbumsHandler = new GetAllAlbumsLambdaHandler();

    @Mock
    private AlbumRepository albumRepository;  // Mock AlbumRepository 接口

    @InjectMocks
    private MusicService musicService; // 自動注入 AlbumRepository


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetPresignedUrls() {
        APIGatewayProxyRequestEvent requestEvent = new APIGatewayProxyRequestEvent();
        requestEvent.setBody("{ \"albumTitle\": \"TestAlbum\", \"title\": \"TestSong\" }");

        APIGatewayProxyResponseEvent responseEvent = getUrlsHandler.handleRequest(requestEvent, context);
        assertEquals(200, responseEvent.getStatusCode());
    }

    @Test
    void testSaveSongToDb() {
        APIGatewayProxyRequestEvent requestEvent = new APIGatewayProxyRequestEvent();
        requestEvent.setBody("{ \"songId\": \"123\", \"title\": \"TestSong\", \"artist\": \"TestArtist\", \"filePath\": \"path/to/file\", \"albumCoverUrl\": \"path/to/cover\", \"albumTitle\": \"TestAlbum\" }");

        APIGatewayProxyResponseEvent responseEvent = saveSongHandler.handleRequest(requestEvent, context);
        assertEquals(200, responseEvent.getStatusCode());
    }

@Test
void testGetAllAlbums() {
    // 模擬數據
    Album album1 = new Album(); // 初始化 album1
    Album album2 = new Album(); // 初始化 album2

    // 創建 album1 的兩首歌
    Song song1 = new Song();
    song1.setId("1");
    song1.setTitle("Song1");
    song1.setArtist("Artist1");
    song1.setDuration(300);
    song1.setFilePath("/path/to/file1");
    song1.setAlbumCoverUrl("/path/to/cover1");
    
    Song song2 = new Song();
    song2.setId("2");
    song2.setTitle("Song2");
    song2.setArtist("Artist2");
    song2.setDuration(320);
    song2.setFilePath("/path/to/file2");
    song2.setAlbumCoverUrl("/path/to/cover2");
    
    album1.setSongs(Arrays.asList(song1, song2));

    // 創建 album2 的兩首歌
    Song song3 = new Song();
    song3.setId("3");
    song3.setTitle("Song3");
    song3.setArtist("Artist3");
    song3.setDuration(310);
    song3.setFilePath("/path/to/file3");
    song3.setAlbumCoverUrl("/path/to/cover3");
    
    Song song4 = new Song();
    song4.setId("4");
    song4.setTitle("Song4");
    song4.setArtist("Artist4");
    song4.setDuration(330);
    song4.setFilePath("/path/to/file4");
    song4.setAlbumCoverUrl("/path/to/cover4");
    
    album2.setSongs(Arrays.asList(song3, song4));

     // 模擬 albumRepository 的行為
     List<Album> mockAlbums = Arrays.asList(album1, album2);
     when(albumRepository.findAll()).thenReturn(mockAlbums);

     APIGatewayProxyRequestEvent requestEvent = new APIGatewayProxyRequestEvent();
     APIGatewayProxyResponseEvent responseEvent = getAllAlbumsHandler.handleRequest(requestEvent, context);

     // 驗證狀態碼和其他預期行為
     assertEquals(200, responseEvent.getStatusCode());

     // 解析響應體以確認返回的數據是否正確
     Type listType = new TypeToken<List<Album>>() {}.getType(); // 添加這個來正確地解析 JSON 到 List<Album>
     List<Album> responseBodyAlbums = new Gson().fromJson(responseEvent.getBody(), listType);

     // 驗證返回的專輯列表
     assertEquals(mockAlbums.size(), responseBodyAlbums.size());
     // 遍歷每個專輯並驗證歌曲數量和屬性
for (int i = 0; i < mockAlbums.size(); i++) {
    Album expectedAlbum = mockAlbums.get(i);
    Album actualAlbum = responseBodyAlbums.get(i);
    
    // 驗證歌曲數量
    assertEquals(expectedAlbum.getSongs().size(), actualAlbum.getSongs().size());
    
    // 遍歷歌曲並檢查屬性
    for (int j = 0; j < expectedAlbum.getSongs().size(); j++) {
        Song expectedSong = expectedAlbum.getSongs().get(j);
        Song actualSong = actualAlbum.getSongs().get(j);
        
        // 驗證歌曲屬性
        assertEquals(expectedSong.getId(), actualSong.getId());
        assertEquals(expectedSong.getTitle(), actualSong.getTitle());
        assertEquals(expectedSong.getArtist(), actualSong.getArtist());
        assertEquals(expectedSong.getDuration(), actualSong.getDuration());
        assertEquals(expectedSong.getFilePath(), actualSong.getFilePath());
        assertEquals(expectedSong.getAlbumCoverUrl(), actualSong.getAlbumCoverUrl());
    }
}
}

}
