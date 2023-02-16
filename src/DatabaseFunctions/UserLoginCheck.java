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
            Connection connection = DriverManager.getConnection(
                    "jdbc:sqlserver://thinkwaveappln.database.windows.net:1433;database=orcl;user=thinkwave@thinkwaveappln;password=Mepcocollege1@;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;");

            PreparedStatement st = (PreparedStatement) connection
                    .prepareStatement("Select USER_ID, INSTID from THINKWAVE.USER_TABLE where USER_ID=? and INSTID=? ");
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
