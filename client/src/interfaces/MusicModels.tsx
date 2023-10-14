export interface Song {
  id: string;
  title: string;
  artist: string;
  albumId: string;
  duration: number;
  filePath: string;
  albumCoverUrl: string;  // 專輯封面的URL
}

export interface Album {
  id: string;
  title: string;
  coverUrl: string;
  songUrls: string[];  // 歌曲的URL列表
}