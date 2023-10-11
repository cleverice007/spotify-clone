# Spotify-Clone 專案

## **簡介**

這是一個全棧的音樂串流應用。前端使用 TypeScript 和 React，後端則使用 Java 和 Spring 框架。本專案利用 AWS Lambda 來處理音樂的上傳和串流，並使用 Docker 進行容器化。

## **功能介紹**

### 音樂串流
- 使用 AWS Lambda 來處理音樂的上傳和串流。
  - **為什麼選擇 Lambda**：Lambda 能即時調整資源，滿足不同量級的使用者需求。

### 歌單管理
- 提供創建、編輯和刪除歌單的功能。
  - **使用的技術**：TypeScript 的 Interface 和 React
  - **為什麼選擇這些技術**：TypeScript 的 Interface 提供更好的型別檢查，使開發更加穩健。

### 使用者評價和評論
- 使用者可以對歌曲和歌單進行評價和評論。
  - **使用的技術**：TypeScript 的 Interface 和 React

### 實時通知
- 使用 WebSocket 實現實時通知。
  - **為什麼選擇 WebSocket**：提供更流暢、即時的使用者體驗。

## **技術棧**

### 前端
- **TypeScript 的 Interface**
  - **優點**：提供靜態型別，讓代碼更易於維護和除錯。

### 後端
- **Java 和 Spring 框架**
  - **優點**：Java 是一種穩健、高性能的後端語言，特別適用於大型應用。

### 雲服務
- **AWS Lambda**
  - **優點**：無需預先分配資源，可以即時擴展。

### 容器化
- **Docker**
  - **優點**：確保在不同環境下的一致性。

## **部署步驟**

### 使用 Docker
1. 建立 Dockerfile。
2. 執行 `docker build -t my-streaming-app .`。
3. 執行 `docker run -p 8080:8080 my-streaming-app`。

# 開發流程

## 環境設定
- **為什麼**: 確保所有開發者都在相同的開發環境下工作，這對於後續的開發和除錯都很有幫助。
- **工具**: Docker 可以在這個階段被利用，以確保環境一致性。

## 基本後端架構
## 資料庫模型設計 （使用aws rds 的postgreSQL）

### 用戶表（Users）

- `id`: 唯一標識符，用於識別每個用戶。
- `username`: 用戶名。
- `email`: 電子郵件地址。
- `password_hash`: 經過哈希處理的密碼。
- `created_at`: 註冊日期。
- `last_login`: 最後一次登入時間。

### 歌單表（Playlists）

- `id`: 唯一標識符，用於識別每個歌單。
- `name`: 歌單名稱。
- `user_id`: 用戶ID，表示哪個用戶創建了這個歌單。
- `created_at`: 創建日期。

### 歌曲表（Songs）

- `id`: 唯一標識符，用於識別每首歌曲。
- `title`: 歌曲名稱。
- `artist`: 演唱者或樂團。
- `duration`: 持續時間。
- `file_path`: 存儲音樂文件的路徑。

### 歌單和歌曲的多對多關係表（Playlist_Songs）

- `playlist_id`: 歌單ID。
- `song_id`: 歌曲ID

## API 端點

### 認證

- `POST /auth/login`: 用戶登入
- `POST /auth/signup`: 用戶註冊

### 歌單管理

- `POST /playlist/create`: 創建新的歌單
- `PUT /playlist/update`: 更新現有歌單
- `GET /playlist/:id`: 獲取指定歌單
- `DELETE /playlist/:id`: 刪除指定歌單

### 歌曲管理

- `POST /song/upload`: 上傳新歌曲
- `PUT /song/update/:id`: 更新現有歌曲
- `GET /song/:id`: 獲取指定歌曲
- `DELETE /song/:id`: 刪除指定歌曲




## 基本前端架構
- **為什麼**: 一旦有了基本的後端，前端可以開始建構基礎頁面和元件。
- **工具**: TypeScript 和 React。

## 歌單管理功能
- **為什麼**: 歌單是音樂串流應用的核心功能之一，相對獨立，適合先開發。
- **工具**: TypeScript、React、Java 和 Spring Boot。

## 音樂串流功能
- **為什麼**: 這個功能相對複雜，可能會涉及到文件存儲、流媒體處理等。
- **工具**: AWS Lambda、Java。

## 使用者評價和評論
- **為什麼**: 這些社交功能通常依賴於前面的歌單和音樂串流功能。
- **工具**: TypeScript、React。

## 實時通知
- **為什麼**: 這是一個進階功能，通常會在基本功能穩定後才加入。
- **工具**: WebSocket。

## 部署和優化
- **為什麼**: 在所有基本功能都開發完成之後，就可以考慮如何將應用部署到生產環境。
- **工具**: AWS EC2、AWS Lambda。


