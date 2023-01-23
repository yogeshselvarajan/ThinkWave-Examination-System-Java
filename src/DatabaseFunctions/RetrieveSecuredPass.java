package DatabaseFunctions;

import java.sql.*;

public class RetrieveSecuredPass {
    public static String[] retrievePass(String userId, String institutionId) {
        String passhash  = null;
        String passsalt = null;
 
        try {
            Connection connection = (Connection) DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl",
                    "system", "orcl");
            PreparedStatement st = (PreparedStatement) connection
                    .prepareStatement("Select ID, INSTID, PASSW_HASH,PASSW_SALT from C##THINKWAVE.USER_TABLE where ID=? and INSTID=? ");
            st.setString(1, userId);
            st.setString(2, institutionId);

            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                passhash = rs.getString(3);
                passsalt = rs.getString(4);
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        String[] passArray = {passhash, passsalt};
        return passArray;
    }
}