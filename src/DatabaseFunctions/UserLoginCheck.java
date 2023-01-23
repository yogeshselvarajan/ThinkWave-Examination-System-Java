package DatabaseFunctions;

import SecureHash.PassBasedEnc;

import java.sql.*;
public class UserLoginCheck {
    public static boolean checkUserLogin(String userId, String institutionId, String plainpassword, String captchaImage, String enteredCaptcha)
    {
        String[] passArray = RetrieveSecuredPass.retrievePass(userId, institutionId);
        String passhash = passArray[0];
        String passsalt = passArray[1];
        try {
            Connection connection = (Connection) DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl",
                    "c##thinkwave", "orcl");
            PreparedStatement st = (PreparedStatement) connection
                    .prepareStatement("Select ID, INSTID from C##THINKWAVE.USER_TABLE where ID=? and INSTID=? ");
            st.setString(1, userId);
            st.setString(2, institutionId);

            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                boolean status = PassBasedEnc.verifyUserPassword(plainpassword,passhash,passsalt);
                return status && captchaImage.equals(enteredCaptcha);
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return false;
    }
}
