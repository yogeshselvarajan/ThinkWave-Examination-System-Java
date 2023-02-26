package DatabaseFunctions.QuestionPaper;

import java.sql.*;

public class RetrieveCourseID
{
    // Function that will accept the course code and return the course ID from the database
    public static int getCourseID(String courseCode)
        {
            // JDBC driver and database URL
            String jdbcDriver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
            String dbUrl = "jdbc:sqlserver://thinkwaveappln.database.windows.net:1433;database=orcl;user=thinkwave@thinkwaveappln;password=Mepcocollege1@;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
            String sql = "SELECT CourseID FROM THINKWAVE.QB_Course WHERE CourseCode = ?";
            Connection conn = null;
            PreparedStatement stmt = null;
            ResultSet rs = null;
            int courseID = 0;

            try
            {
                // Load JDBC driver
                Class.forName(jdbcDriver);

                // Open a connection to the database
                conn = DriverManager.getConnection(dbUrl);

                // Create a prepared statement for the SQL query
                stmt = conn.prepareStatement(sql);

                // Set the parameters for the SQL query
                stmt.setString(1, courseCode);

                // Execute the SQL query
                rs = stmt.executeQuery();

                // Get the course ID from the result set
                while(rs.next())
                {
                    courseID = rs.getInt("CourseID");
                }
            }
            catch (ClassNotFoundException | SQLException e)
            {
                e.printStackTrace();
            }
            finally
            {
                // Close the resources
                try
                {
                    if (rs != null)
                    {
                        rs.close();
                    }
                    if (stmt != null)
                    {
                        stmt.close();
                    }
                    if (conn != null)
                    {
                        conn.close();
                    }
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
            return courseID;
        }
}
