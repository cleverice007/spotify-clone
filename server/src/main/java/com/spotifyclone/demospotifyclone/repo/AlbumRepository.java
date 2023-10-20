package com.spotifyclone.demospotifyclone.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.spotifyclone.demospotifyclone.model.Album;

@Repository
public interface AlbumRepository extends JpaRepository<Album, String> {
}
