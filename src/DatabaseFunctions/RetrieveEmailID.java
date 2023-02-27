package DatabaseFunctions;


import java.sql.*;

public class RetrieveEmailID {

    // Function to check if the email exists in the microso
    public static String retrieveEmail(String userID) {
        String emailID = null;
        try {

            Connection connection = DriverManager.getConnection(
                    "jdbc:sqlserver://thinkwaveappln.database.windows.net:1433;database=orcl;user=thinkwave@thinkwaveappln;password=Mepcocollege1@;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;");

            PreparedStatement st = (PreparedStatement) connection
                    .prepareStatement("Select USER_ID,EMAIL from THINKWAVE.USER_DETAILS where USER_ID=?");
            st.setString(1, userID);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                emailID = rs.getString("EMAIL");
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return emailID;
    }
}
