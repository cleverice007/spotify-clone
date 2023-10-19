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
    // ...
}
