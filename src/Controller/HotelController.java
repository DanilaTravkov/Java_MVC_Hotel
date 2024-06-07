package Controller;

import Model.Hotel;
import Storage.HotelStorage;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public class HotelController {
    private Hotel model;
    private HotelStorage storage;

    public HotelController(Hotel model, HotelStorage storage) {
        this.model = model;
        this.storage = storage;
    }

    public String getHotelName() {
        return model.getHotelName();
    }

    public String getHotelAddress() {
        return model.getHotelAddress();
    }

    public void setHotelName(String hotelName) {
        model.setHotelName(hotelName);
    }

    public void setHotelAddress(String hotelAddress) {
        model.setHotelAddress(hotelAddress);
    }

    public ArrayList<Hotel> listAllHotels() {
        ArrayList<Hotel> data = new ArrayList<>();
        Map<Integer, Hotel> hotels = storage.hotelStorage;
        for (Hotel hotelEntry : hotels.values()) {
            data.add(hotelEntry);
        }
        return data;
    }

    public Hotel getHotelById(int id) throws Exception {
        if (storage.hotelStorage.containsKey(id)) {
            return storage.hotelStorage.get(id);
        }
        else {
            throw new Exception("There is no hotel with id " + id);
        }
    }

    public Hotel createHotel(Scanner input, HotelController hotelController, int entriesCounter) {
        Hotel model = new Hotel(); // create a new model instance for each hotel
        model.setHotelId(entriesCounter);

        System.out.print("Enter hotel name: ");
        String hotelName = input.nextLine(); // Enter hotel name
        model.setHotelName(hotelName); // Set entered hotel name in the model
        System.out.println("Hotel name set: " + hotelName);

        System.out.print("Enter hotel address: ");
        String hotelAddress = input.nextLine(); // Enter hotel address
        model.setHotelAddress(hotelAddress); // Set entered hotel address in the model
        System.out.println("Hotel address set: " + hotelAddress + "\n");

        storage.hotelStorage.put(entriesCounter, model);
        return model;
    }

    /* public boolean deleteHotelById(int id) {

    }
     */
}
