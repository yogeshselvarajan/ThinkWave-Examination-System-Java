package Admin;

import DatabaseFunctions.ConnectionDB;
import DatabaseFunctions.QuestionPaper.RetrieveCourseID;
import DatabaseFunctions.RetrieveEmailID;
import DatabaseFunctions.UpdateLoginActivity;
import DateTime.DateTimePanel;
import Login.LoginPage;
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
import java.awt.event.*;
import java.awt.font.TextAttribute;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

import static DatabaseFunctions.UpdatePassword.updatePassword;
import static Mail.NewUserAddedNotifier.sendnotification;
import static Mail.PasswordChangeNotifier.sendPasswordChangeNotification;
import static RegexChecks.CheckEmail.isValidEmail;

public class AdminDashboard  extends JFrame
{
    File selectedFile;
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
                AdminDashboard frame = new AdminDashboard();
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
                if (label.getText().contains("Welcome to the ThinkWave MCQ Examination System Dashboard")) {
                    panelright.remove(label);
                    panelright.repaint();
                    break;
                }
            }
        }
    }

    private void removeTable(JPanel panel) {
        Component[] components = panel.getComponents();
        for (Component component : components) {
            if (component instanceof JScrollPane) {
                Component viewport = ((JScrollPane) component).getViewport().getView();
                if (viewport instanceof JTable) {
                    JTable table = (JTable) viewport;
                    if (table.getModel() instanceof UserTableModel) {
                        panel.remove(component);
                        break;
                    }
                }
            }
        }
        panel.revalidate();
        panel.repaint();
    }


    public void addMouseListenerToLblUsers(JLabel lblUsers, JPanel panelright1, JPanel panelright2, JPanel panelright3, JPanel panelright4,JPanel panelright){
        lblUsers.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                panelright1.setVisible(true);
                panelright2.setVisible(true);
                panelright3.setVisible(true);
                panelright4.setVisible(false);
                

                // Reset content of panels
                removeTable(panelright4);
                panelright1.removeAll();
                panelright2.removeAll();
                panelright3.removeAll();

                panelright4.removeAll();
                panelright4.revalidate();
                panelright4.repaint();

                panelright4.setBackground(Color.YELLOW);
                panelright4.setBorder(new LineBorder(Color.BLACK, 2));
                panelright4.setLayout(null);

                // Add the text for the top of the panelright1, panelright2 and panelright3 panels
                try{
                    JLabel imgAddBanner= new JLabel(new ImageIcon(ImageIO.read(new File("img/Admin/manage-users-details.png"))));
                    imgAddBanner.setBounds(215, 5, 500, 100);
                    panelright.add(imgAddBanner);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }


                // Add image to panelright1 panel
                try {
                    JLabel imgAddStudent = new JLabel(new ImageIcon(ImageIO.read(new File("img/Admin/student.png"))));
                    imgAddStudent.setBounds(10, 10, 50, 80);
                    panelright1.add(imgAddStudent);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                JLabel lblAddStudent = new JLabel("Add User");
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

                        panelright1.removeAll();
                        panelright2.removeAll();
                        panelright3.removeAll();
                        removeTable(panelright4);

                        panelright4.setVisible(true);

                        try{
                            JLabel imgAddBanner= new JLabel(new ImageIcon(ImageIO.read(new File("img/Admin/add-user-details.png"))));
                            imgAddBanner.setBounds(215, 5, 500, 100);
                            panelright.add(imgAddBanner);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }


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
                                        sendnotification(email, userID,password);
                                        preparedStatement2.executeUpdate();
                                        connection.setAutoCommit(false);
                                        connection.commit(); // commit the transaction


                                    } catch (SQLException ignored) {
                                    } catch (IOException ex) {
                                        throw new RuntimeException(ex);
                                    }
                                } catch (SQLException ignored) {
                                }
                            }
                            panelright4.revalidate();
                            panelright4.repaint();

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
                JLabel lblEditStudent = new JLabel("Edit User Details");
                lblEditStudent.setFont(new Font("Tahoma", Font.BOLD, 20));
                lblEditStudent.setBounds(70, 30, 220, 30);
                panelright2.add(lblEditStudent);
                lblEditStudent.setForeground(Color.BLACK);
                lblEditStudent.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        panelright1.setVisible(false);
                        panelright2.setVisible(false);
                        panelright3.setVisible(false);

                        panelright1.removeAll();
                        panelright2.removeAll();
                        panelright3.removeAll();


                        panelright4.removeAll();
                        panelright4.revalidate();
                        panelright4.repaint();
                        panelright4.setVisible(true);

                        panelright4.setBackground(Color.YELLOW);
                        panelright4.setBorder(new LineBorder(Color.BLACK, 2));
                        panelright4.setLayout(null);

                        try{
                            JLabel imgAddBanner= new JLabel(new ImageIcon(ImageIO.read(new File("img/Admin/edit-user-details.png"))));
                            imgAddBanner.setBounds(215, 5, 500, 100);
                            panelright.add(imgAddBanner);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }


                        // Add a label to the panelright4 panel with the text "User ID"
                        JLabel lblUserID = new JLabel("User ID:");
                        lblUserID.setFont(new Font("Tahoma", Font.BOLD, 20));
                        lblUserID.setBounds(50, 50, 100, 30);
                        panelright4.add(lblUserID);
                        lblUserID.setForeground(Color.BLACK);
                        // Add a text field to the panelright4 panel with the text "User ID"
                        JTextField txtUserID = new JTextField();
                        txtUserID.setFont(new Font("Times New Roman", Font.PLAIN, 22));
                        txtUserID.setBounds(150, 50, 125, 30);
                        panelright4.add(txtUserID);
                        txtUserID.setForeground(Color.BLACK);
                        // txtUserID must accept a number of 5 digits only
                        txtUserID.addKeyListener(new KeyAdapter() {
                            @Override
                            public void keyTyped(KeyEvent e) {
                                char c = e.getKeyChar();
                                if (!(Character.isDigit(c) || (c == KeyEvent.VK_BACK_SPACE) || (c == KeyEvent.VK_DELETE))) {
                                    getToolkit().beep();
                                    e.consume();
                                }
                                if (txtUserID.getText().length() >= 5) {
                                    e.consume();
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
// Add a label to the panelright4 panel with the text "Email:" below the "Mobile Number" label
                        JLabel lblEmail = new JLabel("Email:");
                        lblEmail.setFont(new Font("Tahoma", Font.BOLD, 20));
                        lblEmail.setBounds(50, 250, 150, 30);
                        panelright4.add(lblEmail);
                        lblEmail.setForeground(Color.BLACK);
                        // Add a text field to the panelright4 panel with the text "Email:" below the "Mobile Number" text field
                        JTextField txtEmail = new JTextField();
                        txtEmail.setFont(new Font("Tahoma", Font.PLAIN, 20));
                        txtEmail.setBounds(200, 250, 320, 30);
                        panelright4.add(txtEmail);
                        txtEmail.setForeground(Color.BLACK);
                        // Add a label with the text "Address" to the panelright4 panel  below the "Email" label
                        JLabel lblAddress = new JLabel("Address:");
                        lblAddress.setFont(new Font("Tahoma", Font.BOLD, 20));
                        lblAddress.setBounds(450, 150, 150, 30);
                        panelright4.add(lblAddress);
                        lblAddress.setForeground(Color.BLACK);
                        // Add a text area for the address to the panelright4 panel below the "Email" text field
                        JTextArea txtAddress = new JTextArea();
                        txtAddress.setFont(new Font("Tahoma", Font.PLAIN, 16));
                        txtAddress.setBounds(550, 150, 380, 175);
                        panelright4.add(txtAddress);
                        txtAddress.setForeground(Color.BLACK);

                        // Add a submit button to the panelright4 panel below the text area
                        JButton btnSubmit = new JButton("Submit");
                        btnSubmit.setFont(new Font("Tahoma", Font.BOLD, 20));
                        btnSubmit.setBounds(650, 50, 150, 30);
                        panelright4.add(btnSubmit);
                        btnSubmit.setForeground(Color.BLACK);

                        // By default, all the set visible false
                        txtAddress.setVisible(false);
                        lblAddress.setVisible(false);
                        lblEmail.setVisible(false);
                        txtEmail.setVisible(false);
                        lblMobileNumber.setVisible(false);
                        txtMobileNumber.setVisible(false);
                        lblName.setVisible(false);
                        txtName.setVisible(false);
                        btnSubmit.setVisible(false);


                        // Add a search button to the panelright4 panel next to the text field
                        JButton btnSearch = new JButton("Search");
                        btnSearch.setFont(new Font("Tahoma", Font.BOLD, 20));
                        btnSearch.setBounds(400, 50, 150, 30);
                        panelright4.add(btnSearch);
                        btnSearch.setForeground(Color.BLACK);
                        btnSearch.addActionListener(e13 -> {
                            String name; String mobilenum; String email; String address;
                            if(txtUserID.getText().equals("")){
                                JOptionPane.showMessageDialog(btnSearch, "Please enter the user ID", "Error", JOptionPane.ERROR_MESSAGE);
                                txtUserID.requestFocus();
                            }
                            else{

                            String[] userdetails = getUserDetails(txtUserID.getText());
                            if(userdetails[0] != null && !userdetails[0].equals("Not found"))
                                {// Store the userdetails in variables
                                    name = userdetails[0];
                                    mobilenum = userdetails[1];
                                    email = userdetails[2];
                                    address = userdetails[3];

                                    // Now setVisible the labels and text fields true
                                    txtAddress.setVisible(true);
                                    lblAddress.setVisible(true);
                                    lblEmail.setVisible(true);
                                    txtEmail.setVisible(true);
                                    lblMobileNumber.setVisible(true);
                                    txtMobileNumber.setVisible(true);
                                    lblName.setVisible(true);
                                    txtName.setVisible(true);
                                    btnSubmit.setVisible(true);

                                    // Set the text of the text fields to the userdetails
                                    txtName.setText(name);
                                    txtMobileNumber.setText(mobilenum);
                                    txtEmail.setText(email);
                                    txtAddress.setText(address);

                            }
                            else{
                                JOptionPane.showMessageDialog(btnSearch, "No user with " + txtUserID.getText() + " found", "Error", JOptionPane.ERROR_MESSAGE);
                                txtUserID.setText("");
                                txtUserID.requestFocus();
                            }
                            }
                        });


                        btnSubmit.addActionListener(e14 -> {
                            // Get the text from the text fields
                            String name = txtName.getText();
                            String mobilenum = txtMobileNumber.getText();
                            String email = txtEmail.getText();
                            String address = txtAddress.getText();

                            // Check what fields are all updated
                            if (name.equals("") && mobilenum.equals("") && email.equals("") && address.equals("")) {
                                JOptionPane.showMessageDialog(btnSubmit, "Please fill in at least one field to update");
                                if (name.equals("")) {
                                    txtName.requestFocus();
                                } else if (mobilenum.equals("")) {
                                    txtMobileNumber.requestFocus();
                                } else if (email.equals("")) {
                                    txtEmail.requestFocus();
                                } else
                                    txtAddress.requestFocus();
                            } else {
                                // Update the user details
                                if (txtUserID.getText().equals("")) {
                                    JOptionPane.showMessageDialog(btnSubmit, "Please enter a valid user ID");
                                    txtUserID.requestFocus();
                                } else {
                                    if (!isValidEmail(email)) {
                                        JOptionPane.showMessageDialog(btnSubmit, "Please enter a valid email address");
                                        txtEmail.requestFocus();
                                        txtEmail.setText("");
                                    } else {
                                        updateUserDetails(txtUserID.getText(), name, mobilenum, email, address);
                                        JOptionPane.showMessageDialog(btnSubmit, "User details for " + txtUserID.getText() + " updated successfully");
                                        // Clear all the text fields
                                        txtAddress.setText("");
                                        txtEmail.setText("");
                                        txtMobileNumber.setText("");
                                        txtName.setText("");
                                        txtUserID.setText("");
                                    }
                                }
                            }
                        });


                    }
                });

                // Add image to panelright3 panel
                try {
                    JLabel imgDeleteStudent = new JLabel(new ImageIcon(ImageIO.read(new File("img/Admin/delete-account.png"))));
                    imgDeleteStudent.setBounds(50, 10, 50, 80);
                    panelright3.add(imgDeleteStudent);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

                JLabel lblDeleteStudent = new JLabel("Delete User");
                lblDeleteStudent.setFont(new Font("Tahoma", Font.BOLD, 20));
                lblDeleteStudent.setBounds(120, 30, 175, 30);
                panelright3.add(lblDeleteStudent);
                lblDeleteStudent.setForeground(Color.BLACK);

                lblDeleteStudent.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        removeWelcomeMessage(panelright);
                        removeTable(panelright4);
                        panelright1.setVisible(false);
                        panelright2.setVisible(false);
                        panelright3.setVisible(false);

                        panelright4.setVisible(true);

                        panelright3.removeAll();
                        panelright3.revalidate();
                        panelright3.repaint();

                        panelright4.setBackground(Color.YELLOW);
                        panelright4.setBorder(new LineBorder(Color.BLACK, 2));
                        panelright4.setLayout(null);

                        try{
                            JLabel imgAddBanner= new JLabel(new ImageIcon(ImageIO.read(new File("img/Admin/delete-user-details.png"))));
                            imgAddBanner.setBounds(215, 5, 500, 100);
                            panelright.add(imgAddBanner);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }

                        // Add a label with the text User ID: to the panelright3 panel
                        JLabel lblUserID = new JLabel("User ID:");
                        lblUserID.setFont(new Font("Tahoma", Font.BOLD, 20));
                        lblUserID.setBounds(50, 50, 100, 30);
                        panelright4.add(lblUserID);
                        lblUserID.setForeground(Color.BLACK);

                        // Add a text field to the panelright3 panel next to the label
                        JTextField txtUserID = new JTextField();
                        txtUserID.setFont(new Font("Tahoma", Font.PLAIN, 20));
                        txtUserID.setBounds(200, 50, 125, 30);
                        panelright4.add(txtUserID);
                        txtUserID.setForeground(Color.BLACK);
                        // Allow only 5 digits to be entered in the text field
                        txtUserID.addKeyListener(new KeyAdapter() {
                            @Override
                            public void keyTyped(KeyEvent e) {
                                if (txtUserID.getText().length() >= 5) {
                                    e.consume();
                                }
                            }
                        });
                        // Dont allow any other characters than numbers to be entered in the text field
                        txtUserID.addKeyListener(new KeyAdapter() {
                            @Override
                            public void keyTyped(KeyEvent e) {
                                char c = e.getKeyChar();
                                if (!(Character.isDigit(c) || (c == KeyEvent.VK_BACK_SPACE) || (c == KeyEvent.VK_DELETE))) {
                                    e.consume();
                                }
                            }
                        });

                        // A Button near the userID Field to search the user by ID
                        JButton searchButton = new JButton("Search");
                        searchButton.setBounds(600, 50, 100, 30);
                        searchButton.setFont(new Font("Tahoma", Font.PLAIN, 20));
                        panelright4.add(searchButton);
                        searchButton.addActionListener(e1 -> {
                            if(txtUserID.getText().equals("")) {
                                JOptionPane.showMessageDialog(searchButton, "Please enter the user ID to search!,", "Error", JOptionPane.ERROR_MESSAGE);
                                txtUserID.requestFocus();
                            }
                            else {
                                // check if the user exists in the database
                                String[] userdetails = getUserDetails(txtUserID.getText());
                                if (userdetails[0] != null && !userdetails[0].equals("Not found")) {// Store the userdetails in variables
                                    String name = userdetails[0];
                                    // SHow a prompt to confirm the user by showing the username
                                    String message = "Are you sure you want to delete the user " + name + "?";
                                    int confirm = JOptionPane.showConfirmDialog(searchButton, message , "Confirm", JOptionPane.YES_NO_OPTION);
                                    if (confirm == JOptionPane.YES_OPTION) {
                                        // Delete the user
                                        try {
                                            if (deleteUserData(Integer.parseInt(txtUserID.getText()))) {
                                                JOptionPane.showMessageDialog(searchButton, "User " + txtUserID.getText() + " deleted successfully");
                                                txtUserID.setText("");
                                                txtUserID.requestFocus();
                                            }
                                        } catch (SQLException ex) {
                                            throw new RuntimeException(ex);
                                        }
                                    }

                                } else {
                                        JOptionPane.showMessageDialog(searchButton, "No profile with the user ID " + txtUserID.getText() + " found", "Error", JOptionPane.ERROR_MESSAGE);
                                          txtUserID.setText("");
                                }
                            }

                        });

                    }
                });

                // Repaint the panels to show the new components
                panelright1.repaint();
                panelright2.repaint();
                panelright3.repaint();
            }

            public boolean deleteUserData(int userId) throws SQLException {
                Connection connection =  ConnectionDB.connect();
                PreparedStatement deleteAuth = connection.prepareStatement("DELETE FROM USER_AUTHENTICATION WHERE USER_ID = ?");
                PreparedStatement deleteDetails = connection.prepareStatement("DELETE FROM USER_DETAILS WHERE USER_ID = ?");

                connection.setAutoCommit(false); // Start a transaction
                try {
                    deleteAuth.setInt(1, userId);
                    int deletedAuth = deleteAuth.executeUpdate();

                    deleteDetails.setInt(1, userId);
                    int deletedDetails = deleteDetails.executeUpdate();


                    if (deletedAuth == 0 || deletedDetails == 0) {
                        connection.setAutoCommit(true); // Restore auto-commit mode
                        connection.rollback(); // Roll back the transaction if no rows were deleted
                        throw new SQLException("No rows deleted for USER_ID " + userId);
                    } else {
                        connection.setAutoCommit(false); // Start a transaction
                        connection.commit(); // Commit the transaction if both delete statements succeeded
                        return true;
                    }
                } catch (SQLException e) {
                    connection.rollback(); // Roll back the transaction on any exception
                    throw e;
                } finally {
                    connection.setAutoCommit(true); // Restore auto-commit mode
                    connection.close(); // Close the connection
                }
            }

        });
    }

    public void setupQuestionPapersPanel(JLabel lblQuestionPapers,  JPanel panelright1,  JPanel panelright2,  JPanel panelright3, JPanel panelright4, JPanel panelright) {
        lblQuestionPapers.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                removeWelcomeMessage(panelright);
                panelright1.setVisible(false);
                panelright2.setVisible(false);
                panelright3.setVisible(false);
                panelright.setVisible(true);

                panelright4.removeAll();
                panelright4.revalidate();
                panelright4.repaint();
                panelright4.setVisible(true);

                // Add image to panelright1 panel
                try {
                    JLabel imgStudentList = new JLabel(new ImageIcon(ImageIO.read(new File("img/Admin/add-question-paper.png"))));
                    imgStudentList.setBounds(50, 10, 50, 80);
                    panelright1.add(imgStudentList);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }



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

                        String sql = "INSERT INTO THINKWAVE.QB_Question (InstID, CourseID, Semester, Year, ExamTitle, ExamDate, FileName, QuestionPaperPDF) VALUES (?, ?, ?, ?, ?, CONVERT(VARCHAR, ?, 3), ?, ?)";

                        try
                        {    Connection conn = ConnectionDB.connect();
                             PreparedStatement pstmt = conn.prepareStatement(sql);
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


    private void addWelcomeMessage(JPanel panelright) {
        String message = "<html><div style='text-align: center;'><p style='margin-bottom: 20px;'>Welcome to the ThinkWave MCQ Examination System Dashboard, esteemed Administrator!</p>"
                + "<p style='margin-bottom: 20px;'>Here, you can manage all aspects of the system, from the Users Menu to the Faculty Menu, and everything in between. "
                + "With just a few clicks, you can create, edit, or delete users, upload PDFs of the question bank, and much more.</p>"
                + "<p style='margin-bottom: 20px;'>As an administrator, you play a vital role in ensuring the smooth functioning of the system, and we are confident that you will do an excellent job in fulfilling your duties.</p>"
                + "<p style='margin-bottom: 20px;'>Should you encounter any issues while using the system, please do not hesitate to raise a ticket from within the system, and our developers will be happy to assist you in resolving any issues that you may encounter.</p>"
                + "<p>Thank you for using the ThinkWave MCQ Examination System, and we hope that you have a productive day ahead!</p></div></html>";
        JLabel messageLabel = new JLabel(message);
        messageLabel.setFont(new Font("Serif", Font.BOLD, 19));
        messageLabel.setBounds(50, 50, 1000, 400);
        panelright.add(messageLabel);
        messageLabel.setForeground(Color.WHITE);
        panelright.repaint();
    }

    public void updateUserDetails(String userId, String name, String mobileNum, String email, String address) {
        Connection conn = null;
        PreparedStatement stmt = null;
        String sql = "UPDATE THINKWAVE.USER_DETAILS SET name=?, mobilenum=?, email=?, address=? WHERE user_id=?";
        try {
            // Connect to the database
            conn = ConnectionDB.connect();

            // Prepare the SQL statement to update the user details

            stmt = conn.prepareStatement(sql);

            // Set the parameter values for the SQL statement
            stmt.setString(1, name);
            stmt.setString(2, mobileNum);
            stmt.setString(3, email);
            stmt.setString(4, address);
            stmt.setString(5, userId);

            stmt.executeUpdate();
            conn.setAutoCommit(false);
            conn.commit();
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close the database resources
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
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
    private static String[] getUserDetails(String userId) {
        String[] userDetails = new String[5];
        ResultSet rs = null;
        Connection con;
        PreparedStatement pstmt = null;
        String sql = "SELECT * FROM THINKWAVE.USER_DETAILS WHERE USER_ID = ?";
        try {
            con = ConnectionDB.connect();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, userId);
            rs = pstmt.executeQuery();
            if (!rs.isBeforeFirst()) { // Check if result set is empty
                return new String[]{"Not found"}; // Return "Not found" status
            } else {
                while (rs.next()) {
                    userDetails[0] = rs.getString("NAME");
                    userDetails[1] = rs.getString("MOBILENUM");
                    userDetails[2] = rs.getString("EMAIL");
                    userDetails[3] = rs.getString("ADDRESS");
                    userDetails[4] = rs.getString("INSTID");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            // Close the resources
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return userDetails;
    }

    private void addMouseListenerToChangePassword(JLabel lblChangePassword, JPanel panelright1, JPanel panelright2, JPanel panelright3, JPanel panelright4, JPanel panelright) {
        lblChangePassword.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                panelright1.setVisible(false);
                panelright2.setVisible(false);
                panelright3.setVisible(false);



                // Reset content of panels
                removeWelcomeMessage(panelright);
                panelright1.removeAll();
                panelright2.removeAll();
                panelright3.removeAll();

                panelright4.removeAll();
                panelright4.revalidate();
                panelright4.repaint();
                panelright4.setVisible(true);

                // Add image to panelright1 panel
                try {
                    JLabel imgStudentList = new JLabel(new ImageIcon(ImageIO.read(new File("img/Admin/change-password.png"))));
                    imgStudentList.setBounds(50, 10, 50, 80);
                    panelright1.add(imgStudentList);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

                JLabel userIDLabel = new JLabel("User ID:");
                userIDLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
                userIDLabel.setForeground(Color.BLACK);
                userIDLabel.setBounds(100, 50, 200, 30);
                panelright4.add(userIDLabel);
                JTextField userIDField = new JTextField();
                userIDField.setBounds(350, 50, 100, 30);
                userIDField.setFont(new Font("Tahoma", Font.PLAIN, 20));
                panelright4.add(userIDField);

                // Add a action listener that will allow only 5 digits to be entered in the text field
                userIDField.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyTyped(KeyEvent e) {
                        if (userIDField.getText().length() >= 5) {
                            e.consume();
                        }
                    }
                });


                JLabel passwordLabel = new JLabel("New Password:");
                passwordLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
                passwordLabel.setForeground(Color.BLACK);
                passwordLabel.setBounds(100, 100, 200, 30);
                panelright4.add(passwordLabel);
                JPasswordField passwordField = new JPasswordField();
                passwordField.setBounds(350, 100, 200, 30);
                passwordField.setFont(new Font("Tahoma", Font.PLAIN, 20));
                panelright4.add(passwordField);

                JLabel confirmPasswordLabel = new JLabel("Confirm Password: ");
                confirmPasswordLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
                confirmPasswordLabel.setForeground(Color.BLACK);
                confirmPasswordLabel.setBounds(100, 150, 225, 30);
                panelright4.add(confirmPasswordLabel);
                JPasswordField confirmPasswordField = new JPasswordField();
                confirmPasswordField.setBounds(350, 150, 200, 30);
                confirmPasswordField.setFont(new Font("Tahoma", Font.PLAIN, 20));
                panelright4.add(confirmPasswordField);

                JButton changePasswordButton = new JButton("Change Password");
                changePasswordButton.setBounds(350, 200, 200, 30);
                changePasswordButton.setFont(new Font("Tahoma", Font.PLAIN, 20));
                panelright4.add(changePasswordButton);


                passwordField.setVisible(false);
                confirmPasswordField.setVisible(false);
                passwordLabel.setVisible(false);
                confirmPasswordLabel.setVisible(false);
                changePasswordButton.setVisible(false);

                // A Button near the userID Field to search the user by ID
                JButton searchButton = new JButton("Search");
                searchButton.setBounds(600, 50, 100, 30);
                searchButton.setFont(new Font("Tahoma", Font.PLAIN, 20));
                panelright4.add(searchButton);
                searchButton.addActionListener(e1 -> {
                    if(userIDField.getText().equals("")) {
                        JOptionPane.showMessageDialog(null, "Please enter the user ID to search!,", "Error", JOptionPane.ERROR_MESSAGE);
                        userIDField.requestFocus();
                    }
                    else {
                        // check if the user exists in the database
                        String[] userdetails = getUserDetails(userIDField.getText());
                        if (userdetails[0] != null && !userdetails[0].equals("Not found")) {// Store the userdetails in variables
                            String name = userdetails[0];
                            // SHow a prompt to confirm the user by showing the username
                            String message = "<html><div style='text-align: center;'>Are you sure you want to change the password for <b>" + name + "</b>?</div></html>";
                            int confirm = JOptionPane.showConfirmDialog(searchButton, message , "Confirm", JOptionPane.YES_NO_OPTION);
                            if (confirm == JOptionPane.YES_OPTION) {
                                passwordField.setVisible(true);
                                confirmPasswordField.setVisible(true);
                                passwordLabel.setVisible(true);
                                confirmPasswordLabel.setVisible(true);
                                changePasswordButton.setVisible(true);

                            }

                        } else {
                            JOptionPane.showMessageDialog(searchButton, "No profile with the user ID " + userIDField.getText() + " found!", "Error", JOptionPane.ERROR_MESSAGE);
                            userIDField.requestFocus();
                        }
                    }

                });

                changePasswordButton.addActionListener(e12 -> {
                    if(passwordField.getText().equals("")) {
                        JOptionPane.showMessageDialog(changePasswordButton, "Please enter the new password!", "Error", JOptionPane.ERROR_MESSAGE);
                        passwordField.requestFocus();
                    }
                    else if(confirmPasswordField.getText().equals("")) {
                        JOptionPane.showMessageDialog(changePasswordButton, "Please confirm the password!", "Error", JOptionPane.ERROR_MESSAGE);
                        confirmPasswordField.requestFocus();
                    }
                    else if(!passwordField.getText().equals(confirmPasswordField.getText())) {
                        JOptionPane.showMessageDialog(changePasswordButton, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
                        passwordField.setText("");
                        confirmPasswordField.setText("");
                        passwordField.requestFocus();
                    }
                    else {
                        String password = new String(passwordField.getPassword());
                        String email = RetrieveEmailID.retrieveEmail(Integer.parseInt(userIDField.getText()));
                        if (!updatePassword(Integer.parseInt(userIDField.getText()), password)) {
                            JOptionPane.showMessageDialog(changePasswordButton, "Error while updating the password!", "Error", JOptionPane.ERROR_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(changePasswordButton, "Password changed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                            try {
                                sendPasswordChangeNotification(email, Integer.parseInt(userIDField.getText()));
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                            userIDField.setText("");
                            passwordField.setText("");
                            confirmPasswordField.setText("");
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
        lblHome.setBounds(80, 150, 100, 30);
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

        // Add the text Users to the panelleft panel blow the Home text
        JLabel lblUsers = new JLabel("Manage Users Details");
        lblUsers.setFont(new Font("Tahoma", Font.BOLD, 18));
        lblUsers.setBounds(15, 200, 250, 30);
        panelleft.add(lblUsers);
        lblUsers.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                Font font = lblUsers.getFont();
                Map attributes = font.getAttributes();
                attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
                lblUsers.setFont(font.deriveFont(attributes));
                lblUsers.setForeground(Color.BLUE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                Font font = lblUsers.getFont();
                Map attributes = font.getAttributes();
                attributes.put(TextAttribute.UNDERLINE, -1);
                lblUsers.setFont(font.deriveFont(attributes));
                lblUsers.setForeground(Color.BLACK);
            }
        });
        lblUsers.setForeground(Color.BLACK);


        // Add the text Exams to the panelleft panel
        JLabel lblShowUsersList = new JLabel("Show Users List");
        lblShowUsersList.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblShowUsersList.setBounds(30, 250, 200, 30);
        lblShowUsersList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                Font font = lblShowUsersList.getFont();
                Map attributes = font.getAttributes();
                attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
                lblShowUsersList.setFont(font.deriveFont(attributes));
                lblShowUsersList.setForeground(Color.BLUE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                Font font = lblShowUsersList.getFont();
                Map attributes = font.getAttributes();
                attributes.put(TextAttribute.UNDERLINE, -1);
                lblShowUsersList.setFont(font.deriveFont(attributes));
                lblShowUsersList.setForeground(Color.BLACK);
            }
        });
        panelleft.add(lblShowUsersList);
        lblShowUsersList.setForeground(Color.BLACK);

        // Add the text Remove Session to the panelleft panel blow the Show Users List text
        JLabel lblRemoveSession = new JLabel("Force Logout Users");
        lblRemoveSession.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblRemoveSession.setBounds(30, 300, 200, 30);
        lblRemoveSession.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                Font font = lblRemoveSession.getFont();
                Map attributes = font.getAttributes();
                attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
                lblRemoveSession.setFont(font.deriveFont(attributes));
                lblRemoveSession.setForeground(Color.BLUE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                Font font = lblRemoveSession.getFont();
                Map attributes = font.getAttributes();
                attributes.put(TextAttribute.UNDERLINE, -1);
                lblRemoveSession.setFont(font.deriveFont(attributes));
                lblRemoveSession.setForeground(Color.BLACK);
            }
        });
        panelleft.add(lblRemoveSession);


        // Add the text Question Papers to the panelleft panel blow the Exams text
        JLabel lblQuestionPapers = new JLabel("Question Bank");
        lblQuestionPapers.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblQuestionPapers.setBounds(40, 350, 200, 30);
        lblQuestionPapers.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                Font font = lblQuestionPapers.getFont();
                Map attributes = font.getAttributes();
                attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
                lblQuestionPapers.setFont(font.deriveFont(attributes));
                lblQuestionPapers.setForeground(Color.BLUE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                Font font = lblQuestionPapers.getFont();
                Map attributes = font.getAttributes();
                attributes.put(TextAttribute.UNDERLINE, -1);
                lblQuestionPapers.setFont(font.deriveFont(attributes));
                lblQuestionPapers.setForeground(Color.BLACK);
            }
        });
        panelleft.add(lblQuestionPapers);
        lblQuestionPapers.setForeground(Color.BLACK);

        // Add the text ChangePassword to the panelleft panel blow the Question Papers text
        JLabel lblChangePassword = new JLabel("Change Password");
        lblChangePassword.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblChangePassword.setBounds(25, 400, 200, 30);
        panelleft.add(lblChangePassword);
        lblChangePassword.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                Font font = lblChangePassword.getFont();
                Map attributes = font.getAttributes();
                attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
                lblChangePassword.setFont(font.deriveFont(attributes));
                lblChangePassword.setForeground(Color.BLUE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                Font font = lblChangePassword.getFont();
                Map attributes = font.getAttributes();
                attributes.put(TextAttribute.UNDERLINE, -1);
                lblChangePassword.setFont(font.deriveFont(attributes));
                lblChangePassword.setForeground(Color.BLACK);
            }
        });
        lblChangePassword.setForeground(Color.BLACK);

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

       /* JLabel lblWelcome = new JLabel("You are logged in as: " + RetriveUserName.getUserName(Integer.parseInt(gotUserID)));
        lblWelcome.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblWelcome.setBounds(700, 10, 500, 30);
        panelright.add(lblWelcome);
        lblWelcome.setForeground(Color.YELLOW);*/

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
        addMouseListenerToLblUsers(lblUsers, panelright1, panelright2, panelright3, panelright4, panelright);
        // Performs an action when the user clicks on the Question Papers text
        setupQuestionPapersPanel(lblQuestionPapers, panelright1, panelright2, panelright3, panelright4, panelright);
        addMouseListenerToChangePassword(lblChangePassword, panelright1, panelright2, panelright3, panelright4, panelright);

        addMouseListenerToShowUserList(lblShowUsersList, panelright1, panelright2, panelright3, panelright4, panelright);

        addMouseListenerToForceLogout(lblRemoveSession, panelright1, panelright2, panelright3, panelright4, panelright);

        // import the addTimeFooter method from the DateTimePanel class
        DateTimePanel dateTimePanel = new DateTimePanel();
        dateTimePanel.addTimeFooter(contentPane);
        dateTimePanel.setVisible(true);
    }

    private void addMouseListenerToForceLogout(JLabel lblRemoveSession, JPanel panelright1, JPanel panelright2, JPanel panelright3, JPanel panelright4, JPanel panelright) {

    lblRemoveSession.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            panelright1.setVisible(false);
            panelright2.setVisible(false);
            panelright3.setVisible(false);
            panelright4.setVisible(false);

            // Reset content of panels
            removeWelcomeMessage(panelright);
            removeTable(panelright4);
            panelright1.removeAll();
            panelright2.removeAll();
            panelright3.removeAll();
            panelright4.removeAll();

            panelright4.setBackground(Color.YELLOW);
            panelright4.setBorder(new LineBorder(Color.BLACK, 2));
            panelright4.setLayout(null);
            panelright4.setVisible(true);

            // Add image to panelright1 panel
            try {
                JLabel imgStudentList = new JLabel(new ImageIcon(ImageIO.read(new File("img/Admin/force-logout.png"))));
                imgStudentList.setBounds(50, 10, 50, 80);
                panelright1.add(imgStudentList);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }



            // Add a label with the text User ID: to the panelright3 panel
            JLabel lblUserID = new JLabel("User ID:");
            lblUserID.setFont(new Font("Tahoma", Font.BOLD, 20));
            lblUserID.setBounds(50, 50, 100, 30);
            panelright4.add(lblUserID);
            lblUserID.setForeground(Color.BLACK);

            // Add a text field to the panelright3 panel next to the label
            JTextField txtUserID = new JTextField();
            txtUserID.setFont(new Font("Tahoma", Font.PLAIN, 20));
            txtUserID.setBounds(200, 50, 125, 30);
            panelright4.add(txtUserID);
            txtUserID.setForeground(Color.BLACK);
            // Allow only 5 digits to be entered in the text field
            txtUserID.addKeyListener(new KeyAdapter() {
                @Override
                public void keyTyped(KeyEvent e) {
                    if (txtUserID.getText().length() >= 5) {
                        e.consume();
                    }
                }
            });
            // Dont allow any other characters than numbers to be entered in the text field
            txtUserID.addKeyListener(new KeyAdapter() {
                @Override
                public void keyTyped(KeyEvent e) {
                    char c = e.getKeyChar();
                    if (!(Character.isDigit(c) || (c == KeyEvent.VK_BACK_SPACE) || (c == KeyEvent.VK_DELETE))) {
                        e.consume();
                    }
                }
            });

            // A Button near the userID Field to search the user by ID
            JButton searchButton = new JButton("Search");
            searchButton.setBounds(600, 50, 100, 30);
            searchButton.setFont(new Font("Tahoma", Font.PLAIN, 20));
            panelright4.add(searchButton);
            searchButton.addActionListener(e1 -> {
                if(txtUserID.getText().equals("")) {
                    JOptionPane.showMessageDialog(searchButton, "Please enter the user ID to search!,", "Error", JOptionPane.ERROR_MESSAGE);
                    txtUserID.requestFocus();
                }
                else {
                    // check if the user exists in the database
                    String[] userdetails = getUserDetails(txtUserID.getText());
                    if (userdetails[0] != null && !userdetails[0].equals("Not found")) {// Store the userdetails in variables
                        String name = userdetails[0];
                        // SHow a prompt to confirm the user by showing the username
                        String message = "Are you sure you want to remove the session of the user " + name + "?";
                        int confirm = JOptionPane.showConfirmDialog(searchButton, message , "Confirm", JOptionPane.YES_NO_OPTION);
                        if (confirm == JOptionPane.YES_OPTION) {
                            // Delete the user
                            nullifySessionID(txtUserID.getText());
                            JOptionPane.showMessageDialog(searchButton, "User " + txtUserID.getText() + "'s session removed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                            txtUserID.setText("");
                            txtUserID.requestFocus();

                        }

                    } else {
                        JOptionPane.showMessageDialog(searchButton, "No profile with the user ID " + txtUserID.getText() + " found", "Error", JOptionPane.ERROR_MESSAGE);
                        txtUserID.setText("");
                    }
                }

            });

        }
    });

        // Repaint the panels to show the new components
        panelright1.repaint();
        panelright2.repaint();
        panelright3.repaint();
    }
    private void addMouseListenerToShowUserList(JLabel lblShowUsersList, JPanel panelright1, JPanel panelright2, JPanel panelright3, JPanel panelright4, JPanel panelright) {
            lblShowUsersList.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    panelright1.setVisible(true);
                    panelright2.setVisible(true);
                    panelright3.setVisible(false);
                    panelright4.setVisible(false);


                    // Reset content of panels
                    panelright1.removeAll();
                    panelright2.removeAll();
                    panelright3.removeAll();
                    panelright4.removeAll();

                    // Add image to panelright1 panel
                    try {
                        JLabel imgStudentList = new JLabel(new ImageIcon(ImageIO.read(new File("img/Admin/studentlist.png"))));
                        imgStudentList.setBounds(50, 10, 50, 80);
                        panelright1.add(imgStudentList);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                    // Add content to panelright1
                    JLabel lblShowStudentList = new JLabel("Student List");
                    lblShowStudentList.setBounds(120, 30, 175, 30);
                    lblShowStudentList.setFont(new Font("Tahoma", Font.BOLD, 20));
                    panelright1.add(lblShowStudentList);
                    lblShowStudentList.setForeground(Color.BLACK);

                    // When lblShowStudentList is clicked, an object to UserTableFrame is created and is displayed
                    lblShowStudentList.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            UserTableModel model = new UserTableModel();
                            JTable table = new JTable(model);
                            JScrollPane scrollPane = new JScrollPane(table);
                            String[] userdetails = getUserDetails(gotUserID);
                            if (userdetails[0] != null && !userdetails[0].equals("Not found")) {
                                String adminINSTID = userdetails[4];


                                String sql = "SELECT ud.USER_ID, ud.NAME\n" +
                                        "FROM THINKWAVE.USER_AUTHENTICATION ua\n" +
                                        "INNER JOIN THINKWAVE.USER_DETAILS ud\n" +
                                        "ON ua.USER_ID = ud.USER_ID\n" +
                                        "WHERE ua.ROLE = 'Student'  AND ud.INSTID = '" + adminINSTID + "'";

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

                                    if (rowCount == 0) {
                                        JOptionPane.showMessageDialog(lblShowStudentList, "There are no students registered in the system for this institution", "No Students", JOptionPane.INFORMATION_MESSAGE);
                                        panelright1.setVisible(true);
                                        panelright2.setVisible(true);
                                        panelright3.setVisible(false);

                                        // Reset content of panels
                                        removeWelcomeMessage(panelright);
                                        removeTable(panelright4);

                                        panelright4.setVisible(false);

                                    } else {
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

                                        panelright1.setVisible(false);
                                        panelright2.setVisible(false);
                                        panelright3.setVisible(false);
                                        panelright4.setVisible(true);

                                        panelright4.setLayout(new BorderLayout());
                                        panelright4.add(scrollPane, BorderLayout.CENTER);
                                        panelright4.revalidate();
                                        panelright4.repaint();

                                    }
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    removeTable(panelright4);
                                }
                            } else {
                                JOptionPane.showMessageDialog(lblShowStudentList, "There are no students registered in the system for this institution", "No Students", JOptionPane.INFORMATION_MESSAGE);
                                panelright1.setVisible(true);
                                panelright2.setVisible(true);
                                panelright3.setVisible(false);

                                // Reset content of panels
                                removeWelcomeMessage(panelright);
                                removeTable(panelright4);

                                panelright4.setVisible(false);
                            }
                        }
                    });


                    // Add image to panelright2 panel
                    try {
                        JLabel imgFacultyList = new JLabel(new ImageIcon(ImageIO.read(new File("img/Admin/facultylist.png"))));
                        imgFacultyList.setBounds(50, 10, 50, 80);
                        panelright2.add(imgFacultyList);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                    // Add content to panelright2
                    JLabel lblShowFacultyList = new JLabel("Faculty List");
                    lblShowFacultyList.setBounds(120, 30, 175, 30);
                    lblShowFacultyList.setFont(new Font("Tahoma", Font.BOLD, 20));
                    panelright2.add(lblShowFacultyList);
                    lblShowFacultyList.setForeground(Color.BLACK);
                    lblShowFacultyList.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            UserTableModel model = new UserTableModel();
                            JTable table = new JTable(model);
                            JScrollPane scrollPane = new JScrollPane(table);
                            String[] userdetails = getUserDetails(gotUserID);
                            if (userdetails[0] != null && !userdetails[0].equals("Not found")) {
                                String adminINSTID = userdetails[4];


                                String sql = "SELECT ud.USER_ID, ud.NAME\n" +
                                        "FROM THINKWAVE.USER_AUTHENTICATION ua\n" +
                                        "INNER JOIN THINKWAVE.USER_DETAILS ud\n" +
                                        "ON ua.USER_ID = ud.USER_ID\n" +
                                        "WHERE ua.ROLE = 'Faculty'  AND ud.INSTID = '" + adminINSTID + "'";

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

                                    if (rowCount == 0) {
                                        JOptionPane.showMessageDialog(lblShowStudentList, "There are no faculty registered in the system for this institution", "No Students", JOptionPane.INFORMATION_MESSAGE);

                                        panelright1.setVisible(true);
                                        panelright2.setVisible(true);
                                        panelright3.setVisible(false);
                                        panelright4.setVisible(false);

                                        // Reset content of panels
                                        removeWelcomeMessage(panelright);
                                        removeTable(panelright4);
                                    } else {
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

                                        panelright1.setVisible(false);
                                        panelright2.setVisible(false);

                                        panelright4.setLayout(new BorderLayout());
                                        panelright4.add(scrollPane, BorderLayout.CENTER);
                                        panelright4.setVisible(true);

                                    }
                                } catch (Exception ex) {
                                    removeTable(panelright4);
                                    ex.printStackTrace();
                                }
                            } else {
                                JOptionPane.showMessageDialog(lblShowStudentList, "There are no faculty registered in the system for this institution", "No Students", JOptionPane.INFORMATION_MESSAGE);
                                panelright1.setVisible(true);
                                panelright2.setVisible(true);
                                panelright3.setVisible(false);
                                panelright4.setVisible(false);

                                // Reset content of panels
                                removeWelcomeMessage(panelright);
                                removeTable(panelright4);
                            }

                        }
                    });

                }
            });
        }

}