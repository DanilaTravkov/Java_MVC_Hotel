package View;

import Model.*;
import Session.Session;

import java.util.Scanner;
import java.util.Map;
import java.util.Comparator;
import View.MainView;

import static Controller.Controller.*;
import static Utils.Utils.authorize;

public class AdminView {
    private final Admin admin;
    private final Scanner input = new Scanner(System.in);

    public AdminView(Admin admin) {
        this.admin = admin;
    }

    public void displayAdminMenu() {
        boolean continueMenu = true;
        while (continueMenu) {
            System.out.println("Admin Menu:");
            System.out.println("1. Manage Hotels");
            System.out.println("2. Manage Users");
            System.out.println("3. Manage Bookings");
            System.out.println("4. Manage Rooms");
            System.out.println("5. Logout");

            String choice = input.nextLine();
            switch (choice) {
                case "1":
                    authorize(admin, "admin");
                    manageHotels();
                    break;
                case "2":
                    authorize(admin, "admin");
                    manageUsers();
                    break;
                case "3":
                    authorize(admin, "admin");
                    //
                    break;
                case "4":
                    authorize(admin, "admin");
                    manageRooms();
                    break;
                case "5":

                    Session.getInstance().logout(); // Logout the user
                    System.out.println("Logged out successfully.\n");
                    System.exit(0);
                    return;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }


    private void manageHotels() {
        while (true) {
            System.out.println("\nHotel Management Menu");
            System.out.print(
                    "1 - List all hotels\n" +
                            "2 - Show hotel by id\n" +
                            "3 - Create hotel\n" +
                            "4 - Delete hotel by id\n" +
                            "5 - Update hotel by id\n" +
                            "0 - Back to main menu: "
            );

            switch (input.nextLine()) {
                case "1":
                    listAllHotels();
                    break;
                case "2":
                    showHotelById();
                    break;
                case "3":
                    createHotel();
                    break;
                case "4":
                    deleteHotelById();
                    break;
                case "5":
                    updateHotelById();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("\nInvalid option. Try again.\n");
                    break;
            }
        }
    }

    private void listAllHotels() {
        for (Hotel h : getHotelController().listAllInstances().values()) {
            System.out.println("\nid: " + h.getHotelId());
            System.out.println("Hotel name: " + h.getHotelName());
            System.out.println("Hotel address: " + h.getHotelAddress() + "\n");
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
        getHotelController().addInstance(hotel.getHotelId(), hotel);

        System.out.println("\n Succssesfully created a new hotel");
        return hotel;
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

    private void manageUsers() {
        while (true) {
            System.out.println("\nUser Management Menu");
            System.out.print(
                    "1 - List all users\n" +
                            "2 - Show user by id\n" +
                            "3 - Delete user by id\n" +
                            "4 - Update user by id\n" +
                            "0 - Back to main menu: "
            );

            switch (input.nextLine()) {
                case "1":
                    listAllUsers();
                    break;
                case "2":
                    showUserById();
                    break;
                case "3":
                    deleteUserById();
                    break;
                case "4":
                    updateUserById();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("\nInvalid option. Try again.\n");
                    break;
            }
        }
    }

    private void listAllUsers() {
        for (User u : getUserController().listAllInstances().values()) {
            System.out.println("\nid: " + u.getId());
            System.out.println("Username: " + u.getUsername());
            System.out.println("Role: " + u.getRole() + "\n");
        }
    }

    private void showUserById() {
        System.out.print("Enter user id: ");
        String inputId = input.nextLine();
        User u = getUserController().getInstanceById(Integer.valueOf(inputId));
        if (u != null) {
            System.out.println("\nid: " + u.getId());
            System.out.println("Username: " + u.getUsername());
            System.out.println("Role: " + u.getRole() + "\n");
        } else {
            System.out.println("\nNo user with id " + inputId + "\n");
        }
    }

    private void deleteUserById() {
        System.out.print("Enter user id to delete: ");
        String idToDelete = input.nextLine();
        getUserController().deleteInstanceById(Integer.valueOf(idToDelete));
        System.out.println("Successfully deleted user with id " + idToDelete);
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

    private void manageRooms() {
        while (true) {
            System.out.println("\nRooms Management Menu");
            System.out.print(
                    "1 - List all rooms\n" +
                    "2 - Show room by id\n" +
                    "3 - Create room\n" +
                    "4 - Delete room by id\n" +
                    "5 - Update room by id\n" +
                    "0 - Back to main menu: "
            );

            switch (input.nextLine()) {
                case "1":
                    listAllRooms();
                    break;
                case "2":
                    showRoomById();
                    break;
                case "3":
                    addNewRoom();
                    break;
                case "4":
                    deleteRoomById();
                    break;
                case "5":
                    updateRoomById();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("\nInvalid option. Try again.\n");
                    break;
            }
        }
    }

    private void listAllRooms() {
        for (Room r : getRoomController().listAllInstances().values()) {
            System.out.println("\nid: " + r.getRoomId());
            System.out.println("Hotel id: " + r.getHotelId());
            System.out.println("Cleaner id: " + r.getCleanerId());
            System.out.println("Room type: " + r.getRoomType());
            System.out.println("Room status: " + r.getStatus() + "\n");
        }
    }

    private void showRoomById() {
        System.out.print("Enter room id: ");
        String inputId = input.nextLine();
        Room r = getRoomController().getInstanceById(Integer.valueOf(inputId));
        if (r != null) {
            System.out.println("\nid: " + r.getRoomId());
            System.out.println("Hotel id: " + r.getHotelId());
            System.out.println("Cleaner id: " + r.getCleanerId());
            System.out.println("Room type: " + r.getRoomType());
            System.out.println("Room status: " + r.getStatus() + "\n");
        } else {
            System.out.println("No room with id " + inputId);
        }
    }

    private void addNewRoom() {
        System.out.print("Enter hotel id: ");
        Integer hotelId = Integer.parseInt(input.nextLine());
        System.out.print("Enter cleaner id: ");
        Integer cleanerId = Integer.parseInt(input.nextLine());
        System.out.print("Enter room type: ");
        String roomType = input.nextLine().toUpperCase();
        Room.RoomType roomTypeValue = null;
        try {
            roomTypeValue = Room.RoomType.valueOf(roomType);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid room type. Please enter SINGLE or DOUBLE.");
        }
        System.out.print("Enter room status: ");
        String roomStatus = input.nextLine().toUpperCase();
        Room.RoomStatus roomStatusValue = null;
        try {
            roomStatusValue = Room.RoomStatus.valueOf(roomStatus);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid room type. Please enter SINGLE or DOUBLE.");
        }

        Room newRoom = new Room();
        newRoom.setHotelId(hotelId);
        newRoom.setCleanerId(cleanerId);
        newRoom.setRoomType(roomTypeValue);
        newRoom.setStatus(roomStatusValue);

        getRoomController().addInstance(newRoom.getRoomId(), newRoom);
        System.out.println("Room added successfully.\n");
    }

    private void deleteRoomById() {
        System.out.print("Enter room id to delete: ");
        String idToDelete = input.nextLine();
        getRoomController().deleteInstanceById(Integer.valueOf(idToDelete));
        System.out.println("Successfully deleted room with id " + idToDelete);
    }

    private void updateRoomById() {
        System.out.print("Enter room id to update: ");
        Integer roomIdToUpdate = Integer.parseInt(input.nextLine());
        Room roomToUpdate = getRoomController().getInstanceById(roomIdToUpdate);
        if (roomToUpdate != null) {
            System.out.print("Enter new hotel id: ");
            Integer newHotelId = Integer.parseInt(input.nextLine());
            System.out.print("Enter new cleaner id: ");
            Integer newCleanerId = Integer.parseInt(input.nextLine());
            System.out.print("Enter new room type: ");
            String newRoomType = input.nextLine();
            System.out.print("Enter new room status: ");
            String newStatus = input.nextLine();

            getRoomController().updateInstanceById(roomIdToUpdate, new String[]{String.valueOf(newHotelId), String.valueOf(newCleanerId), newRoomType, newStatus});
            System.out.println("Room updated successfully.\n");

        } else {
            System.out.println("\nNo room with id " + roomIdToUpdate + "\n");
        }
    }
}
