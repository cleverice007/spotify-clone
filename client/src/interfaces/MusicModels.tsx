export interface Song {
  id: string;
  title: string;
  artist: string;
  albumId: string;
  duration: number;
  filePath: string;
}

export interface Album {
  id: string;
  title: string;
  coverUrl: string;
  songUrls: string[];  // 歌曲的URL列表
}