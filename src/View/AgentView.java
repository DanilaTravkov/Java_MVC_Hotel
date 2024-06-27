package View;

import Model.Agent;
import Model.Booking;
import Model.Guest;
import Model.User;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

import static Controller.Controller.getBookingController;
import static Controller.Controller.getUserController;
import static Utils.Utils.authorize;

public class AgentView {
    private Agent agent;
    private final Scanner input = new Scanner(System.in);

    public AgentView(Agent agent) {
        this.agent = agent;
    }

    public void displayAgentMenu() {
        while (true) {
            System.out.println("Receptionist Menu:");
            System.out.println("1. Manage Bookings");
            System.out.println("2. View Guest Information");
            System.out.println("3. Logout");

            String choice = input.nextLine();
            switch (choice) {
                case "1":
                    authorize(agent, "agent");
                    manageBookings();
                    break;
                case "2":
                    authorize(agent, "agent");
                    // TODO: view guest information (including bookings)
                    viewGuestInformation();
                    break;
                case "3":
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    private void manageBookings() {
        while (true) {
            System.out.println("\nBooking Management Menu");
            System.out.print(
                    "1 - List all bookings\n" +
                            "2 - Show booking by id\n" +
                            "3 - Delete booking by id\n" +
                            "4 - Update booking by id\n" +
                            "5 - Accept booking\n" +
                            "6 - Deny booking\n" +
                            "0 - Back to main menu: "
            );

            switch (input.nextLine()) {
                case "1":
                    listAllBookings();
                    break;
                case "2":
                    showBookingById();
                    break;
                case "3":
                    deleteBookingById();
                    break;
                case "4":
                    updateBookingById();
                    break;
                case "5":
                    acceptBooking();
                    break;
                case "6":
                    denyBooking();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("\nInvalid option. Try again.\n");
                    break;
            }
        }
    }

    private void viewGuestInformation() {
        Collection<User> guests = new ArrayList<>();
        Collection<Booking> bookings = getBookingController().listAllInstances().values();
        boolean bookingExists = false;
        for (User user : getUserController().listAllInstances().values()) {
            if (user.getRole().equals("guest")) {
                guests.add(user);
                for (Booking bookingOfGuest : bookings) {
                    bookingExists = true;
                    if (bookingOfGuest.getUserId() == user.getId()) {
                        System.out.println("Booking ID: " + bookingOfGuest.getBookingId());
                    }
                }
            }
            if (bookingExists) {
                System.out.println("User ID: " + user.getId());
                System.out.println("Username: " + user.getUsername()  + "\n");
            }
        }
    }

    private void acceptBooking() {
        listAllBookings();
        Booking bookingToAcceptDeny = null;
        listAllBookings();
        System.out.println("Enter booking ID to deny: ");
        Integer bookingID = Integer.parseInt(input.nextLine());
        for (Booking booking : getBookingController().listAllInstances().values()) {
            if (booking.getBookingId() == bookingID) {
                bookingToAcceptDeny = booking;
            }
        }
        bookingToAcceptDeny.setBookingStatus(Booking.BookingStatus.DENIED);
        // TODO: add csv data updates every time a model is updated;
        String[] updatedValues = new String[]{
                String.valueOf(bookingToAcceptDeny.getHotelId()),
                String.valueOf(bookingToAcceptDeny.getUserId()),
                String.valueOf(bookingToAcceptDeny.getRoomId()),
                bookingToAcceptDeny.getStartDate(),
                bookingToAcceptDeny.getEndDate(),
                String.valueOf(Booking.BookingStatus.CONFIRMED)
        };
        getBookingController().updateInstanceById(bookingToAcceptDeny.getBookingId(), updatedValues);
        System.out.println("Booking with id" + bookingID + " has been accepted");
    }

    private void denyBooking() {
        Booking bookingToAcceptDeny = null;
        listAllBookings();
        System.out.println("Enter booking ID to accept: ");
        Integer bookingID = Integer.parseInt(input.nextLine());
        for (Booking booking : getBookingController().listAllInstances().values()) {
            if (booking.getBookingId() == bookingID) {
                bookingToAcceptDeny = booking;
            }
        }
        bookingToAcceptDeny.setBookingStatus(Booking.BookingStatus.DENIED);
        // TODO: add csv data updates every time a model is updated;
        String[] updatedValues = new String[]{
                String.valueOf(bookingToAcceptDeny.getHotelId()),
                String.valueOf(bookingToAcceptDeny.getUserId()),
                String.valueOf(bookingToAcceptDeny.getRoomId()),
                bookingToAcceptDeny.getStartDate(),
                bookingToAcceptDeny.getEndDate(),
                String.valueOf(Booking.BookingStatus.DENIED)
        };
        getBookingController().updateInstanceById(bookingToAcceptDeny.getBookingId(), updatedValues);
        System.out.println("Booking with id" + bookingID + " has been denied");
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

    private void updateBookingById() {
        System.out.print("Enter booking id to update: ");
        Integer bookingIdToUpdate = Integer.parseInt(input.nextLine());
        Booking bookingToUpdate = getBookingController().getInstanceById(bookingIdToUpdate);
        if (bookingToUpdate != null) {
            System.out.print("Enter new hotel id: ");
            Integer newHotelId = Integer.parseInt(input.nextLine());
            System.out.print("Enter new user id: ");
            Integer newUserId = Integer.parseInt(input.nextLine());
            System.out.print("Enter new room id: ");
            Integer newRoomId = Integer.parseInt(input.nextLine());
            System.out.print("Enter new start date (yyyy-MM-dd): ");
            String newStartDate = input.nextLine();
            System.out.print("Enter new end date (yyyy-MM-dd): ");
            String newEndDate = input.nextLine();

            String[] updatedValues = new String[]{
                    String.valueOf(newHotelId),
                    String.valueOf(newUserId),
                    String.valueOf(newRoomId),
                    newStartDate,
                    newEndDate
            };

            getBookingController().updateInstanceById(bookingIdToUpdate, updatedValues);
            System.out.println("Booking updated successfully.\n");
        } else {
            System.out.println("\nNo booking with id " + bookingIdToUpdate + "\n");
        }
    }
}


