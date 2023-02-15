package Login;

import DatabaseFunctions.CheckEmailExists;
import DateTime.DateTimePanel;
import Mail.MailSender;
import RegexChecks.CheckEmail;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import java.io.File;
import java.io.IOException;

import static DatabaseFunctions.RetrieveUserID.retrieveUserID;

public class ResetPassword extends JFrame
{
    String verificationCode = null;
    String userID = null;
    /**
     * Launch the application.
     */
    public static void main(String[] args)
    {
        EventQueue.invokeLater(() -> {
            try {
                ResetPassword obj1 = new ResetPassword();
                obj1.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    // Function that generates a verification code of type <TW-XXXXXX> where X is a random digit
    public static String generateVerificationCode()
    {
        String verificationCode = "TW-";
        for (int i = 0; i < 6; i++) {
            int randomDigit = (int) (Math.random() * 10);
            verificationCode += randomDigit;
        }
        return verificationCode;
    }

    public ResetPassword()
    {
        JPanel frame;
        setResizable(false);
        frame = new JPanel();
        frame.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(frame);
        frame.setLayout(null);
        frame.setBackground((new Color(26, 238, 118)));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(false);
        setAlwaysOnTop(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Adding the title image of the page to the frame
        try {
            JLabel image = new JLabel(new ImageIcon(ImageIO.read(new File("img/Login/thinkwave name.png"))));
            image.setBounds(500, 30, 1400, 100);
            frame.add(image);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            JLabel image = new JLabel(new ImageIcon(ImageIO.read(new File("img/Login/forgot password.png"))));
            image.setBounds(30, 50, 400, 700);
            frame.add(image);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            JLabel image = new JLabel(new ImageIcon(ImageIO.read(new File("img/Login/ForgotPass Heading.png"))));
            image.setBounds(200, 90, 1400, 300);
            frame.add(image);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String subtext = "Enter your email address associated with your ThinkWave account";
        JLabel sublabel1= new JLabel(subtext);
        sublabel1.setFont(new Font("Arial", Font.PLAIN, 20));
        sublabel1.setBounds(600, 150, 1400, 300);
        frame.add(sublabel1);

        String subtext2 = "and we will send you a link to reset your password.";
        JLabel sublabel2= new JLabel(subtext2);
        sublabel2.setFont(new Font("Arial", Font.PLAIN, 20));
        sublabel2.setBounds(700, 180, 1400, 300);
        frame.add(sublabel2);

        JLabel emailID = new JLabel("Email :");
        emailID.setBackground(Color.BLACK);
        emailID.setForeground(Color.BLACK);
        emailID.setFont(new Font("Tahoma", Font.PLAIN, 25));
        emailID.setBounds(700, 350, 193, 30);
        emailID.setForeground(new Color(178,34,34));
        frame.add(emailID);

        JTextField emailField = new JTextField();
        emailField.setFont(new Font("Tahoma", Font.PLAIN, 25));
        emailField.setBounds(700, 400, 400, 30);
        emailField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (emailField.getText().length() >= 30) // limit textfield to 30 characters
                    e.consume();
            }
        });
        frame.add(emailField);

        // Button that says Wait, I remember my password
        JButton rememberPass = new JButton("Wait, I remember my password");
        rememberPass.setBackground(Color.CYAN);
        rememberPass.setForeground(Color.BLUE);
        rememberPass.setFont(new Font("Tahoma", Font.PLAIN, 15));
        rememberPass.setBounds(700, 460, 250, 30);
        rememberPass.addActionListener(e -> {
            int dialogResult = JOptionPane.showConfirmDialog (rememberPass, "Are you sure you want to head to the login page and try to login again?","Warning",JOptionPane.YES_NO_OPTION);
            if(dialogResult == JOptionPane.YES_OPTION) {
                // Going to the login page
                dispose();
                LoginPage obj1;
                try {
                    obj1 = new LoginPage();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                obj1.setVisible(true);
            }
        });
        frame.add(rememberPass);

        // Button that says Send Verification Code
        JButton btnsendCode = new JButton("Send Verification Code");
        btnsendCode.setBackground(Color.BLUE);
        btnsendCode.setForeground(Color.WHITE);
        btnsendCode.setFont(new Font("Tahoma", Font.PLAIN, 15));
        btnsendCode.setBounds(700, 500, 250, 30);
        btnsendCode.addActionListener(e ->
        {
            // Checking if the email field is empty
            if (emailField.getText().equals(""))
            {
                JOptionPane.showMessageDialog(btnsendCode, "Please enter your email address to proceed.", "Error", JOptionPane.ERROR_MESSAGE);
                emailField.requestFocus();
            }
            else if(!CheckEmail.isValidEmail(emailField.getText()))
            {
                JOptionPane.showMessageDialog(btnsendCode, "Please enter a valid email address format.", "Error", JOptionPane.ERROR_MESSAGE);
                emailField.setText("");
                emailField.requestFocus();
            }
            if(!CheckEmailExists.checkEmailExists(emailField.getText()))
            {
                JOptionPane.showMessageDialog(btnsendCode, "The email address you entered is not associated with any ThinkWave account.");
                // Dialog box to ask if the user wants to try again resetting the password or head to the login page
                int dialogResult = JOptionPane.showConfirmDialog (btnsendCode, "Do you want to try entering email again?","Warning",JOptionPane.YES_NO_OPTION);
                if(dialogResult == JOptionPane.YES_OPTION) {
                    emailField.setText("");
                    // Focus on the email field
                    emailField.requestFocus();
                }
            }
            else
            {
                userID = retrieveUserID(emailField.getText());
                verificationCode = generateVerificationCode();

                // Sending the verification code to the email address
                String subject = "ThinkWave Account Password Reset";
                String message = "\nThinkWave Account \nPassword Reset Code" +
                        "\n\nPlease use the following code to reset the password for the ThinkWave account associated with this email address : " + emailField.getText() +
                        "\n\n Here is your verification code : " + verificationCode +
                        ".\nEnter this code in the prompted field asked at ThinkWave Forgot Password Page to reset your password." +
                        "\n\n\n If you did not request a password reset, please ignore this email. If you feel that your account has been compromised, " +
                        "please contact us via email at alerts.thinkwave.app@gmail.com .\n\n\n Thanks,\n The ThinkWave Account Team";

                // Sending the email
                MailSender mailSender = new MailSender();
                mailSender.Send_Email(emailField.getText(), subject, message);

                JOptionPane.showMessageDialog(btnsendCode, "Verification code has been sent to your email address. Please check your inbox. If you do not see the email in your inbox, please check your spam folder.");

                // Show the dialog box to enter the verification code
                String code = JOptionPane.showInputDialog(btnsendCode, "Please enter the verification code sent to your email address : ");
                if (code != null) {
                    // Checking if the verification code is correct
                    if (code.equals(verificationCode)) {
                        // Going to the reset password page
                        // Show verification success dialog box
                        JOptionPane.showMessageDialog(btnsendCode, "Verification successful. Please enter your new password on the next page.");
                        dispose();
                        // Run the NewPasswordPage class
                        NewPassChange obj2 = new NewPassChange(emailField.getText());
                        obj2.setVisible(true);

                    } else {
                        // Show verification failed dialog box
                        JOptionPane.showMessageDialog(btnsendCode, "Verification failed. Please try again.");
                        emailField.setText("");
                        emailField.requestFocus();
                    }
                }
            }
        });
        frame.add(btnsendCode);

        // Button that says Back To Log in on the right bottom corner
        JButton btnBackToLogin = new JButton("Back To Login");
        btnBackToLogin.setBackground(Color.CYAN);
        btnBackToLogin.setForeground(Color.BLUE);
        btnBackToLogin.setFont(new Font("Tahoma", Font.PLAIN, 15));
        btnBackToLogin.setBounds(700, 540, 250, 30);
        btnBackToLogin.addActionListener(e -> {
            int dialogResult = JOptionPane.showConfirmDialog (btnBackToLogin, "Are you sure you want to head to the login page?","Warning",JOptionPane.YES_NO_OPTION);
            if(dialogResult == JOptionPane.YES_OPTION) {
                // Going to the login page
                dispose();
                LoginPage obj1;
                try {
                    obj1 = new LoginPage();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                obj1.setVisible(true);
            }
        });
        frame.add(btnBackToLogin);

        // Button that exits the application
        JButton btnExit = new JButton("Exit");
        btnExit.setBackground(Color.RED);
        btnExit.setForeground(Color.WHITE);
        btnExit.setFont(new Font("Tahoma", Font.PLAIN, 15));
        btnExit.setBounds(700, 580, 250, 30);
        btnExit.addActionListener(e -> {
            int dialogResult = JOptionPane.showConfirmDialog (btnExit, "Are you sure you want to exit the application?","Warning",JOptionPane.YES_NO_OPTION);
            if(dialogResult == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        frame.add(btnExit);

        DateTimePanel dateTimePanel = new DateTimePanel();
        dateTimePanel.addTimeFooter(frame);
        dateTimePanel.setVisible(true);
    }
}

