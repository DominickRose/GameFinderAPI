package com.ismadoro.utils;

import org.postgresql.util.PSQLException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUtil {

    public static Connection createConnection() {
        try {
            String host = System.getenv("DB_HOST");
            String port = System.getenv("DB_PORT");
            String database = System.getenv("DB_DATABASE");
            String username = System.getenv("DB_USERNAME");
            String password = System.getenv("DB_PASSWORD");
            String connectionUrl = host + ":" + port + "/" + database + "?user=" + username + "&password=" + password;
            Connection connection = DriverManager.getConnection(connectionUrl);
            return connection;
        } catch (SQLException sqlException) {
            //Just to have something to psuh
            return null;
        }

    }

    public static void main(String[] args) {
        System.out.println(createConnection());
    }
}
