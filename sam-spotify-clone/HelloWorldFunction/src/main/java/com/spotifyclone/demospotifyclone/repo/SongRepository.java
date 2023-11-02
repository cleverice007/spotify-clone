package com.spotifyclone.demospotifyclone.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.spotifyclone.demospotifyclone.model.Song;

public interface SongRepository extends JpaRepository<Song, String> {
}
