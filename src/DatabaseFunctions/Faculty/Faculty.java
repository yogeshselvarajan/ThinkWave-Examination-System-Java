package DatabaseFunctions.Faculty;

import DatabaseFunctions.ConnectionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Faculty
{
    public static boolean examExists(String examName, String facultyID) {
        String sql = "SELECT exam_id FROM THINKWAVE.EXAM_DETAILS WHERE exam_name = ? AND faculty_id = ?";
        if(facultyID == null || facultyID.isEmpty() || examName == null || examName.isEmpty())
            return false; // return false if either of the parameters is null or empty (to avoid SQL injection
        try (Connection conn = ConnectionDB.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, examName);
            pstmt.setString(2, facultyID);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next(); // return true if there is at least one row
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false; // return false if there was an error
        }
    }

    public static String[] getExamDetails(String examName, String facultyID) {
        String[] examDetails = new String[5];
        try {
            Connection conn = ConnectionDB.connect();
            String sql = "SELECT exam_id FROM THINKWAVE.EXAM_DETAILS WHERE exam_name=? AND faculty_id=?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, examName);
            pstmt.setString(2, facultyID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int examID = rs.getInt("exam_id");
                sql = "SELECT exam_name, exam_date, duration, total_marks,exam_id FROM THINKWAVE.EXAM_DETAILS WHERE exam_id=?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, examID);
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    examDetails[0] = rs.getString("exam_name");
                    examDetails[1] = rs.getString("exam_date");
                    examDetails[2] = rs.getString("duration");
                    examDetails[3] = rs.getString("total_marks");
                    examDetails[4] = String.valueOf(examID);
                }
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return examDetails;
    }

}
