package DatabaseFunctions;

import java.sql.*;

public class RetrieveSaltValue
{
    public static String retrieveSalt(String userId) {
        String passsalt = null;
        try {
            Connection connection = (Connection) DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl",
                    "c##thinkwave", "orcl");
            PreparedStatement st = (PreparedStatement) connection
                    .prepareStatement("Select ID, PASSW_SALT from C##THINKWAVE.USER_TABLE where ID=?");
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
