package DatabaseFunctions;

import java.sql.*;

public class RetrieveSecuredPass {
    public static String[] retrievePass(String userId, String institutionId) {
        String passhash  = null;
        String passsalt = null;
 
        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:sqlserver://thinkwaveappln.database.windows.net:1433;database=orcl;user=thinkwave@thinkwaveappln;password=Mepcocollege1@;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;");

            PreparedStatement st = (PreparedStatement) connection
                    .prepareStatement("Select USER_ID, INSTID, PASSW_HASH,PASSW_SALT from THINKWAVE.USER_TABLE where USER_ID=? and INSTID=? ");
            st.setString(1, userId);
            st.setString(2, institutionId);

            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                passhash = rs.getString(3);
                passsalt = rs.getString(4);
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        String[] passArray = {passhash, passsalt};
        return passArray;
    }
}