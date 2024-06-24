package Storage;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/* The Storage class
storage - a hashmap of key-value pairs where keys are unique integers and values are models to be stored
pathToCSV - actual path to a local CSV data file
fromCSVLine - a function which accepts an array of strings (data from user inputs) and returns a model instance
toCSVLine - serializes data from a model to an array of strings
header - a header line for csv files
*  */

public class Storage<ModelType> {
    private final Map<Integer, ModelType> storage = new HashMap<>();
    private final String pathToCSV;
    private final Function<String[], ModelType> fromCSVLine;
    private final Function<ModelType, String[]> toCSVLine;
    private final String[] header;

    public Storage(String pathToCSV, Function<String[], ModelType> fromCSVLine, Function<ModelType, String[]> toCSVLine, String[] header) {
        this.pathToCSV = pathToCSV;
        this.fromCSVLine = fromCSVLine;
        this.toCSVLine = toCSVLine;
        this.header = header;
        loadFromCSV();
    }

    private void loadFromCSV() {
        try (CSVReader reader = new CSVReader(new FileReader(pathToCSV))) {
            String[] line;
            boolean firstLine = true;
            while ((line = reader.readNext()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                ModelType model = fromCSVLine.apply(line);
                storage.put(Integer.parseInt(line[0]), model);
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
    }

    public void saveToCSV() {
        try (CSVWriter writer = new CSVWriter(new FileWriter(pathToCSV))) {
            // Write the header line
            writer.writeNext(header);

            // Write the data lines
            for (ModelType model : storage.values()) {
                writer.writeNext(toCSVLine.apply(model));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<Integer, ModelType> getStorage() {
        return storage;
    }
}
