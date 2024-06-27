package View;

import Controller.Controller;
import Model.Booking;
import Model.Guest;
import Model.Hotel;
import Model.Room;

import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import Session.Session;

import static Controller.Controller.getHotelController;

public class GuestView {
    private Guest guest;
    private final Scanner input = new Scanner(System.in);

    public GuestView(Guest guest) {
        this.guest = guest;
    }

    public void displayUserMenu() {
        // Display user menu options
        System.out.println("User Menu:");
        System.out.println("1. View Profile");
        System.out.println("2. View Reservations");
        System.out.println("3. Make Reservation");
        System.out.println("4. Logout");

        switch (input.nextLine()) {
            case "1":
                viewProfile();
                break;
            case "2":
                viewReservations();
                break;
            case "3":
                makeReservation();
                break;
            case "4":
                logout();
                break;
            default:
                System.out.println("\nInvalid option. Try again.\n");
                break;
        }
    }

    private void viewProfile() {
        // Display guest profile information
        System.out.println("\nGuest Profile:");
        System.out.println("Name: " + guest.getFirstName() + " " + guest.getLastName());
        System.out.println("Gender: " + guest.getGender());
        System.out.println("Date of Birth: " + guest.getBirthDate());
        System.out.println("Phone: " + guest.getPhone());
        System.out.println("Address: " + guest.getAddress());
        System.out.println("Username (Email): " + guest.getUsername());
        System.out.println(); // Empty line for separation
    }

    private void viewReservations() {
        // Display guest's reservations
        Controller<Booking> bookingController = Controller.getBookingController();
        Map<Integer, Booking> bookings = bookingController.listAllInstances();

        boolean foundReservation = false;
        for (Booking booking : bookings.values()) {
            if (booking.getUserId() == guest.getId()) {
                System.out.println("Reservation ID: " + booking.getBookingId());
                System.out.println("Room ID: " + booking.getRoomId());
                System.out.println("Start Date: " + booking.getStartDate());
                System.out.println("End Date: " + booking.getEndDate());
                System.out.println("Status: " + booking.getBookingStatus());
                System.out.println(); // Empty line for separation
                foundReservation = true;
            }
        }

        if (!foundReservation) {
            System.out.println("\nNo reservations found for guest " + guest.getUsername());
        }
    }

    private void makeReservation() {
        // Guest makes a new reservation
        System.out.println("\nEnter hotel ID to reserve:");
        int hotelId = Integer.parseInt(input.nextLine());

        Room.RoomType roomType = null;
        while (roomType == null) {
            System.out.println("Enter room type to reserve (SINGLE / DOUBLE): ");
            String roomTypeInput = input.nextLine().toUpperCase();

            try {
                roomType = Room.RoomType.valueOf(roomTypeInput);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid room type. Please enter SINGLE or DOUBLE.");
            }
        }

        System.out.println("Enter start date (YYYY-MM-DD):");
        String startDate = input.nextLine();

        System.out.println("Enter end date (YYYY-MM-DD):");
        String endDate = input.nextLine();

        // Check if the room type is available for the selected dates
        Controller<Room> roomController = Controller.getRoomController();
        Map<Integer, Room> rooms = roomController.listAllInstances();

        boolean roomsAvailable = false;
        System.out.println("\nAvailable rooms:");
        for (Room room : rooms.values()) {
            if (room.getRoomType() == roomType && room.getStatus() == Room.RoomStatus.FREE && room.getHotelId() == hotelId) {
                System.out.println("Room ID: " + room.getRoomId());
                roomsAvailable = true;
            }
        }

        if (!roomsAvailable) {
            System.out.println("No available rooms of type " + roomType + " for the selected dates in hotel " + hotelId);
            return;
        }

        System.out.println("\nEnter the Room ID(s) you wish to reserve (comma-separated if multiple):");
        String[] roomIds = input.nextLine().split(",");

        Controller<Booking> bookingController = Controller.getBookingController();

        for (String roomIdStr : roomIds) {
            int roomId = Integer.parseInt(roomIdStr.trim());
            Room room = roomController.getInstanceById(roomId);

            if (room != null && room.getRoomType() == roomType && room.getStatus() == Room.RoomStatus.FREE && room.getHotelId() == hotelId) {
                Booking booking = new Booking();
                int lastBookingId = 0; // Default value if no records are present

                Map<Integer, Hotel> hotelMap = getHotelController().listAllInstances();
                if (!hotelMap.isEmpty()) {
                    lastBookingId = hotelMap.keySet().stream()
                            .max(Comparator.naturalOrder())
                            .orElse(0); // If no max found, defaults to 0
                }

                booking.setBookingId(lastBookingId + 1);
                booking.setUserId(guest.getId());
                booking.setHotelId(hotelId);
                booking.setRoomId(roomId);
                booking.setStartDate(startDate);
                booking.setEndDate(endDate);
                booking.setBookingStatus(Booking.BookingStatus.PENDING); // Initial status

                bookingController.addInstance(booking.getBookingId(), booking);
                System.out.println("Room ID " + roomId + " reservation created successfully. Waiting for agent's confirmation.");
            } else {
                System.out.println("Room ID " + roomId + " is not available for reservation.");
            }
        }
    }

    private void logout() {
        Session.getInstance().logout();
        System.out.println("Logged out successfully.\n");
    }
}


