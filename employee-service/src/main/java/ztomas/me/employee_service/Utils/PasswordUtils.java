package ztomas.me.employee_service.Utils;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtils {
    public static String hashPassword(String pwd)
    {
        return BCrypt.hashpw(pwd, BCrypt.gensalt(10));
    }

    public static boolean checkPassword(String pwd, String hashed)
    {
        return BCrypt.checkpw(pwd, hashed);
    }
}
