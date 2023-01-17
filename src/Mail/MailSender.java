package Mail;


import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

public class MailSender {

    static final String User_Email = "alerts.thinkwave.app@gmail.com"; //your email
    static final String Password = "txvnfeshybthtxzi"; // your email password
    static final String Sender = "alerts.thinkwave.app@gmail.com"; // Insert Your email again


    public void Send_Email(String Receiver_mail, String Subject, String Body)
    {
        final Session newsession = Session.getInstance(this.Mail_Properties(), new Authenticator()
        {
            @Override
            // password authentication
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(User_Email, Password);
            }
        });

        // MimeMessage is used to create the email message
        try
        {
            final Message Demo_Message = new MimeMessage(newsession);
            Demo_Message.setRecipient(Message.RecipientType.TO, new InternetAddress(Receiver_mail));
            Demo_Message.setFrom(new InternetAddress(Sender));
            Demo_Message.setSubject(Subject); // email subject
            Demo_Message.setText(Body); // The content of email
            Demo_Message.setSentDate(new Date());
            Transport.send(Demo_Message);// Transport the email
        } catch (final MessagingException e)
        { // exception to catch the errors
            e.printStackTrace();
        }
    }

    // The permanent  set of properties containing string keys, the following
    // setting the properties for SMTP function
    public Properties Mail_Properties() {
        Properties Mail_Prop = new Properties();
        Mail_Prop.put("mail.smtp.host", "smtp.gmail.com");
        Mail_Prop.put("mail.smtp.socketFactory.port", "465");
        Mail_Prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        Mail_Prop.put("mail.smtp.ssl.protocols", "TLSv1.2");
        Mail_Prop.put("mail.smtp.auth", "true");
        Mail_Prop.put("mail.smtp.port", "465");
        return Mail_Prop;
    }
}