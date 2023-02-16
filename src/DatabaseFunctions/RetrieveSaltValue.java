package DatabaseFunctions;

import java.sql.*;

public class RetrieveSaltValue
{
    public static String retrieveSalt(String userId) {
        String passsalt = null;
        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:sqlserver://thinkwaveappln.database.windows.net:1433;database=orcl;user=thinkwave@thinkwaveappln;password=Mepcocollege1@;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;");

            PreparedStatement st = (PreparedStatement) connection
                    .prepareStatement("Select USER_ID, PASSW_SALT from THINKWAVE.USER_TABLE where USER_ID=?");
            st.setString(1, userId);

            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                passsalt = rs.getString(2);
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return passsalt;
    }
}
