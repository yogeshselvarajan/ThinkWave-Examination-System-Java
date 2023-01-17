package Login;

import FetchFromDatabase.AdminLoginCheck;
import FetchFromDatabase.UserLoginCheck;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.Serial;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class LoginPage extends JFrame {
    @Serial
    private static final long serialVersionUID = 1L;
    private static JTextField institutionIDField, userIDField;
    private static JPasswordField passwordField;
    private final JButton btnUserLogin;

    /**
     * Launch the application.
     */
    public static void main(String[] args)
    {
        EventQueue.invokeLater(() -> {
            try {
                LoginPage frame = new LoginPage();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Create the frame.
     */
    public LoginPage()
    {
        setResizable(false);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        contentPane.setBackground((new Color(26, 238, 118)));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setAlwaysOnTop(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        // Adding the title image of the page to the frame
        try {
            JLabel image = new JLabel(new ImageIcon(ImageIO.read(new File("img/thinkwave.png"))));
            image.setBounds(5, 10, 1400, 100);
            contentPane.add(image);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            JLabel image = new JLabel(new ImageIcon(ImageIO.read(new File("img/slogan.png"))));
            image.setBounds(200, 125, 1000, 50);
            contentPane.add(image);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            JLabel image = new JLabel(new ImageIcon(ImageIO.read(new File("img/login.png"))));
            image.setBounds(580, 190, 200, 50);
            contentPane.add(image);
        } catch (IOException e) {
            e.printStackTrace();
        }

        JLabel lblInstituteID = new JLabel("Institution ID :");
        lblInstituteID.setBackground(Color.BLACK);
        lblInstituteID.setForeground(Color.BLACK);
        lblInstituteID.setFont(new Font("Tahoma", Font.PLAIN, 25));
        lblInstituteID.setBounds(470, 260, 193, 52);
        lblInstituteID.setForeground(new Color(178,34,34));
        contentPane.add(lblInstituteID);
        institutionIDField = new JTextField();
        institutionIDField.setFont(new Font("Segoe Print", Font.BOLD, 20));
        institutionIDField.setBounds(670, 263, 80, 35);
        institutionIDField.setColumns(5);
        contentPane.add(institutionIDField);

        JLabel lblUserID = new JLabel("User ID :");
        lblUserID.setForeground(Color.BLACK);
        lblUserID.setBackground(Color.CYAN);
        lblUserID.setFont(new Font("Tahoma", Font.PLAIN, 25));
        lblUserID.setBounds(470, 330, 193, 52);
        lblUserID.setForeground(new Color(178,34,34));
        contentPane.add(lblUserID);
        userIDField = new JTextField();
        userIDField.setFont(new Font("Sogoe Print", Font.BOLD, 20));
        userIDField.setBounds(670, 333, 80, 35);
        userIDField.setColumns(5);
        contentPane.add(userIDField);

        JLabel lblPassword = new JLabel("Password :");
        lblPassword.setForeground(Color.BLACK);
        lblPassword.setBackground(Color.CYAN);
        lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 25));
        lblPassword.setBounds(470, 400, 193, 52);
        lblPassword.setForeground(new Color(178,34,34));
        contentPane.add(lblPassword);
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Sogoe Print", Font.BOLD, 20));
        passwordField.setBounds(670, 400, 160, 35);
        contentPane.add(passwordField);

        /* Button to log in as a user (both institution & user level) */
        btnUserLogin = new JButton("Login");
        btnUserLogin.setFont(new Font("Tahoma", Font.PLAIN, 26));
        btnUserLogin.setForeground(new Color(174,34,34));
        btnUserLogin.setBackground(new Color(0,191,255));
        btnUserLogin.setBounds(505, 460, 100, 40);
        btnUserLogin.addActionListener(e ->
        {
            String institutionID = String.valueOf(institutionIDField.getText());
            String userID = String.valueOf(userIDField.getText());
            String password = String.valueOf(passwordField.getPassword());

            if(UserLoginCheck.checkUserLogin(userID, institutionID, password))
            {
                JOptionPane.showMessageDialog(btnUserLogin, "You have successfully logged in");
                dispose();
            }
            else if (institutionID.equals("") || userID.equals("") || password.equals(""))
            {
                JOptionPane.showMessageDialog(null, "One Or More Fields Are Empty", "Empty Fields", JOptionPane.WARNING_MESSAGE);
                if (institutionID.equals(""))
                    institutionIDField.requestFocus();
                else if (userID.equals(""))
                    userIDField.requestFocus();
                else passwordField.requestFocus();
            }
            else
            {
                JOptionPane.showMessageDialog(btnUserLogin, "Wrong Username & Password!", "Login Error", JOptionPane.ERROR_MESSAGE);
                userIDField.setText("");
                passwordField.setText("");
                userIDField.requestFocus();
            }
        });
        contentPane.add(btnUserLogin);

        /* Button to reset the fields */
        JButton btnReset = new JButton("Reset");
        btnReset.setFont(new Font("Tahoma", Font.PLAIN, 26));
        btnReset.setForeground(new Color(174,34,34));
        btnReset.setBackground(new Color(0,191,255));
        btnReset.setBounds(640, 460, 100, 40);
        btnReset.addActionListener(e -> {
            int a = JOptionPane.showConfirmDialog(btnReset, "Do you really want to reset all the fields?");
            if (a == JOptionPane.YES_OPTION) {
                institutionIDField.setText("");
                userIDField.setText("");
                passwordField.setText("");
                institutionIDField.requestFocus();
            }
        });
        contentPane.add(btnReset);

        /* Button to verify the admin login */
        JButton btnAdminLogin = new JButton("Administrator Login");
        btnAdminLogin.setFont(new Font("Tahoma", Font.PLAIN, 18));
        btnAdminLogin.setBounds(300, 530, 200, 40);
        btnAdminLogin.setForeground(new Color(174,34,34));
        btnAdminLogin.setBackground(new Color(0,191,255));
        btnAdminLogin.addActionListener(e ->
        {
            String institutionID = String.valueOf(institutionIDField.getText());
            String userID = String.valueOf(userIDField.getText());
            String password = String.valueOf(passwordField.getPassword());

            if (AdminLoginCheck.checkAdminLogin(userID, institutionID, password)) {
                JOptionPane.showMessageDialog(btnAdminLogin, "You have successfully logged in (Administrator)");
                dispose();
            } else if (institutionID.equals("") || userID.equals("") || password.equals(""))
            {
                JOptionPane.showMessageDialog(null, "One Or More Fields Are Empty", "Empty Fields", JOptionPane.WARNING_MESSAGE);
                if (institutionID.equals(""))
                    institutionIDField.requestFocus();
                else if (userID.equals(""))
                    userIDField.requestFocus();
                else passwordField.requestFocus();
            }
            else
            {
                JOptionPane.showMessageDialog(btnAdminLogin, "Wrong Admin Username & Password!", "Login Error", JOptionPane.ERROR_MESSAGE);
                userIDField.setText("");
                passwordField.setText("");
                userIDField.requestFocus();
            }
        });
        contentPane.add(btnAdminLogin);

        /* Button to open forget password frame */
        JButton btnForgetPassword = new JButton("Forget Password?");
        btnForgetPassword.setFont(new Font("Tahoma", Font.PLAIN, 18));
        btnForgetPassword.setBounds(525, 530, 180, 40);
        btnForgetPassword.setForeground(new Color(174,34,34));
        btnForgetPassword.setBackground(new Color(0,191,255));
        btnForgetPassword.addActionListener(e ->
        {
            dispose();
            /*ForgetPassword ob = new ForgetPassword();
            ob.main(null);*/
        });
        contentPane.add(btnForgetPassword);

        /* Button to open the registration form */
        JButton btnRegister = new JButton("Register with Us!");
        btnRegister.setFont(new Font("Tahoma", Font.PLAIN, 18));
        btnRegister.setBounds(735, 530, 200, 40);
        btnRegister.setForeground(new Color(174,34,34));
        btnRegister.setBackground(new Color(0,191,255));
        btnRegister.addActionListener(e ->
        {
            dispose();
            /*GetBasicDetails obj = new GetBasicDetails();
            obj.main(null);*/

        });
        contentPane.add(btnRegister);

        /* Button to raise query to the dev team */
        JButton btnHelpQuery = new JButton("Contact Us");
        btnHelpQuery.setFont(new Font("Tahoma", Font.PLAIN, 18));
        btnHelpQuery.setBounds(480, 600, 150, 40);
        btnHelpQuery.setForeground(new Color(174,34,34));
        btnHelpQuery.setBackground(new Color(0,191,255));
        btnHelpQuery.addActionListener(e ->
        {
            // Do you want to be redirected to the raise query page?
            int a = JOptionPane.showConfirmDialog(btnHelpQuery, "Do you want to be redirected to the raise query page?");
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
        });
        contentPane.add(btnHelpQuery);

        /* Button to close the application */
        JButton btnExit = new JButton("Exit Application");
        btnExit.setFont(new Font("Tahoma", Font.PLAIN, 18));
        btnExit.setBounds(680, 600, 200, 40);
        btnExit.setForeground(new Color(174,34,34));
        btnExit.setBackground(new Color(0,191,255));
        btnExit.addActionListener(e ->
        {
            int a = JOptionPane.showConfirmDialog(btnExit, "Are you sure you want to exit the application?", "Select", JOptionPane.YES_NO_OPTION);
            if (a == 0)
                System.exit(0);
        });
        contentPane.add(btnExit);
    }

    // Function to get the UserName typed by the user when he logs in
    public static String getUserID() {
        return userIDField.getText();
    }
}