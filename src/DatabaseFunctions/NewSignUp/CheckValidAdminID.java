package DatabaseFunctions.NewSignUp;

import java.sql.*;

public class CheckValidAdminID
{
    /*
       @function - This function will check if the admin ID is already in the database, and return true if it is taken and false if it is not
       @param adminID - The admin ID that the user entered
       @return - True if the admin ID is already in the database, false if it is not
       @ throws SQLException - If there is an error with the database connection
       @author - Yogesh S
     */
  public static boolean checkValidAdminID(String adminID) throws SQLException
  {
    // Create a connection to the database
    Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "c##thinkwave", "orcl");
    // Create a statement to execute the query
    Statement statement = conn.createStatement();
    // Create a result set to hold the results of the query
    ResultSet rs = statement.executeQuery("SELECT ID FROM USER_TABLE WHERE ID = '" + adminID + "'");
    // If the result set is empty, then the admin ID is not taken
    if (!rs.next()) {
      return false;
    } else {
      return true;
    }
  }
}