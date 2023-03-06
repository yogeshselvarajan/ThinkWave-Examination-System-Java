package DatabaseFunctions;

import SecureHash.PassBasedEnc;

import java.sql.*;

public class UpdatePassword {
    public static boolean updatePassword(int userId, String password) {
        String salt = RetrieveSaltValue.retrieveSalt(userId);
        String securePassword = PassBasedEnc.generateSecurePassword(password, salt);
        Connection con = null;
        String updateSql = "UPDATE THINKWAVE.USER_AUTHENTICATION SET PASSW_HASH=?, PASSW_SALT=? WHERE USER_ID= '" + userId + "'";
        try {
            con = ConnectionDB.connect();
            PreparedStatement ps = con.prepareStatement(updateSql);
            ps.setString(1, securePassword);
            ps.setString(2, salt);
            ps.executeUpdate();
            ps.close();
            con.setAutoCommit(false);
            con.commit();
            con.close();
            return true;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            return false;
        }
    }
}
