package DatabaseFunctions;

import java.sql.*;

public class RetrieveSecuredPass {
    public static String[] retrievePass(String userId, String institutionId) {
        String passhash  = null;
        String passsalt = null;

        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:sqlserver://thinkwaveappln.database.windows.net:1433;database=orcl;user=thinkwave@thinkwaveappln;password=Mepcocollege1@;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;");

            PreparedStatement st = connection.prepareStatement("SELECT PASSW_HASH, PASSW_SALT\n" +
                    "FROM THINKWAVE.USER_AUTHENTICATION ua\n" +
                    "JOIN THINKWAVE.USER_DETAILS ud ON ua.USER_ID = ud.USER_ID\n" +
                    "WHERE ud.USER_ID = ? AND ud.INSTID = ?");
            st.setString(1, userId);
            st.setString(2, institutionId);

            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                passhash = rs.getString("PASSW_HASH");
                passsalt = rs.getString("PASSW_SALT");
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        String[] passArray = {passhash, passsalt};
        return passArray;
    }
}
