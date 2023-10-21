import React from 'react';
import { Album, Song } from '../interfaces/MusicModels';


interface Props {
  albums: Album[];
  onAlbumClick: (albumId: string) => void;
  onUploadClick: () => void;
}

const AlbumGallery: React.FC<Props> = ({ albums, onAlbumClick, onUploadClick }) => {
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
      {/* 新增的空白 card 用於開啟上傳表單 */}
      <div onClick={onUploadClick} className="w-40 text-center">
        <div className="w-full rounded-md bg-gray-300" style={{ height: '180px' }}>
          <div className="flex items-center justify-center h-full">
            <span className="text-2xl font-bold text-white">+</span>
          </div>
        </div>
        <div>Upload New Album</div>
      </div>
    </div>
  );
};

export default AlbumGallery;
