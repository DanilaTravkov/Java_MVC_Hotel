package Session;

import Model.User;

// Lazy singleton session to store the user who's logged into the system

public class Session {
    private static Session instance;
    private User loggenInUser;

    private Session() {}

    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    public User getLoggedInUser() {
        return loggenInUser;
    }

    public void setLoggedInUser(User loggedInUser) {
        this.loggenInUser = loggedInUser;
    }

    public void logout() {
        loggenInUser = null;
    }

}
