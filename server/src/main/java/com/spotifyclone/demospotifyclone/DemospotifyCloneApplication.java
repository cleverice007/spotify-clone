package com.spotifyclone.demospotifyclone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@SpringBootApplication
public class DemospotifyCloneApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemospotifyCloneApplication.class, args);
    }

    @Component
    public class DatabaseConnectionChecker implements ApplicationListener<ContextRefreshedEvent> {

        private final DataSource dataSource;

        public DatabaseConnectionChecker(DataSource dataSource) {
            this.dataSource = dataSource;
        }

        @Override
        public void onApplicationEvent(ContextRefreshedEvent event) {
            try (Connection connection = dataSource.getConnection()) {
                System.out.println("Successfully connected to PostgreSQL database!");
            } catch (SQLException e) {
                System.out.println("Failed to connect to PostgreSQL database.");
                e.printStackTrace();
            }
        }
    }
}
