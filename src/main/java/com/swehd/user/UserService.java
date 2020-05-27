package com.swehd.user;

import java.util.regex.Pattern;

public class UserService {

    /**
     * Check if email address valid or no.
     * @param email an email entered by the user.
     * @return true if the email is valid else false.
     */
    public static boolean isValid(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."
                + "[a-zA-Z0-9_+&*-]+)*@"
                + "(?:[a-zA-Z0-9-]+\\.)+[a-z"
                + "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null) {
            return false;
        }
        return pat.matcher(email).matches();
    }

    /**
     * Check if the text is longer than 3.
     * @param name a name entered by the user.
     * @return false is the name is length less than 3, else true.
     */
    public static boolean longerThanThree(String name) {
        if (name.length() < 3) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * This method calls the {@link #checkIfExists(String, String)} method to
     * check if user is exists or no.
     * @param name the entered user's name.
     * @param email the entered user's email.
     * @return if user is exists return true else return false.
     */
    public static boolean checkIfExists(String name, String email) {
        if (UserDao.getInstance().isExistUser(name, email) != null) {
            return true;
        } else {
            return false;
        }
    }
}
