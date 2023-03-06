package DatabaseFunctions;

import java.sql.*;

public class RetriveUserName
{
    public static String getUserName(int userID) {
        String userName = null;
        String query = "Select USER_ID,NAME from THINKWAVE.USER_DETAILS where USER_ID = ?";
        Connection con = null;
        try {
            con = ConnectionDB.connect();
            PreparedStatement st = con.prepareStatement(query);
            st.setInt(1, userID);
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
