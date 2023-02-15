package Admin;

import DateTime.DateTimePanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class AdminDashboard  extends JFrame
{
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                AdminDashboard frame = new AdminDashboard();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void addMouseListenerToLblStudents(JLabel lblStudents, JPanel panelright1, JPanel panelright2, JPanel panelright3) {
        lblStudents.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                panelright1.setVisible(true);
                panelright2.setVisible(true);
                panelright3.setVisible(true);

                // Reset content of panels
                panelright1.removeAll();
                panelright2.removeAll();
                panelright3.removeAll();

                // Add image to panelright1 panel
                try {
                    JLabel imgAddStudent = new JLabel(new ImageIcon(ImageIO.read(new File("img/Admin/student.png"))));
                    imgAddStudent.setBounds(10, 10, 50, 80);
                    panelright1.add(imgAddStudent);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                JLabel lblAddStudent = new JLabel("Add Student");
                lblAddStudent.setFont(new Font("Tahoma", Font.BOLD, 20));
                lblAddStudent.setBounds(120, 30, 150, 30);
                panelright1.add(lblAddStudent);
                lblAddStudent.setForeground(Color.BLACK);

                // Add image to panelright2 panel
                try {
                    JLabel imgEditStudent = new JLabel(new ImageIcon(ImageIO.read(new File("img/Admin/pencil.png"))));
                    imgEditStudent.setBounds(10, 10, 50, 80);
                    panelright2.add(imgEditStudent);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                JLabel lblEditStudent = new JLabel("Edit Student Details");
                lblEditStudent.setFont(new Font("Tahoma", Font.BOLD, 20));
                lblEditStudent.setBounds(70, 30, 220, 30);
                panelright2.add(lblEditStudent);
                lblEditStudent.setForeground(Color.BLACK);

                // Add image to panelright3 panel
                try {
                    JLabel imgDeleteStudent = new JLabel(new ImageIcon(ImageIO.read(new File("img/Admin/delete-account.png"))));
                    imgDeleteStudent.setBounds(50, 10, 50, 80);
                    panelright3.add(imgDeleteStudent);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                JLabel lblDeleteStudent = new JLabel("Delete Student");
                lblDeleteStudent.setFont(new Font("Tahoma", Font.BOLD, 20));
                lblDeleteStudent.setBounds(120, 30, 175, 30);
                panelright3.add(lblDeleteStudent);
                lblDeleteStudent.setForeground(Color.BLACK);

                // Repaint the panels to show the new components
                panelright1.repaint();
                panelright2.repaint();
                panelright3.repaint();
            }
        });
    }

    public void addMouseListenerToLblTeachers(JLabel lblTeachers, JPanel panelright1, JPanel panelright2, JPanel panelright3) {
        lblTeachers.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                panelright1.setVisible(true);
                panelright2.setVisible(true);
                panelright3.setVisible(true);

                // Reset content of panels
                panelright1.removeAll();
                panelright2.removeAll();
                panelright3.removeAll();


                // Add image to panelright1 panel
                try {
                    JLabel imgAddTeacher = new JLabel(new ImageIcon(ImageIO.read(new File("img/Admin/teacher.png"))));
                    imgAddTeacher.setBounds(10, 10, 50, 80);
                    panelright1.add(imgAddTeacher);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                JLabel lblAddTeacher = new JLabel("Add Teacher");
                lblAddTeacher.setFont(new Font("Tahoma", Font.BOLD, 20));
                lblAddTeacher.setBounds(120, 30, 150, 30);
                panelright1.add(lblAddTeacher);
                lblAddTeacher.setForeground(Color.BLACK);

                // Add image to panelright2 panel
                try {
                    JLabel imgEditTeacher = new JLabel(new ImageIcon(ImageIO.read(new File("img/Admin/pencil.png"))));
                    imgEditTeacher.setBounds(10, 10, 50, 80);
                    panelright2.add(imgEditTeacher);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                JLabel lblEditTeacher = new JLabel("Edit Teacher Details");
                lblEditTeacher.setFont(new Font("Tahoma", Font.BOLD, 20));
                lblEditTeacher.setBounds(70, 30, 220, 30);
                panelright2.add(lblEditTeacher);
                lblEditTeacher.setForeground(Color.BLACK);

                // Add image to panelright3 panel
                try {
                    JLabel imgDeleteTeacher = new JLabel(new ImageIcon(ImageIO.read(new File("img/Admin/delete-account.png"))));
                    imgDeleteTeacher.setBounds(50, 10, 50, 80);
                    panelright3.add(imgDeleteTeacher);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                JLabel lblDeleteTeacher = new JLabel("Delete Teacher");
                lblDeleteTeacher.setFont(new Font("Tahoma", Font.BOLD, 20));
                lblDeleteTeacher.setBounds(120, 30, 175, 30);
                panelright3.add(lblDeleteTeacher);
                lblDeleteTeacher.setForeground(Color.BLACK);

                // Repaint the panels to show the new components
                panelright1.repaint();
                panelright2.repaint();
                panelright3.repaint();
            }
        });
    }




    public AdminDashboard() throws IOException
    {
        setResizable(false);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        contentPane.setBackground(Color.LIGHT_GRAY);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setAlwaysOnTop(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Adding the title image of the page to the frame
            JLabel image = new JLabel(new ImageIcon(ImageIO.read(new File("img/Login/thinkwave.png"))));
            image.setBounds(5, 10, 1400, 100);
            contentPane.add(image);

            JLabel image1 = new JLabel(new ImageIcon(ImageIO.read(new File("img/Admin/slogan.png"))));
            image1.setBounds(200, 125, 1100, 50);
            contentPane.add(image1);

            JLabel image2 = new JLabel(new ImageIcon(ImageIO.read(new File("img/Admin/admin dashboard.png"))));
            image2.setBounds(155, 185, 1200, 50);
            contentPane.add(image2);

           /* JLabel image3 = new JLabel(new ImageIcon(ImageIO.read(new File("./img/Admin/admin ui.jpg"))));
            image3.setBounds(15, 235, 600, 500);
            contentPane.add(image3);
*/
        // A panel at the top of the frame
        JPanel paneltop = new JPanel();
        paneltop.setBounds(0, 0, 1400, 195);
        // Set colour to panel background to #ff9478 (light orange)
        paneltop.setBackground(new Color(255, 148, 120));
        paneltop.setLayout(null);
        contentPane.add(paneltop);

        // A panel with different colour below the paneltop panel to separate the two panels
        JPanel paneltop2 = new JPanel();
        paneltop2.setBounds(0, 195, 1400, 50);
        // Set colour to panel background to #ff9478 (light orange)
        paneltop2.setBackground(new Color(255, 148, 120));
        paneltop2.setLayout(null);
        contentPane.add(paneltop2);

        // A panel below the paneltop2 panel at the left side of the frame to act as a sidebar
        JPanel panelleft = new JPanel();
        panelleft.setBounds(2, 245, 250, 485);
        // Set colour to panel background to  light grey
        panelleft.setBackground(Color.CYAN);
        panelleft.setBorder(new LineBorder(Color.BLACK, 2));
        panelleft.setLayout(null);
        contentPane.add(panelleft);

        // Add the thinkwave logo to the panelleft panel above Home text
        JLabel lblLogo = new JLabel(new ImageIcon(ImageIO.read(new File("img/Admin/thinkwave.jpg"))));
        lblLogo.setBounds(50, 50, 150, 50);
        panelleft.add(lblLogo);

        // Add the text Home to the panelleft panel
        JLabel lblHome = new JLabel("Home");
        lblHome.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblHome.setBounds(80, 150, 100, 30);
        panelleft.add(lblHome);
        lblHome.setForeground(Color.BLACK);

        // Add the text Students to the panelleft panel blow the Home text
        JLabel lblStudents = new JLabel("Students");
        lblStudents.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblStudents.setBounds(80, 200, 100, 30);
        panelleft.add(lblStudents);
        lblStudents.setForeground(Color.BLACK);

        // Add the text Teachers to the panelleft panel blow the Students text
        JLabel lblTeachers = new JLabel("Teachers");
        lblTeachers.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblTeachers.setBounds(80, 250, 100, 30);
        panelleft.add(lblTeachers);
        lblTeachers.setForeground(Color.BLACK);

        // Add the text Exams to the panelleft panel blow the Teachers text
        JLabel lblExams = new JLabel("Exams");
        lblExams.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblExams.setBounds(80, 300, 100, 30);
        panelleft.add(lblExams);
        lblExams.setForeground(Color.BLACK);

        // Add the text Question Papers to the panelleft panel blow the Exams text
        JLabel lblQuestionPapers = new JLabel("Question Papers");
        lblQuestionPapers.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblQuestionPapers.setBounds(40, 350, 200, 30);
        panelleft.add(lblQuestionPapers);
        lblQuestionPapers.setForeground(Color.BLACK);

        // Add the text Settings to the panelleft panel blow the Question Papers text
        JLabel lblSettings = new JLabel("Settings");
        lblSettings.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblSettings.setBounds(80, 400, 100, 30);
        panelleft.add(lblSettings);
        lblSettings.setForeground(Color.BLACK);

        // Add the text Logout to the panelleft panel blow the Settings text
        JLabel lblLogout = new JLabel("Logout");
        lblLogout.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblLogout.setBounds(80, 450, 100, 30);
        panelleft.add(lblLogout);
        lblLogout.setForeground(Color.BLACK);

        // A panel to the right of the panelleft panel to display the content when the user clicks on the sidebar options
        JPanel panelright = new JPanel();
        panelright.setBounds(252, 245, 1110, 485);
        // Set colour to panel background to  light blue
        panelright.setBackground(Color.BLUE);
        panelright.setBorder(new LineBorder(Color.BLACK, 2));
        panelright.setLayout(null);
        contentPane.add(panelright);
        panelright.setVisible(true);

        // A panel inside the panelright panel to display the content
        JPanel panelright1 = new JPanel();
        panelright1.setBounds(65, 10, 300, 100);
        // Set colour to panel background to  yellow
        panelright1.setBackground(Color.YELLOW);
        panelright1.setBorder(new LineBorder(Color.BLACK, 2));
        panelright1.setLayout(null);
        panelright1.setVisible(false);
        panelright.add(panelright1);

        // A panel inside the panelright panel on the right side to panelright1 panel to display the content
        JPanel panelright2 = new JPanel();
        panelright2.setBounds(425, 10, 300, 100);
        // Set colour to panel background to  yellow
        panelright2.setBackground(Color.YELLOW);
        panelright2.setBorder(new LineBorder(Color.BLACK, 2));
        panelright2.setLayout(null);
        panelright2.setVisible(false);
        panelright.add(panelright2);

        // A panel inside the panelright panel on the right side to panelright2 panel to display the content
        JPanel panelright3 = new JPanel();
        panelright3.setBounds(790, 10, 300, 100);
        // Set colour to panel background to  yellow
        panelright3.setBackground(Color.YELLOW);
        panelright3.setBorder(new LineBorder(Color.BLACK, 2));
        panelright3.setLayout(null);
        panelright3.setVisible(false);
        panelright.add(panelright3);

        // Performs an action when the user clicks on the Students text
        addMouseListenerToLblStudents(lblStudents, panelright1, panelright2, panelright3);
        // Performs an action when the user clicks on the Teachers text
        addMouseListenerToLblTeachers(lblTeachers, panelright1, panelright2, panelright3);

        // import the addTimeFooter method from the DateTimePanel class
        DateTimePanel dateTimePanel = new DateTimePanel();
        dateTimePanel.addTimeFooter(contentPane);
        dateTimePanel.setVisible(true);
    }

}
