package FetchFromDatabase;

import org.jetbrains.annotations.Nullable;
import java.sql.*;

public class CheckConnection
{
    // Function to check the connection to the database
    public static @Nullable Connection getConnection() {
        try
        {
            String driver = "oracle.jdbc.driver.OracleDriver";
            String url = "jdbc:oracle:thin:@localhost:1521:orcl";
            String username = "c##thinkwave";
            String password = "orcl";
            Class.forName(driver);
            return DriverManager.getConnection(url, username, password);
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        return null;
    }

    public static void main(String[] args) {
        // call the checkConnection function
        try {
            Connection connection = getConnection();
            System.out.println("Connected to the database");
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
