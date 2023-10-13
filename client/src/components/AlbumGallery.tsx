import React, { useState, useEffect } from 'react';
import { Album } from '../interfaces/MusicModels';
import { getAllAlbums, getSongsInAlbum } from '../services/musicService'; 
const AlbumGallery: React.FC = () => {
  const [albums, setAlbums] = useState<Album[]>([]);

  useEffect(() => {
    const fetchAlbums = async () => {
      const fetchedAlbums = await getAllAlbums();
      setAlbums(fetchedAlbums);
    };

    fetchAlbums();
  }, []);
  const handleAlbumClick = async (albumId: string) => {
    const songs = await getSongsInAlbum(albumId);
    // TODO: 根據取得的歌曲播放音樂
  };

  return (
    <div className="flex flex-wrap gap-4">
      {albums.map(album => (
        <div key={album.id} className="w-40 text-center" onClick={() => handleAlbumClick(album.id)}>
          <img src={album.coverUrl} alt={album.title} className="w-full rounded-md" />
          <div>{album.title}</div>
        </div>
      ))}
    </div>
  );
};

export default AlbumGallery;
