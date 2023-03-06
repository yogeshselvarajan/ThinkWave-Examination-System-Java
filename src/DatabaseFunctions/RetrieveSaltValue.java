package DatabaseFunctions;

import java.sql.*;

public class RetrieveSaltValue
{
    public static String retrieveSalt(int userId) {
        String passsalt = null;
        Connection con = null;
        String SQLQuery = "Select PASSW_SALT from THINKWAVE.USER_AUTHENTICATION WHERE USER_ID='" + userId + "'";
        try {
            con = ConnectionDB.connect();
            PreparedStatement ps = con.prepareStatement(SQLQuery);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                passsalt = rs.getString(1);
            }
            ps.close();
            con.close();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return passsalt;
    }
}
