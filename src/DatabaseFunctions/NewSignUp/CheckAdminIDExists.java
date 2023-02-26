package DatabaseFunctions.NewSignUp;

import java.sql.*;

public class CheckAdminIDExists
{
    /*
       @function - This function will check if the admin ID is already in the database, and return true if it is taken and false if it is not
       @param adminID - The admin ID that the user entered
       @return - True if the admin ID is already in the database, false if it is not
       @ throws SQLException - If there is an error with the database connection
       @author - Yogesh S
     */
  public static boolean checkAdminID(String adminID)
  {
    try
    {
    // Create a connection to the database
      Connection conn = DriverManager.getConnection(
              "jdbc:sqlserver://thinkwaveappln.database.windows.net:1433;database=orcl;user=thinkwave@thinkwaveappln;password=Mepcocollege1@;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;");

      // Create a statement to execute the query
    Statement statement = conn.createStatement();
    // Create a result set to hold the results of the query
    ResultSet rs = statement.executeQuery("SELECT USER_ID FROM THINKWAVE.USER_AUTHENTICATION WHERE ROLE = 'Admin' AND USER_ID = '" + adminID + "'");
    // If rs.next() returns true, then the admin ID is already in the database
    if(rs.next())
    {
      // Close the connection to the database
      conn.close();
      // Return true
      return true;
    }
    // Close the connection to the database
    conn.close();
    }
    catch(SQLException e)
    {
      e.printStackTrace();
    }
    // Return false
    return false;
  }
}