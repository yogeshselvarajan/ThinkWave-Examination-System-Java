package Admin;

import DatabaseFunctions.ConnectionDB;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.JFrame;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BarChartEx extends JFrame {
    private int numMale;
    private int numFemale;

    public BarChartEx() {
        retrieveData();
        createBarChart();
    }

    private void retrieveData() {
        try {
            Connection conn = ConnectionDB.connect();
            String sql = "SELECT COUNT(*) FROM THINKWAVE.USER_DETAILS WHERE GENDER = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, "M");
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                numMale = rs.getInt(1);
            }
            stmt.setString(1, "F");
            rs = stmt.executeQuery();
            if (rs.next()) {
                numFemale = rs.getInt(1);
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createBarChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.setValue(numMale, "Count", "Male");
        dataset.setValue(numFemale, "Count", "Female");

        JFreeChart chart = ChartFactory.createBarChart("User Gender Statistics", "Gender", "Count", dataset, PlotOrientation.VERTICAL, false, true, false);

        try {
            File imgFolder = new File("./img/Graphs/");
            if (!imgFolder.exists()) {
                imgFolder.mkdirs();
            }
            String fileName = "UserGenderStatistics.png";
            File imgFile = new File(imgFolder, fileName);
            ChartUtilities.saveChartAsPNG(imgFile, chart, 500, 300);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void generateBarChart() {
        int numMale = 0;
        int numFemale = 0;

        try {
            Connection conn = ConnectionDB.connect();
            String sql = "SELECT COUNT(*) FROM THINKWAVE.USER_DETAILS WHERE GENDER = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, "M");
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                numMale = rs.getInt(1);
            }
            stmt.setString(1, "F");
            rs = stmt.executeQuery();
            if (rs.next()) {
                numFemale = rs.getInt(1);
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.setValue(numMale, "Count", "Male");
        dataset.setValue(numFemale, "Count", "Female");

        JFreeChart chart = ChartFactory.createBarChart("User Gender Statistics", "Gender", "Count", dataset, PlotOrientation.VERTICAL, false, true, false);

        try {
            File imgFolder = new File("./img/Graphs/");
            if (!imgFolder.exists()) {
                imgFolder.mkdirs();
            }
            String fileName = "UserGenderStatistics.png";
            File imgFile = new File(imgFolder, fileName);
            ChartUtilities.saveChartAsPNG(imgFile, chart, 500, 300);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new BarChartEx();
    }
}
