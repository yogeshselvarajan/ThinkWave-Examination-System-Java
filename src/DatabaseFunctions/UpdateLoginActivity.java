package DatabaseFunctions;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class UpdateLoginActivity {

    // function to update the login activity of the user in the database
    public static void updateLoginActivity(String userId) {
        try {
            Connection connection = (Connection) DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl",
                    "c##thinkwave", "orcl");
            PreparedStatement st = (PreparedStatement) connection
                    .prepareStatement("UPDATE C##THINKWAVE.USER_TABLE SET LAST_ACTIVITY=SYSDATE WHERE ID=?");
            st.setString(1, userId);
            ResultSet rs = st.executeQuery();
            rs.close();
            st.close();
            connection.setAutoCommit(false);
            connection.commit();
            connection.close();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }
}
