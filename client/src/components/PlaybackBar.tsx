import React, { useEffect, useRef } from 'react';
import { Song,Album } from '../interfaces/MusicModels';

interface PlaybackBarProps {
  currentSong: Song | null;
  isPlaying: boolean;
  onPlayPause: () => void;  // 控制播放和暫停的callback
}

const PlaybackBar: React.FC<PlaybackBarProps> = ({ currentSong, isPlaying, onPlayPause }) => {
    const audioRef = useRef<HTMLAudioElement | null>(null);
  
    useEffect(() => {
      if (audioRef.current) {
        if (isPlaying) {
          audioRef.current.play();
        } else {
          audioRef.current.pause();
        }
      }
    }, [isPlaying]);
  
    return (
      <div className="playback-bar">
        {currentSong ? (
          <>
            <div className="song-info">
              <img src={currentSong.albumCoverUrl} alt={currentSong.title} />
              <span>{currentSong.title}</span>
            </div>
            <button 
              onClick={onPlayPause} 
              className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded m-2"
            >
              {isPlaying ? 'Pause' : 'Play'}
            </button>
            <audio ref={audioRef} src={currentSong.filePath} />
          </>
        ) : (
          <div>No song selected</div>
        )}
      </div>
    );
  }
  

export default PlaybackBar;
