package Storage;

import Model.Hotel;

import java.util.HashMap;
import java.util.Map;

public class HotelStorage {
    // An in-memory storage, can potentially be replaced by a database or a file storage.
    public Map<Integer, Hotel> hotelStorage = new HashMap<Integer, Hotel>();
}
