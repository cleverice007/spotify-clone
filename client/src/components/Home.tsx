import React, { useState, useEffect } from 'react';
import { Song } from '../interfaces/MusicModels';
import { getAllAlbums } from '../services/musicService';
import AlbumGallery from './AlbumGallery';
import PlaybackBar from './PlaybackBar';

const Home: React.FC = () => {
  const [currentSong, setCurrentSong] = useState<Song | null>(null);
  const [isPlaying, setIsPlaying] = useState<boolean>(false);

  const handleSongChange = (song: Song) => {
    setCurrentSong(song);
    setIsPlaying(true);
  };

  const togglePlayPause = () => {
    setIsPlaying(!isPlaying);
  };

  return (
    <div>
      <AlbumGallery onSongChange={handleSongChange} />
      <PlaybackBar 
        currentSong={currentSong} 
        isPlaying={isPlaying} 
        onPlayPause={togglePlayPause} 
      />
    </div>
  );
}

export default Home;
