package DatabaseFunctions;

import java.sql.*;

public class RetriveRole
{
    public static String getRole(String userID) {
        String role = null;
        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:sqlserver://thinkwaveappln.database.windows.net:1433;database=orcl;user=thinkwave@thinkwaveappln;password=Mepcocollege1@;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;");

            PreparedStatement st = (PreparedStatement) connection
                    .prepareStatement("Select USER_ID,ROLE from THINKWAVE.USER_AUTHENTICATION where USER_ID = '" + userID + "'");

            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                role = rs.getString(2);
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return role;
    }

    public static void main(String[] args) {
        System.out.println(getRole("1"));
    }
}
