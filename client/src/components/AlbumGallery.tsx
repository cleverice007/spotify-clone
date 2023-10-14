import React, { useState, useEffect } from 'react';
import { Album,Song } from '../interfaces/MusicModels';
import { getAllAlbums, getSongsInAlbum } from '../services/musicService'; 
import PlaybackBar from './PlaybackBar';

const AlbumGallery: React.FC = () => {
  const [albums, setAlbums] = useState<Album[]>([]);
  const [currentSong, setCurrentSong] = useState<Song | null>(null); 
  const [isPlaying, setIsPlaying] = useState<boolean>(false);
  

  useEffect(() => {
    const fetchAlbums = async () => {
      const fetchedAlbums = await getAllAlbums();
      setAlbums(fetchedAlbums);
    };

    fetchAlbums();
  }, []);
  const handleAlbumClick = async (albumId: string, albumCoverUrl: string) => {
    const songs = await getSongsInAlbum(albumId);
    if (songs.length > 0) { // 確保專輯裡有歌曲
        setCurrentSong(songs[0]);
        setIsPlaying(true); // 開始播放
    }
  };

  return (
    <div className="flex flex-wrap gap-4">
      {albums.map(album => (
        <div key={album.id} className="w-40 text-center" onClick={() => handleAlbumClick(album.id, album.coverUrl)}>
          <img src={album.coverUrl} alt={album.title} className="w-full rounded-md" />
          <div>{album.title}</div>
        </div>
      ))}
      <PlaybackBar currentSong={currentSong} isPlaying={isPlaying} onPlayPause={() => setIsPlaying(!isPlaying)} albums={albums} />
    </div>
  );
};

export default AlbumGallery;