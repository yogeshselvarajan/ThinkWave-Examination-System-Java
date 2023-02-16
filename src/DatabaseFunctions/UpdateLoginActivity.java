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
            Connection connection = DriverManager.getConnection(
                    "jdbc:sqlserver://thinkwaveappln.database.windows.net:1433;database=orcl;user=thinkwave@thinkwaveappln;password=Mepcocollege1@;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;");

            PreparedStatement st = (PreparedStatement) connection
                    .prepareStatement("UPDATE THINKWAVE.USER_TABLE SET LAST_LOGIN = SYSDATETIME() WHERE USER_ID = ?");
            st.setString(1, userId);
            int rs = st.executeUpdate();
            st.close();
            connection.setAutoCommit(false);
            connection.commit();
            connection.close();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }
}
