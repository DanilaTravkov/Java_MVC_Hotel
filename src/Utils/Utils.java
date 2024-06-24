package Utils;

import Model.User;
import Session.Session;

// Utilities

public class Utils {

    public static boolean authorize(String role) {
        User loggedInUser = Session.getInstance().getLoggedInUser();
        if (!role.equals(loggedInUser.getRole())) {
            System.out.println("\nUnauthorized action. Only " + role + " can perform this action.\n");
            return false;
        }
        return true;
    }
}
