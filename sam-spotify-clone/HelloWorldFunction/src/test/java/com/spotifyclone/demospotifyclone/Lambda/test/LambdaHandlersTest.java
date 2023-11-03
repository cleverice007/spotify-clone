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
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

class LambdaHandlersTest {

    private final GetPresignedUrlsLambdaHandler getUrlsHandler = new GetPresignedUrlsLambdaHandler();
    private final SaveSongToDbLambdaHandler saveSongHandler = new SaveSongToDbLambdaHandler();
    private final GetAllAlbumsLambdaHandler getAllAlbumsHandler = new GetAllAlbumsLambdaHandler();


    @Mock
    private AlbumRepository albumRepository;

    private final Context context = mock(Context.class);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        getAllAlbumsHandler.setAlbumRepository(albumRepository);
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
    // 在此添加其他檢查，例如，確保返回的JSON包含正確的專輯和歌曲列表等
}

}
