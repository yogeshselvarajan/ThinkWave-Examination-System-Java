package DatabaseFunctions.Admin;

import DatabaseFunctions.ConnectionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetLoginDetails
{
    // Function to get the userid and the institution id of the user and the mobile number of the user
    public static String[] getLoginDetails(String username)
    {
        String[] loginDetails = new String[2];
        String sql = "SELECT USER_ID, INSTID FROM THINKWAVE.USER_DETAILS WHERE NAME = '" + username + "'";
        try (Connection connection = ConnectionDB.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                loginDetails[0] = resultSet.getString("USER_ID");
                loginDetails[1] = resultSet.getString("INSTID");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return loginDetails;
    }
}
