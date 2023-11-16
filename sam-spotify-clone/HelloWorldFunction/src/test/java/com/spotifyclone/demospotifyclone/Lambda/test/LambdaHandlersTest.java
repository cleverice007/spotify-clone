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
import com.spotifyclone.demospotifyclone.repo.AlbumRepository;
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
    private AlbumRepository albumRepository;  // Mock AlbumRepository 接口
    private Context context;

    @InjectMocks
    private MusicService musicService; // 自動注入 AlbumRepository

    private GetPresignedUrlsLambdaHandler getUrlsHandler;
    private SaveSongToDbLambdaHandler saveSongHandler;
    private GetAllAlbumsLambdaHandler getAllAlbumsHandler;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        context = mock(Context.class); // 初始化 context 變數
    // 由於 MusicService 是注入的，應該在此之前就初始化
    getUrlsHandler = new GetPresignedUrlsLambdaHandler(musicService);
    saveSongHandler = new SaveSongToDbLambdaHandler(musicService);
    getAllAlbumsHandler = new GetAllAlbumsLambdaHandler(musicService);    }

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
