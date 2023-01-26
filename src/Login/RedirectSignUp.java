package Login;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class RedirectSignUp extends JFrame {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                RedirectSignUp frame = new RedirectSignUp();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public RedirectSignUp() {
        setResizable(false);
        JPanel mainPanel = new JPanel();
        mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(mainPanel);
        mainPanel.setLayout(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setAlwaysOnTop(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Border for the back image
        Border border = BorderFactory.createLineBorder(Color.BLACK, 3);

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

        // A panel at the top of the frame
        JPanel paneltop = new JPanel();
        paneltop.setBounds(0, 120, 1400, 295);
        // Set colour to panel background to #6610f2;
        paneltop.setBackground(new Color(102, 16, 242));
        paneltop.setLayout(null);
        mainPanel.add(paneltop);

        // Label with white large text insde the panel with "Get Started with ThinkWave Today"
        JLabel lblGetStarted = new JLabel("Get Started with ThinkWave Today");
        lblGetStarted.setForeground(Color.WHITE);
        lblGetStarted.setFont(new Font("Segoe UI", Font.BOLD, 30));
        lblGetStarted.setBounds(150, 5, 600, 70);
        paneltop.add(lblGetStarted);

        // Label with the text "No credit card required. No commitment. No strings attached." below the label with "Get Started with ThinkWave Today"
        JLabel lblNoCreditCard = new JLabel("No credit card required. No commitment. No strings attached.");
        lblNoCreditCard.setForeground(Color.WHITE);
        lblNoCreditCard.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        lblNoCreditCard.setBounds(150, 50, 600, 50);
        paneltop.add(lblNoCreditCard);

        // Label with bullet points and with text "Create and manage exams" below the label with "No credit card required. No commitment. No strings attached."
        JLabel lblCreateAndManage = new JLabel("<html><ul><li>Create and manage exams</li>" +
                "<li>Secure authentication and access control for users with OTP</li>" +
                "<li>Automated grading and feedback</li>" +
                "<li>Customizable reports</li>" +
                "<li>Easy to use and intuitive interface</li></ul></html>");
        lblCreateAndManage.setForeground(Color.WHITE);
        lblCreateAndManage.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        lblCreateAndManage.setBounds(150, 100, 600, 150);
        paneltop.add(lblCreateAndManage);

        // Label with text "Sign Up" to the side of the text "Get Started with ThinkWave Today"
        JLabel lblSignUp = new JLabel(" Sign Up with Email");
        lblSignUp.setForeground(Color.WHITE);
        lblSignUp.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblSignUp.setBounds(900, 100, 250, 50);
        // Set border around the label
        lblSignUp.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        lblSignUp.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        // If Mouse is hovered over the label, change the border to green colour
        lblSignUp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblSignUp.setBorder(BorderFactory.createLineBorder(Color.GREEN, 2));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblSignUp.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
            }
        });
        paneltop.add(lblSignUp);

        // Email ICON to the side of the label with text "Sign Up" with border around it
        JLabel lblEmailIcon = new JLabel(new ImageIcon("./img/Register/email.png"));
        lblEmailIcon.setBounds(1100, 100, 50, 50);
        paneltop.add(lblEmailIcon);

        // Split the two labels with text "Sign Up" and "Login" with a line
        JSeparator separator = new JSeparator();
        separator.setBounds(850, 180, 350, 2);
        paneltop.add(separator);

        // Label with text "Login" below the label with text "Sign Up"
        JLabel lblLogin = new JLabel(" Login with UserID");
        lblLogin.setForeground(Color.WHITE);
        lblLogin.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblLogin.setBounds(900, 200, 250, 50);
        // Set border around the label
        lblLogin.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        lblLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        // If Mouse is hovered over the label, change the border to green colour
        lblLogin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblLogin.setBorder(BorderFactory.createLineBorder(Color.GREEN, 2));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblLogin.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
            }
        });
        paneltop.add(lblLogin);

        // Login ICON to the side of the label with text "Login" with border around it
        JLabel lblLoginIcon = new JLabel(new ImageIcon("./img/Register/login.png"));
        lblLoginIcon.setBounds(1100, 200, 50, 50);
        paneltop.add(lblLoginIcon);



        // Add a panel after the paneltop panel
        JPanel panel2 = new JPanel();
        panel2.setBounds(0, 415, 1400, 320);
        // Set colour to panel background to #7bdcb5
        panel2.setBackground(new Color(123, 220, 181));
        panel2.setLayout(null);
        mainPanel.add(panel2);

        // Add a label with the text "Why Choose ThinkWave?" to the panel2
        JLabel lblWhyChoose = new JLabel("<html><u>Why Choose ThinkWave?</u></html>");
        lblWhyChoose.setForeground(new Color(102, 16, 242));
        lblWhyChoose.setFont(new Font("Segoe UI", Font.BOLD, 30));
        lblWhyChoose.setBounds(500, 3, 600, 70);
        panel2.add(lblWhyChoose);


        // Label with text  Easy to use to the panel2 below the label with the text "Why Choose ThinkWave?"
        JLabel lblEasyToUse = new JLabel("<html><h1>1) Easy to use</h1></html>");
        // Set colour to #0693e3
        lblEasyToUse.setForeground(new Color(102, 16, 242));
        lblEasyToUse.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblEasyToUse.setBounds(125, 55, 600, 50);
        panel2.add(lblEasyToUse);


        // Label with the description of the text "Easy to use" to the panel2
        JLabel lblEasyToUseDescription = new JLabel("<html><p>ThinkWave Examination System is designed to be intuitive and easy to use for both administrators and users. " +
                "It is very user friendly, allowing users to easily navigate the different features and options available.</p></html>");
        lblEasyToUseDescription.setForeground(Color.BLACK);
        lblEasyToUseDescription.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        lblEasyToUseDescription.setBounds(50, 85, 375, 190);
        panel2.add(lblEasyToUseDescription);


        // Label with the text "2) Secure" to the right of the label with the text "Easy to use"
        JLabel lblSecure = new JLabel("<html><h1>2) Secure</h1></html>");
        // Set colour to #0693e3
        lblSecure.setForeground(new Color(102, 16, 242));
        lblSecure.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblSecure.setBounds(600, 55, 600, 50);
        panel2.add(lblSecure);

        // Label with the description of the text "Secure"
        JLabel lblSecureDescription = new JLabel("<html><p>ThinkWave Examination System is secure, allowing institutions to rest assured that their data is safe and secure. " +
                "All data is backed up and the system is designed with multiple layers of security, including encryption and authentication.</p></html>");
        lblSecureDescription.setForeground(Color.BLACK);
        lblSecureDescription.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        lblSecureDescription.setBounds(500, 85, 375, 190);
        panel2.add(lblSecureDescription);

        // Label with the text "3) Flexible"  to the center below the 2 label's description to the panel2
        JLabel lblFlexible = new JLabel("<html><h1>3) Flexible</h1></html>");
        // Set colour to #0693e3
        lblFlexible.setForeground(new Color(102, 16, 242));
        lblFlexible.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblFlexible.setBounds(1050, 60, 600, 50);
        panel2.add(lblFlexible);

        // Label with the description of the text "Flexible" below the description of the text "Easy to use" to the panel2
        JLabel lblFlexibleDescription = new JLabel("<html><p>ThinkWave Examination System is flexible and can be used in a variety of ways. " +
                "It can be used to create and manage examinations, track student performance, generate reports, and more.</p></html>");
        lblFlexibleDescription.setForeground(Color.BLACK);
        lblFlexibleDescription.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        lblFlexibleDescription.setBounds(950, 85, 375, 170);
        panel2.add(lblFlexibleDescription);

        // A panel at the bottom of the frame to display the current date and time
        JPanel panellow = new JPanel();
        panellow.setBounds(0, 732, 1366, 40);
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
        time.setBounds(1045, 5, 320, 30);
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
        lblDevelopedBy.setBounds(35, 5, 1078, 30);
        panellow.add(lblDevelopedBy);




    }
}
