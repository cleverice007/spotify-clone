import { createFFmpeg } from "@ffmpeg/ffmpeg";

const ffmpeg = createFFmpeg({ log: true });

export const getDuration = async (file) => {
  await ffmpeg.load();
  
  // 將檔案轉為Uint8Array格式
  const data = new Uint8Array(await file.arrayBuffer());

  // 寫入檔案到ffmpeg
  ffmpeg.FS("writeFile", "temp.mp4", data);

  // 獲取時長
  const result = await ffmpeg.run("-i", "temp.mp4");
  // 從result中解析時長（這裡可能需要你根據實際輸出結果來進行處理）

  return duration; // 返回時長
};
