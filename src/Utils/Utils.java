package Utils;

import Model.User;
import Session.Session;

public class Utils {

    public static boolean authorize(User user, String role) {
        if (user.getRole().equals(role)) {
            return true;
        } else {
            System.out.println("\nYou are not authorized to perform this operation.\n");
            return false;
        }
    }
}
