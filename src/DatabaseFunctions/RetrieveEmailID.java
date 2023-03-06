package DatabaseFunctions;


import java.sql.*;

public class RetrieveEmailID {

    // Function to check if the email exists in the microso
    public static String retrieveEmail(int userID) {
        String emailID = null;
        String query = "Select USER_ID,EMAIL from THINKWAVE.USER_DETAILS where USER_ID=?";
        Connection con = null;
        try {
            con = ConnectionDB.connect();
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, String.valueOf(userID));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                emailID = rs.getString("EMAIL");
            }
               rs.close();
                ps.close();
                con.close();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return emailID;
    }

    public static void main(String[] args) {
        System.out.println(retrieveEmail(10001));
    }
}
