package DatabaseFunctions;

import SecureHash.PassBasedEnc;
import java.util.UUID;
import java.sql.*;

public class UserLoginCheck {
    public static String checkUserLogin(String userId, String institutionId, String plainpassword, String captchaImage, String enteredCaptcha) {
        String[] passArray = RetrieveSecuredPass.retrievePass(userId, institutionId);
        String passhash = passArray[0];
        String passsalt = passArray[1];
        try {
            Connection connection = ConnectionDB.connect();
            PreparedStatement st = (PreparedStatement) connection
                    .prepareStatement("Select USER_ID, INSTID,NAME,SESSIONID from THINKWAVE.USER_DETAILS where USER_ID='" + userId + "' and INSTID='" + institutionId + "'");

            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                String username = rs.getString("NAME");
                String sessionId = rs.getString("SESSIONID");
                boolean status = PassBasedEnc.verifyUserPassword(plainpassword,passhash,passsalt);
                if (status && captchaImage.equals(enteredCaptcha)) {
                    if (sessionId == null) {
                        // No active session, allow login
                        sessionId = UUID.randomUUID().toString();
                        PreparedStatement updateStmt = (PreparedStatement) connection
                                .prepareStatement("UPDATE THINKWAVE.USER_DETAILS SET SESSIONID = ? WHERE USER_ID = ? AND INSTID = ?");
                        updateStmt.setString(1, sessionId);
                        updateStmt.setString(2, userId);
                        updateStmt.setString(3, institutionId);
                        updateStmt.executeUpdate();
                        return username;
                    } else {
                        // Session already active, deny login
                        return "Already a session active";
                    }
                } else {
                    return "Invalid Credentials";
                }
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return "User not found";
    }

    public static void main(String[] args) {
        System.out.println(checkUserLogin("20001", "INS01", "orcl", "eraTFb", "eraTFb"));
    }
}
