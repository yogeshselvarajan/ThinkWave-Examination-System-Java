package RegexChecks;

public class CheckPhoneIndian {
    // Check if the phone number is valid with 91 as prefix
    public static boolean isValidPhone(String phone) {
        String phoneRegex = "^\\+?91[0-9]{10}$";
        java.util.regex.Pattern pat = java.util.regex.Pattern.compile(phoneRegex);
        if (phone == null)
            return false;
        return pat.matcher(phone).matches();
}
}
