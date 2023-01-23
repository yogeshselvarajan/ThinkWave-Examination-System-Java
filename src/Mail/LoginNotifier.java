package Mail;

import FetchFromDatabase.RetrieveEmailID;
import FetchFromDatabase.RetriveUserName;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class LoginNotifier {

    public static void sendLoginNotification(String email,String id) throws UnknownHostException {
        final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Calendar now = Calendar.getInstance();
        RetriveUserName ob1 = new RetriveUserName();
        String name = ob1.getUserName(id);
        String IP = InetAddress.getLocalHost().getHostAddress();


        String subject = "New Login Detected! - ThinkWave Examination System";
        String body = "Hello " + name + ",\n\n" +
                "\tThis is to notify you that you have been successfully logged into the ThinkWave Examination System recently.\n\n" +
                "\tWhen: " + dateFormat.format(now.getTime()) + "\n" +
                "\nDevice Details:\n" +
                "\t 1) Device Name: " + InetAddress.getLocalHost().getHostName() + "\n" +
                "\t 2) Device Operating Systen: " + System.getProperty("os.name")  + " version-" + System.getProperty("os.version") + "\n" +
                "\t 3) Device Architecture: " + System.getProperty("os.arch") + "\n" +
                "\n\n Network Details:\n" +
                "\t 1) IP Address: " + IP + "\n" +
                "\n" +
                "If you didn't login using your credentials, please inform us immediately us via email at alerts.thinkwave.app@gmail.com" +
                "\n\n\n Thank you,\n The ThinkWave Team";

        MailSender objMailSender = new MailSender();
        objMailSender.Send_Email(email, subject, body);
    }
}
