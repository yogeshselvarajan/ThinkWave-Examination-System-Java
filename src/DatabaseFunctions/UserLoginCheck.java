package DatabaseFunctions;

import SecureHash.PassBasedEnc;

import java.sql.*;
public class UserLoginCheck {
    public static String checkUserLogin(String userId, String institutionId, String plainpassword, String captchaImage, String enteredCaptcha)
    {
        String[] passArray = RetrieveSecuredPass.retrievePass(userId, institutionId);
        String passhash = passArray[0];
        String passsalt = passArray[1];
        try {
            Connection connection = ConnectionDB.connect();
            PreparedStatement st = (PreparedStatement) connection
                    .prepareStatement("Select USER_ID, INSTID, NAME from THINKWAVE.USER_DETAILS where USER_ID=? and INSTID=? ");
            st.setString(1, userId);
            st.setString(2, institutionId);

            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                String username = rs.getString("NAME");
                boolean status = PassBasedEnc.verifyUserPassword(plainpassword,passhash,passsalt);
                if (status && captchaImage.equals(enteredCaptcha))
                    return username;
                else
                    return "Invalid Credentials";
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return "User not found";
    }
}
