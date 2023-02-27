package Admin;

import DatabaseFunctions.ConnectionDB;
import DatabaseFunctions.QuestionPaper.RetrieveCourseID;
import DatabaseFunctions.UpdateLoginActivity;
import DateTime.DateTimePanel;
import Mail.NewUserAddedNotifier;
import SecureHash.PassBasedEnc;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Properties;

import static RegexChecks.CheckEmail.isValidEmail;

public class AdminDashboard  extends JFrame
{
    File selectedFile;
    final static Connection connection;

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
                AdminDashboard frame = new AdminDashboard();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void addMouseListenerToLblStudents(JLabel lblStudents, JPanel panelright1, JPanel panelright2, JPanel panelright3, JPanel panelright4) {
        lblStudents.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                panelright1.setVisible(true);
                panelright2.setVisible(true);
                panelright3.setVisible(true);
                panelright4.setVisible(false);

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
                // Add a mouse listener to the lblAddStudent label if pressed,
                lblAddStudent.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        panelright1.setVisible(false);
                        panelright2.setVisible(false);
                        panelright3.setVisible(false);
                        panelright4.setVisible(true);
                        panelright1.removeAll();
                        panelright2.removeAll();
                        panelright3.removeAll();
                        panelright4.removeAll();



                        // Add a label to the panelright4 panel with the text "User ID"
                        JLabel lblUserID = new JLabel("User ID:");
                        lblUserID.setFont(new Font("Tahoma", Font.BOLD, 20));
                        lblUserID.setBounds(50, 50, 100, 30);
                        panelright4.add(lblUserID);
                        lblUserID.setForeground(Color.BLACK);
                        // Add a text field to the panelright4 panel with the text "User ID"
                        JTextField txtUserID = new JTextField();
                        txtUserID.setFont(new Font("Tahoma", Font.PLAIN, 20));
                        txtUserID.setBounds(150, 50, 200, 30);
                        panelright4.add(txtUserID);
                        txtUserID.setForeground(Color.BLACK);
                        // The text field must allow only 1-10000(for student), 10001-20000(for lecturer) and 20001-30000(for admin) numbers
                        txtUserID.addKeyListener(new KeyAdapter() {
                            @Override
                            public void keyTyped(KeyEvent e) {
                                char c = e.getKeyChar();
                                if (!((c >= '0') && (c <= '9') ||
                                        (c == KeyEvent.VK_BACK_SPACE) ||
                                        (c == KeyEvent.VK_DELETE))) {
                                    getToolkit().beep();
                                    e.consume();
                                }
                            }
                        });
                        // Totally only 5 digits are allowed
                        txtUserID.addKeyListener(new KeyAdapter() {
                            @Override
                            public void keyTyped(KeyEvent e) {
                                if (txtUserID.getText().length() >= 5 ) // limit textfield to 3 characters
                                    e.consume();
                            }
                        });

                        // Add a label to the panelright4 panel with the text "Institution ID:" below the "User ID" label
                        JLabel lblInstitutionID = new JLabel("Institution ID:");
                        lblInstitutionID.setFont(new Font("Tahoma", Font.BOLD, 20));
                        lblInstitutionID.setBounds(50, 100, 150, 30);
                        panelright4.add(lblInstitutionID);
                        lblInstitutionID.setForeground(Color.BLACK);
                        // Add a text field to the panelright4 panel with the text "Institution ID:" below the "User ID" text field
                        JTextField txtInstitutionID = new JTextField();
                        txtInstitutionID.setFont(new Font("Tahoma", Font.PLAIN, 20));
                        txtInstitutionID.setBounds(200, 100, 150, 30);
                        panelright4.add(txtInstitutionID);
                        txtInstitutionID.setForeground(Color.BLACK);
                        txtInstitutionID.addKeyListener(new KeyAdapter() {
                            @Override
                            public void keyTyped(KeyEvent e) {
                                char c = e.getKeyChar();
                                if (txtInstitutionID.getText().length() >= 5 || (txtInstitutionID.getText().length() == 0 && c != 'I')) {
                                    e.consume(); // ignore input if already 5 characters or first character is not "I"
                                } else if (txtInstitutionID.getText().length() == 1 && c != 'N') {
                                    e.consume(); // ignore input if second character is not "N"
                                } else if (txtInstitutionID.getText().length() == 2 && c != 'S') {
                                    e.consume(); // ignore input if third character is not "S"
                                } else if (txtInstitutionID.getText().length() >= 3 && !Character.isDigit(c)) {
                                    e.consume(); // ignore input if last two characters are not digits
                                }
                            }
                        });

                        // Add a label to the panelright4 panel with the text "Name:" below the "Institution ID" label
                        JLabel lblName = new JLabel("Name:");
                        lblName.setFont(new Font("Tahoma", Font.BOLD, 20));
                        lblName.setBounds(50, 150, 100, 30);
                        panelright4.add(lblName);
                        lblName.setForeground(Color.BLACK);
                        // Add a text field to the panelright4 panel with the text "Name:" below the "Institution ID" text field
                        JTextField txtName = new JTextField();
                        txtName.setFont(new Font("Tahoma", Font.PLAIN, 20));
                        txtName.setBounds(200, 150, 150, 30);
                        panelright4.add(txtName);
                        txtName.setForeground(Color.BLACK);


                        // Add a label to the panelright4 panel with the text "Mobile Number:" below the "Institution ID" label
                        JLabel lblMobileNumber = new JLabel("Mobile Number:");
                        lblMobileNumber.setFont(new Font("Tahoma", Font.BOLD, 16));
                        lblMobileNumber.setBounds(50, 200, 150, 30);
                        panelright4.add(lblMobileNumber);
                        lblMobileNumber.setForeground(Color.BLACK);
                        // Add a text field to the panelright4 panel with the text "Mobile Number:" below the "Institution ID" text field
                        JTextField txtMobileNumber = new JTextField();
                        txtMobileNumber.setFont(new Font("Tahoma", Font.PLAIN, 20));
                        txtMobileNumber.setBounds(200, 200, 150, 30);
                        panelright4.add(txtMobileNumber);
                        txtMobileNumber.setForeground(Color.BLACK);

                        // This code checks that the mobile number starts with "7", "8", or "9", and is exactly 10 digits long.
                        // It also ensures that all characters are digits.
                        // If the user tries to enter a character that violates any of these conditions, the event is consumed and the input is ignored.
                        txtMobileNumber.addKeyListener(new KeyAdapter() {
                            @Override
                            public void keyTyped(KeyEvent e) {
                                char c = e.getKeyChar();
                                if (txtMobileNumber.getText().length() >= 10 || (txtMobileNumber.getText().length() == 0 && c != '7' && c != '8' && c != '9')) {
                                    e.consume(); // ignore input if already 10 characters or first character is not "7", "8", or "9"
                                } else if (txtMobileNumber.getText().length() >= 1 && !Character.isDigit(c)) {
                                    e.consume(); // ignore input if last nine characters are not digits
                                }
                            }
                        });

                        // Add a label to the panelright4 panel with the text "Email:" below the "Mobile Number" label
                        JLabel lblEmail = new JLabel("Email:");
                        lblEmail.setFont(new Font("Tahoma", Font.BOLD, 20));
                        lblEmail.setBounds(50, 250, 150, 30);
                        panelright4.add(lblEmail);
                        lblEmail.setForeground(Color.BLACK);
                        // Add a text field to the panelright4 panel with the text "Email:" below the "Mobile Number" text field
                        JTextField txtEmail = new JTextField();
                        txtEmail.setFont(new Font("Tahoma", Font.PLAIN, 20));
                        txtEmail.setBounds(200, 250, 150, 30);
                        panelright4.add(txtEmail);
                        txtEmail.setForeground(Color.BLACK);
                       // Add a label with the text "Address" to the panelright4 panel  below the "Email" label
                        JLabel lblAddress = new JLabel("Address:");
                        lblAddress.setFont(new Font("Tahoma", Font.BOLD, 20));
                        lblAddress.setBounds(450, 50, 150, 30);
                        panelright4.add(lblAddress);
                        lblAddress.setForeground(Color.BLACK);
                        // Add a text area for the address to the panelright4 panel below the "Email" text field
                        JTextArea txtAddress = new JTextArea();
                        txtAddress.setFont(new Font("Tahoma", Font.PLAIN, 16));
                        txtAddress.setBounds(550, 50, 380, 175);
                        panelright4.add(txtAddress);
                        txtAddress.setForeground(Color.BLACK);

                        // Add a label with the text "Gender" to the panelright4 panel  below the "Address" label
                        JLabel lblGender = new JLabel("Gender:");
                        lblGender.setFont(new Font("Tahoma", Font.BOLD, 20));
                        lblGender.setBounds(50, 300, 150, 30);
                        panelright4.add(lblGender);
                        lblGender.setForeground(Color.BLACK);
                        JComboBox<String> cmbGender = new JComboBox<>();
                        cmbGender.setFont(new Font("Tahoma", Font.PLAIN, 20));
                        cmbGender.setBounds(200, 300, 150, 30);
                        panelright4.add(cmbGender);
                        cmbGender.setForeground(Color.BLACK);
                        cmbGender.addItem("Choose");
                        cmbGender.addItem("Male");
                        cmbGender.addItem("Female");

                        // Add a button with the text "Submit" to the panelright4 panel  below the "Address" label
                        JButton btnSubmit = new JButton("Submit");
                        btnSubmit.setFont(new Font("Tahoma", Font.BOLD, 20));
                        btnSubmit.setBounds(500, 300, 150, 30);
                        panelright4.add(btnSubmit);
                        btnSubmit.setForeground(Color.BLACK);
                        btnSubmit.addActionListener(e1 -> {
                            // Get the values from the text fields and combo box and store them in variables
                            int userID = Integer.parseInt(txtUserID.getText());
                            String institutionID = txtInstitutionID.getText();
                            String name = txtName.getText();
                            String mobileNumber = txtMobileNumber.getText();
                            String email = txtEmail.getText();
                            String address = txtAddress.getText();
                            String gender = (String) cmbGender.getSelectedItem();
                            // Check if the gender is "Male", then set the gender as M and vice versa
                            if (Objects.equals(gender, "Male"))
                                gender = "M";
                            else if  (Objects.equals(gender, "Female"))
                                gender = "F";
                           else
                               JOptionPane.showMessageDialog(btnSubmit, "Please choose a gender!", "Error", JOptionPane.ERROR_MESSAGE);


                            // Check if the values are not empty
                              if (txtUserID.getText().isEmpty() || txtInstitutionID.getText().isEmpty() || txtName.getText().isEmpty() || txtMobileNumber.getText().isEmpty() || txtEmail.getText().isEmpty() || txtAddress.getText().isEmpty() || Objects.equals(cmbGender.getSelectedItem(), "Choose")) {
                                    JOptionPane.showMessageDialog(btnSubmit, "Please fill in all the fields!", "Error", JOptionPane.ERROR_MESSAGE);
                                }
                            // Check if the email is valid
                            else if (!isValidEmail(email)) {
                                JOptionPane.showMessageDialog(btnSubmit, "Please enter a valid email address!", "Error", JOptionPane.ERROR_MESSAGE);
                            }

                           else {
                                  // Add the student to the database
                                  String queryforDetails = "INSERT INTO THINKWAVE.USER_DETAILS (USER_ID, INSTID, NAME, MOBILENUM, EMAIL, ADDRESS, GENDER) VALUES (?, ?, ?, ?, ?, ?, ?)";
                                  String queryforAuth = "INSERT INTO THINKWAVE.USER_AUTHENTICATION (USER_ID, LAST_LOGIN, PASSW_HASH, PASSW_SALT, INST_ID) VALUES (?, ?, ?, ?, ?)";
                                  String Salt = PassBasedEnc.getSaltvalue(30);
                                  String password = email.split("@")[0];
                                  String hash = PassBasedEnc.generateSecurePassword(password, Salt);

                                  try (connection;
                                       PreparedStatement preparedStatement = connection.prepareStatement(queryforDetails)) {
                                      preparedStatement.setInt(1, userID);
                                      preparedStatement.setString(2, institutionID);
                                      preparedStatement.setString(3, name);
                                      preparedStatement.setString(4, mobileNumber);
                                      preparedStatement.setString(5, email);
                                      preparedStatement.setString(6, address);
                                      preparedStatement.setString(7, gender);
                                      preparedStatement.executeUpdate();
                                      JOptionPane.showMessageDialog(btnSubmit, "User added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                                      txtUserID.setText("");
                                      txtInstitutionID.setText("");
                                      txtName.setText("");
                                      txtMobileNumber.setText("");
                                      txtEmail.setText("");
                                      txtAddress.setText("");
                                      cmbGender.setSelectedIndex(0);
                                      connection.setAutoCommit(false);
                                      connection.commit(); // commit the transaction
                                      try (PreparedStatement preparedStatement2 = connection.prepareStatement(queryforAuth)) {
                                          preparedStatement2.setInt(1, userID);
                                          preparedStatement2.setString(2, UpdateLoginActivity.updateLoginActivity(userID));
                                          preparedStatement2.setString(3, hash);
                                          preparedStatement2.setString(4, Salt);
                                          preparedStatement2.setString(5, institutionID);
                                          NewUserAddedNotifier.sendnotification(email, String.valueOf(userID),password);
                                          preparedStatement2.executeUpdate();

                                      } catch (SQLException ignored) {
                                      } catch (IOException ex) {
                                          throw new RuntimeException(ex);
                                      }
                                  } catch (SQLException ignored) {
                                          }
                              }

                        });

                        // Add a button with the text "Reset" to the panelright4 panel  below the "Submit" button
                        JButton btnReset = new JButton("Reset");
                        btnReset.setFont(new Font("Tahoma", Font.BOLD, 20));
                        btnReset.setBounds(700, 300, 150, 30);
                        panelright4.add(btnReset);
                        btnReset.setForeground(Color.BLACK);
                        btnReset.addActionListener(e12 -> {
                            txtUserID.setText("");
                            txtInstitutionID.setText("");
                            txtName.setText("");
                            txtMobileNumber.setText("");
                            txtEmail.setText("");
                            txtAddress.setText("");
                            cmbGender.setSelectedIndex(0);

                        });

                    }
                });


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

    public void addMouseListenerToLblTeachers(JLabel lblTeachers, JPanel panelright1, JPanel panelright2, JPanel panelright3, JPanel panelright4) {
        lblTeachers.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                panelright1.setVisible(true);
                panelright2.setVisible(true);
                panelright3.setVisible(true);
                panelright4.setVisible(false);

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
    public void setupQuestionPapersPanel(JLabel lblQuestionPapers,  JPanel panelright1,  JPanel panelright2,  JPanel panelright3, JPanel panelright4, JPanel panelright) {
        lblQuestionPapers.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                panelright1.setVisible(false);
                panelright2.setVisible(false);
                panelright3.setVisible(false);
                panelright4.setVisible(true);
                panelright.setVisible(true);

                // Add a label with the text CourseCode: to the panelright4 panel
                JLabel lblCourseCode = new JLabel("Course Code:");
                lblCourseCode.setFont(new Font("Tahoma", Font.BOLD, 20));
                lblCourseCode.setBounds(10, 10, 150, 30);
                panelright4.add(lblCourseCode);
                lblCourseCode.setForeground(Color.BLACK);
                lblCourseCode.setVisible(true);
                // Add a text field to the panelright4 panel to enter the course code
                JTextField txtCourseCode = new JTextField();
                txtCourseCode.setBounds(150, 10, 100, 30);
                panelright4.add(txtCourseCode);
                txtCourseCode.setVisible(true);

                // Add a label with the text Semester: to the panelright4 panel below the course code label
                JLabel lblSemester = new JLabel("Semester:");
                lblSemester.setFont(new Font("Tahoma", Font.BOLD, 20));
                lblSemester.setBounds(10, 50, 150, 30);
                panelright4.add(lblSemester);
                lblSemester.setForeground(Color.BLACK);
                lblSemester.setVisible(true);
                // Add a text field to the panelright4 panel to enter the semester
                JTextField txtSemester = new JTextField();
                txtSemester.setBounds(150, 50, 100, 30);
                panelright4.add(txtSemester);
                txtSemester.setVisible(true);

                // Add a label with the text Year: to the panelright4 panel below the semester label
                JLabel lblYear = new JLabel("Year:");
                lblYear.setFont(new Font("Tahoma", Font.BOLD, 20));
                lblYear.setBounds(10, 90, 150, 30);
                panelright4.add(lblYear);
                lblYear.setForeground(Color.BLACK);
                lblYear.setVisible(true);

                // Add a drop down list to the panelright4 panel to enter the year with the options I, II, III, IV, Others
                String[] years = {"Choose","I", "II", "III", "IV", "Others"};
                JComboBox<String> cmbYear = new JComboBox<>(years);
                cmbYear.setBounds(150, 90, 100, 30);
                panelright4.add(cmbYear);
                cmbYear.setVisible(true);

                // Add a label with the textExam Name: to the panelright4 panel below the year label
                JLabel lblExamName = new JLabel("Exam Name:");
                lblExamName.setFont(new Font("Tahoma", Font.BOLD, 20));
                lblExamName.setBounds(10, 130, 150, 30);
                panelright4.add(lblExamName);
                lblExamName.setForeground(Color.BLACK);
                lblExamName.setVisible(true);
                // Add a text field to the panelright4 panel to enter the exam name
                JTextField txtExamName = new JTextField();
                txtExamName.setBounds(150, 130, 200, 30);
                panelright4.add(txtExamName);
                txtExamName.setVisible(true);

                // Add a label with the text "Exam Date:" to the panel
                JLabel lblExamDate = new JLabel("Exam Date:");
                lblExamDate.setBounds(10, 170, 150, 30);
                lblExamDate.setFont(new Font("Tahoma", Font.BOLD, 20));
                panelright4.add(lblExamDate);
                // Create JDateChooser to select the date
                UtilDateModel model = new UtilDateModel();
                Properties p = new Properties();
                p.put("text.today", "Today");
                p.put("text.month", "Month");
                p.put("text.year", "Year");
                JDatePickerImpl datePicker = new JDatePickerImpl(new JDatePanelImpl(model, p), new DateLabelFormatter());
                datePicker.setBounds(150, 170, 200, 30);
                panelright4.add(datePicker);
                datePicker.setVisible(true);

                // Add a label with the text "File Name:" to the panelright4 panel below the Exam Date label
                JLabel lblFileName = new JLabel("File Name:");
                lblFileName.setFont(new Font("Tahoma", Font.BOLD, 20));
                lblFileName.setBounds(10, 210, 150, 30);
                panelright4.add(lblFileName);
                lblFileName.setForeground(Color.BLACK);
                lblFileName.setVisible(true);
                // Add a text field to the panelright4 panel to enter the file name
                JTextField txtFileName = new JTextField();
                txtFileName.setBounds(150, 210, 200, 30);
                panelright4.add(txtFileName);
                txtFileName.setVisible(true);

                // Add a button to the panelright4 panel to upload the file near the file name text field
                JButton btnUpload = new JButton("Browse");
                btnUpload.setBounds(360, 210, 100, 30);
                panelright4.add(btnUpload);
                btnUpload.setVisible(true);
                btnUpload.addActionListener(e2 ->
                {
                    {
                        JFileChooser fileChooser = new JFileChooser();
                        fileChooser.setFileFilter(new FileNameExtensionFilter("PDF Files", "pdf"));
                        int result = fileChooser.showOpenDialog(panelright4);
                        if (result == JFileChooser.APPROVE_OPTION) {
                            selectedFile = fileChooser.getSelectedFile();
                            if (!selectedFile.getName().toLowerCase().endsWith(".pdf"))
                                JOptionPane.showMessageDialog(panelright4, "Please select a PDF file");
                             else
                                txtFileName.setText(selectedFile.getName().substring(selectedFile.getName().lastIndexOf("\\") + 1));
                        }
                    }
                });

                // Add a button with the text "Upload Question Paper" to the panelright4 panel below the file name label at center
                JButton btnUploadQuestionPaper = new JButton("Upload Question Paper");
                btnUploadQuestionPaper.setBounds(150, 250, 200, 30);
                panelright4.add(btnUploadQuestionPaper);

                btnUploadQuestionPaper.addActionListener(e2 ->
                {
                    {
                            // Get the input from the text fields and store them in variables
                            String insID = "INS01";
                            int courseID = RetrieveCourseID.getCourseID(txtCourseCode.getText());
                            int semester = Integer.parseInt(txtSemester.getText());
                            String year = Objects.requireNonNull(cmbYear.getSelectedItem()).toString();
                            String examName = txtExamName.getText();
                            String examDate = datePicker.getJFormattedTextField().getText();
                            String fileName = txtFileName.getText();

                        String dbUrl = "jdbc:sqlserver://thinkwaveappln.database.windows.net:1433;database=orcl;user=thinkwave@thinkwaveappln;password=Mepcocollege1@;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
                                String sql = "INSERT INTO THINKWAVE.QB_Question (InstID, CourseID, Semester, Year, ExamTitle, ExamDate, FileName, QuestionPaperPDF) VALUES (?, ?, ?, ?, ?, CONVERT(VARCHAR, ?, 3), ?, ?)";

                                try (Connection conn = DriverManager.getConnection(dbUrl);
                                     PreparedStatement pstmt = conn.prepareStatement(sql)) {
                                    pstmt.setString(1, insID);
                                    pstmt.setInt(2, courseID);
                                    pstmt.setInt(3, semester);
                                    pstmt.setString(4, year);
                                    pstmt.setString(5, examName);
                                    pstmt.setString(6, examDate);
                                    pstmt.setString(7, fileName);
                                    FileInputStream fis = new FileInputStream(selectedFile);
                                    pstmt.setBinaryStream(8, fis, (int) selectedFile.length());
                                    int rowsAffected = pstmt.executeUpdate();
                                    if (rowsAffected > 0) {
                                        txtCourseCode.setText("");
                                        txtSemester.setText("");
                                        cmbYear.setSelectedIndex(0);
                                        txtExamName.setText("");
                                        datePicker.getJFormattedTextField().setText("");
                                        txtFileName.setText("");
                                        JOptionPane.showMessageDialog(btnUploadQuestionPaper, "Question paper uploaded successfully!");
                                    }
                                } catch (SQLException | FileNotFoundException ex) {
                                    throw new RuntimeException(ex);
                                }
                            }
                });
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

        // A panel inside the panel right panel to display the required details when the user clicks on the sidebar options
        JPanel panelright4 = new JPanel();
        panelright4.setBounds(65, 120, 1025, 350);
        // Set colour to panel background to  yellow
        panelright4.setBackground(Color.YELLOW);
        panelright4.setBorder(new LineBorder(Color.BLACK, 2));
        panelright4.setLayout(null);
        panelright4.setVisible(false);
        panelright.add(panelright4);

        // Performs an action when the user clicks on the Students text
        addMouseListenerToLblStudents(lblStudents, panelright1, panelright2, panelright3, panelright4);
        // Performs an action when the user clicks on the Teachers text
        addMouseListenerToLblTeachers(lblTeachers, panelright1, panelright2, panelright3, panelright4);
        // Performs an action when the user clicks on the Question Papers text
        setupQuestionPapersPanel(lblQuestionPapers, panelright1, panelright2, panelright3, panelright4, panelright);



        // import the addTimeFooter method from the DateTimePanel class
        DateTimePanel dateTimePanel = new DateTimePanel();
        dateTimePanel.addTimeFooter(contentPane);
        dateTimePanel.setVisible(true);
    }

}
