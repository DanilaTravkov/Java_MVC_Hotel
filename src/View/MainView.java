package View;


import Session.Session;
import View.AdminView;
import View.AgentView;
import View.CleanerView;
import View.GuestView;
import java.util.Scanner;
import Session.Session;

import Model.*;
import Session.Session;
import Utils.Utils;

import java.util.Comparator;
import java.util.Map;
import java.util.Scanner;

import static Controller.Controller.*;

public class MainView {
    private Session session;
    private final Scanner input = new Scanner(System.in);

    public MainView() {
        this.session = Session.getInstance();
    }

    public void displayMainMenu() {
        User loggedInUser = session.getLoggedInUser();
        if (loggedInUser == null) {
            displayLoginRegisterMenu();
        } else {
            while (true) {
                if (loggedInUser instanceof Admin) {
                    AdminView adminView = new AdminView((Admin) loggedInUser);
                    adminView.displayAdminMenu();

                } else if (loggedInUser instanceof Cleaner) {
                    CleanerView cleanerView = new CleanerView((Cleaner) loggedInUser);
                    cleanerView.displayCleanerMenu();

                } else if (loggedInUser instanceof Agent) {
                    AgentView receptionistView = new AgentView((Agent) loggedInUser);
                    receptionistView.displayAgentMenu();
                } else {
                    GuestView userView = new GuestView((Guest) loggedInUser);
                    userView.displayUserMenu();

                }

                // После выполнения меню возвращаемся сюда
                // Если пользователь вышел из системы, выходим из цикла
//                if (session.getLoggedInUser() == null) {
//                    break;
//                }
            }
        }
    }

    private boolean loginUser() {
        System.out.print("Enter username: ");
        String username = input.nextLine();
        System.out.print("Enter password: ");
        String password = input.nextLine();

        // Set the session
        for (User user : getUserController().listAllInstances().values()) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                User loggedInUser;
                switch (user.getRole().toLowerCase()) {
                    case "admin":
                        loggedInUser = new Admin();
                        break;
                    case "agent":
                        loggedInUser = new Agent();
                        break;
                    case "cleaner":
                        loggedInUser = new Cleaner();
                        break;
                    case "guest":
                        loggedInUser = new Guest();
                        break;
                    default:
                        System.out.println("Invalid role.");
                        return false;
                }
                // Копируем данные из user в loggedInUser
                loggedInUser.setId(user.getId());
                loggedInUser.setUsername(user.getUsername());
                loggedInUser.setPassword(user.getPassword());
                loggedInUser.setRole(user.getRole());

                Session.getInstance().setLoggedInUser(loggedInUser);
                return true;
            }
        }
        System.out.println("\nInvalid username or password.\n");
        return false;
    }

    private void displayLoginRegisterMenu() {
        while (true) {
            System.out.println("Welcome menu");
            System.out.print(
                    "1 - Register\n" +
                            "2 - Login\n" +
                            "0 - Exit: "
            );
            switch (input.nextLine()) {
                case "1":
                    registerUser(null);
                    break;
                case "2":
                    if (loginUser()) {
                        User loggedInUser = Session.getInstance().getLoggedInUser();
                        System.out.println("\nLogin successful. Welcome, " + loggedInUser.getUsername() + "!");
                        System.out.println("You are logged in as " + loggedInUser.getRole() + "\n");
                        displayMainMenu(); // Вызываем mainMenu после успешного логина и остаемся в нем до выхода
                    }
                    break;
                case "0":
                    System.exit(0);
                    break;
                default:
                    System.out.println("\nInvalid option. Try again.\n");
                    break;
            }
        }
    }

    private void registerUser(String role) {
        System.out.print("Enter username: ");
        String username = input.nextLine();
        System.out.print("Enter password: ");
        String password = input.nextLine();
        User user;

        // Determine the correct role if not provided
        if (role == null) {
            System.out.print("Enter role (admin, agent, cleaner, guest): ");
            role = input.nextLine();
        }

        // Create user based on role
        switch (role) {
            case "admin":
                user = new Admin();
                System.out.println("Creating admin");
                break;
            case "agent":
                user = new Agent();
                break;
            case "cleaner":
                user = new Cleaner();
                break;
            case "guest":
                user = new Guest();
                break;
            default:
                System.out.println("Invalid role specified.");
                return;
        }

        // Retrieve the last ID from storage and set the new ID
        int lastUserId = 0; // Default value if no records are present

        Map<Integer, User> userMap = getUserController().listAllInstances();
        if (!userMap.isEmpty()) {
            lastUserId = userMap.keySet().stream()
                    .max(Comparator.naturalOrder())
                    .orElse(0); // If no max found, defaults to 0
        }

        user.setId(lastUserId + 1);
        user.setUsername(username);
        user.setPassword(password);
        user.setRole(role);

        getUserController().addInstance(user.getId(), user);
        System.out.println("\nUser registered successfully.\n");
    }

}
