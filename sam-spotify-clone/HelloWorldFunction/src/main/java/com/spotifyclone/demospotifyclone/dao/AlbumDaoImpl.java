package com.spotifyclone.demospotifyclone.dao;

import com.spotifyclone.demospotifyclone.model.Album;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AlbumDaoImpl implements AlbumDao {
    private Connection connection;

    public AlbumDaoImpl() {
        try {
            // 从环境变量获取数据库连接信息
            String dbInstanceName = System.getenv("DB_INSTANCE_NAME");
            String url = "jdbc:postgresql://" + dbInstanceName + ":5432/spotify_clone_mason_3";
            String username = System.getenv("DB_USERNAME");
            String password = System.getenv("DB_PASSWORD");

            this.connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Album> findAll() {
        System.out.println("AlbumDaoImpl: Executing findAll");
        List<Album> albums = new ArrayList<>();
        String sql = "SELECT * FROM albums";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                Album album = new Album();
                album.setId(resultSet.getString("id"));
                album.setTitle(resultSet.getString("title"));
                album.setCoverUrl(resultSet.getString("cover_url"));
                albums.add(album);
                System.out.println("AlbumDaoImpl: Found album - " + album);
            }
        } catch (SQLException e) {
            System.out.println("AlbumDaoImpl: SQLException - " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("AlbumDaoImpl: Returning albums - " + albums);
        return albums;
    }

    @Override
    public Optional<Album> findById(String id) {
        String sql = "SELECT * FROM albums WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Album album = new Album();
                album.setId(resultSet.getString("id"));
                album.setTitle(resultSet.getString("title"));
                album.setCoverUrl(resultSet.getString("cover_url"));
                return Optional.of(album);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public void save(Album album) {
        String sql = "INSERT INTO albums (id, title, cover_url) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, album.getId());
            preparedStatement.setString(2, album.getTitle());
            preparedStatement.setString(3, album.getCoverUrl());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<Album> findByTitle(String title) {
        String sql = "SELECT * FROM albums WHERE title = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, title);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Album album = new Album();
                album.setId(resultSet.getString("id"));
                album.setTitle(resultSet.getString("title"));
                album.setCoverUrl(resultSet.getString("cover_url"));
                return Optional.of(album);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
