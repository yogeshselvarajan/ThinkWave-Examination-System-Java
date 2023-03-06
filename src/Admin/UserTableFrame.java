package Admin;

import DatabaseFunctions.ConnectionDB;

import javax.swing.*;
import java.sql.*;

public class UserTableFrame extends JFrame {
    private UserTableModel model;

    public UserTableFrame(String roleName) {
        setTitle("Student List");
        setSize(600, 400);

        // create a custom TableModel and JTable
        model = new UserTableModel();
        JTable table = new JTable(model);

        // create a JScrollPane and add the JTable to it
        JScrollPane scrollPane = new JScrollPane(table);

        // add the JScrollPane to the JFrame
        add(scrollPane);

        String sql = "SELECT ud.USER_ID, ud.NAME\n" +
                "FROM THINKWAVE.USER_AUTHENTICATION ua\n" +
                "INNER JOIN THINKWAVE.USER_DETAILS ud\n" +
                "ON ua.USER_ID = ud.USER_ID\n" +
                "WHERE ua.ROLE = '" + roleName + "'";

        // fetch user data from database and populate the TableModel
        Connection con = null;
        try {
            con = ConnectionDB.connect();
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.setFetchSize(1000); // set the fetch size to a non-zero value
            ResultSet rs = stmt.executeQuery(sql);

            // store the data in a two-dimensional array
            int rowCount = 0;
            while (rs.next()) {
                rowCount++;
            }
            rs.beforeFirst();

            Object[][] data = new Object[rowCount][2];
            int i = 0;
            while (rs.next()) {
                data[i][0] = rs.getInt("USER_ID");
                data[i][1] = rs.getString("NAME");
                i++;
            }

            model.setData(data);

            rs.close();
            stmt.close();
            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
