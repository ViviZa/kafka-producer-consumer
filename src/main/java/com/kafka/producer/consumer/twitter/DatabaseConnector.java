package com.kafka.producer.consumer.twitter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class DatabaseConnector {

    private Logger logger = LoggerFactory.getLogger(DatabaseConnector.class);

    public void insert(String id_str, String created_at, String consumed_at, String consumed_through) {
        String sql = "INSERT INTO tweet(id_str, created_at, consumed_at, consumed_through) VALUES(?,?,?,?)";

        logger.info("prepare sql statement");
        try (Connection conn = this.connect();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, id_str);
            statement.setString(2, created_at);
            statement.setString(3, consumed_at);
            statement.setString(4, consumed_through);
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

    private Connection connect() {
        String url = "jdbc:sqlite:../../databases/twittertweets.db";
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return connection;
    }


}
