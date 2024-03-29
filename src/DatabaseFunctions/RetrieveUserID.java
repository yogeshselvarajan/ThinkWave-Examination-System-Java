package DatabaseFunctions;

import java.sql.*;

public class RetrieveUserID {
    public static String retrieveUserID(String email) {
        String userID = null;
        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:sqlserver://thinkwaveappln.database.windows.net:1433;database=orcl;user=thinkwave@thinkwaveappln;password=Mepcocollege1@;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;");

            PreparedStatement st = (PreparedStatement) connection
                    .prepareStatement("Select USER_ID from THINKWAVE.USER_DETAILS where EMAIL=?");
            st.setString(1, email);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                userID = rs.getString("ID");
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return userID;
    }
}