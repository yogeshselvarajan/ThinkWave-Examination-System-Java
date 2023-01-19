package FetchFromDatabase;

import java.sql.*;

public class RetrieveUserID {
    public static String retrieveUserID(String email) {
        String userID = null;
        try {
            Connection connection = (Connection) DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl",
                    "c##thinkwave", "orcl");
            PreparedStatement st = (PreparedStatement) connection
                    .prepareStatement("Select ID from C##THINKWAVE.USER_TABLE where EMAIL=?");
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