package View;

import Model.*;
import Session.Session;
import Utils.Utils;

import java.util.Comparator;
import java.util.Map;
import java.util.Scanner;

import static Controller.Controller.*;

public class MainView {
    private final Scanner input = new Scanner(System.in);

    public void showWelcomeMenu() {
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
                        showMainMenu(loggedInUser);
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

    private void showMainMenu(User user) {
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
                    "10 - Booking operations (agent only)\n" +
                    "0 - Exit: "
            );
            switch (input.nextLine()) {
                case "1":
                    if (Utils.authorize(user, "admin")) {
                        Hotel hotel = createHotel();
                        getHotelController().addInstance(hotel.getHotelId(), hotel);
                        System.out.println("\nHotel created successfully.\n");
                    }
                    break;
                case "2":
                    listAllHotels();
                    break;
                case "3":
                    showHotelById();
                    break;
                case "4":
                    if (Utils.authorize(user, "admin")) {
                        deleteHotelById();
                    }
                    break;
                case "5":
                    if (Utils.authorize(user, "admin")) {
                        registerUser("agent");
                    }
                    break;
                case "6":
                    if (Utils.authorize(user, "admin")) {
                        registerUser("cleaner");
                    }
                    break;
                case "7":
                    Session.getInstance().logout();
                    System.out.println("\nLogged out successfully.\n");
                    return;
                case "8":
                    if (Utils.authorize(user, "admin")) {
                        updateHotelById();
                    }
                    break;
                case "9":
                    if (Utils.authorize(user, "admin")) {
                        updateUserById();
                    }
                    break;
                case "10":
                    if (Utils.authorize(user, "agent")) {
                        bookingOperations();
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

    private Hotel createHotel() {
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

    private boolean loginUser() {
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

    private void listAllHotels() {
        for (Hotel h : getHotelController().listAllInstances().values()) {
            System.out.println("\nid: " + h.getHotelId());
            System.out.println("Hotel name: " + h.getHotelName());
            System.out.println("Hotel address: " + h.getHotelAddress() + "\n");
        }
    }

    private void showHotelById() {
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
    }

    private void deleteHotelById() {
        System.out.print("Enter hotel id to delete: ");
        String idToDelete = input.nextLine();
        getHotelController().deleteInstanceById(Integer.valueOf(idToDelete));
        System.out.println("Successfully deleted hotel with id " + idToDelete);
    }

    private void updateHotelById() {
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
    }

    private void updateUserById() {
        System.out.print("Enter user id to update: ");
        Integer userIdToUpdate = Integer.parseInt(input.nextLine());
        User userToUpdate = getUserController().getInstanceById(userIdToUpdate);
        if (userToUpdate != null) {
            System.out.print("Enter new username: ");
            String newUsername = input.nextLine();
            System.out.print("Enter new password: ");
            String newPassword = input.nextLine();
            System.out.print("Enter new role (admin, agent, cleaner, guest): ");
            String newRole = input.nextLine();

            getUserController().updateInstanceById(userIdToUpdate, new String[]{newUsername, newPassword, newRole});
            System.out.println("User updated successfully.\n");

        } else {
            System.out.println("\nNo user with id " + userIdToUpdate + "\n");
        }
    }

    private void bookingOperations() {
        while (true) {
            System.out.println("\nBooking operations menu\n");
            System.out.print(
                    "1 - Create a booking\n" +
                            "2 - List all bookings\n" +
                            "3 - Show booking by id\n" +
                            "4 - Delete a booking by id\n" +
                            "5 - Update booking by id\n" +
                            "0 - Back to main menu: "
            );
            switch (input.nextLine()) {
                case "1":
                    createBooking();
                    break;
                case "2":
                    listAllBookings();
                    break;
                case "3":
                    showBookingById();
                    break;
                case "4":
                    deleteBookingById();
                    break;
                case "5":
                    updateBookingById();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("\nThere is no such option\n");
                    break;
            }
        }
    }

    private void createBooking() {
        Booking booking = new Booking();

        System.out.print("Enter hotel id: ");
        booking.setHotelId(Integer.parseInt(input.nextLine()));

        System.out.print("Enter user id: ");
        booking.setUserId(Integer.parseInt(input.nextLine()));

        System.out.print("Enter room id: ");
        booking.setRoomId(Integer.parseInt(input.nextLine()));

        System.out.print("Enter booking start date (yyyy-MM-dd): ");
        booking.setStartDate(input.nextLine());

        System.out.print("Enter booking end date (yyyy-MM-dd): ");
        booking.setEndDate(input.nextLine());

        // Retrieve the last ID from storage and set the new ID
        int lastBookingId = 0; // Default value if no records are present

        Map<Integer, Booking> bookingMap = getBookingController().listAllInstances();
        if (!bookingMap.isEmpty()) {
            lastBookingId = bookingMap.keySet().stream()
                    .max(Comparator.naturalOrder())
                    .orElse(0); // If no max found, defaults to 0
        }

        booking.setBookingId(lastBookingId + 1);

        getBookingController().addInstance(booking.getBookingId(), booking);
        System.out.println("\nBooking created successfully.\n");
    }

    private void listAllBookings() {
        for (Booking b : getBookingController().listAllInstances().values()) {
            System.out.println("\nid: " + b.getBookingId());
            System.out.println("Hotel id: " + b.getHotelId());
            System.out.println("User id: " + b.getUserId());
            System.out.println("Room id: " + b.getRoomId());
            System.out.println("Start date: " + b.getStartDate());
            System.out.println("End date: " + b.getEndDate() + "\n");
        }
    }

    private void showBookingById() {
        System.out.print("Enter booking id: ");
        String inputId = input.nextLine();
        Booking b = getBookingController().getInstanceById(Integer.valueOf(inputId));
        if (b != null) {
            System.out.println("\nid: " + b.getBookingId());
            System.out.println("Hotel id: " + b.getHotelId());
            System.out.println("User id: " + b.getUserId());
            System.out.println("Room id: " + b.getRoomId());
            System.out.println("Start date: " + b.getStartDate());
            System.out.println("End date: " + b.getEndDate() + "\n");
        } else {
            System.out.println("\nNo booking with id " + inputId + "\n");
        }
    }

    private void deleteBookingById() {
        System.out.print("Enter booking id to delete: ");
        String idToDelete = input.nextLine();
        getBookingController().deleteInstanceById(Integer.valueOf(idToDelete));
        System.out.println("Successfully deleted booking with id " + idToDelete);
    }

    public void updateBookingById() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the Booking ID to update:");
        int bookingIdToUpdate = scanner.nextInt();

        System.out.println("Enter new Hotel ID:");
        int newHotelId = scanner.nextInt();

        System.out.println("Enter new User ID:");
        int newUserId = scanner.nextInt();

        System.out.println("Enter new Room ID:");
        int newRoomId = scanner.nextInt();

        scanner.nextLine(); // consume the newline

        System.out.println("Enter new Start Date (YYYY-MM-DD):");
        String newStartDate = scanner.nextLine();

        System.out.println("Enter new End Date (YYYY-MM-DD):");
        String newEndDate = scanner.nextLine();

        // Преобразуем параметры в массив строк
        String[] updatedValues = new String[]{
                String.valueOf(newHotelId),
                String.valueOf(newUserId),
                String.valueOf(newRoomId),
                newStartDate,
                newEndDate
        };

        getBookingController().updateInstanceById(bookingIdToUpdate, updatedValues);
    }

    private boolean validateBookingConfirmation(Booking booking) {
        // Implement logic to check if there are available rooms for the selected dates
        // This will depend on the current status of rooms and their availability
        // Example logic:
        Room room = getRoomController().getInstanceById(booking.getRoomId());
        return room.getStatus() == Room.RoomStatus.FREE;
    }

    private void updateRoomStatus(int roomId, Room.RoomStatus status) {
        // Implement logic to update the room status
        Room roomToUpdate = getRoomController().getInstanceById(roomId);
        if (roomToUpdate != null) {
            roomToUpdate.setStatus(status);
            getRoomController().updateInstanceById(roomId, new String[]{"status"});
        } else {
            System.out.println("Wrong room id or room with this id does not exist");
        }
    }

    private void refundGuest(Booking booking) {
        // Implement logic to refund the guest if booking is canceled or denied
        // This may involve financial transactions or adjustments
        // For simulation purposes, we'll assume no refunds (based on the provided requirements).
    }

}
