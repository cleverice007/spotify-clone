package com.spotifyclone.demospotifyclone.Lambda.test;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.spotifyclone.demospotifyclone.Lambda.LambdaHandlers.GetAllAlbumsLambdaHandler;
import com.spotifyclone.demospotifyclone.Lambda.LambdaHandlers.GetPresignedUrlsLambdaHandler;
import com.spotifyclone.demospotifyclone.Lambda.LambdaHandlers.SaveSongToDbLambdaHandler;
import com.spotifyclone.demospotifyclone.model.Album;
import com.spotifyclone.demospotifyclone.model.Song;
import com.spotifyclone.demospotifyclone.service.MusicService;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.s3.S3Client;
import com.spotifyclone.demospotifyclone.dao.AlbumDao;
import com.spotifyclone.demospotifyclone.dao.AlbumDaoImpl;
import com.spotifyclone.demospotifyclone.model.Album;


import static org.junit.jupiter.api.Assertions.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.InjectMocks;


import java.util.Arrays;
import java.util.List;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.Map;
import com.google.gson.Gson;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.anyString;


class LambdaHandlersTest {

    @Mock
    private S3Client mockS3Client;

    @Mock
    private AlbumDao mockAlbumDao;

    @Mock
    private MusicService mockMusicService;

    @InjectMocks
    private GetPresignedUrlsLambdaHandler getUrlsHandler;

    @InjectMocks
    private SaveSongToDbLambdaHandler saveSongHandler;

    @InjectMocks
    private GetAllAlbumsLambdaHandler getAllAlbumsHandler;

    private Context context;

    @BeforeEach
void setUp() {
    MockitoAnnotations.initMocks(this);
    context = mock(Context.class);

    // 初始化模擬數據
    Album album1 = new Album();
    album1.setId("1");
    album1.setTitle("Album 1");
    album1.setCoverUrl("cover1.jpg");
    Song song1 = new Song("1", "Song1", "Artist1", 300, "path1", "cover1");
    Song song2 = new Song("2", "Song2", "Artist2", 320, "path2", "cover2");
    album1.setSongs(Arrays.asList(song1, song2));

    Album album2 = new Album();
    album2.setId("2");
    album2.setTitle("Album 2");
    album2.setCoverUrl("cover2.jpg");
    Song song3 = new Song("3", "Song3", "Artist3", 310, "path3", "cover3");
    Song song4 = new Song("4", "Song4", "Artist4", 330, "path4", "cover4");
    album2.setSongs(Arrays.asList(song3, song4));

    List<Album> mockAlbums = Arrays.asList(album1, album2);

    // 模擬 MusicService 和 AlbumDao 的行為
    when(mockMusicService.getPresignedUrlForSongUpload(anyString(), anyString())).thenReturn("mockUrl");
    when(mockMusicService.getPresignedUrlForCoverUpload(anyString())).thenReturn("mockCoverUrl");
    when(mockMusicService.uploadSongToDB(anyString(), anyString(), anyString(), anyString(), anyString(), anyString())).thenReturn("mockResponse");
    when(mockAlbumDao.findAll()).thenReturn(mockAlbums);

    // 初始化 lambda handler
    getUrlsHandler = new GetPresignedUrlsLambdaHandler(mockMusicService);
    saveSongHandler = new SaveSongToDbLambdaHandler(mockMusicService);
    getAllAlbumsHandler = new GetAllAlbumsLambdaHandler(mockMusicService);
}
    // 定義一個單元測試方法來測試獲取預簽名URL的功能
    @Test
    void testGetPresignedUrls() {
        
        // 創建APIGateway的請求事件並設定請求正文內容，這裡是用來模擬客戶端發送的數據
        APIGatewayProxyRequestEvent requestEvent = new APIGatewayProxyRequestEvent();
        requestEvent.setBody("{ \"albumTitle\": \"TestAlbum\", \"title\": \"TestSong\" }");
        
        // 使用Mockito框架創建一個對MusicService接口的模擬對象
        MusicService mockMusicService = mock(MusicService.class);
        // 配置當調用模擬對象的getPresignedUrlForSongUpload方法時返回一個指定的URL
        when(mockMusicService.getPresignedUrlForSongUpload("TestAlbum", "TestSong")).thenReturn("songUrl");
        // 配置當調用模擬對象的getPresignedUrlForCoverUpload方法時返回另一個指定的URL
        when(mockMusicService.getPresignedUrlForCoverUpload("TestAlbum")).thenReturn("coverUrl");
        
        // 創建GetUrlsHandler的實例，並傳入模擬的MusicService對象
        GetPresignedUrlsLambdaHandler getUrlsHandler = new GetPresignedUrlsLambdaHandler(mockMusicService);
    
        // 執行handleRequest方法進行測試，這應該會觸發模擬對象的相應行為
        APIGatewayProxyResponseEvent responseEvent = getUrlsHandler.handleRequest(requestEvent, context);
    
        // 斷言來檢查HTTP響應的狀態碼是否為200
        assertEquals(200, responseEvent.getStatusCode());
    
        // 使用Gson將響應正文從JSON字符串解析為Map對象
        Type responseType = new TypeToken<Map<String, String>>(){}.getType();
        Map<String, String> presignedUrls = new Gson().fromJson(responseEvent.getBody(), responseType);
    
        // 斷言來驗證返回的Map對象中預簽名URL是否與期望值一致
        assertEquals("songUrl", presignedUrls.get("songPresignedUrl"));
        assertEquals("coverUrl", presignedUrls.get("coverPresignedUrl"));
    
        // 使用Mockito的verify方法來檢查模擬的MusicService是否有調用指定的方法
        verify(mockMusicService).getPresignedUrlForSongUpload("TestAlbum", "TestSong");
        verify(mockMusicService).getPresignedUrlForCoverUpload("TestAlbum");
    }
    
    @Test
    public void testSaveSongToDb() {
        // 创建模拟的MusicService对象
        MusicService mockMusicService = mock(MusicService.class);
        // 配置模拟对象返回预期结果
        String expectedResponse = "Expected response";
        when(mockMusicService.uploadSongToDB(anyString(), anyString(), anyString(), anyString(), anyString(), anyString())).thenReturn(expectedResponse);

        // 创建请求事件
        APIGatewayProxyRequestEvent requestEvent = new APIGatewayProxyRequestEvent();
        requestEvent.setBody("{ \"songId\": \"123\", \"title\": \"TestSong\", \"artist\": \"TestArtist\", \"filePath\": \"path/to/file\", \"albumCoverUrl\": \"path/to/cover\", \"albumTitle\": \"TestAlbum\" }");

        SaveSongToDbLambdaHandler handler = new SaveSongToDbLambdaHandler(mockMusicService);

        // 调用handleRequest方法
        APIGatewayProxyResponseEvent responseEvent = handler.handleRequest(requestEvent, mock(Context.class));

        // 验证状态码
        assertEquals(200, responseEvent.getStatusCode());
        // 验证返回的正文
        assertEquals(expectedResponse, responseEvent.getBody());
    }


    @Test
    void testGetAllAlbums() {
        // 創建請求事件
        APIGatewayProxyRequestEvent requestEvent = new APIGatewayProxyRequestEvent();
        // 調用 Lambda 處理函數
        APIGatewayProxyResponseEvent responseEvent = getAllAlbumsHandler.handleRequest(requestEvent, context);
    
        // 驗證 HTTP 響應狀態碼
        assertEquals(200, responseEvent.getStatusCode());
    
        // 解析響應數據
        Type listType = new TypeToken<List<Album>>() {}.getType();
        List<Album> responseBodyAlbums = new Gson().fromJson(responseEvent.getBody(), listType);
    
        // 驗證專輯數量
        assertEquals(2, responseBodyAlbums.size());
    
        // 遍歷並驗證每個專輯的屬性
        for (int i = 0; i < responseBodyAlbums.size(); i++) {
            Album expectedAlbum = mockAlbums.get(i);
            Album actualAlbum = responseBodyAlbums.get(i);
    
            assertEquals(expectedAlbum.getId(), actualAlbum.getId());
            assertEquals(expectedAlbum.getTitle(), actualAlbum.getTitle());
            assertEquals(expectedAlbum.getCoverUrl(), actualAlbum.getCoverUrl());
    
            // 驗證歌曲數量
            assertEquals(expectedAlbum.getSongs().size(), actualAlbum.getSongs().size());
    
            // 遍歷並驗證每首歌曲的屬性
            for (int j = 0; j < expectedAlbum.getSongs().size(); j++) {
                Song expectedSong = expectedAlbum.getSongs().get(j);
                Song actualSong = actualAlbum.getSongs().get(j);
    
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
