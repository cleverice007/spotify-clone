package com.spotifyclone.demospotifyclone.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.spotifyclone.demospotifyclone.model.Album;
import java.util.Optional;


@Repository
public interface AlbumRepository extends JpaRepository<Album, String> {
        // 根據標題查找專輯
        Optional<Album> findByTitle(String title);
}
