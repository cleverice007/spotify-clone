package com.spotifyclone.demospotifyclone.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "albums")
public class Album {
    @Id
    private String id;
    private String title;
    @Column(name = "cover_url")
    private String coverUrl;

    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Song> songs = new ArrayList<>(); // 使用ArrayList

    // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    // 添加歌曲到專輯
    public void addSong(Song song) {
        this.songs.add(song);
        song.setAlbum(this);
    }
}
