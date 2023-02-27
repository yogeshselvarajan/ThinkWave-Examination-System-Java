package DatabaseFunctions;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class UpdateLoginActivity {

    // function to update the login activity of the user in the database

    public static String updateLoginActivity(int userId) {
        Calendar now = Calendar.getInstance();
        String labeltime;
        final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("IST"));
        if (now.get(Calendar.AM_PM) == Calendar.AM)
            labeltime = dateFormat.format(now.getTime()) + " AM IST";
        else
            labeltime = dateFormat.format(now.getTime()) + " PM IST";
        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:sqlserver://thinkwaveappln.database.windows.net:1433;database=orcl;user=thinkwave@thinkwaveappln;password=Mepcocollege1@;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;");

            PreparedStatement st = (PreparedStatement) connection
                    .prepareStatement("UPDATE THINKWAVE.USER_AUTHENTICATION SET LAST_LOGIN = ? WHERE USER_ID = '" + userId + "'");
            st.setString(1, labeltime);
            int rs = st.executeUpdate();
            st.close();
            connection.setAutoCommit(false);
            connection.commit();
            connection.close();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return labeltime;
    }
}
