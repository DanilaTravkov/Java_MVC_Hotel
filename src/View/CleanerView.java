package View;

import Controller.Controller;
import Model.Cleaner;
import Model.Room;
import Model.User;

import java.util.Map;
import java.util.Scanner;
import Session.Session;

public class CleanerView {
    private Cleaner cleaner;
    private final Scanner input = new Scanner(System.in);

    public CleanerView(Cleaner cleanerUser) {
        this.cleaner = cleanerUser;
    }

    public void displayCleanerMenu() {
        // Display cleaner menu options
        System.out.println("Cleaner Menu:");
        System.out.println("1. View Assigned Rooms");
        System.out.println("2. Complete Cleaning Tasks");
        System.out.println("3. Logout");

        switch (input.nextLine()) {
            case "1":
                viewAssignedRooms();
                break;
            case "2":
                completeCleaningTasks();
                break;
            case "3":
                logout();
                break;
            default:
                System.out.println("\nInvalid option. Try again.\n");
                break;
        }
    }

    private void viewAssignedRooms() {
        // Fetch rooms assigned to the cleaner using UserController
        Controller<User> userController = Controller.getUserController();
        Map<Integer, User> users = userController.listAllInstances();

        for (User user : users.values()) {
            if (user.getRole().equals("cleaner") && user.getId() == cleaner.getId()) {
                System.out.println("\nAssigned Rooms for Cleaner " + user.getUsername() + ":");
                // Fetch rooms assigned to this cleaner
                Controller<Room> roomController = Controller.getRoomController();
                Map<Integer, Room> rooms = roomController.listAllInstances();

                for (Room room : rooms.values()) {
                    // Assuming each room has a cleanerId associated with it
                    if (room.getCleanerId() == user.getId()) {
                        System.out.println("Room ID: " + room.getRoomId());
                        System.out.println("Room Type: " + room.getRoomType());
                        System.out.println("Room Status: " + room.getStatus());
                        System.out.println(); // Empty line for separation
                    }
                }
                return;
            }
        }
        System.out.println("\nNo assigned rooms found for Cleaner " + cleaner.getUsername());
    }

    private void completeCleaningTasks() {
        // Mark cleaning tasks as completed for assigned rooms
        System.out.println("\nEnter room ID to mark as cleaned:");
        int roomId = Integer.parseInt(input.nextLine());

        Controller<Room> roomController = Controller.getRoomController();
        Room room = roomController.getInstanceById(roomId);

        if (room != null && room.getCleanerId() == cleaner.getId()) {
            // Assuming room status is updated or task is marked as completed
            room.setStatus(Room.RoomStatus.FREE); // Assuming status FREE for cleaned rooms
            roomController.updateInstanceById(roomId, new String[]{String.valueOf(room.getRoomType()), room.getStatus().toString()});
            System.out.println("Room " + roomId + " cleaned successfully.");
        } else {
            System.out.println("Room " + roomId + " is not assigned to you or does not exist.");
        }
    }

    private void logout() {
        Session.getInstance().logout();
        System.out.println("Logged out successfully.\n");
    }
}
