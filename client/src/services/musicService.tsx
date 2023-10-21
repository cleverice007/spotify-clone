
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
  const formData = new FormData();

  formData.append("audioFile", songData.filePath);
  formData.append("coverFile", songData.albumCoverUrl);
  formData.append("albumTitle", songData.albumTitle);
  formData.append("songTitle", songData.title);
  formData.append("artist", songData.artist);

  try {
    const response = await axios.post(`${API_URL}/upload`, formData);
    return response.data;
  } catch (error) {
    console.error("Error uploading song:", error);
    throw error;
  }
};
