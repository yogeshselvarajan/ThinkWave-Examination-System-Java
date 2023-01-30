package DatabaseFunctions.NewSignUp;

import SecureHash.PassBasedEnc;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InsertSignUp
{
    // Method that will register the user in the database using C##THINKWAVE.INSTITUTION_ADMIN table
    // This method will insert the user into the database
    public static void insertUser(String plainpassword, String adminID, String instEmail, String emailverified, String instID, String adminEmail, String InstName, String instAddress) {
        PassBasedEnc ob = new PassBasedEnc();
        // Generate a random salt value using PassBasedEnc class
        String salt = ob.getSaltvalue(30);
        String hashedpass = ob.generateSecurePassword(plainpassword, salt);

        // TODO: Insert the user into the database
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "C##THINKWAVE", "orcl");
            // SQL query to insert into INSTITUTION_ADMIN table in the database
            PreparedStatement pst = connection.prepareStatement("insert into " +
                    "C##THINKWAVE.INSTITUTION_ADMIN(ADMIN_USER_ID, INST_EMAIL_ADDRESS, PASSW_HASH_INST, " +
                    "INST_ADMIN_EMAIL_VERIFIED, INST_ID, PASSW_SALT_INST," +
                    " ADMIN_EMAIL_ID, INST_NAME, INST_ADDRESS) " +
                    "values(?, ?, ?, ?, ?, ?, ?, ?, ?)");
           // Set the values for the prepared statement
            pst.setString(1,adminID);
            pst.setString(2,instEmail);
            pst.setString(3,hashedpass);
            pst.setString(4,emailverified);
            pst.setString(5,instID);
            pst.setString(6,salt);
            pst.setString(7,adminEmail);
            pst.setString(8,InstName);
            pst.setString(9,instAddress);
            // Execute the query
            pst.executeUpdate();
            connection.setAutoCommit(false);
            connection.commit();
            connection.close();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            if (connection != null) {
                try{
                    // If there is an error, rollback the changes
                    connection.rollback();
                    // Prompt the user that the registration was unsuccessful
                    JOptionPane.showMessageDialog(null, "An error occurred while registering the user. Please try again later.");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

