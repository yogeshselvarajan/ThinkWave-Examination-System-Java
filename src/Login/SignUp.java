package Login;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.TimeZone;

import static DatabaseFunctions.NewSignUp.CheckAdminIDExists.checkAdminID;
import static DatabaseFunctions.NewSignUp.FetchLatestInstituteID.fetchLatestInstituteID;
import static DatabaseFunctions.NewSignUp.InsertSignUp.insertUser;
import static Mail.SendRegisterEmail.sendRegisterEmail;
import static Mail.VerifyEmailForAdmin.generateVerificationCode;
import static Mail.VerifyEmailForAdmin.sendAdminVerificationEmail;

public class SignUp extends JFrame
{
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                SignUp obj = new SignUp();
                obj.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public SignUp()
    {
        setResizable(false);
        JPanel mainPanel = new JPanel();
        mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(mainPanel);
        mainPanel.setLayout(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(false);
        setAlwaysOnTop(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Border border = BorderFactory.createLineBorder(Color.BLACK, 3);
        final String[] code = {null};
        final String[] emailverification = {null};


        // Add a panel at the top of the frame
        JPanel panelheader = new JPanel();
        panelheader.setBounds(0, 0, 1400, 120);
        panelheader.setBackground(new Color(255, 49, 49));
        panelheader.setLayout(null);
        mainPanel.add(panelheader);

        // Load the thinkwave gif image on the frame
        JLabel gif = new JLabel(new ImageIcon("./img/Register/text.gif"));
        gif.setBounds(0, 0, 1400, 120);
        panelheader.add(gif);

        // A back image to the panelheader
        JLabel lblBack = new JLabel(new ImageIcon("./img/Register/left-arrow.png"));
        lblBack.setBounds(10, 10, 100, 100);
        lblBack.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblBack.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int a = JOptionPane.showConfirmDialog(lblBack, "Are you sure to go back to Login Screen ?", "ThinkWave", JOptionPane.YES_NO_OPTION);
                if (a == JOptionPane.YES_OPTION) {
                    dispose();
                    EventQueue.invokeLater(() -> {
                        try {
                            LoginPage frame = new LoginPage();
                            frame.setVisible(true);
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    });
                }
            }
        });
        lblBack.setBorder(border);
        panelheader.add(lblBack);

        // A exit image to the panelheader right end
        JLabel lblExit = new JLabel(new ImageIcon("./img/Register/remove-button.png"));
        lblExit.setBounds(1250, 10, 100, 100);
        lblExit.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblExit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int a = JOptionPane.showConfirmDialog(lblExit, "Are you sure to exit?", "ThinkWave", JOptionPane.YES_NO_OPTION);
                if (a == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });
        lblExit.setBorder(border);
        panelheader.add(lblExit);

        // Add the Sign Up.jpg image to the frame
        JLabel lblSignUp = new JLabel(new ImageIcon("./img/Register/Sign Up.jpg"));
        lblSignUp.setBounds(30, 130, 600, 600);
        mainPanel.add(lblSignUp);


        // A panel at the top of the frame
        JPanel paneltop = new JPanel();
        paneltop.setBounds(650, 120, 715, 615);
        // Set colour to panel background to #6610f2;
        paneltop.setBackground(Color.CYAN);
        paneltop.setLayout(null);
        paneltop.setBorder(border);
        mainPanel.add(paneltop);

        // A Label with the text "Get your free account now"
        JLabel lblGetYourFreeAccountNow = new JLabel("Get your free account now");
        lblGetYourFreeAccountNow.setFont(new Font("Helvetica", Font.BOLD, 30));
        lblGetYourFreeAccountNow.setForeground(Color.BLACK);
        lblGetYourFreeAccountNow.setBounds(190, 10, 500, 50);
        paneltop.add(lblGetYourFreeAccountNow);

        // A label to get the name of the institution
        JLabel lblNameOfInstitution = new JLabel("Name of Institution:");
        lblNameOfInstitution.setFont(new Font("Tahoma", Font.BOLD, 18));
        lblNameOfInstitution.setForeground(Color.BLACK);
        lblNameOfInstitution.setBounds(50, 70, 210, 50);
        paneltop.add(lblNameOfInstitution);
        // A text field next to the label to show the instituion number for the institution
        //  number is the unique number given to the institution by the system
        JTextField txtInstitutionNumber = new JTextField();
        txtInstitutionNumber.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        txtInstitutionNumber.setForeground(Color.BLACK);
        txtInstitutionNumber.setBounds(250, 70, 80, 40);
        // The fetchLatestInstituteID() method is used to get the latest institution number that is unique
        txtInstitutionNumber.setText(fetchLatestInstituteID());
        txtInstitutionNumber.setEditable(false);
        paneltop.add(txtInstitutionNumber);


        // Text field to get the name of the institution next to txtInstitutionNumber
        JTextField txtNameOfInstitution = new JTextField();
        txtNameOfInstitution.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        txtNameOfInstitution.setForeground(Color.BLACK);
        txtNameOfInstitution.setBounds(340, 70, 365, 40);
        txtNameOfInstitution.setText("Enter Institution Name");
txtNameOfInstitution.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txtNameOfInstitution.getText().equals("Enter Institution Name")) {
                    txtNameOfInstitution.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (txtNameOfInstitution.getText().equals("")) {
                    txtNameOfInstitution.setText("Enter Institution Name");
                }
            }
        });
        paneltop.add(txtNameOfInstitution);

        // A label to get the address of the institution
        JLabel lblAddressOfInstitution = new JLabel("Address of Institution:");
        lblAddressOfInstitution.setFont(new Font("Tahoma", Font.BOLD, 18));
        lblAddressOfInstitution.setForeground(Color.BLACK);
        lblAddressOfInstitution.setBounds(50, 130, 220, 50);
        paneltop.add(lblAddressOfInstitution);
        // A text area to get the address of the institution parallel to the label with scroll bar both horizontal and vertical
        JTextArea txtAddressOfInstitution = new JTextArea();
        txtAddressOfInstitution.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        txtAddressOfInstitution.setForeground(Color.BLACK);
        txtAddressOfInstitution.setBounds(275, 130, 400, 100);
        txtAddressOfInstitution.setLineWrap(true);
        txtAddressOfInstitution.setWrapStyleWord(true);
        txtAddressOfInstitution.setText("Enter Full Institution Address Here");
        JScrollPane scrollAddress = new JScrollPane(txtAddressOfInstitution);
        scrollAddress.setBounds(305, 130, 400, 100);
        scrollAddress.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        // When focus is gained on the text area, the text is cleared
        txtAddressOfInstitution.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txtAddressOfInstitution.getText().equals("Enter Full Institution Address Here")) {
                    txtAddressOfInstitution.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (txtAddressOfInstitution.getText().equals("")) {
                    txtAddressOfInstitution.setText("Enter Full Institution Address Here");
                }
            }
        });
        paneltop.add(scrollAddress);

        // A label to get the College Email ID of the institution
        JLabel lblCollegeEmailID = new JLabel("College Email ID:");
        lblCollegeEmailID.setFont(new Font("Tahoma", Font.BOLD, 18));
        lblCollegeEmailID.setForeground(Color.BLACK);
        lblCollegeEmailID.setBounds(50, 240, 210, 50);
        paneltop.add(lblCollegeEmailID);
        // Text field to get the College Email ID of the institution parallel to the label
        JTextField txtCollegeEmailID = new JTextField();
        txtCollegeEmailID.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        txtCollegeEmailID.setForeground(Color.BLACK);
        txtCollegeEmailID.setBounds(305, 240, 400, 40);
        txtCollegeEmailID.setText("Enter the Institution's Official Email ID");
        // When focus is gained on the text field, the text is cleared
        txtCollegeEmailID.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txtCollegeEmailID.getText().equals("Enter the Institution's Official Email ID")) {
                    txtCollegeEmailID.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (txtCollegeEmailID.getText().equals("")) {
                    txtCollegeEmailID.setText("Enter the Institution's Official Email ID");
                }
            }
        });
        paneltop.add(txtCollegeEmailID);

        //  A label to get the Admin Email ID to register the institution
        JLabel lblAdminEmailID = new JLabel("Admin Email ID:");
        lblAdminEmailID.setFont(new Font("Tahoma", Font.BOLD, 18));
        lblAdminEmailID.setForeground(Color.BLACK);
        lblAdminEmailID.setBounds(50, 300, 210, 50);
        paneltop.add(lblAdminEmailID);
        // Text field to get the Admin Email ID to register the institution parallel to the label
        JTextField txtAdminEmailID = new JTextField();
        txtAdminEmailID.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        txtAdminEmailID.setForeground(Color.BLACK);
        txtAdminEmailID.setBounds(305, 300, 400, 40);
        txtAdminEmailID.setText("Enter the Admin's Email ID");
        // When focus is gained on the text field, the text is cleared
        txtAdminEmailID.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txtAdminEmailID.getText().equals("Enter the Admin's Email ID")) {
                    txtAdminEmailID.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (txtAdminEmailID.getText().equals("")) {
                    txtAdminEmailID.setText("Enter the Admin's Email ID");
                }
            }
        });
        paneltop.add(txtAdminEmailID);

        // A button very bottom to the admin email id with Obtain code text
        JButton btnObtainCode = new JButton("Obtain Code");
        btnObtainCode.setFont(new Font("Tahoma", Font.BOLD, 18));
        btnObtainCode.setForeground(Color.BLACK);
        btnObtainCode.setBounds(305, 350, 150, 40);
        // When the button is clicked, the code is sent to the admin email id
        btnObtainCode.addActionListener(e -> {
            // The code is sent to the admin email id
            String to = txtAdminEmailID.getText();
            try {
                code[0] = generateVerificationCode();
                sendAdminVerificationEmail(to, code[0]);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            // Diplays a message to the user that the code has been sent to the admin email id
            JOptionPane.showMessageDialog(btnObtainCode, "Verification Code has been sent to the Admin Email ID. Please check your email inbox/ spam folder. If you do not receive the email, please check your internet connection and try again.", "Verification Code Sent", JOptionPane.INFORMATION_MESSAGE);
        });
        paneltop.add(btnObtainCode);

        // A text field to get the code sent to the admin email id parallel to the button
        JTextField txtCode = new JTextField();
        txtCode.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        txtCode.setForeground(Color.BLACK);
        txtCode.setBounds(465, 350, 240, 40);
        txtCode.setText("Enter Code sent to Admin Email ID");
        // When focus is gained on the text field, the text is cleared
        txtCode.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txtCode.getText().equals("Enter Code sent to Admin Email ID")) {
                    txtCode.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (txtCode.getText().equals("")) {
                    txtCode.setText("Enter Code sent to Admin Email ID");
                }
            }
        });
        paneltop.add(txtCode);

        // A label to get the username of the admin
        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setFont(new Font("Tahoma", Font.BOLD, 18));
        lblUsername.setForeground(Color.BLACK);
        lblUsername.setBounds(50, 410, 210, 50);
        paneltop.add(lblUsername);
        // Text field to get the username of the admin parallel to the label
        JTextField txtUsername = new JTextField();
        txtUsername.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        txtUsername.setForeground(Color.BLACK);
        txtUsername.setBounds(305, 410, 400, 40);
        // Set ADM by defult as the username and on pressing, user can append only 2 numbers to it
        txtUsername.setText("Username is of the form ADXXX");
        txtUsername.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (txtUsername.getText().length() >= 5) // limit textfield to 5 characters
                    e.consume();
            }
        });

        //The AD should not delete the first two characters
        txtUsername.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    if (txtUsername.getText().length() <= 2) {
                        e.consume();
                    }
                }
            }
        });
        // If the user clicks on the text field, the text should be deleted and set to AD and allow the user to enter only 3 characters
        txtUsername.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                txtUsername.setText("AD");
            }
        });
        paneltop.add(txtUsername);

        // A label to get the password of the admin
        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setFont(new Font("Tahoma", Font.BOLD, 18));
        lblPassword.setForeground(Color.BLACK);
        lblPassword.setBounds(50, 470, 210, 50);
        paneltop.add(lblPassword);
        // Text field to get the password of the admin parallel to the label
        JPasswordField txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        txtPassword.setForeground(Color.BLACK);
        txtPassword.setBounds(305, 470, 300, 40);
        txtPassword.setEchoChar('*');
        paneltop.add(txtPassword);

        // A icon to show the password next to the password text field
        JLabel lblShowPassword = new JLabel("");
        lblShowPassword.setIcon(new ImageIcon("img/Login/show.png"));
        lblShowPassword.setBounds(615, 470, 40, 40);
        lblShowPassword.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblShowPassword.setBorder(border);
        lblShowPassword.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (txtPassword.getEchoChar() == '*') {
                    txtPassword.setEchoChar((char) 0);
                } else {
                    txtPassword.setEchoChar('*');
                }
            }
        });
        paneltop.add(lblShowPassword);


        // Button with Sign Up text to register the institution
        JButton btnSignUp = new JButton("Sign Up");
        btnSignUp.setFont(new Font("Tahoma", Font.BOLD, 18));
        btnSignUp.setForeground(Color.BLACK);
        btnSignUp.setBounds(305, 530, 150, 40);
        btnSignUp.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnSignUp.addActionListener( e ->
        {
            String insID = txtInstitutionNumber.getText();
            String institutionName = txtNameOfInstitution.getText();
            String address = txtAddressOfInstitution.getText();
            String collegeEmailID = txtCollegeEmailID.getText();
            String adminEmailID = txtAdminEmailID.getText();
            String verifycode = txtCode.getText();
            String adminID = txtUsername.getText();
            String password =  txtPassword.getText();
            // if the verification code is correct, then the emailverification variable is set to 'Y' else 'N'
            if (verifycode.equals(code[0]))
                emailverification[0] = "Y";
            else emailverification[0] = "N";

            // Check if all the fields institution name,address,email id, admin email,code, username and password are filled
            if (txtNameOfInstitution.getText().equals("") || txtNameOfInstitution.getText().equals("Enter Institution Name") ||
                    txtAddressOfInstitution.getText().equals("") || txtAddressOfInstitution.getText().equals("Enter Address") ||
                    txtCollegeEmailID.getText().equals("") || txtCollegeEmailID.getText().equals("Enter Email ID") ||
                    txtAdminEmailID.getText().equals("") || txtAdminEmailID.getText().equals("Enter Admin Email ID") ||
                    txtCode.getText().equals("") || txtCode.getText().equals("Enter Code sent to Admin Email ID") ||
                    txtUsername.getText().equals("") || txtUsername.getText().equals("Username is of the form ADXXX") ||
                    txtPassword.getText().equals("") || txtPassword.getText().equals("Password should contain 1 uppercase, 1 lowercase, 1 number and 1 special character")) {
                JOptionPane.showMessageDialog(btnSignUp, "Please fill all the fields");

            }
            // If the fields are filled, check if the password is strong enough
            else if (!isStrongPassword(Arrays.toString(txtPassword.getPassword()))) {
                JOptionPane.showMessageDialog(btnSignUp, "Password is not strong enough");
                txtPassword.setText("");
                txtPassword.requestFocus();
            }

            // If the admin ID is not taken, check if the verfication code is correct and set email verification to 'Y' or 'N'
            // Check if the verification code is correct
            else if (emailverification[0].equals("N"))
            {
                JOptionPane.showMessageDialog(btnSignUp, "Invalid Code! Please try again", "Error", JOptionPane.ERROR_MESSAGE);
                txtCode.setText("");
                txtCode.requestFocus();
            }
            // If the verification code is correct, check if the email ID is already taken
            else if(checkAdminID(adminID))
            {
                        JOptionPane.showMessageDialog(btnSignUp, "Admin ID already taken! Please try another one", "Error", JOptionPane.ERROR_MESSAGE);
                        txtUsername.setText("");
                        txtUsername.requestFocus();
            }
            else
            {
                insertUser(password, adminID, collegeEmailID,emailverification[0],insID,adminEmailID,institutionName,address);
                // Send Registration Successful email to the admin
                    sendRegisterEmail(adminEmailID,institutionName,address,collegeEmailID,insID,adminID,password);
                    JOptionPane.showMessageDialog(btnSignUp, "You have been registered successfully");
                }
        });
        paneltop.add(btnSignUp);

        // A label with the text "Already have an account? <html><u>Sign In</u></html>" to redirect to the sign in page
        JLabel lblSignIn = new JLabel("Already have an account? Login Now");
        lblSignIn.setFont(new Font("Times New Roman", Font.PLAIN, 18));
        lblSignIn.setForeground(Color.BLACK);
        lblSignIn.setBounds(265, 560, 400, 40);
        // Set the cursor to hand when the user hovers over the label
        lblSignIn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        // Set orange colour to the button background
        lblSignIn.setBackground(new Color(255, 165, 0));
        // When mouse is hovered over the label, the text is underlined and the text colour is changed to blue
        lblSignIn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                lblSignIn.setText("<html><u>Already have an account? Login Now</u></html>");
                lblSignIn.setForeground(Color.BLUE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                lblSignIn.setText("Already have an account? Login Now");
                lblSignIn.setForeground(Color.BLACK);
            }
        });
        // When the user clicks on the label, the LoginPage is opened
        lblSignIn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                    dispose();
                    EventQueue.invokeLater(() -> {
                    try {
                        LoginPage frame = new LoginPage();
                        frame.setVisible(true);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    });
            }
        });
        paneltop.add(lblSignIn);

        // A label with red warning text to display  If you're a student/faculty, please contact your institution to provide you with an account
        JLabel lblWarning = new JLabel("If you're a student/faculty, please contact your institution to provide you with an account");
        lblWarning.setFont(new Font("Times New Roman", Font.PLAIN, 18));
        lblWarning.setForeground(Color.RED);
        lblWarning.setBounds(50, 580, 800, 40);
        paneltop.add(lblWarning);

        // A panel at the bottom of the frame to display the current date and time
        JPanel panellow = new JPanel();
        panellow.setBounds(0, 735, 1366, 30);
        // Set red colour to panel background
        panellow.setBackground(new Color(255, 49, 49));
        panellow.setLayout(null);
        mainPanel.add(panellow);

        Calendar now = Calendar.getInstance();
        String labeltime;
        final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("IST"));
        if(now.get(Calendar.AM_PM) == Calendar.AM)
            labeltime = dateFormat.format(now.getTime()) + " AM IST";
        else
            labeltime = dateFormat.format(now.getTime()) + " PM IST";
        JLabel time = new JLabel("  " + labeltime);
        time.setFont(new Font("Segoe Print", Font.BOLD, 18));
        time.setForeground(Color.WHITE);
        time.setBounds(1045, 0, 320, 30);
        Border border1 = BorderFactory.createLineBorder(Color.BLACK, 3);
        time.setBorder(border1);
        panellow.add(time);
        time.setVisible(true);
        new Timer(1000, e -> {
            // Get the IST time zone and set it to the label to display the time
            Calendar now1 = Calendar.getInstance();
            // Set the AM/PM label
            if (now1.get(Calendar.AM_PM) == Calendar.AM)
                time.setText("  " + dateFormat.format(now1.getTime()) + " AM IST");
            else
                time.setText("  " + dateFormat.format(now1.getTime()) + " PM IST");
        }).start();
        JLabel lblDevelopedBy = new JLabel("Developed By: Yogesh S and Sakthipriyan S , CSE Department, Mepco Schlenk Engineering College, Sivakasi");
        lblDevelopedBy.setFont(new Font("Segoe Print", Font.BOLD, 18));
        lblDevelopedBy.setForeground(Color.WHITE);
        lblDevelopedBy.setBounds(35, 3, 1078, 30);
        panellow.add(lblDevelopedBy);

    }

    // Function to check if the password is strong enough
    public boolean isStrongPassword(String password) {
        // Check if the password is at least 8 characters long
        if (password.length() < 8) {
            return false;
        }
        // Check if the password contains at least one uppercase letter
        if (!password.matches(".*[A-Z].*")) {
            return false;
        }
        // Check if the password contains at least one lowercase letter
        if (!password.matches(".*[a-z].*")) {
            return false;
        }
        // Check if the password contains at least one number
        if (!password.matches(".*[0-9].*")) {
            return false;
        }
        // Check if the password contains at least one special character
        return password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*");
        // If all the conditions are satisfied, the password is strong enough
    }
}
