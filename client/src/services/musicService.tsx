
import axios from 'axios';
import { Song, Album } from '../interfaces/MusicModels';

const API_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api';

// 獲得專輯裡的歌曲
export const getSongsInAlbum = async (albumId: string): Promise<Song[]> => {
  try {
    const response = await axios.get(`${API_URL}/albums/${albumId}/songs`);
    return response.data;
  } catch (error) {
    console.error(`There was an error fetching the songs for album ${albumId}:`, error);
    return [];
  }
};


// 獲得所有的專輯
export const getAllAlbums = async (): Promise<Album[]> => {
  try {
    const response = await axios.get(`${API_URL}/albums`);
    return response.data;
  } catch (error) {
    console.error("There was an error fetching the albums:", error);
    return [];
  }
};

export const uploadSong = async (songData: {
  title: string,
  artist: string,
  filePath: File,
  albumTitle: string,
  albumCoverUrl: File
}) => {

  // 1. 獲取S3的預簽名URL
  let presignedUrls;
  try {
    presignedUrls = await getPresignedUrl(songData.albumTitle, songData.title);
  } catch (error) {
    console.error("Error getting presigned URLs:", error);
    throw error;
  }

  // 2. 上傳到S3
  await axios.put(presignedUrls.songPresignedUrl, songData.filePath);
  await axios.put(presignedUrls.coverPresignedUrl, songData.albumCoverUrl);

  // 3. 獲得S3的URL或路徑 
  const songS3Path = `https://spotify-clone-mason.s3.amazonaws.com/${songData.albumTitle}/songs/${songData.title}.mp3`;
  const coverS3Path = `https://spotify-clone-mason.s3.amazonaws.com/${songData.albumTitle}/cover.jpg`;

  // 4. 發送到後端
  try {
    const response = await axios.post(`${API_URL}/upload`, {
      albumTitle: songData.albumTitle,
      songTitle: songData.title,
      artist: songData.artist,
      filePath: songS3Path,
      albumCoverUrl: coverS3Path
    });
    return response.data;
  } catch (error) {
    console.error("Error uploading song details to backend:", error);
    throw error;
  }
};


export const getPresignedUrl = async (albumTitle: string, title: string) => {
  const endpoint = '<YOUR_LAMBDA_ENDPOINT>'; // 替換成您的Lambda endpoint
  const data = {
      albumTitle: albumTitle,
      title: title,
  };

  try {
      const response = await axios.post(endpoint, data);
      return response.data; // 預先簽名的URL應該在response的data中
  } catch (error) {
      console.error("Error fetching presigned URL: ", error);
      throw error;
  }
};