package DatabaseFunctions;

import java.sql.*;

public class CheckEmailExists {
    static String userID = null;
    // Function to check if the email exists in the oracle database
       public static boolean checkEmailExists(String email) {
              boolean emailExists = false;
              try {
                  Connection connection = DriverManager.getConnection(
                          "jdbc:sqlserver://thinkwaveappln.database.windows.net:1433;database=orcl;user=thinkwave@thinkwaveappln;password=Mepcocollege1@;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;");

                  PreparedStatement st = (PreparedStatement) connection
                          .prepareStatement("Select EMAIL,USER_ID from C##THINKWAVE.USER_TABLE where EMAIL=?");
                st.setString(1, email);
                ResultSet rs = st.executeQuery();
                if (rs.next()) {
                     userID = rs.getString("ID");
                     emailExists = true;
                }
              } catch (SQLException sqlException) {
                sqlException.printStackTrace();
              }
           return emailExists;
       }
}
