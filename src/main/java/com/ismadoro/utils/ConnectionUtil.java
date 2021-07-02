package com.ismadoro.utils;

import org.postgresql.util.PSQLException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUtil {

    public static Connection createConnection() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:postgresql://proj1-db.cpwt7piygmnx.us-east-1.rds.amazonaws.com:5432/postgres?user=postgres&password=password");
            return connection;
        } catch (SQLException sqlException) {
            return null;
        }

    }

    public static void main(String[] args) {
        System.out.println(createConnection());
    }
}
