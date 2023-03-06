package Mail;

import DatabaseFunctions.RetriveUserName;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class PasswordChangeNotifier {
    public static void sendPasswordChangeNotification(String email, int id) throws IOException {
        Calendar now = Calendar.getInstance();
        final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("IST"));
        String name = RetriveUserName.getUserName(id);
        String dateTime = dateFormat.format(now.getTime()) + " IST";

        String subject = "Password Change Detected! - ThinkWave Examination System";
        String body= "Dear " + name + ",\n\n" +
                "This is an automated message to inform you that the password for your account on ThinkWave Examination System has been recently changed by your institution admin." +
                "\n\nPlease use the latest password provided to your institution admin to login to ThinkWave Examination System. " +
                "If you have any issues with your login, please contact your institution admin." +
                "\n\nIf this activity was not done by your knowledge, please contact your institution admin immediately." +
                "\n\nThank you,\nThe ThinkWave Team";

        // Send the email
        MailSender objMailSender = new MailSender();
        objMailSender.Send_Email(email, subject, body);
}
}
