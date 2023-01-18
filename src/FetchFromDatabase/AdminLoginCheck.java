package FetchFromDatabase;

import SecureHash.PassBasedEnc;

import java.sql.*;

public class AdminLoginCheck {
    // Function that checks if the user credentials are correct by comparing user id, institution id and password with the database
    public static boolean checkAdminLogin(String adminId, String institutionId, String password, String captchaImage, String enteredCaptcha) {
        String[] passArray = RetrieveSecuredPass.retrievePass(adminId, institutionId);
        String passhash = passArray[0];
        String passsalt = passArray[1];
        try {
            Connection connection = (Connection) DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl",
                    "c##thinkwave", "orcl");
            PreparedStatement st1 = (PreparedStatement) connection
                    .prepareStatement("Select INSTID, INSTITUTION_ADMIN_ID from C##THINKWAVE.INSTITUTION_TABLE where INSTID=? and INSTITUTION_ADMIN_ID=? ");
            st1.setString(1, adminId);
            st1.setString(2, institutionId);

            ResultSet rs = st1.executeQuery();
            if (rs.next()) {
                boolean status = PassBasedEnc.verifyUserPassword(password,passhash,passsalt);
                return status && captchaImage.equals(enteredCaptcha);
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return false;
        }

    }
