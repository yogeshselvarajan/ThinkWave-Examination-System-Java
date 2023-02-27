package DatabaseFunctions;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionDB
{
    public static Properties readDbConfig() {
        Properties properties = new Properties();
        try (FileInputStream fileInputStream = new FileInputStream("db.properties")) {
            properties.load(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    public static Connection connect() throws SQLException {
        Properties dbConfig = readDbConfig();
        Connection connection = null;
        String url = dbConfig.getProperty("url");
        String driver = dbConfig.getProperty("driver");

        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
