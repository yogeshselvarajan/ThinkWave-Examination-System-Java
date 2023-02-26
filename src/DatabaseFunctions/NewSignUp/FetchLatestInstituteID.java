package DatabaseFunctions.NewSignUp;

import java.sql.*;

public class FetchLatestInstituteID {
    // Function that will check if the Admin ID already exists in the database
    public static String fetchLatestInstituteID() {
        // Variable to store the latest institute ID
        String latestInstituteID = null;

        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:sqlserver://thinkwaveappln.database.windows.net:1433;database=orcl;user=thinkwave@thinkwaveappln;password=Mepcocollege1@;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;");

            // Query to fetch the LATEST C##THINKWAVE.INSTITUTION_ADMIN.INST_ID FROM INSTITUTION_ADMIN TABLE by sorting it in descending order and fetching the first row
            PreparedStatement st = (PreparedStatement) connection
                    .prepareStatement("Select MAX(C##THINKWAVE.INSTITUTION_ADMIN.INST_ID) from C##THINKWAVE.INSTITUTION_ADMIN order by C##THINKWAVE.INSTITUTION_ADMIN.INST_ID desc");
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                // Fetch the latest institute ID in the database and add 1 to it. It should be of the Format: INS_xxxx where xxxx is the institute ID + 1
                if (rs.getString(1) == null) {
                    latestInstituteID = "INS0001";
                } else {
                    latestInstituteID = rs.getString(1);
                    // Increment the latest institute ID by 1
                    latestInstituteID = "INS" + String.format("%04d", Integer.parseInt(latestInstituteID.substring(3)) + 1);
                }
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return latestInstituteID;
    }

}