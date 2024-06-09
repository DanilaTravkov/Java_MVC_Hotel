package Storage;

import Model.Hotel;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HotelStorage {
    public Map<Integer, Hotel> hotelStorage = new HashMap<>();

    public HotelStorage() {
        loadFromCSV();
    }

    private void loadFromCSV() {
        try (CSVReader reader = new CSVReader(new FileReader("src/Data/hotels.csv"))) {
            String[] line;
            boolean firstLine = true; // Add a flag to skip the header line
            while ((line = reader.readNext()) != null) {
                if (firstLine) {
                    firstLine = false; // Skip the header line
                    continue;
                }
                Hotel hotel = new Hotel();
                hotel.setHotelId(Integer.parseInt(line[0]));
                hotel.setHotelName(line[1]);
                hotel.setHotelAddress(line[2]);
                hotelStorage.put(hotel.getHotelId(), hotel);
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
    }

    public void saveToCSV() {
        try (CSVWriter writer = new CSVWriter(new FileWriter("src/Data/hotels.csv"))) {
            // Write the header line
            String[] header = {"Hotel ID", "Hotel Name", "Hotel Address"};
            writer.writeNext(header);

            // Write the data lines
            for (Hotel hotel : hotelStorage.values()) {
                String[] line = {String.valueOf(hotel.getHotelId()), hotel.getHotelName(), hotel.getHotelAddress()};
                writer.writeNext(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
