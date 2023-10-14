import React from 'react';
import { Album, Song } from '../interfaces/MusicModels';

interface Props {
  albums: Album[];
  onAlbumClick: (albumId: string) => void;
}

const AlbumGallery: React.FC<Props> = ({ albums, onAlbumClick }) => {
  return (
    <div className="flex flex-wrap gap-4">
      {albums.map(album => (
        <div 
          key={album.id} 
          className="w-40 text-center" 
          onClick={() => onAlbumClick(album.id)}
        >
          <img src={album.coverUrl} alt={album.title} className="w-full rounded-md" />
          <div>{album.title}</div>
        </div>
      ))}
    </div>
  );
};

export default AlbumGallery;
