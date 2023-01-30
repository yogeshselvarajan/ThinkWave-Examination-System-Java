package Mail;

public class SendRegisterEmail
{

    public static void sendRegisterEmail(String toemail,String insName,String insAddress, String insEmail, String insID, String userID, String password)
    {   String subject = " New Account and Institution Registered for Thinkwave Examination System";
        String body = "Dear Administrator,\n\n"+
                      "We are pleased to inform you that your new account and institution have been successfully registered in the Thinkwave Examination System.\n\n"+
                      "Here are the details registered of your institution:\n"+
                       "\t1. Institution Name: "+ insName+"\n"+
                       "\t2. Institution Address: "+ insAddress +"\n"+
                       "\t3. Institution Email: "+ insEmail +"\n\n"+
                      "Here are the login details of your account:\n"+
                         "\t1. Institution ID: "+ insID +"\n"+
                         "\t2. User ID: "+ userID +"\n"+
                         "\t3. Password: "+ password +"\n\n"+
                       "Please note that the password is case sensitive and must be entered exactly as it is shown above.\n\n"+
                        "You can login to the system using the above credentials at the login page of the Thinkwave Examination System.\n\n"+

                      "Please note that the Thinkwave Examination System is a secure system and is only accessible to authorized users. Please do not share your login credentials with anyone.\n\n"+
                       "The Thinkwave Examination system provides secure offline testing and assessment capabilities to your institution, enabling students to take exams and test in a safe and secure environment. It also allows for easy management of test results and student data.\n\n"+
                       "We are delighted to have you join the Thinkwave family and look forward to helping you utilize the system to its fullest potential. Please do not hesitate to contact us if you have any questions or need assistance with the system at alerts.thinkwave.app@gmail.com.\n\n"+
                       "Sincerely,\n"+
                       "Thinkwave Examination System";
        MailSender mailSender = new MailSender();
        mailSender.Send_Email(toemail, subject, body);
    }
}
