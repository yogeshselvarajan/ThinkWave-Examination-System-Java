package Faculty;

import Admin.AdminDashboard;
import Admin.DateLabelFormatter;
import DatabaseFunctions.ConnectionDB;
import DatabaseFunctions.Faculty.Faculty;
import DatabaseFunctions.QuestionPaper.RetrieveCourseID;
import DatabaseFunctions.RetrieveEmailID;
import DatabaseFunctions.UpdateLoginActivity;
import DateTime.DateTimePanel;
import Login.LoginPage;
import SecureHash.PassBasedEnc;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.List;
import java.awt.event.*;
import java.awt.font.TextAttribute;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.SecureRandom;
import java.sql.*;
import java.util.*;

import static DatabaseFunctions.Faculty.Faculty.examExists;
import static DatabaseFunctions.Faculty.Faculty.getExamDetails;

public class FacultyDashboard extends JFrame {
    final static Connection connection;

    static String gotUserID = null;

    static {
        try {
            connection = ConnectionDB.connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                FacultyDashboard frame = new FacultyDashboard();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static void getUserIDFromLogin(String passedid) {
        gotUserID = passedid;
    }

    private void removeWelcomeMessage(JPanel panelright) {
        Component[] components = panelright.getComponents();
        for (Component component : components) {
            if (component instanceof JLabel label) {
                String text = label.getText();
                if (text != null && text.contains("Welcome to the ThinkWave MCQ Examination System Dashboard")) {
                    panelright.remove(label);
                    panelright.repaint();
                    break;
                }
            }
        }
    }

    private void addWelcomeMessage(JPanel panelright) {
        String message = "<html><div style='text-align: center;'><p style='margin-bottom: 20px;'>Welcome to the ThinkWave MCQ Examination System Dashboard, esteemed Faculty Member!</p>"
                + "<p style='margin-bottom: 20px;'>Here, you can access all aspects of the system related to your institution, from creating and managing exams to viewing your students' performance data. "
                + "With just a few clicks, you can create exams, view students' performance reports and much more.</p>"
                + "<p style='margin-bottom: 20px;'>As a faculty member, you play a vital role in ensuring the success of your students, and this system is designed to assist you in that effort. We believe that you will find the system intuitive and user-friendly, but if you encounter any issues, please do not hesitate to raise a ticket from within the system, and our developers will be happy to assist you in resolving any issues that you may encounter.</p>"
                + "<p>Thank you for using the ThinkWave MCQ Examination System, and we hope that it will help you achieve your teaching goals and improve your students' learning outcomes!</p></div></html>";
        JLabel messageLabel = new JLabel(message);
        messageLabel.setFont(new Font("Serif", Font.BOLD, 19));
        messageLabel.setBounds(50, 50, 1000, 400);
        panelright.add(messageLabel);
        messageLabel.setForeground(Color.WHITE);
        panelright.repaint();
    }

    private void nullifySessionID(String userID) {
        String updateSql = "UPDATE THINKWAVE.USER_DETAILS SET SESSIONID = NULL WHERE USER_ID = ?";
        try {
            Connection con = ConnectionDB.connect();
            PreparedStatement ps = con.prepareStatement(updateSql);
            ps.setString(1, userID);
            ps.executeUpdate();
            con.setAutoCommit(false);
            con.commit();
            ps.close();
            con.close();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

    public FacultyDashboard() throws IOException {
        setResizable(false);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        contentPane.setBackground(Color.LIGHT_GRAY);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(false);
        setAlwaysOnTop(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Adding the title image of the page to the frame
        JLabel image = new JLabel(new ImageIcon(ImageIO.read(new File("img/Login/thinkwave.png"))));
        image.setBounds(5, 10, 1400, 100);
        contentPane.add(image);

        JLabel image1 = new JLabel(new ImageIcon(ImageIO.read(new File("img/Admin/slogan.png"))));
        image1.setBounds(200, 125, 1100, 50);
        contentPane.add(image1);

        JLabel image2 = new JLabel(new ImageIcon(ImageIO.read(new File("img/Faculty/faculty dashboard.png"))));
        image2.setBounds(155, 193, 1200, 50);
        contentPane.add(image2);

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

        // A panel below the paneltop2 panel on the left side of the frame to act as a sidebar
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
        lblHome.setBounds(80, 100, 100, 30);
        lblHome.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                Font font = lblHome.getFont();
                Map attributes = font.getAttributes();
                attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
                lblHome.setFont(font.deriveFont(attributes));
                lblHome.setForeground(Color.BLUE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                Font font = lblHome.getFont();
                Map attributes = font.getAttributes();
                attributes.put(TextAttribute.UNDERLINE, -1);
                lblHome.setFont(font.deriveFont(attributes));
                lblHome.setForeground(Color.BLACK);
            }
        });
        panelleft.add(lblHome);
        lblHome.setForeground(Color.BLACK);

        // Add the text Exam to the panelleft panel
        JLabel lblExam = new JLabel("Schedule Exam");
        lblExam.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblExam.setBounds(50, 150, 200, 30);
        panelleft.add(lblExam);
        // set black colour to the text
        lblExam.setForeground(Color.BLACK);
        lblExam.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                Font font = lblExam.getFont();
                Map attributes = font.getAttributes();
                attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
                lblExam.setFont(font.deriveFont(attributes));
                lblExam.setForeground(Color.BLUE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                Font font = lblExam.getFont();
                Map attributes = font.getAttributes();
                attributes.put(TextAttribute.UNDERLINE, -1);
                lblExam.setFont(font.deriveFont(attributes));
                lblExam.setForeground(Color.BLACK);
            }
        });
        panelleft.add(lblExam);

        // Add the text Questions to the panelleft panel
        JLabel lblQuestions = new JLabel("Questions");
        lblQuestions.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblQuestions.setBounds(80, 200, 100, 30);
        panelleft.add(lblQuestions);
        // set black colour to the text
        lblQuestions.setForeground(Color.BLACK);
        lblQuestions.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                Font font = lblQuestions.getFont();
                Map attributes = font.getAttributes();
                attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
                lblQuestions.setFont(font.deriveFont(attributes));
                lblQuestions.setForeground(Color.BLUE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                Font font = lblQuestions.getFont();
                Map attributes = font.getAttributes();
                attributes.put(TextAttribute.UNDERLINE, -1);
                lblQuestions.setFont(font.deriveFont(attributes));
                lblQuestions.setForeground(Color.BLACK);
            }
        });

        JLabel lblReports = new JLabel("Reports");
        lblReports.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblReports.setBounds(80, 250, 100, 30);
        panelleft.add(lblReports);
        // set black colour to the text
        lblReports.setForeground(Color.BLACK);
        lblReports.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                Font font = lblReports.getFont();
                Map attributes = font.getAttributes();
                attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
                lblReports.setFont(font.deriveFont(attributes));
                lblReports.setForeground(Color.BLUE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                Font font = lblReports.getFont();
                Map attributes = font.getAttributes();
                attributes.put(TextAttribute.UNDERLINE, -1);
                lblReports.setFont(font.deriveFont(attributes));
                lblReports.setForeground(Color.BLACK);
            }
        });
        panelleft.add(lblReports);

        // Add the text Logout to the panelleft panel blow the ChangePassword text
        JLabel lblLogout = new JLabel("Logout");
        lblLogout.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblLogout.setBounds(80, 450, 100, 30);
        lblLogout.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblLogout.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                Font font = lblLogout.getFont();
                Map attributes = font.getAttributes();
                attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
                lblLogout.setFont(font.deriveFont(attributes));
                lblLogout.setForeground(Color.BLUE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                Font font = lblLogout.getFont();
                Map attributes = font.getAttributes();
                attributes.put(TextAttribute.UNDERLINE, -1);
                lblLogout.setFont(font.deriveFont(attributes));
                lblLogout.setForeground(Color.BLACK);
            }
        });
        panelleft.add(lblLogout);
        // When the user clicks on the logout text, the user is logged out and the login page is displayed
        lblLogout.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String message = "<html><body style='width: 300px; font-size: 14px;'>" +
                        "<p style='margin-top: 10px;'>Logging out will <span style='color: red;'>end the current session</span> assigned.</p>" +
                        "<p style='margin-bottom: 10px;'><br>Are you sure?</p>" +
                        "</body></html>";
                int option = JOptionPane.showConfirmDialog(getContentPane(), message, "Application Message", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (option == JOptionPane.YES_OPTION) {
                    nullifySessionID(gotUserID);
                    UpdateLoginActivity.updateLoginActivity(Integer.parseInt(gotUserID));
                    dispose();
                    LoginPage.main(null);
                }
            }
        });
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

        // Display the Welcome text for the admin.
        addWelcomeMessage(panelright);

        // A panel inside the panelright panel to display the content
        JPanel panelright1 = new JPanel();
        panelright1.setBounds(65, 95, 300, 100);
        // Set colour to panel background to  yellow
        panelright1.setBackground(Color.YELLOW);
        panelright1.setBorder(new LineBorder(Color.BLACK, 2));
        panelright1.setLayout(null);
        panelright1.setVisible(false);
        panelright.add(panelright1);

        // A panel inside the panelright panel on the right side to panelright1 panel to display the content
        JPanel panelright2 = new JPanel();
        panelright2.setBounds(425, 95, 300, 100);
        // Set colour to panel background to  yellow
        panelright2.setBackground(Color.YELLOW);
        panelright2.setBorder(new LineBorder(Color.BLACK, 2));
        panelright2.setLayout(null);
        panelright2.setVisible(false);
        panelright.add(panelright2);

        // A panel inside the panelright panel on the right side to panelright2 panel to display the content
        JPanel panelright3 = new JPanel();
        panelright3.setBounds(790, 95, 300, 100);
        // Set colour to panel background to  yellow
        panelright3.setBackground(Color.YELLOW);
        panelright3.setBorder(new LineBorder(Color.BLACK, 2));
        panelright3.setLayout(null);
        panelright3.setVisible(false);
        panelright.add(panelright3);

        // A panel inside the panel right panel to display the required details when the user clicks on the sidebar options
        JPanel panelright4 = new JPanel();
        panelright4.setBounds(65, 120, 1025, 350);
        // Set colour to panel background to  yellow
        panelright4.setBackground(Color.YELLOW);
        panelright4.setBorder(new LineBorder(Color.BLACK, 2));
        panelright4.setLayout(null);
        panelright4.setVisible(false);
        panelright.add(panelright4);

        // Performs an action when the user clicks on the Users text
        addMouseListenerToLblExams(lblExam, panelright1, panelright2, panelright3, panelright4, panelright);
        addMouseListenerToQuestions(lblQuestions, panelright1, panelright2, panelright3, panelright4, panelright);

        // import the addTimeFooter method from the DateTimePanel class
        DateTimePanel dateTimePanel = new DateTimePanel();
        dateTimePanel.addTimeFooter(contentPane);
        dateTimePanel.setVisible(true);

    }

    public void addExamDetailsPanel(JPanel panel) {
        // Create and add the "Exam Name" label and text field
        JLabel lblExamName = new JLabel("Exam Name");
        lblExamName.setFont(new Font("Tahoma", Font.BOLD, 18));
        lblExamName.setBounds(50, 50, 125, 30);
        panel.add(lblExamName);
        lblExamName.setForeground(Color.BLACK);

        JTextField txtExamName = new JTextField();
        txtExamName.setFont(new Font("Tahoma", Font.PLAIN, 20));
        txtExamName.setBounds(175, 50, 200, 30);
        panel.add(txtExamName);
        txtExamName.setForeground(Color.BLACK);

        // Create and add the "Exam Date" label and date picker
        JLabel lblExamDate = new JLabel("Exam Date:");
        lblExamDate.setBounds(50, 100, 150, 30);
        lblExamDate.setFont(new Font("Tahoma", Font.BOLD, 18));
        panel.add(lblExamDate);
        lblExamDate.setForeground(Color.BLACK);

        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePickerImpl datePicker = new JDatePickerImpl(new JDatePanelImpl(model, p), new DateLabelFormatter());
        datePicker.setBounds(175, 100, 200, 30);
        panel.add(datePicker);
        datePicker.setVisible(true);

        // Create and add the "Duration" label and spinner
        JLabel lblDuration = new JLabel("Duration:");
        lblDuration.setFont(new Font("Tahoma", Font.BOLD, 18));
        lblDuration.setBounds(50, 150, 100, 30);
        panel.add(lblDuration);
        lblDuration.setForeground(Color.BLACK);

        SpinnerNumberModel minutes = new SpinnerNumberModel(15, 15, 180, 15);
        JSpinner spinner = new JSpinner(minutes);
        spinner.setBounds(175, 150, 50, 30);
        JFormattedTextField txt = ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField();
        txt.setEditable(false);
        panel.add(spinner);
        spinner.setVisible(true);

        JLabel lblMinutes = new JLabel("minutes");
        lblMinutes.setFont(new Font("Tahoma", Font.BOLD, 18));
        lblMinutes.setBounds(230, 150, 100, 30);
        panel.add(lblMinutes);
        lblMinutes.setForeground(Color.BLACK);

        // Create and add the "Total Marks" label and text field
        JLabel lblTotalMarks = new JLabel("Total Marks:");
        lblTotalMarks.setFont(new Font("Tahoma", Font.BOLD, 18));
        lblTotalMarks.setBounds(50, 200, 125, 30);
        panel.add(lblTotalMarks);
        lblTotalMarks.setForeground(Color.BLACK);

        JTextField txtTotalMarks = new JTextField();
        txtTotalMarks.setFont(new Font("Tahoma", Font.PLAIN, 20));
        txtTotalMarks.setBounds(175, 200, 50, 30);
        panel.add(txtTotalMarks);
        txtTotalMarks.setForeground(Color.BLACK);

        // Create and add the "Schedule Exam" button
        JButton btnSubmit = new JButton("Schedule Exam");
        btnSubmit.setFont(new Font("Tahoma", Font.BOLD, 18));
        btnSubmit.setBounds(175, 300, 175, 30);
        panel.add(btnSubmit);
        btnSubmit.setForeground(Color.BLACK);
        btnSubmit.setBackground(Color.WHITE);

        // Add the action listener to the button
        btnSubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String examName = txtExamName.getText();
                String examDate = (datePicker.getJFormattedTextField().getText());
                String duration = spinner.getValue().toString();
                String totalMarks = txtTotalMarks.getText();


                if(examName.isEmpty() || examDate.isEmpty() || duration.isEmpty() || totalMarks.isEmpty())
                {
                    JOptionPane.showMessageDialog(btnSubmit, "Please fill all the fields!");
                    // focus on the first empty field
                    if(examName.isEmpty()) {
                        txtExamName.requestFocus();
                    } else if(examDate.isEmpty()) {
                        datePicker.requestFocus();
                    } else if(duration.isEmpty()) {
                        spinner.requestFocus();
                    } else {
                        txtTotalMarks.requestFocus();
                    }
                }
                else if(!examName.matches("[a-zA-Z ]+")) {
                    JOptionPane.showMessageDialog(btnSubmit, "Exam name should contain only alphabets and spaces!");
                    txtExamName.requestFocus();
                }
                else if(!totalMarks.matches("[0-9]+")) {
                    JOptionPane.showMessageDialog(btnSubmit, "Total marks should contain only numbers!");
                    txtTotalMarks.requestFocus();
                }
                else if(Integer.parseInt(totalMarks) < 1 || Integer.parseInt(totalMarks) > 100) {
                    JOptionPane.showMessageDialog(btnSubmit, "Total marks should be between 1 and 100!");
                    txtTotalMarks.requestFocus();
                }
                else
                {
                   if(uploadExamDetails(examName, examDate, duration, totalMarks)) {
                       JOptionPane.showMessageDialog(btnSubmit, "Exam details saved successfully! Now you can add questions to the exam by clicking on the 'Questions' tab on the left.", "Success", JOptionPane.INFORMATION_MESSAGE);
                       // clear the text fields
                       txtExamName.setText("");
                       datePicker.getJFormattedTextField().setText("");
                       spinner.setValue(15);
                       txtTotalMarks.setText("");
                   } else {
                       JOptionPane.showMessageDialog(btnSubmit, "Error saving exam details! Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                   }
                }
            }
        });
    }

    private boolean uploadExamDetails(String examName, String examDate, String duration, String totalMarks) {

        String sql = "INSERT INTO THINKWAVE.EXAM_DETAILS (exam_id, faculty_id, exam_name, exam_date, duration, total_marks, is_published) VALUES ";
        sql += "((SELECT COALESCE(MAX(exam_id), 0) + 1 FROM THINKWAVE.EXAM_DETAILS), ?, ?, ?, ?, ?, ?)";

        String facultyID = gotUserID;

            // insert the exam details into the database table
            try {
                Connection conn = ConnectionDB.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, facultyID);
                pstmt.setString(2, examName);
                pstmt.setString(3, examDate);
                pstmt.setString(4, duration);
                pstmt.setString(5, totalMarks);
                pstmt.setBoolean(6, false);
                pstmt.executeUpdate();
                conn.close();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
    }

    private void addMouseListenerToLblExams(JLabel lblExam, JPanel panelright1, JPanel panelright2, JPanel panelright3, JPanel panelright4, JPanel panelright) {
        lblExam.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                panelright1.setVisible(false);
                panelright2.setVisible(false);
                panelright3.setVisible(false);
                panelright4.setVisible(false);

                // Reset content of panels
                removeWelcomeMessage(panelright);
                // removeTable(panelright4);
                panelright1.removeAll();
                panelright2.removeAll();
                panelright3.removeAll();
                panelright4.removeAll();

                panelright1.setBackground(Color.YELLOW);
                panelright1.setBorder(new LineBorder(Color.BLACK, 2));
                panelright1.setLayout(null);
                panelright1.setVisible(true);

                panelright2.setVisible(true);
                panelright2.setBackground(Color.YELLOW);
                panelright2.setBorder(new LineBorder(Color.BLACK, 2));
                panelright2.setLayout(null);

                // Add image to panelright1 panel
                try {
                    JLabel imgAddExam = new JLabel(new ImageIcon(ImageIO.read(new File("img/Faculty/exam.png"))));
                    imgAddExam.setBounds(20, 10, 70, 80);
                    panelright1.add(imgAddExam);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                JLabel lblAddExam = new JLabel("Add Exam Details");
                lblAddExam.setFont(new Font("Tahoma", Font.BOLD, 18));
                lblAddExam.setBounds(105, 30, 160, 30);
                panelright1.add(lblAddExam);
                lblAddExam.setForeground(Color.BLACK);

                // Add image to panelright2 panel
                try {
                    JLabel imgEditExam = new JLabel(new ImageIcon(ImageIO.read(new File("img/Faculty/test.png"))));
                    imgEditExam.setBounds(20, 10, 70, 80);
                    panelright2.add(imgEditExam);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                JLabel lblEditExam = new JLabel("Edit Exam Details");
                lblEditExam.setFont(new Font("Tahoma", Font.BOLD, 18));
                lblEditExam.setBounds(105, 30, 220, 30);
                panelright2.add(lblEditExam);
                lblEditExam.setForeground(Color.BLACK);


                // if lblAddExam is clicked
                lblAddExam.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        panelright1.setVisible(false);
                        panelright2.setVisible(false);
                        panelright3.setVisible(false);
                        panelright4.setVisible(false);

                       /* // Reset content of panels
                        removeWelcomeMessage(panelright);
                        // removeTable(panelright4);*/
                        panelright1.removeAll();
                        panelright2.removeAll();
                        panelright3.removeAll();
                        panelright4.removeAll();

                        panelright4.setBackground(Color.YELLOW);
                        panelright4.setBorder(new LineBorder(Color.BLACK, 2));
                        panelright4.setLayout(null);
                        panelright4.setVisible(true);

                        addExamDetailsPanel(panelright4);

                    }
                });

                lblEditExam.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        panelright1.setVisible(false);
                        panelright2.setVisible(false);
                        panelright3.setVisible(false);
                        panelright4.setVisible(false);

                        // Reset content of panels
                        removeWelcomeMessage(panelright);
                        /*   removeTable(panelright4);*/
                        panelright1.removeAll();
                        panelright2.removeAll();
                        panelright3.removeAll();
                        panelright4.removeAll();

                        panelright4.setBackground(Color.YELLOW);
                        panelright4.setBorder(new LineBorder(Color.BLACK, 2));
                        panelright4.setLayout(null);
                        panelright4.setVisible(true);


                        JLabel lblExamName = new JLabel("Exam Name");
                        lblExamName.setFont(new Font("Tahoma", Font.BOLD, 18));
                        lblExamName.setBounds(50, 50, 125, 30);
                        panelright4.add(lblExamName);
                        lblExamName.setForeground(Color.BLACK);

                        JTextField txtExamName = new JTextField();
                        txtExamName.setFont(new Font("Tahoma", Font.PLAIN, 20));
                        txtExamName.setBounds(175, 50, 200, 30);
                        panelright4.add(txtExamName);
                        txtExamName.setForeground(Color.BLACK);
                        // txtExamName shoudl accept only spaces and alphabets
                        txtExamName.addKeyListener(new KeyAdapter() {
                            @Override
                            public void keyTyped(KeyEvent e) {
                                char c = e.getKeyChar();
                                if (!(Character.isAlphabetic(c) || (c == KeyEvent.VK_BACK_SPACE) || (c == KeyEvent.VK_DELETE) || (c == KeyEvent.VK_SPACE))) {
                                    e.consume();
                                }
                            }
                        });


                        // A button next to txtExamName to search for the exam
                        JButton btnSearch = new JButton("Search");
                        btnSearch.setFont(new Font("Tahoma", Font.BOLD, 20));
                        btnSearch.setBounds(400, 50, 125, 30);
                        panelright4.add(btnSearch);
                        btnSearch.setForeground(Color.BLACK);

                        btnSearch.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                String examName = txtExamName.getText();
                                if (examName.isEmpty()) {
                                    JOptionPane.showMessageDialog(btnSearch, "Please enter the exam name!", "Error", JOptionPane.ERROR_MESSAGE);
                                    txtExamName.requestFocus();
                                } else {
                                    // Check if exam exists
                                    if (examExists(examName,gotUserID)) {
                                        // If exam exists, display the exam details
                                        panelright4.removeAll();
                                        panelright4.setBackground(Color.YELLOW);
                                        panelright4.setBorder(new LineBorder(Color.BLACK, 2));
                                        panelright4.setLayout(null);
                                        panelright4.setVisible(true);

                                        // Create and add the "Exam Name" label and text field
                                        JLabel lblExamName = new JLabel("Exam Name");
                                        lblExamName.setFont(new Font("Tahoma", Font.BOLD, 18));
                                        lblExamName.setBounds(50, 50, 125, 30);
                                        panelright4.add(lblExamName);
                                        lblExamName.setForeground(Color.BLACK);

                                        JTextField txtExamName = new JTextField();
                                        txtExamName.setFont(new Font("Tahoma", Font.PLAIN, 20));
                                        txtExamName.setBounds(175, 50, 200, 30);
                                        panelright4.add(txtExamName);
                                        txtExamName.setForeground(Color.BLACK);

                                        // Create and add the "Exam Date" label and date picker
                                        JLabel lblExamDate = new JLabel("Exam Date:");
                                        lblExamDate.setBounds(50, 100, 150, 30);
                                        lblExamDate.setFont(new Font("Tahoma", Font.BOLD, 18));
                                        panelright4.add(lblExamDate);
                                        lblExamDate.setForeground(Color.BLACK);

                                        UtilDateModel model = new UtilDateModel();
                                        Properties p = new Properties();
                                        p.put("text.today", "Today");
                                        p.put("text.month", "Month");
                                        p.put("text.year", "Year");
                                        JDatePickerImpl datePicker = new JDatePickerImpl(new JDatePanelImpl(model, p), new DateLabelFormatter());
                                        datePicker.setBounds(175, 100, 200, 30);
                                        panelright4.add(datePicker);
                                        datePicker.setVisible(true);

                                        // Create and add the "Duration" label and spinner
                                        JLabel lblDuration = new JLabel("Duration:");
                                        lblDuration.setFont(new Font("Tahoma", Font.BOLD, 18));
                                        lblDuration.setBounds(50, 150, 100, 30);
                                        panelright4.add(lblDuration);
                                        lblDuration.setForeground(Color.BLACK);

                                        SpinnerNumberModel minutes = new SpinnerNumberModel(15, 15, 180, 15);
                                        JSpinner spinner = new JSpinner(minutes);
                                        spinner.setBounds(175, 150, 50, 30);
                                        JFormattedTextField txt = ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField();
                                        txt.setEditable(false);
                                        panelright4.add(spinner);
                                        spinner.setVisible(true);

                                        JLabel lblMinutes = new JLabel("minutes");
                                        lblMinutes.setFont(new Font("Tahoma", Font.BOLD, 18));
                                        lblMinutes.setBounds(230, 150, 100, 30);
                                        panelright4.add(lblMinutes);
                                        lblMinutes.setForeground(Color.BLACK);

                                        // Create and add the "Total Marks" label and text field
                                        JLabel lblTotalMarks = new JLabel("Total Marks:");
                                        lblTotalMarks.setFont(new Font("Tahoma", Font.BOLD, 18));
                                        lblTotalMarks.setBounds(50, 200, 125, 30);
                                        panelright4.add(lblTotalMarks);
                                        lblTotalMarks.setForeground(Color.BLACK);

                                        JTextField txtTotalMarks = new JTextField();
                                        txtTotalMarks.setFont(new Font("Tahoma", Font.PLAIN, 20));
                                        txtTotalMarks.setBounds(175, 200, 50, 30);
                                        panelright4.add(txtTotalMarks);
                                        txtTotalMarks.setForeground(Color.BLACK);

                                        // Create and add the "Schedule Exam" button
                                        JButton btnUpdateExam = new JButton("Update Exam");
                                        btnUpdateExam.setFont(new Font("Tahoma", Font.BOLD, 20));
                                        btnUpdateExam.setBounds(175, 250, 200, 30);
                                        panelright4.add(btnUpdateExam);
                                        btnUpdateExam.setForeground(Color.BLACK);


                                        String[] examDetails = getExamDetails(examName,gotUserID);
                                        txtExamName.setText(examDetails[0]);
                                        txtExamName.setEditable(true);

                                        datePicker.getJFormattedTextField().setText(examDetails[1]);
                                        datePicker.getJFormattedTextField().setEditable(true);

                                        spinner.setValue(Integer.parseInt(examDetails[2]));
                                        spinner.setEnabled(true);

                                        txtTotalMarks.setText(examDetails[3]);
                                        txtTotalMarks.setEditable(true);

                                        btnUpdateExam.addActionListener( e1 ->
                                        {
                                            String new_examName = txtExamName.getText();
                                            String new_examDate = datePicker.getJFormattedTextField().getText();
                                            String new_duration = spinner.getValue().toString();
                                            String new_totalMarks = txtTotalMarks.getText();
                                            String facultyID = gotUserID;
                                            String sql = "UPDATE THINKWAVE.EXAM_DETAILS SET exam_name = ?, exam_date = ?, duration = ?, total_marks = ? WHERE exam_id = ? AND faculty_id = ?";

                                            int confirmDialogResult = JOptionPane.showConfirmDialog(btnUpdateExam,
                                                    "Are you sure you want to update the following details?\n\n"
                                                            + "Exam Name: " + (new_examName.equals(examDetails[0]) ? examDetails[0] : new_examName) + "\n"
                                                            + "Exam Date: " + (new_examDate.equals(examDetails[1]) ? examDetails[1] : new_examDate) + "\n"
                                                            + "Duration: " + (new_duration.equals(examDetails[2]) ? examDetails[2] : new_duration) + "\n"
                                                            + "Total Marks: " + (new_totalMarks.equals(examDetails[3]) ? examDetails[3] : new_totalMarks) + "\n\n"
                                                            + "Click Yes to confirm or No to cancel.", "Confirm Update", JOptionPane.YES_NO_OPTION);

                                            if (confirmDialogResult == JOptionPane.YES_OPTION) {
                                                // user clicked Yes, update the exam details in the database
                                                try {
                                                    Connection conn = ConnectionDB.connect();
                                                    PreparedStatement pstmt = conn.prepareStatement(sql);

                                                    pstmt.setString(1, new_examName.equals(examDetails[0]) ? examDetails[0] : new_examName);
                                                    pstmt.setString(2, new_examDate.equals(examDetails[1]) ? examDetails[1] : new_examDate);
                                                    pstmt.setString(3, new_duration.equals(examDetails[2]) ? examDetails[2] : new_duration);
                                                    pstmt.setString(4, new_totalMarks.equals(examDetails[3]) ? examDetails[3] : new_totalMarks);
                                                    pstmt.setInt(5, Integer.parseInt(examDetails[4]));
                                                    pstmt.setString(6, facultyID);

                                                    int result = pstmt.executeUpdate();
                                                    if (result > 0) {
                                                        JOptionPane.showMessageDialog(btnUpdateExam, "Exam details for <" + examName + "> updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                                                    } else {
                                                        JOptionPane.showMessageDialog(btnUpdateExam, "Exam details for <" + examName + "> could not be updated!", "Error", JOptionPane.ERROR_MESSAGE);
                                                    }
                                                } catch (SQLException throwables) {
                                                    throwables.printStackTrace();
                                                }
                                            } else {
                                                // user clicked No or closed the dialog, do not update the exam details
                                                JOptionPane.showMessageDialog(btnUpdateExam, "Update cancelled by user.", "Cancelled", JOptionPane.INFORMATION_MESSAGE);
                                            }

                                        });




                                    } else {
                                        JOptionPane.showMessageDialog(btnSearch, "You haven't added the exam <" + examName + "> yet! Retry Again or Add the exam!", "Error", JOptionPane.ERROR_MESSAGE);
                                        txtExamName.requestFocus();
                                    }
                                }
                            }
                        });



                    }
                });
            }
        });
            }


    private void addMouseListenerToQuestions(JLabel lblQuestions, JPanel panelright1, JPanel panelright2, JPanel panelright3, JPanel panelright4, JPanel panelright) {
                lblQuestions.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        panelright1.setVisible(false);
                        panelright2.setVisible(false);
                        panelright3.setVisible(false);
                        panelright4.setVisible(false);

                        // Reset content of panels
                        removeWelcomeMessage(panelright);
                        /*   removeTable(panelright4);*/
                        panelright1.removeAll();
                        panelright2.removeAll();
                        panelright3.removeAll();
                        panelright4.removeAll();

                        panelright1.setBackground(Color.YELLOW);
                        panelright1.setBorder(new LineBorder(Color.BLACK, 2));
                        panelright1.setLayout(null);
                        panelright1.setVisible(true);

                        panelright2.setVisible(true);
                        panelright2.setBackground(Color.YELLOW);
                        panelright2.setBorder(new LineBorder(Color.BLACK, 2));
                        panelright2.setLayout(null);

                        panelright3.setVisible(true);
                        panelright3.setBackground(Color.YELLOW);
                        panelright3.setBorder(new LineBorder(Color.BLACK, 2));
                        panelright3.setLayout(null);


                        // Add image to panelright1 panel
                        try {
                            JLabel imgAddQuestion = new JLabel(new ImageIcon(ImageIO.read(new File("img/Faculty/question.png"))));
                            imgAddQuestion.setBounds(20, 10, 70, 80);
                            panelright1.add(imgAddQuestion);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                        JLabel lblAddQuestion = new JLabel("Add Question");
                        lblAddQuestion.setFont(new Font("Tahoma", Font.BOLD, 18));
                        lblAddQuestion.setBounds(105, 30, 160, 30);
                        panelright1.add(lblAddQuestion);
                        lblAddQuestion.setForeground(Color.BLACK);

                        // Add image to panelright2 panel
                        try {
                            JLabel imgEditQuestion = new JLabel(new ImageIcon(ImageIO.read(new File("img/Faculty/edit.png"))));
                            imgEditQuestion.setBounds(20, 10, 70, 80);
                            panelright2.add(imgEditQuestion);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                        JLabel lblEditQuestion = new JLabel("Edit Question");
                        lblEditQuestion.setFont(new Font("Tahoma", Font.BOLD, 18));
                        lblEditQuestion.setBounds(105, 30, 220, 30);
                        panelright2.add(lblEditQuestion);
                        lblEditQuestion.setForeground(Color.BLACK);

                        try {
                            JLabel imgDeleteQuestion = new JLabel(new ImageIcon(ImageIO.read(new File("img/Faculty/delete.png"))));
                            imgDeleteQuestion.setBounds(20, 10, 70, 80);
                            panelright3.add(imgDeleteQuestion);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }

                        JLabel lblDeleteQuestion = new JLabel("Delete Question");
                        lblDeleteQuestion.setFont(new Font("Tahoma", Font.BOLD, 18));
                        lblDeleteQuestion.setBounds(105, 30, 220, 30);
                        panelright3.add(lblDeleteQuestion);
                        lblDeleteQuestion.setForeground(Color.BLACK);

                    }
                });
            }

            private void addMouseListenerToReports(JLabel lblReports, JPanel panelright1, JPanel panelright2, JPanel panelright3, JPanel panelright4, JPanel panelright) {
                lblReports.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        panelright1.setVisible(false);
                        panelright2.setVisible(false);
                        panelright3.setVisible(false);
                        panelright4.setVisible(false);

                        // Reset content of panels
                        removeWelcomeMessage(panelright);
                        /*   removeTable(panelright4);*/
                        panelright1.removeAll();
                        panelright2.removeAll();
                        panelright3.removeAll();
                        panelright4.removeAll();

                        panelright4.setBackground(Color.GRAY);
                        panelright4.setBorder(new LineBorder(Color.BLACK, 2));
                        panelright4.setLayout(null);
                        panelright4.setVisible(true);

                /*generatePieChart();
                generateBarChart();*/

                        // Add a image to the panelright4 panel
                        // Add image to panelright1 panel
                        try {
                            JLabel imgInstStat = new JLabel(new ImageIcon(ImageIO.read(new File("img/Graphs/InstitutionStatistics.png"))));
                            imgInstStat.setBounds(10, 0, 500, 350);
                            panelright4.add(imgInstStat);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }

                        // Add a image to the panelright4 panel
                        // Add image to panelright1 panel
                        try {
                            JLabel imgUserGenderStat = new JLabel(new ImageIcon(ImageIO.read(new File("img/Graphs/UserGenderStatistics.png"))));
                            imgUserGenderStat.setBounds(520, 0, 500, 350);
                            panelright4.add(imgUserGenderStat);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                });
            }
    }


