package com.spotifyclone.demospotifyclone.service;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.springframework.web.multipart.MultipartFile;
import com.spotifyclone.demospotifyclone.model.Album;

import java.util.Map;
import java.util.List;

public class LambdaHandlerService implements RequestHandler<Map<String, Object>, String> {

    private MusicService musicService = new MusicService();

    @Override
    public String handleRequest(Map<String, Object> input, Context context) {
        // 根據input來判斷應該呼叫哪個方法
        String action = (String) input.get("action");
        if ("uploadSong".equals(action)) {
            // 從input中提取所需的參數，然後調用handleUploadSong方法
            // 注意：使用AWS Lambda和API Gateway時，MultipartFile可能不適用，而是直接使用byte[]或base64編碼的字符串
            // 此處僅作為示例
            MultipartFile audioFile = (MultipartFile) input.get("audioFile");
            MultipartFile coverFile = (MultipartFile) input.get("coverFile");
            String albumTitle = (String) input.get("albumTitle");
            String songTitle = (String) input.get("songTitle");
            return handleUploadSong(audioFile, coverFile, albumTitle, songTitle);
        } else if ("getAllAlbums".equals(action)) {
            List<Album> albums = handleGetAllAlbums();
            // 將結果轉換為JSON字符串或其他適當的格式返回
            // 此處僅作為示例
            return albums.toString();
        } else {
            return "Unsupported action";
        }
    }

    public String handleUploadSong(MultipartFile audioFile, MultipartFile coverFile, String albumTitle, String songTitle) {
        return musicService.uploadFileToS3(audioFile, coverFile, albumTitle, songTitle);
    }

    public List<Album> handleGetAllAlbums() {
        return musicService.getAllAlbums();
    }
}
