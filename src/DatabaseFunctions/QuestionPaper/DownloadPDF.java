package DatabaseFunctions.QuestionPaper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class DownloadPDF {
    public static void main(String[] args) {
        // JDBC driver and database URL
        String jdbcDriver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        String dbUrl = "jdbc:sqlserver://thinkwaveappln.database.windows.net:1433;database=orcl;user=thinkwave@thinkwaveappln;password=Mepcocollege1@;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";

        // SQL query to select the PDF file
        String sql = "SELECT QUESTION_PAPER,FILE_NAME FROM THINKWAVE.QUESTION_BANK WHERE QUESTION_ID = ?";

        // Show the file chooser dialog to select where to save the file
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("PDF files", "pdf");
        fileChooser.setFileFilter(filter);
        int result = fileChooser.showSaveDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            // Get the selected file
            String filename = fileChooser.getSelectedFile().getAbsolutePath();
            if (!filename.endsWith(".pdf")) {
                filename += ".pdf";
            }

            // Prompt the user to enter the qid of the file to download
            String input = javax.swing.JOptionPane.showInputDialog("Enter the qid of the file to download:");
            if (input == null || input.isEmpty()) {
                return;
            }
            int qid = Integer.parseInt(input);

            try {
                // Register the JDBC driver
                Class.forName(jdbcDriver);

                // Open a connection to the database
                Connection conn = DriverManager.getConnection(dbUrl);

                // Prepare the SQL statement
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, qid);

                // Execute the SQL statement and get the result set
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    // Get the file data and file name from the result set
                    byte[] fileData = rs.getBytes("QUESTION_PAPER");
                    String fileName = rs.getString("FILE_NAME");

                    // Save the file to the local file system
                    FileOutputStream fos = new FileOutputStream(filename);
                    fos.write(fileData);
                    fos.close();

                    // Display a message to indicate success
                    String message = "File " + fileName + " downloaded successfully to " + filename;
                    javax.swing.JOptionPane.showMessageDialog(null, message);
                } else {
                    // Display a message to indicate failure
                    javax.swing.JOptionPane.showMessageDialog(null, "No file found with qid " + qid);
                }

                // Close the result set, statement, and connection
                rs.close();
                stmt.close();
                conn.close();
            } catch (ClassNotFoundException e) {
                System.err.println("Error loading JDBC driver: " + e.getMessage());
            } catch (SQLException e) {
                System.err.println("Error downloading file: " + e.getMessage());
            } catch (IOException e) {
                System.err.println("Error writing file: " + e.getMessage());
            }
        }
    }
}
