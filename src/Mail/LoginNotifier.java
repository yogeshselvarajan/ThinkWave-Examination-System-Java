package Mail;

import DatabaseFunctions.RetriveUserName;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;


public class LoginNotifier {
    public static String findIP() throws MalformedURLException {
        URL whatismyip = new URL("http://checkip.amazonaws.com");
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(
                    whatismyip.openStream()));
            String ip = in.readLine();
            return ip;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    // Function to Check if the operating system is Windows, Linux, Mac, etc.
    public static String getOS() {
        String OS = System.getProperty("os.name").toLowerCase();
        if (OS.contains("win")) {
            return "Windows";
        } else if (OS.contains("nix") || OS.contains("nux") || OS.contains("aix")) {
            return "Linux";
        } else if (OS.contains("mac")) {
            return "Mac";
        } else {
            return "Other";
        }
    }

    public static void sendLoginNotification(String email, String id) throws IOException {
        Calendar now = Calendar.getInstance();
        final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("IST"));
        String name = RetriveUserName.getUserName(Integer.parseInt(id));
        String IP = findIP();
        String dateTime = dateFormat.format(now.getTime()) + " IST";

        String subject = "New Login Detected! - ThinkWave Examination System";
        String body = "Dear " + name + ",\n\n" +
                        "This is an automated message to inform you that a new log in activity on " + dateTime + " has been done via " +
                        "ThinkWave Application using the account credentials associated with this email address." +
                        "\n\nThe details of the activity are as follows:" +
                        "\n\t\t Device Name: " + InetAddress.getLocalHost().getHostName() +
                        "\n\t\t Device Platform: " + getOS() +
                        "\n\t\t IP Address: " + IP + "\n" +
                        "\nIf this activity was done by you, then you can disregard this message. " +
                        "\nIf this activity was not done by you, we urge you to take immediate action by changing your password." +
                        "\n\nIf you need any assistance, please contact us immediately through our email at alerts.thinkwave.app@gmail.com" +
                        "\n\n\nThank you,\n The ThinkWave Team";
        // Send the email
        MailSender objMailSender = new MailSender();
        objMailSender.Send_Email(email, subject, body);
    }

    public static void main(String[] args) {
        try {
           sendLoginNotification("sample@gmail", "1");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
