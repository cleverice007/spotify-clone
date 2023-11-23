package com.spotifyclone.demospotifyclone.dao;

import com.spotifyclone.demospotifyclone.model.Album;
import java.util.List;
import java.util.Optional;

public interface AlbumDao {
    List<Album> findAll();
    Optional<Album> findById(String id);
    void save(Album album);
    Optional<Album> findByTitle(String title); 
}
