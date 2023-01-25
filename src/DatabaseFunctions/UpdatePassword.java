package DatabaseFunctions;

import SecureHash.PassBasedEnc;

import java.sql.*;

public class UpdatePassword {
    public static void updatePassword(String userId, String password) {
        PassBasedEnc ob = new PassBasedEnc();
        String salt = RetrieveSaltValue.retrieveSalt(userId);
        String securePassword = ob.generateSecurePassword(password, salt);
        try {
            Connection connection = (Connection) DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl",
                    "c##thinkwave", "orcl");
            // Update the PASSW_HASH, PASSW_SALT for the userid in the database
            PreparedStatement st = (PreparedStatement) connection
                    .prepareStatement("UPDATE C##THINKWAVE.USER_TABLE SET PASSW_HASH=?  WHERE ID=?");
            st.setString(1, securePassword);
            st.setString(2,userId);
            ResultSet rs = st.executeQuery();
            rs.close();
            st.close();
            connection.setAutoCommit(false);
            connection.close();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }
}
