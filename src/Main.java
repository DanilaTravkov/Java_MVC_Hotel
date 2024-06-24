import Model.*;
import Session.Session;

import java.util.Comparator;
import java.util.Map;
import java.util.Scanner;

import static Controller.Controller.getHotelController;
import static Controller.Controller.getUserController;
import static Utils.Utils.authorize;

public class Main {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        while (true) {
            System.out.println("Welcome menu");
            System.out.print(
                    "1 - Register\n" +
                    "2 - Login\n" +
                    "0 - Exit: "
            );
            switch (input.nextLine()) {
                case "1":
                    registerUser(input, null);
                    break;
                case "2":
                    if (loginUser(input)) {
                        System.out.println("\nLogin successful. Welcome, " + Session.getInstance().getLoggedInUser().getUsername() + "!");
                        System.out.println("You are logged in as " + Session.getInstance().getLoggedInUser().getRole() + "\n");
                        mainMenu(input);
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

    private static void mainMenu(Scanner input) {
        while (true) {
            System.out.println("Main menu");
            System.out.print(
                    "1 - Create a hotel (admin only)\n" +
                            "2 - List all hotels\n" +
                            "3 - Show info about hotel by id\n" +
                            "4 - Delete a hotel by id (admin only)\n" +
                            "5 - Add a receptionist (admin only)\n" +
                            "6 - Add a cleaner (admin only)\n" +
                            "7 - Logout\n" +
                            "8 - Update hotel by id (admin only)\n" +
                            "9 - Update user by id (admin only)\n" +
                            "0 - Exit: "
            );
            switch (input.nextLine()) {
                case "1":
                    if (!authorize("admin")) break;
                    Hotel hotel = createHotel(input);
                    getHotelController().addInstance(hotel.getHotelId(), hotel);
                    System.out.println("\nHotel created successfully.\n");
                    break;
                case "2":
                    for (Hotel h : getHotelController().listAllInstances().values()) {
                        System.out.println("\nid: " + h.getHotelId());
                        System.out.println("Hotel name: " + h.getHotelName());
                        System.out.println("Hotel address: " + h.getHotelAddress() + "\n");
                    }
                    break;
                case "3":
                    System.out.print("Enter hotel id: ");
                    String inputId = input.nextLine();
                    Hotel h = getHotelController().getInstanceById(Integer.valueOf(inputId));
                    if (h != null) {
                        System.out.println("\nid: " + h.getHotelId());
                        System.out.println("Hotel name: " + h.getHotelName());
                        System.out.println("Hotel address: " + h.getHotelAddress() + "\n");
                    } else {
                        System.out.println("\nNo hotel with id " + inputId + "\n");
                    }
                    break;
                case "4":
                    if (!authorize("admin")) break;
                    System.out.print("Enter hotel id to delete: ");
                    String idToDelete = input.nextLine();
                    getHotelController().deleteInstanceById(Integer.valueOf(idToDelete));
                    System.out.println("Successfully deleted hotel with id " + idToDelete);
                    break;
                case "5":
                    if (!authorize("admin")) break;
                    registerUser(input, "agent");
                    break;
                case "6":
                    if (!authorize("admin")) break;
                    registerUser(input, "cleaner");
                    break;
                case "7":
                    Session.getInstance().logout();
                    System.out.println("\nLogged out successfully.\n");
                    return;
                case "8": // Пример для обновления отеля
                    if (!authorize("admin")) break;
                    System.out.print("Enter hotel id to update: ");
                    Integer hotelIdToUpdate = Integer.parseInt(input.nextLine());
                    Hotel hotelToUpdate = getHotelController().getInstanceById(hotelIdToUpdate);
                    if (hotelToUpdate != null) {
                        System.out.print("Enter new hotel name: ");
                        String newHotelName = input.nextLine();
                        System.out.print("Enter new hotel address: ");
                        String newHotelAddress = input.nextLine();

                        getHotelController().updateInstanceById(hotelIdToUpdate, new String[]{newHotelName, newHotelAddress});
                        System.out.println("Hotel updated successfully.\n");

                    } else {
                        System.out.println("\nNo hotel with id " + hotelIdToUpdate + "\n");
                    }
                    break;

                case "9": // Пример для обновления пользователя
                    if (!authorize("admin")) break;
                    System.out.print("Enter user id to update: ");
                    Integer userIdToUpdate = Integer.parseInt(input.nextLine());
                    User userToUpdate = getUserController().getInstanceById(userIdToUpdate);
                    if (userToUpdate != null) {
                        System.out.print("Enter new username: ");
                        String newUsername = input.nextLine();
                        System.out.print("Enter new password: ");
                        String newPassword = input.nextLine();
                        System.out.print("Enter new role: ");
                        String newRole = input.nextLine();

                        // Call updateInstanceById with appropriate arguments
                        getUserController().updateInstanceById(userIdToUpdate, new String[]{newUsername, newPassword, newRole});
                        System.out.println("User updated successfully.\n");
                    } else {
                        System.out.println("\nNo user with id " + userIdToUpdate + "\n");
                    }
                    break;

                case "0":
                    System.exit(0);
                    break;
                default:
                    System.out.println("\nThere is no such option\n");
                    break;
            }
        }
    }

    private static Hotel createHotel(Scanner input) {
        Hotel hotel = new Hotel();

        System.out.print("Enter hotel name: ");
        hotel.setHotelName(input.nextLine());

        System.out.print("Enter hotel address: ");
        hotel.setHotelAddress(input.nextLine());

        // Retrieve the last ID from storage and set the new ID
        int lastHotelId = 0; // Default value if no records are present

        Map<Integer, Hotel> hotelMap = getHotelController().listAllInstances();
        if (!hotelMap.isEmpty()) {
            lastHotelId = hotelMap.keySet().stream()
                    .max(Comparator.naturalOrder())
                    .orElse(0); // If no max found, defaults to 0
        }

        hotel.setHotelId(lastHotelId + 1);

        return hotel;
    }

    private static void registerUser(Scanner input, String role) {
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


    private static boolean loginUser(Scanner input) {
        System.out.print("Enter username: ");
        String username = input.nextLine();
        System.out.print("Enter password: ");
        String password = input.nextLine();

        // Set the session
        for (User user : getUserController().listAllInstances().values()) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                Session.getInstance().setLoggedInUser(user);
                return true;
            }
        }
        System.out.println("\nInvalid username or password.\n");
        return false;
    }
}
