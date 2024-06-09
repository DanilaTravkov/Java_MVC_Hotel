import Controller.HotelController;
import Model.Hotel;
import Storage.HotelStorage;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    static int entriesCounter = 0;
    static HotelStorage storage = new HotelStorage();

    public static void main(String[] args) throws Exception {

        Hotel model = new Hotel(); // create a model instance
        HotelController controller = new HotelController(model, storage); // pass the model to the controller constructor
        Scanner input = new Scanner(System.in); // create a scanner instance

        while (true) {
            System.out.println("Main menu");
            System.out.print(
                    "1 - create a hotel\n" +
                    "2 - list all hotels\n" +
                    "3 - show info about hotel by id\n" +
                    "4 - delete a hotel by id\n" +
                    "0 - exit: "
            );
            switch (input.nextLine()) {
                case "1":
//                    createHotel(input, controller);
                    controller.createHotel(input, controller, entriesCounter);
                    entriesCounter++;
                    break;
                case "2":
                    ArrayList<Hotel> data = controller.listAllHotels();
                    if (data.isEmpty()) {
                        System.out.println("\nNo hotels found\n");
                    }
                    for (Hotel hotel : data) {
                        System.out.println("\n" + "id: " + hotel.getHotelId());
                        System.out.println("Hotel name: " + hotel.getHotelName());
                        System.out.println("Hotel address: " + hotel.getHotelAddress() + "\n");
                    }
                    break;
                case "3":
                    System.out.print("Enter hotel id: ");
                    int inputId = Integer.parseInt(input.nextLine());
                    Hotel hotel = controller.getHotelById(inputId);
                    System.out.println("\n" + "id: " + hotel.getHotelId());
                    System.out.println("Hotel name: " + hotel.getHotelName());
                    System.out.println("Hotel address: " + hotel.getHotelAddress() + "\n");
                    break;
                case "0":
                    System.exit(0);
                    break;
                default:
                    System.exit(1);
                    break;
            }
        }
    }
}
