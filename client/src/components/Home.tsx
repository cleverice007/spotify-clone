import React, { useState, useEffect } from 'react';
import { Album, Song } from '../interfaces/MusicModels';
import { getAllAlbums, getSongsInAlbum, getPresignedUrl } from '../services/musicService';
import AlbumGallery from './AlbumGallery';
import PlaybackBar from './PlaybackBar';

const Home: React.FC = () => {
  const [albums, setAlbums] = useState<Album[]>([]);
  const [currentSong, setCurrentSong] = useState<Song | null>(null);
  const [isPlaying, setIsPlaying] = useState<boolean>(false);
  const [presignedUrl, setPresignedUrl] = useState<string>("");

  useEffect(() => {
    const fetchAlbums = async () => {
      const fetchedAlbums = await getAllAlbums();
      setAlbums(fetchedAlbums);
    };

    fetchAlbums();
  }, []);

  useEffect(() => {
    const fetchPresignedUrl = async () => {
        try {
            const url = await getPresignedUrl("AlbumSample", "SongSample");
            setPresignedUrl(url);
        } catch (error) {
            console.error("Error fetching presigned URL on /home: ", error);
        }
    };

    fetchPresignedUrl();
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

  const handleUploadClick = async () => {
   console.log("Uploading song to album: AlbumSample, song: SongSample");
  }

  const togglePlayPause = () => {
    setIsPlaying(!isPlaying);
  };

  return (
    <div>
      <AlbumGallery albums={albums} onAlbumClick={handleAlbumClick} onUploadClick={handleUploadClick} />
      <PlaybackBar 
        currentSong={currentSong} 
        isPlaying={isPlaying} 
        onPlayPause={togglePlayPause} 
      />
    </div>
  );
}

export default Home;
