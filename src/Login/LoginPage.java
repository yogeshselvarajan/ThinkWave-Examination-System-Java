package Login;

import Admin.AdminDashboard;
import Captcha.ImageGenerator;
import DatabaseFunctions.RetrieveEmailID;
import DatabaseFunctions.RetriveRole;
import DatabaseFunctions.UpdateLoginActivity;
import DatabaseFunctions.UserLoginCheck;
import DateTime.DateTimePanel;
import Faculty.FacultyDashboard;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serial;

import static Mail.LoginNotifier.sendLoginNotification;


public class LoginPage extends JFrame {
    @Serial
    private static final long serialVersionUID = 1L;
    private static JTextField institutionIDField, userIDField;
    private static JPasswordField passwordField;
    JButton btnUserLogin;
    public static String captchaFromImage, captchaEnteredByUser;
    static JPanel contentPane;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                LoginPage frame = new LoginPage();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    // Function that generates captcha by calling the ImageGenerator class and returns the captcha
    public static void generateCaptcha() throws IOException {
        try {
            // Generating the captcha image
            captchaFromImage = ImageGenerator.Generate();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Create the frame.
     */
    public LoginPage() throws IOException {
        setResizable(false);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        contentPane.setBackground(Color.LIGHT_GRAY);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(false);
        setAlwaysOnTop(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Adding the title image of the page to the frame
        try {
            JLabel image = new JLabel(new ImageIcon(ImageIO.read(new File("img/Login/thinkwave.png"))));
            image.setBounds(5, 10, 1400, 100);
            contentPane.add(image);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            JLabel image = new JLabel(new ImageIcon(ImageIO.read(new File("img/Login/slogan.png"))));
            image.setBounds(200, 125, 1000, 50);
            contentPane.add(image);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            JLabel image = new JLabel(new ImageIcon(ImageIO.read(new File("img/Login/login.png"))));
            image.setBounds(580, 190, 200, 50);
            contentPane.add(image);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            JLabel image = new JLabel(new ImageIcon(ImageIO.read(new File("./img/Login/Login UI Image.png"))));
            image.setBounds(15, 200, 600, 500);
            contentPane.add(image);
        } catch (IOException e) {
            e.printStackTrace();
        }

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

        // A label with the "Home" text to go back to the home page
        JLabel lblHome = new JLabel("   Home");
        lblHome.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int a = JOptionPane.showConfirmDialog(institutionIDField, "Do you want to go to Home Page?", "Select", JOptionPane.YES_NO_OPTION);
                if (a == JOptionPane.YES_OPTION) {
                    dispose();
                    EventQueue.invokeLater(() -> {
                    });
                }
            }
        });
        lblHome.setForeground(Color.BLACK);
        lblHome.setFont(new Font("Tahoma", Font.PLAIN, 20));
        lblHome.setBounds(1050, 10, 100, 30);
        lblHome.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        lblHome.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        // When mouse hovers
        paneltop2.add(lblHome);

        // A label with the "Any Queries" text to go to the contact us page
        JLabel lblAnyQueries = new JLabel(" Contact Us");
        lblAnyQueries.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int a = JOptionPane.showConfirmDialog(institutionIDField, "Do you want to be redirected to the raise query page?", "Select", JOptionPane.YES_NO_OPTION);
                if (a == JOptionPane.YES_OPTION) {
                    dispose();
                    EventQueue.invokeLater(() -> {
                        try {
                            HelpForm frame = new HelpForm();
                            frame.setVisible(true);
                        } catch (Exception exp1) {
                            exp1.printStackTrace();
                        }
                    });
                }
            }
        });
        lblAnyQueries.setForeground(Color.BLACK);
        lblAnyQueries.setFont(new Font("Tahoma", Font.PLAIN, 20));
        lblAnyQueries.setBounds(1170, 10, 125, 30);
        lblAnyQueries.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        lblAnyQueries.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        paneltop2.add(lblAnyQueries);

        // A panel side to the Login Image UI image to hold all the text fields and buttons
        JPanel loginpanel = new JPanel();
        loginpanel.setBounds(650, 248, 712, 480);
        loginpanel.setBackground(Color.LIGHT_GRAY);
        // Set red border to the panel
        loginpanel.setBorder(new LineBorder(new Color(255, 0, 0), 3));
        // Set background colour to the panel to #FCCF31
        loginpanel.setBackground(new Color(252, 207, 49));
        contentPane.add(loginpanel);
        loginpanel.setLayout(null);
        contentPane.add(loginpanel);

        // Label with the text: Log in with your data that you have entered during your registration.
        JLabel lblLoginWithYour = new JLabel("Log in with your data that you have entered during your registration.");
        lblLoginWithYour.setForeground(Color.BLACK);
        lblLoginWithYour.setFont(new Font("Times New Roman", Font.ROMAN_BASELINE, 24));
        lblLoginWithYour.setBounds(40, 5, 700, 52);
        lblLoginWithYour.setForeground(Color.BLACK);
        loginpanel.add(lblLoginWithYour);


        JLabel lblInstituteID = new JLabel("Institution ID :");
        lblInstituteID.setBackground(Color.BLACK);
        lblInstituteID.setForeground(Color.BLACK);
        lblInstituteID.setFont(new Font("Times New Roman", Font.ROMAN_BASELINE, 28));
        lblInstituteID.setBounds(150, 50, 200, 52);
        lblInstituteID.setForeground(new Color(178, 34, 34));
        loginpanel.add(lblInstituteID);
        institutionIDField = new JTextField();
        institutionIDField.setFont(new Font("Segoe Print", Font.PLAIN, 22));
        institutionIDField.setBounds(325, 60, 100, 30);
        institutionIDField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        // Action Listener to allow at most 5 characters to be entered in the text field
        institutionIDField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (institutionIDField.getText().length() >= 5) // limit textfield to 5 characters
                    e.consume();
            }
        });
        loginpanel.add(institutionIDField);

        JLabel lblUserID = new JLabel("User ID :");
        lblUserID.setForeground(Color.BLACK);
        lblUserID.setBackground(Color.CYAN);
        lblUserID.setFont(new Font("Times New Roman", Font.ROMAN_BASELINE, 28));
        lblUserID.setBounds(150, 125, 193, 52);
        lblUserID.setForeground(new Color(178, 34, 34));
        loginpanel.add(lblUserID);
        userIDField = new JTextField();
        userIDField.setFont(new Font("Times New Roman", Font.PLAIN, 22));
        userIDField.setBounds(325, 125, 100, 35);
        // Action Listener to allow at most 5 characters to be entered in the text field
        userIDField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (userIDField.getText().length() >= 5) // limit textfield to 5 characters
                    e.consume();
            }
        });
        userIDField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        loginpanel.add(userIDField);

        JLabel lblPassword = new JLabel("Password :");
        lblPassword.setForeground(Color.BLACK);
        lblPassword.setBackground(Color.CYAN);
        lblPassword.setFont(new Font("Times New Roman", Font.ROMAN_BASELINE, 28));
        lblPassword.setBounds(150, 200, 193, 52);
        lblPassword.setForeground(new Color(178, 34, 34));
        loginpanel.add(lblPassword);
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        passwordField.setBounds(325, 205, 160, 35);
        passwordField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        passwordField.setEchoChar('*');
        loginpanel.add(passwordField);

        // A image icon to show the password if clicked
        ImageIcon showPasswordIcon = new ImageIcon("./img/Login/show.png");
        JLabel showPassword = new JLabel(showPasswordIcon);
        showPassword.setBounds(485, 205, 35, 35);
        showPassword.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (passwordField.getEchoChar() == '*') {
                    passwordField.setEchoChar((char) 0);
                } else {
                    passwordField.setEchoChar('*');
                }
            }
        });
        // Add border around the image icon
        showPassword.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        loginpanel.add(showPassword);

        // A image icon to indicate Caps Lock if the field has Caps Lock on
        ImageIcon capsLockIcon = new ImageIcon("./img/Login/capslock.png");
        JLabel capsLock = new JLabel(capsLockIcon);
        capsLock.setBounds(520, 205, 35, 35);
        capsLock.setVisible(false);
        loginpanel.add(capsLock);



// Add a key listener to detect when caps lock is on
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK))
                    capsLock.setVisible(true);
                else
                    capsLock.setVisible(false);
            }
        });

        JLabel lblForgetPassword = new JLabel("Forgot Password? Click here");
        lblForgetPassword.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblForgetPassword.setFont(new Font("Times New Roman", Font.ROMAN_BASELINE, 20));
        lblForgetPassword.setForeground(new Color(0, 0, 0));
        lblForgetPassword.setBounds(370, 240, 240, 40);
        // When mouse is hovered over the label, the label changes colour to blue and underlines the text and
        // when the mouse is not hovered over the label, the label changes colour to black and removes the underline
        lblForgetPassword.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                lblForgetPassword.setForeground(new Color(0, 0, 255));
                lblForgetPassword.setText("<html><u>Forgot Password? Click here</u></html>");
            }
            @Override
            public void mouseExited(MouseEvent e) {
                lblForgetPassword.setForeground(new Color(0, 0, 0));
                lblForgetPassword.setText("Forgot Password? Click here");
            }
        });
        lblForgetPassword.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int a = JOptionPane.showConfirmDialog(institutionIDField, "Do you want to reset your password?", "Reset Password", JOptionPane.YES_NO_OPTION);
                if (a == JOptionPane.YES_OPTION) {
                    dispose();
                    EventQueue.invokeLater(() -> {
                        try {

                        } catch (Exception exp1) {
                            exp1.printStackTrace();
                        }
                    });
                }
            }
        });
        loginpanel.add(lblForgetPassword);


        try {
            generateCaptcha();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        // Adding the captcha image to the frame
        BufferedImage image = ImageIO.read(new File("./img/Login/CaptchaText.png"));
        JLabel loadedImage = new JLabel(new ImageIcon(image));
        loadedImage.setBounds(425, 285, 150, 35);
        loadedImage.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        loginpanel.add(loadedImage);

        JTextField enterCaptcha = new JTextField();
        enterCaptcha.setFont(new Font("Times New Roman", Font.ROMAN_BASELINE, 25));
        enterCaptcha.setBounds(180, 285, 225, 35);
        enterCaptcha.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        // Set the "Enter Captcha" as placeholder text
        enterCaptcha.setText("   Enter Captcha Text");
        // When text field is clicked, the default text is removed
        enterCaptcha.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                enterCaptcha.setText("");
            }
        });
        // Allow only 6 characters to be entered in the captcha field key listener
        enterCaptcha.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (enterCaptcha.getText().length() >= 6) // limit text-field to 6 characters
                    e.consume();
            }
        });
        loginpanel.add(enterCaptcha);

        /* Button to log in as a user (both institution & user level) */
        btnUserLogin = new JButton("Login");
        btnUserLogin.setFont(new Font("Tahoma", Font.PLAIN, 20));
        btnUserLogin.setForeground(new Color(174, 34, 34));
        btnUserLogin.setBackground(new Color(255, 192, 203));
        btnUserLogin.setBounds(400, 355, 105, 35);
        btnUserLogin.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        // Set a green border around the button when the mouse hovers over it and remove it when the mouse exits
        btnUserLogin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnUserLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                btnUserLogin.setBorder(BorderFactory.createLineBorder(Color.GREEN, 3));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btnUserLogin.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
            }
        });
        btnUserLogin.addActionListener(e ->
        {
            getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)); // change cursor to loading icon

            String institutionID = String.valueOf(institutionIDField.getText());
            String userID = String.valueOf(userIDField.getText());
            String password = String.valueOf(passwordField.getPassword());
            captchaEnteredByUser = enterCaptcha.getText();

            String email = RetrieveEmailID.retrieveEmail(Integer.parseInt(userID));

            String login_result = UserLoginCheck.checkUserLogin(userID, institutionID, password, captchaFromImage, captchaEnteredByUser);

            if (institutionID.equals("") || userID.equals("") || password.equals("") || captchaEnteredByUser.equals("")) {
                getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)); // change cursor to default icon
                JOptionPane.showMessageDialog(btnUserLogin, "One Or More Fields Are Empty", "Empty Fields", JOptionPane.WARNING_MESSAGE);
                if (institutionID.equals(""))
                    institutionIDField.requestFocus();
                else if (userID.equals(""))
                    userIDField.requestFocus();
                else if (password.equals(""))
                    passwordField.requestFocus();
                else
                    enterCaptcha.requestFocus();
            }
            else if (login_result.equals("Already a session active"))
            {String prompt_message = "<html><body>" +
                    "<p style='font-size:12pt;font-weight:bold;'>Login Failed</p>" +
                    "<p>Sorry, you cannot log in at this time as there is already an active session for your user " +
                    "account. Our system allows only one active session per user at a time.</p>" +
                    "<p>Please logout from the existing session before attempting to log in again. </p>" +
                    "<p>Thank you for your understanding.</p>" +
                    "<br>" +
                    "<p>If the active session currently present is not initiated by you, " +
                    "please raise a ticket through the application immediately to secure your account.</p>" +
                    "<p style='color:red;font-weight:bold;'>IMPORTANT: Do not share your login credentials with anyone " +
                    "and always log out from your account when you are done. </p>" +
                    "</body></html>";
                JOptionPane.showMessageDialog(btnUserLogin,prompt_message, "Login Failed", JOptionPane.ERROR_MESSAGE);
                getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)); // change cursor to default icon
                userIDField.setText("");
                passwordField.setText("");
                enterCaptcha.setText("Enter Captcha Text");
                userIDField.requestFocus();

            }
            else if(login_result.equals("User not found"))
            {
                getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)); // change cursor to default icon
                JOptionPane.showMessageDialog(btnUserLogin, "User not found!", "Login Failed", JOptionPane.ERROR_MESSAGE);
                userIDField.setText("");
                passwordField.setText("");
                enterCaptcha.setText("Enter Captcha Text");
                userIDField.requestFocus();
            }
            else if(login_result.equals("Invalid Credentials")) {
                JOptionPane.showMessageDialog(btnUserLogin, "Invalid username/password (or) Wrong Captcha text entered!", "Login Error", JOptionPane.ERROR_MESSAGE);
                getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)); // change cursor to default icon
                userIDField.setText("");
                passwordField.setText("");
                enterCaptcha.setText("Enter Captcha Text");
                userIDField.requestFocus();
            }
            else
            {
                UpdateLoginActivity.updateLoginActivity(Integer.parseInt(userID));
                String role = RetriveRole.getRole(userID);
                try {
                    sendLoginNotification(email, userID);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

                String message = "<html><body style='width: 300px;'><p style='font-size: 16px;'>Logging in will create a new session.</p><p style='font-size: 16px;'>Each user is allowed only one session at a time to ensure secure access to the application.</p></body></html>";
                JOptionPane.showMessageDialog(btnUserLogin, message, "Application Message", JOptionPane.INFORMATION_MESSAGE);

                dispose();

                EventQueue.invokeLater(() -> {
                    try {
                        if (role.equals("Admin")) {

                            AdminDashboard.getUserIDFromLogin(userID);
                            AdminDashboard.main(null);
                        } else if (role.equals("Faculty")) {
                            FacultyDashboard.getUserIDFromLogin(userID);
                            FacultyDashboard.main(null);
                        } else if (role.equals("Student")) {
                            /*StudentDashboard.getUserIDFromLogin(userID);
                            StudentDashboard.main(null);*/

                        } else
                        {

                        }
                    } catch (Exception exp1) {
                        exp1.printStackTrace();
                    }
                });
            }

        });
        loginpanel.add(btnUserLogin);


        /* Button to reset the fields */
        JButton btnReset = new JButton("Reset");
        btnReset.setFont(new Font("Tahoma", Font.PLAIN, 20));
        btnReset.setForeground(new Color(174, 34, 34));
        btnReset.setBackground(new Color(255, 192, 203));
        btnReset.setBounds(275, 355, 100, 35);
        btnReset.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        btnReset.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        // Set a red border around the button when the mouse hovers over it and remove it when the mouse exits
        btnReset.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnReset.setBorder(BorderFactory.createLineBorder(Color.RED, 3));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btnReset.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
            }
        });
        btnReset.addActionListener(e -> {
            int a = JOptionPane.showConfirmDialog(btnReset, "Do you really want to reset all the fields?");
            if (a == JOptionPane.YES_OPTION) {
                institutionIDField.setText("");
                userIDField.setText("");
                passwordField.setText("");
                enterCaptcha.setText("");
                institutionIDField.requestFocus();
            }
        });
        loginpanel.add(btnReset);

        // Label for new user
        JLabel lblNewUser = new JLabel("Click here to Get started with us for free!");
        lblNewUser.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblNewUser.setFont(new Font("Times New Roman", Font.ROMAN_BASELINE, 20));
        lblNewUser.setForeground(new Color(0, 0, 0));
        lblNewUser.setBounds(225, 390, 330, 35);
        lblNewUser.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                lblNewUser.setForeground(new Color(0, 0, 255));
                lblNewUser.setText("<html><u>Click here to Get started with us for free!</u></html>");
            }
            @Override
            public void mouseExited(MouseEvent e) {
                lblNewUser.setForeground(new Color(0, 0, 0));
                lblNewUser.setText("Click here to Get started with us for free!");
            }
        });
        lblNewUser.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                EventQueue.invokeLater(() -> {
                    try {

                    } catch (Exception exp1) {
                        exp1.printStackTrace();
                    }
                });
            }
        });
        loginpanel.add(lblNewUser);

        // A Button at right side of lblNewUser to close the application
        JButton btnClose = new JButton("Exit Application");
        btnClose.setFont(new Font("Tahoma", Font.PLAIN, 20));
        btnClose.setForeground(new Color(0, 0, 0));
        btnClose.setBackground(new Color(255, 192, 203));
        btnClose.setBounds(300, 430, 170, 40);
        btnClose.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        btnClose.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        // Set a red border around the button when the mouse hovers over it and remove it when the mouse exits
        btnClose.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnClose.setBorder(BorderFactory.createLineBorder(Color.RED, 3));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btnClose.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
            }
        });
        btnClose.addActionListener(e -> {
            int a = JOptionPane.showConfirmDialog(btnClose, "Are you sure you want to exit the application?", "Select", JOptionPane.YES_NO_OPTION);
            if (a == 0)
                System.exit(0);
        });
        loginpanel.add(btnClose);

        // Add the date time panel to the content pane
        DateTimePanel dateTimePanel = new DateTimePanel();
        dateTimePanel.addTimeFooter(contentPane);
        dateTimePanel.setVisible(true);

    }

}