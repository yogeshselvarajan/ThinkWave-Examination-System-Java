package FetchFromDatabase;

import java.sql.*;

public class RetriveUserName
{
    public String getUserName(String userID) {
        String userName = null;
        try {
            Connection connection = (Connection) DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl",
                    "system", "orcl");
            PreparedStatement st = (PreparedStatement) connection
                    .prepareStatement("Select ID, NAME from C##THINKWAVE.USER_TABLE where ID=? ");
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
