import React from 'react';
import { Song,Album } from '../interfaces/MusicModels';

interface PlaybackBarProps {
  currentSong: Song | null;
  isPlaying: boolean;
  onPlayPause: () => void;  // 控制播放和暫停的callback
}

const PlaybackBar: React.FC<PlaybackBarProps> = ({ currentSong, isPlaying, onPlayPause }) => {
  return (
    <div className="playback-bar">
      {currentSong ? (
        <>
          <div className="song-info">
            <img src={currentSong.coverUrl} alt={currentSong.title} />
            <span>{currentSong.title}</span>
          </div>
          <button onClick={onPlayPause}>
            {isPlaying ? 'Pause' : 'Play'}
          </button>
          {/* TODO: 你可以在這裡增加其他控制按鈕，如下一首、上一首、音量控制等 */}
        </>
      ) : (
        <div>No song selected</div>
      )}
    </div>
  );
}

export default PlaybackBar;
