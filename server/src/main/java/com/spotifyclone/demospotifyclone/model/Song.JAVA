package com.spotifyclone.demospotifyclone.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;


@Entity
@Table(name = "songs")
public class Song {
    @Id
    private String id;
    private String title;
    private String artist;
    private Integer duration;
    @Column(name = "file_path")
    private String filePath;
    @Column(name = "album_cover_url")
    private String albumCoverUrl;

    @ManyToOne
    @JoinColumn(name = "album_id", referencedColumnName = "id")
    private Album album;

    // Getters and Setters
    // ...
}