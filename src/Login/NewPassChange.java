package Login;

import DatabaseFunctions.UpdatePassword;
import Mail.MailSender;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static DatabaseFunctions.RetrieveUserID.retrieveUserID;

public class NewPassChange extends JFrame {
    private final JPasswordField passwordField;
    private final JPasswordField confirmPasswordField;
    private final JButton changeButton;

    public NewPassChange(String email) {
        setResizable(false);
        JPanel panel = new JPanel();
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(panel);
        panel.setLayout(null);
        panel.setBackground((new Color(26, 238, 118)));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(false);
        setAlwaysOnTop(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Adding the title image of the page to the frame
        try {
            JLabel image = new JLabel(new ImageIcon(ImageIO.read(new File("img/Login/thinkwave name.png"))));
            image.setBounds(500, 40, 1400, 100);
            panel.add(image);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Adding the title image of the page to the frame
        try {
            JLabel image = new JLabel(new ImageIcon(ImageIO.read(new File("img/Login/Reset Password.png"))));
            image.setBounds(10, 90, 1400, 100);
            panel.add(image);
        } catch (IOException e) {
            e.printStackTrace();
        }

        JLabel passwordLabel = new JLabel("New Password");
        passwordLabel.setFont(new Font("Tahoma", Font.PLAIN, 25));
        passwordLabel.setForeground(new Color(178,34,34));
        passwordLabel.setBounds(500, 200, 200, 30);
        panel.add(passwordLabel);
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Tahoma", Font.PLAIN, 25));
        passwordField.setBounds(500, 250, 300, 30);
        panel.add(passwordField);

        JLabel confirmPasswordLabel = new JLabel("Confirm Password");
        confirmPasswordLabel.setFont(new Font("Tahoma", Font.PLAIN, 25));
        confirmPasswordLabel.setForeground(new Color(178,34,34));
        confirmPasswordLabel.setBounds(500, 300, 200, 30);
        panel.add(confirmPasswordLabel);
        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setFont(new Font("Tahoma", Font.PLAIN, 25));
        confirmPasswordField.setBounds(500, 350, 300, 30);
        panel.add(confirmPasswordField);

        changeButton = new JButton("Reset Password");
        changeButton.setFont(new Font("Tahoma", Font.PLAIN, 25));
        changeButton.setBounds(525, 450, 250, 30);
        changeButton.setBackground(new Color(0, 153, 0));
        changeButton.setForeground(Color.WHITE);
        changeButton.addActionListener(e -> {
            // Check if the password and confirm password fields are not the same
            if (!Arrays.equals(passwordField.getPassword(), confirmPasswordField.getPassword())) {
                JOptionPane.showMessageDialog(changeButton, "Password and Confirm Password fields are not same", "Error", JOptionPane.ERROR_MESSAGE);
                passwordField.setText("");
                confirmPasswordField.setText("");
                passwordField.requestFocus();
            } else {
                // Check if  the password and confirm password fields are same, then set the password in a variable
                String password = new String(passwordField.getPassword());

                UpdatePassword.updatePassword((retrieveUserID(email)), password);
                JOptionPane.showMessageDialog(changeButton, "Password changed successfully and you will be redirected to login page.", "Success", JOptionPane.INFORMATION_MESSAGE);

                String subject = "Password Changed! - ThinkWave";
                String message = "\nThinkWave \n\nDid you change your password? " +
                        "\nWe noticed the password for your ThinkWave account was recently changed. If this was you, you can safely disregard this email" +
                        "\n\n\t Changed Password" +
                        "\n\t When: " + java.time.LocalDate.now() + " " + java.time.LocalTime.now() + " " + java.time.ZoneId.systemDefault() +
                        "\n\n\n If you didn't change your password, please contact us immediately us via email at alerts.thinkwave.app@gmail.com" +
                        "\n\n\n Thank you,\n The ThinkWave Team";

                // Sending the email
                MailSender mailSender = new MailSender();
                mailSender.Send_Email(email,subject, message);
                dispose();
                try {
                    new LoginPage().setVisible(true);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        panel.add(changeButton);

        JButton cancelButton = new JButton("Home");
        cancelButton.setFont(new Font("Tahoma", Font.PLAIN, 25));
        cancelButton.setBounds(525, 500, 250, 30);
        cancelButton.setBackground(Color.RED);
        cancelButton.setForeground(Color.WHITE);
        panel.add(cancelButton);
        cancelButton.addActionListener(e -> {
            dispose();
            try {
                new LoginPage().setVisible(true);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

    }
}
