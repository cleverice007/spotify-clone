package com.spotifyclone.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import java.util.List;

@Entity
@Table(name = "albums")
public class Album {
    @Id
    private String id;
    private String title;
    @Column(name = "cover_url")
    private String coverUrl;
    @ElementCollection
    @Column(name = "song_urls")
    private List<String> songUrls;

    // Getters and Setters
    // ...
}
