package FetchFromDatabase;

import java.sql.*;

public class AdminLoginCheck {
    // Function that checks if the user credentials are correct by comparing user id, institution id and password with the database
    public static boolean checkAdminLogin(String adminId, String institutionId, String password) {
        try {
            Connection connection = (Connection) DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl",
                    "system", "orcl");
            PreparedStatement st = (PreparedStatement) connection
                    .prepareStatement("Select INSTID, INSTITUTION_ADMIN_ID, INSTITUTION_PASSWORD from C##THINKWAVE.INSTITUTION_TABLE where INSTID=? and INSTITUTION_ADMIN_ID=? and INSTITUTION_PASSWORD=?");
            st.setString(1, adminId);
            st.setString(2, institutionId);
            st.setString(3, password);

            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return false;
    }
}
