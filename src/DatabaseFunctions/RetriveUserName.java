package DatabaseFunctions;

import java.sql.*;

public class RetriveUserName
{
    public static String getUserName(String userID) {
        String userName = null;
        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:sqlserver://thinkwaveappln.database.windows.net:1433;database=orcl;user=thinkwave@thinkwaveappln;password=Mepcocollege1@;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;");

            PreparedStatement st = (PreparedStatement) connection
                    .prepareStatement("Select USER_ID,NAME from THINKWAVE.USER_DETAILS where USER_ID = ?");
            st.setString(1, userID);

            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                userName = rs.getString(2);
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return userName;
    }
}
