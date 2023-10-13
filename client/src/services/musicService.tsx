
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
