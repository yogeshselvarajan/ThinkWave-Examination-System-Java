package DatabaseFunctions;


import java.sql.*;

public class CheckConnection
{
    // Function to check the connection to the database
    public static Connection getConnection() {
        try
        {
            String jdbcDriver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
            String dbUrl = "jdbc:sqlserver://thinkwaveappln.database.windows.net:1433;database=orcl;user=thinkwave@thinkwaveappln;password=Mepcocollege1@;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";

            Class.forName(jdbcDriver);
            return DriverManager.getConnection(dbUrl);
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
