package FetchFromDatabase;

import SecureHash.PassBasedEnc;
import java.sql.*;
public class UserLoginCheck {
    public static boolean checkUserLogin(String userId, String institutionId, String plainpassword)
    {
        String[] passArray = RetrieveSecuredPass.retrievePass(userId, institutionId);
        String passhash = passArray[0];
        String passsalt = passArray[1];
        try {
            Connection connection = (Connection) DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl",
                    "system", "orcl");
            PreparedStatement st = (PreparedStatement) connection
                    .prepareStatement("Select ID, INSTID from C##THINKWAVE.USER_TABLE where ID=? and INSTID=? ");
            st.setString(1, userId);
            st.setString(2, institutionId);

            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                Boolean status = PassBasedEnc.verifyUserPassword(plainpassword,passhash,passsalt);
                if(status==true)
                    return true;
                else
                    return false;
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return false;
    }
}
