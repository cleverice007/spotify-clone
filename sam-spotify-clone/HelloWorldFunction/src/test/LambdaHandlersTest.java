package com.spotifyclone.demospotifyclone.Lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LambdaHandlersTest {

    private final GetPresignedUrlsLambdaHandler getUrlsHandler = new LambdaHandlers.GetPresignedUrlsLambdaHandler();
    private final SaveSongToDbLambdaHandler saveSongHandler = new LambdaHandlers.SaveSongToDbLambdaHandler();
    private final GetAllAlbumsLambdaHandler getAllAlbumsHandler = new LambdaHandlers.GetAllAlbumsLambdaHandler();

    private final Context context = mock(Context.class);

    @BeforeEach
    void setUp() {
        // 在此初始化任何共同的模擬物件或測試前置條件
    }

    @Test
    void testGetPresignedUrls() {
        APIGatewayProxyRequestEvent requestEvent = new APIGatewayProxyRequestEvent();
        requestEvent.setBody("{ \"albumTitle\": \"TestAlbum\", \"title\": \"TestSong\" }");

        APIGatewayProxyResponseEvent responseEvent = getUrlsHandler.handleRequest(requestEvent, context);
        assertEquals(200, responseEvent.getStatusCode());
        // 其他斷言可以確認響應的正確性
    }

    @Test
    void testSaveSongToDb() {
        APIGatewayProxyRequestEvent requestEvent = new APIGatewayProxyRequestEvent();
        requestEvent.setBody("{ \"songId\": \"123\", \"title\": \"TestSong\", \"artist\": \"TestArtist\", \"filePath\": \"path/to/file\", \"albumCoverUrl\": \"path/to/cover\", \"albumTitle\": \"TestAlbum\" }");

        APIGatewayProxyResponseEvent responseEvent = saveSongHandler.handleRequest(requestEvent, context);
        assertEquals(200, responseEvent.getStatusCode());
        // 其他斷言可以確認響應的正確性
    }

    @Test
    void testGetAllAlbums() {
        APIGatewayProxyRequestEvent requestEvent = new APIGatewayProxyRequestEvent();
        // 為 getAllAlbums 函數提供必要的請求參數，如果有的話

        APIGatewayProxyResponseEvent responseEvent = getAllAlbumsHandler.handleRequest(requestEvent, context);
        assertEquals(200, responseEvent.getStatusCode());
        // 其他斷言可以確認響應的正確性
    }
}
