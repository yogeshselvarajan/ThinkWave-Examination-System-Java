package FetchFromDatabase;

import java.sql.*;

public class CheckEmailExists {
    static String userID = null;
    // Function to check if the email exists in the oracle database
       public static boolean checkEmailExists(String email) {
              boolean emailExists = false;
              try {
                Connection connection = (Connection) DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl",
                          "c##thinkwave", "orcl");
                PreparedStatement st = (PreparedStatement) connection
                          .prepareStatement("Select EMAIL,ID from C##THINKWAVE.USER_TABLE where EMAIL=?");
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
