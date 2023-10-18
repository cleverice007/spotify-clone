package com.spotifyclone.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;

@Entity
@Table(name = "songs")
public class Song {
    @Id
    private String id;
    private String title;
    private String artist;
    @Column(name = "album_id")
    private String albumId;
    private Integer duration;
    @Column(name = "file_path")
    private String filePath;
    @Column(name = "album_cover_url")
    private String albumCoverUrl;

    // Getters and Setters
    // ...
}
