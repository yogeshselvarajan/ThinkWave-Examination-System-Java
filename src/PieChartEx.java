import DatabaseFunctions.ConnectionDB;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PieChartEx extends JFrame {
    private int numStudents;
    private int numFaculties;
    private int numAdmins;

    public PieChartEx() {
        retrieveData();
        createPieChart();
    }

    private void retrieveData() {
        try {
            Connection conn = ConnectionDB.connect();
            String sql = "SELECT COUNT(*) FROM THINKWAVE.USER_AUTHENTICATION ua INNER JOIN THINKWAVE.USER_DETAILS ud ON ua.USER_ID = ud.USER_ID WHERE ua.ROLE = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, "Student");
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                numStudents = rs.getInt(1);
            }
            stmt.setString(1, "Faculty");
            rs = stmt.executeQuery();
            if (rs.next()) {
                numFaculties = rs.getInt(1);
            }
            stmt.setString(1, "Admin");
            rs = stmt.executeQuery();
            if (rs.next()) {
                numAdmins = rs.getInt(1);
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createPieChart() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("Students", numStudents);
        dataset.setValue("Faculties", numFaculties);
        dataset.setValue("Admins", numAdmins);

        JFreeChart chart = ChartFactory.createPieChart("Institution Statistics", dataset, false, true, false);

        try {
            File imgFolder = new File("./img/Graphs/");
            if (!imgFolder.exists()) {
                imgFolder.mkdirs();
            }
            String fileName = "InstitutionStatistics.png";
            File imgFile = new File(imgFolder, fileName);
            ChartUtilities.saveChartAsPNG(imgFile, chart, 500, 300);
            System.out.println("Chart saved as " + imgFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new PieChartEx();
    }

}
