package DatabaseFunctions;

import SecureHash.PassBasedEnc;

import java.sql.*;

public class AdminLoginCheck {
    // Function that checks if the user credentials are correct by comparing user id, institution id and password with the database
    public static boolean checkAdminLogin(String adminId, String institutionId, String password, String captchaImage, String enteredCaptcha) {
        String passhash  = null;
        String passsalt = null;
        try {
            Connection connection = (Connection) DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl",
                    "c##thinkwave", "orcl");
            PreparedStatement stmt = (PreparedStatement) connection
                    .prepareStatement("Select INST_ID, ADMIN_USER_ID,PASSW_HASH_INST,PASSW_SALT_INST from C##THINKWAVE.INSTITUTION_ADMIN where INST_ID=? and ADMIN_USER_ID=? ");
            stmt.setString(1, institutionId);
            stmt.setString(2, adminId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                passhash = rs.getString(3);
                passsalt = rs.getString(4);
                boolean status = PassBasedEnc.verifyUserPassword(password,passhash,passsalt);
                return status && captchaImage.equals(enteredCaptcha);
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return false;
        }

    public static void main(String[] args) {
        System.out.println(checkAdminLogin("AD001", "INS0001", "Thinkwave1", "ApUXQk", "ApUXQk"));
    }

    }
