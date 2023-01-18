package Login;

import Mail.MailSender;
import RegexChecks.CheckEmail;
import RegexChecks.CheckPhoneIndian;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.Serial;
import java.util.Objects;

public class HelpForm extends JFrame {
    @Serial
    private static final long serialVersionUID = 1L;
    private static JTextField FirstNameField, LastNameField, emailIDField, phoneNumField;
    private static JTextArea messageArea;

    /**
     * Create the frame.
     */
    public HelpForm() {
        setResizable(false);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        contentPane.setBackground(new Color(150, 173, 252));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(false);
        setAlwaysOnTop(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Adding the title image of the page to the frame
        try {
            JLabel image = new JLabel(new ImageIcon(ImageIO.read(new File("img/Contact Us/Heading1.png"))));
            image.setBounds(5, 10, 1400, 100);
            contentPane.add(image);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            JLabel image = new JLabel(new ImageIcon(ImageIO.read(new File("img/Contact Us/Heading2.png"))));
            image.setBounds(200, 180, 1000, 50);
            contentPane.add(image);
        } catch (IOException e) {
            e.printStackTrace();
        }

        JLabel lblNewLabel = new JLabel("Whether you are a student or an institution, our team is ready to answer all your questions.");
        lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
        lblNewLabel.setBounds(285, 100, 1000, 50);
        contentPane.add(lblNewLabel);

        FirstNameField = new JTextField();
        FirstNameField.setFont(new Font("Segoe Print", Font.BOLD, 15));
        FirstNameField.setBounds(550, 263, 150, 35);
        FirstNameField.setText("First Name");
        // When we press the field, the text disappears and the user can enter their own text and if the field is empty, the text reappears
        FirstNameField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (FirstNameField.getText().equals("First Name")) {
                    FirstNameField.setText("First Name");
                }
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                if (FirstNameField.getText().equals("")) {
                    FirstNameField.setText("First Name");
                }
            }
        });
        // When clicked, the first name text field is cleared
        FirstNameField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                FirstNameField.setText("");
            }
        });
        contentPane.add(FirstNameField);

        LastNameField = new JTextField();
        LastNameField.setFont(new Font("Segoe Print", Font.BOLD, 15));
        LastNameField.setBounds(730, 263, 150, 35);
        LastNameField.setText("Last Name");
        LastNameField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (LastNameField.getText().equals("Last Name")) {
                    LastNameField.setText("");
                }
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                if (LastNameField.getText().equals("")) {
                    LastNameField.setText("Last Name");
                }
            }
        });
        contentPane.add(LastNameField);

        emailIDField = new JTextField();
        emailIDField.setFont(new Font("Segoe Print", Font.BOLD, 15));
        emailIDField.setBounds(550, 313, 330, 35);
        emailIDField.setText("Email ID");
        // When we press the field, the text disappears and the user can enter their own text and if the field is empty, the text reappears
        emailIDField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (emailIDField.getText().equals("Email ID")) {
                    emailIDField.setText("");
                }
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                if (emailIDField.getText().equals("")) {
                    emailIDField.setText("Email ID");
                }
            }
        });
        contentPane.add(emailIDField);

        // Query Type Drop Down Menu
        String[] queryType = {"Select Query Type", "General Query", "Technical Query", "Application Feedback", "Other"};
        JComboBox<String> queryTypeDropDown = new JComboBox<>(queryType);
        queryTypeDropDown.setFont(new Font("Segoe Print", Font.BOLD, 15));
        queryTypeDropDown.setBounds(550, 363, 330, 35);
        contentPane.add(queryTypeDropDown);

        phoneNumField = new JTextField();
        phoneNumField.setFont(new Font("Segoe Print", Font.BOLD, 15));
        phoneNumField.setBounds(550, 413, 330, 35);
        phoneNumField.setText("Phone Number (with 91 as prefix)");
        //Allow only indian phone numbers with country code to be entered
        phoneNumField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char c = evt.getKeyChar();
                if (!((c >= '0') && (c <= '9') ||
                        (c == java.awt.event.KeyEvent.VK_BACK_SPACE) ||
                        (c == java.awt.event.KeyEvent.VK_DELETE))) {
                    getToolkit().beep();
                    evt.consume();
                }
            }
        });
        phoneNumField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (phoneNumField.getText().equals("Phone Number (with 91 as prefix)")) {
                    phoneNumField.setText("91");
                }
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                if (phoneNumField.getText().equals("")) {
                    phoneNumField.setText("Phone Number (with 91 as prefix)");
                }
            }
        });
        contentPane.add(phoneNumField);

        // Text Area for User message to be entered
        messageArea = new JTextArea();
        messageArea.setFont(new Font("Segoe Print", Font.BOLD, 15));
        messageArea.setBounds(550, 463, 330, 150);
        messageArea.setText("Your message goes here");
        // When the textarea is clicked, the text disappears and the user can enter their own text and if the field is empty, the text reappears
        messageArea.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                messageArea.setText("");
            }
        });
        messageArea.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (messageArea.getText().equals("Your message goes here")) {
                    messageArea.setText("");
                }
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                if (messageArea.getText().equals("")) {
                    messageArea.setText("Your message goes here");
                }
            }
        });
        contentPane.add(messageArea);

        JButton sendButton = new JButton("Send Message");
        sendButton.setFont(new Font("Segoe Print", Font.BOLD, 15));
        sendButton.setBounds(550, 630, 330, 35);
        sendButton.setBackground(new Color(0, 153, 0));
        sendButton.setForeground(Color.BLACK);
        sendButton.setBorder(BorderFactory.createLineBorder(Color.black, 2));
        sendButton.addActionListener(e ->
        {
            String firstName = FirstNameField.getText();
            String lastName = LastNameField.getText();
            String emailID = emailIDField.getText();
            String queryTypeSelected = Objects.requireNonNull(queryTypeDropDown.getSelectedItem()).toString();
            String phoneNum = phoneNumField.getText();
            String message = messageArea.getText();

            //Check if all fields are filled one by one and if not filled , focus on that field
            if (firstName.equals("First Name") || firstName.equals(""))
                FirstNameField.requestFocus();
            if (lastName.equals("Last Name") || lastName.equals(""))
                LastNameField.requestFocus();
            if (emailID.equals("Email ID") || emailID.equals(""))
                emailIDField.requestFocus();
            if (queryTypeSelected.equals("Select Query Type"))
                queryTypeDropDown.requestFocus();
            if (phoneNum.equals("Phone Number (with 91 as prefix)") || phoneNum.equals("91"))
                phoneNumField.requestFocus();
            if (message.equals("Your message goes here") || message.equals(""))
                messageArea.requestFocus();

            if (!CheckEmail.isValidEmail(emailID)) {
                JOptionPane.showMessageDialog(null, "Please enter a valid email ID");
                emailIDField.requestFocus();
            } else if (!CheckPhoneIndian.isValidPhone(phoneNum)) {
                JOptionPane.showMessageDialog(null, "Please enter a valid phone number");
                phoneNumField.requestFocus();
                phoneNumField.setText("91");
            }
            else
            {
                //If all fields are filled, submit the query message to the developer team
                MailSender mailSender = new MailSender();
                // A String with : New <Query Type> from <First Name> <Last Name>(<emailID>) has been received.
                String subject = "New " + queryTypeSelected + " from " + firstName + " " + lastName + "(" + emailID + ") has been received.";
                mailSender.Send_Email("alerts.thinkwave.app@gmail.com", subject, "First Name: " + firstName + "\nLast Name: " + lastName + "\nEmail ID: " + emailID + "\nQuery Type: " + queryTypeSelected + "\nPhone Number: " + phoneNum + "\nMessage: " + message);

                String ticketID = "#" + (int) (Math.random() * 1000000);
                String companyName = "ThinkWave";
                String messageToCustomer = "Hello " + firstName + " " + lastName + "," +
                        "\n\nWe are confirming that we received a new submission from you of the type: " + queryTypeSelected +
                        ".\nYour Submission number is " + ticketID +
                        ".\n\nHere is what you submitted to us:\n\n\t" + message +
                        "\n\nWe will get back to you as soon as possible." +
                        "\n\nThank you for contacting " + companyName + "." +
                        "\n\nRegards," +
                        "\n" + companyName + " Team" +
                        "\n If you have any queries, please contact us at: alerts.thinkwave.app@gmail.com" +
                        ".\n\nIf you need to add any additional information, please create a new submission indicating the submission number " + ticketID + " in message from the Contact Us Page of the application." +
                        "\n\n\t\t\t This is an auto-generated email. Please do not reply to this email." ;

                String subjectToCustomer = "Query Submission Confirmation";
                mailSender.Send_Email(emailID, subjectToCustomer, messageToCustomer);
                JOptionPane.showMessageDialog(null, "Your query has been submitted to the developers team successfully. We will get back to you soon.");

                //Clear all fields
                FirstNameField.setText("First Name");
                LastNameField.setText("Last Name");
                emailIDField.setText("Email ID");
                queryTypeDropDown.setSelectedIndex(0);
                phoneNumField.setText("Phone Number (with 91 as prefix)");
                messageArea.setText("Your message goes here");
            }
        });
        contentPane.add(sendButton);

        JButton backToHomeButton = new JButton("Return to Login Page");
        backToHomeButton.setFont(new Font("Segoe Print", Font.BOLD, 15));
        backToHomeButton.setBounds(200, 680, 200, 35);
        backToHomeButton.setBackground(Color.CYAN);
        backToHomeButton.setForeground(Color.BLUE);
        backToHomeButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        backToHomeButton.addActionListener(e ->
        {
            // Do you want to return to the login page? Yes or No
            int a = JOptionPane.showConfirmDialog(null, "Do you want to return to the login page?", "Select", JOptionPane.YES_NO_OPTION);
            if (a == 0) {
                //If yes, return to the login page
                try {
                    new LoginPage().setVisible(true);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                dispose();
            }
        });
        contentPane.add(backToHomeButton);

        JButton resetBtn = new JButton("Reset");
        resetBtn.setFont(new Font("Segoe Print", Font.BOLD, 15));
        resetBtn.setBounds(550, 680, 330, 35);
        resetBtn.setBackground(Color.BLACK);
        resetBtn.setForeground(Color.WHITE);
        resetBtn.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        resetBtn.addActionListener(e ->
        {
            FirstNameField.setText("First Name");
            LastNameField.setText("Last Name");
            emailIDField.setText("Email ID");
            queryTypeDropDown.setSelectedIndex(0);
            phoneNumField.setText("Phone Number (with 91 as prefix)");
            messageArea.setText("Your message goes here");
        });
        contentPane.add(resetBtn);

        JButton exitButton = new JButton("Exit Application");
        exitButton.setFont(new Font("Segoe Print", Font.BOLD, 15));
        exitButton.setForeground(new Color(255, 0, 0));
        exitButton.setBackground(new Color(185, 185, 2));
        exitButton.setBounds(1000, 680, 200, 35);
        // Set a red border to the button to indicate that it is an exit button
        exitButton.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
        exitButton.addActionListener(e ->
        {
            // Do you want to exit the application? Yes or No
            int a = JOptionPane.showConfirmDialog(null, "Do you want to exit the application?", "Select", JOptionPane.YES_NO_OPTION);
            if (a == 0) {
                //If yes, exit the application
                System.exit(0);
            }
        });
        contentPane.add(exitButton);
    }
}