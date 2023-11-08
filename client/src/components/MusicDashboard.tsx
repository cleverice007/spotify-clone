/*import React, { useState } from 'react';
import UploadForm from './UploadForm';
import AlbumGallery from '../components/AlbumGallery';
import PlaybackBar from '../components/PlaybackBar';

const MusicDashboard: React.FC = () => {
    const [selectedAlbumId, setSelectedAlbumId] = useState<string | null>(null);
    const [isUploadModalOpen, setIsUploadModalOpen] = useState<boolean>(false);

    const handleAlbumClick = (albumId: string) => {
        setSelectedAlbumId(albumId);
    };

    const handleUploadClick = () => {
        setIsUploadModalOpen(true);
    };

    // ... 其他邏輯 ...

    return (
        <div>
            {isUploadModalOpen && <UploadForm />}
            <AlbumGallery onAlbumClick={handleAlbumClick} />
            {selectedAlbumId && <PlaybackBar />}
        </div>
    );
};

export default MusicDashboard;
*/