package controller;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import model.Main;

/**
 * Created by truesik on 26.07.2015.
 */
public class DBHelper {
    private static DBHelper instance;
    private static Connection conn;

    public static DBHelper getInstance() {
        if (instance == null) {
            instance = new DBHelper();
        }
        return instance;
    }

    private DBHelper() {
        try {
            creation("create.sql");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void creation(String file) {
        try {
            Statement stmt = getConn().createStatement();
            String sql = readResource(DBHelper.class, file);
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String readResource(Class cpHolder, String path) throws Exception {
        URL url = cpHolder.getResource(path);
        Path resPath = Paths.get(url.toURI());
        return new String(Files.readAllBytes(resPath), "UTF8");
    }

    public Connection getConn() {
        if (conn == null) {
            try {
                Class.forName("org.sqlite.JDBC");
                String dbUrl = "jdbc:sqlite:database.db";
                conn = DriverManager.getConnection(dbUrl);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return conn;
    }

    public static void closeConn() {
        closeRes(conn);
        conn = null;
    }

    public static void closeRes(AutoCloseable res) {
        try {
            if (res != null) {
                res.close();
                res = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
