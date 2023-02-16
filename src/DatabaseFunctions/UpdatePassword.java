package DatabaseFunctions;

import SecureHash.PassBasedEnc;

import java.sql.*;

public class UpdatePassword {
    public static void updatePassword(String userId, String password) {
        String salt = RetrieveSaltValue.retrieveSalt(userId);
        String securePassword = PassBasedEnc.generateSecurePassword(password, salt);
        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:sqlserver://thinkwaveappln.database.windows.net:1433;database=orcl;user=thinkwave@thinkwaveappln;password=Mepcocollege1@;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;");

            // Update the PASSW_HASH, PASSW_SALT for the userid in the database
            PreparedStatement st = (PreparedStatement) connection
                    .prepareStatement("UPDATE C##THINKWAVE.USER_TABLE SET PASSW_HASH=?  WHERE USER_ID =?");
            st.setString(1, securePassword);
            st.setString(2,userId);
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
