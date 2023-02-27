package Mail;

import DatabaseFunctions.Admin.GetLoginDetails;
import DatabaseFunctions.RetriveUserName;

import java.io.IOException;

public class NewUserAddedNotifier
{
    public static void sendnotification(String email, String id, String password) throws IOException
    {
        String name = RetriveUserName.getUserName(id);

        String[] loginInfo = GetLoginDetails.getLoginDetails(name);

        String subject = "ThinkWave Examination System Login Information";
        String body = "Dear " + name + ",\n" +
                "\n" +
                "We hope this email finds you well. We are pleased to inform you that an account has been created for you in the ThinkWave Examination System by the admin of your institution.\n" +
                "\n" +
                "The login credentials for your account are as follows:\n" +
                "\n" +
                "\t\t Institution ID: "+ loginInfo[1]+ "\n" +
                "\t\t User ID: "+ loginInfo[0]+ "\n" +
                "\t\t Password: "+ password + "\n" +
                "\n" +
                "We kindly request that you log in with this credentials and change the password immediately after your first login to ensure the safety of your account.\n" +
                "\n" +
                "If you encounter any difficulty logging in or require any assistance with the system, please do not hesitate to contact us immediately at alerts.thinkwave.app@gmail.com.\n" +
                "\n" +
                "Thank you for your cooperation, and we wish you the best of luck with your academic endeavors.\n" +
                "\n" +
                "Note: This is an automated email. Please do not reply to this message.\n" +
                "\n" +
                "We hope you find the above information helpful. If you have any further questions, please feel free to contact us at any time.\n" +
                "\n" +
                "Best regards,\n" +
                "The ThinkWave Team\n" +
                "\n" +
                "\n" +
                "\n";


        MailSender obj = new MailSender();
        obj.Send_Email(email, subject, body);
}
}
