// components/AlbumGallery.tsx
import React, { useState, useEffect } from 'react';
import { Album } from '../interfaces/MusicModels';
import { getSongsInAlbum } from '../services/musicService'; // 從 musicService 引入

const AlbumGallery: React.FC = () => {
  const [albums, setAlbums] = useState<Album[]>([]);

  useEffect(() => {
    // TODO: 根據你的需求，這裡需要從 API 獲取 albums 的資訊
  }, []);

  const handleAlbumClick = async (albumId: string) => {
    const songs = await getSongsInAlbum(albumId);
    // TODO: 根據取得的歌曲播放音樂
  };

  return (
    <div className="flex flex-wrap gap-4">
      {albums.map(album => (
        <div key={album.id} className="w-40 text-center" onClick={() => handleAlbumClick(album.id)}>
          <img src={album.imageUrl} alt={album.title} className="w-full rounded-md" />
          <div>{album.title}</div>
        </div>
      ))}
    </div>
  );
};

export default AlbumGallery;
