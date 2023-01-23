package FetchFromDatabase;


import java.sql.*;

public class RetrieveEmailID {

    // Function to check if the email exists in the oracle database
    public static String retrieveEmail(String userID) {
        String emailID = null;
        try {
            Connection connection = (Connection) DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl",
                    "c##thinkwave", "orcl");
            PreparedStatement st = (PreparedStatement) connection
                    .prepareStatement("Select ID,EMAIL from C##THINKWAVE.USER_TABLE where ID=?");
            st.setString(1, userID);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                emailID = rs.getString("EMAIL");
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return emailID;
    }
}
