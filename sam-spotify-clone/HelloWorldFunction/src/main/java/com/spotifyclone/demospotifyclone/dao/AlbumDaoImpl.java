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
            // 设置数据库连接参数
            String url = "jdbc:postgresql://spotify-clone-mason-3.c1iiegeq6pn3.ap-southeast-1.rds.amazonaws.com:5432/spotify_clone_mason_3";
            String username = "mason";
            String password = "cleverice007";

            // 连接数据库
            this.connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Album> findAll() {
        List<Album> albums = new ArrayList<>();
        String sql = "SELECT * FROM albums";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                Album album = new Album();
                album.setId(resultSet.getString("id"));
                album.setTitle(resultSet.getString("title"));
                album.setCoverUrl(resultSet.getString("cover_url"));
                // ... 设置其他字段 ...
                albums.add(album);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
                // ... 设置其他字段 ...
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
            // ... 绑定其他字段 ...
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
                // ... 设置其他字段 ...
                return Optional.of(album);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
