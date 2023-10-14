import React, { useState, useEffect } from 'react';
import { Album, Song } from '../interfaces/MusicModels';
import { getAllAlbums, getSongsInAlbum } from '../services/musicService';
import AlbumGallery from './AlbumGallery';
import PlaybackBar from './PlaybackBar';

const Home: React.FC = () => {
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

  const handleAlbumClick = async (albumId: string) => {
    const songs = await getSongsInAlbum(albumId);
    if (songs.length > 0) { 
      setCurrentSong(songs[0]);
      setIsPlaying(true);
    }
  };

  const handleSongChange = (song: Song) => {
    setCurrentSong(song);
    setIsPlaying(true);
  };

  const togglePlayPause = () => {
    setIsPlaying(!isPlaying);
  };

  return (
    <div>
      <AlbumGallery albums={albums} onAlbumClick={handleAlbumClick} />
      <PlaybackBar 
        currentSong={currentSong} 
        isPlaying={isPlaying} 
        onPlayPause={togglePlayPause} 
      />
    </div>
  );
}

export default Home;
