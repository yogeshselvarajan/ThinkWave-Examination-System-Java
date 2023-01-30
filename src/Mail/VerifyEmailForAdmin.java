package Mail;

import java.io.IOException;

public class VerifyEmailForAdmin
{
    // This method will send the email to the admin to verify the email
    public static void sendAdminVerificationEmail(String email, String code) throws IOException
    {
        String subject = "Verify Admin Email - ThinkWave Examination System";
        String body = "Dear Admin,\n\t\t" +
                        "This is an automated message to inform you that you want to register a new account with the email address " + email
                + ".\n\n" + "If you want to register a new account with this email address," +
                "please enter the following verification code in the verification box prompted: " + code + "\n\n" +
                "If you don't want to register a new account with this email address, please ignore this email. " +
                " If you have any questions, please contact us via email at alerts.thinkwave.app@gmail.com \n\n" +
                "Thank you,\n" +
                "ThinkWave Examination System";

        MailSender mailSender = new MailSender();
        mailSender.Send_Email(email, subject, body);
    }

    // Method to generate verification code with TW-XXXXXX format where X is a random digit
    public static String generateVerificationCode()
    {
        String code = "TW-";
        for (int i = 0; i < 6; i++)
        {
            code += (int) (Math.random() * 10);
        }
        return code;
    }
}
