# spotify-clone


## **簡介**

這是一個全棧的串流應用。前端使用TypeScript和React，後端則採用Java。這個專案利用AWS Lambda來處理音樂串流，並使用Docker和Kubernetes進行容器化和部署。

## **功能**

### 音樂播放
- 使用AWS Lambda來處理音樂的上傳和串流。
  - **為什麼使用Lambda**：Lambda能即時調整資源，適應不同量級的使用者需求。
  
### 歌單管理
- 可以創建、編輯和刪除個人歌單。
  - **使用哪些工具**：React和TypeScript
  - **為什麼**：TypeScript提供更好的型別檢查，使開發更加穩健。

### 使用者評價和評論
- 使用者可以對歌曲和歌單進行評價和評論。
  - **使用哪些工具**：React和TypeScript
  
### 實時通知
- 使用WebSocket來實現實時通知。
  - **為什麼使用WebSocket**：提供更流暢、即時的用戶體驗。

## **技術棧**

### 前端
- **TypeScript**
  - **為什麼**：提供靜態型別，讓代碼更易於維護和偵錯。
- **React**
  - **為什麼**：React是廣泛使用的前端庫，有豐富的社區和文檔。

### 後端
- **Java**
  - **為什麼**：Java是一種穩健、高性能的後端語言，特別適合大規模應用。

### 雲端服務
- **AWS Lambda**
  - **為什麼**：無需預先分配資源，能即時擴展。

### 容器化和部署
- **Docker**
  - **為什麼**：Docker讓你能在不同的環境下確保一致性。
- **Kubernetes**
  - **為什麼**：在多節點的情況下，Kubernetes能自動處理負載均衡和服務發現。

## **部署**

### Docker
1. 建立Dockerfile
2. 執行 `docker build -t my-streaming-app .`
3. 執行 `docker run -p 8080:8080 my-streaming-app`

### Kubernetes
1. 建立Kubernetes配置文件
2. 執行 `kubectl apply -f my-streaming-app.yaml`
