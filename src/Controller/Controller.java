package Controller;

import Model.Booking;
import Model.Hotel;
import Model.Room;
import Model.User;
import Storage.Storage;

import java.util.Map;

public class Controller<ModelType> {

    private static Storage<Hotel> hotelStorage;
    private static Controller<Hotel> hotelController;

    private static Storage<User> userStorage;
    private static Controller<User> userController;

    private static Storage<Booking> bookingStorage;
    private static Controller<Booking> bookingController;

    private static Storage<Room> roomStorage;
    private static Controller<Room> roomController;

    private final Storage<ModelType> storage;

    public Controller(Storage<ModelType> storage) {
        this.storage = storage;
    }

    public void addInstance(Integer id, ModelType model) {
        storage.getStorage().put(id, model);
        storage.saveToCSV();
    }

    public ModelType getInstanceById(Integer id) {
        return storage.getStorage().get(id);
    }

    public Map<Integer, ModelType> listAllInstances() {
        return storage.getStorage();
    }

    public void updateInstanceById(Integer id, String[] newValues) {
        ModelType instanceToUpdate = storage.getStorage().get(id);
        if (instanceToUpdate != null) {
            if (instanceToUpdate instanceof Hotel && newValues.length >= 2) {
                Hotel hotel = (Hotel) instanceToUpdate;
                hotel.setHotelName(newValues[0]);
                hotel.setHotelAddress(newValues[1]);
            } else if (instanceToUpdate instanceof User && newValues.length >= 3) {
                User user = (User) instanceToUpdate;
                user.setUsername(newValues[0]);
                user.setPassword(newValues[1]);
                user.setRole(newValues[2]);
            } else if (instanceToUpdate instanceof Booking && newValues.length >= 5) {
                Booking booking = (Booking) instanceToUpdate;
                booking.setHotelId(Integer.parseInt(newValues[0]));
                booking.setUserId(Integer.parseInt(newValues[1]));
                booking.setRoomId(Integer.parseInt(newValues[2]));
                booking.setStartDate(newValues[3]);
                booking.setEndDate(newValues[4]);
                // Обновите статус бронирования, если передано достаточное количество значений
                if (newValues.length == 6) {
                    booking.setBookingStatus(Booking.BookingStatus.valueOf(newValues[5]));
                }
            } else {
                System.out.println("Invalid number of new values or incorrect instance type.");
                return;
            }

            storage.saveToCSV(); // Сохраняем изменения в CSV
            System.out.println("Instance with ID " + id + " updated successfully.\n");
        } else {
            System.out.println("Instance with ID " + id + " not found.\n");
        }
    }

    public void deleteInstanceById(Integer id) {
        storage.getStorage().remove(id);
        storage.saveToCSV();
    }

    public static Controller<Hotel> getHotelController() {
        if (hotelController == null) {
            hotelStorage = new Storage<>(
                    "src/Data/hotels.csv",
                    line -> {
                        Hotel hotel = new Hotel();
                        hotel.setHotelId(Integer.parseInt(line[0].isEmpty() ? "0" : line[0])); // Установка числового ID из CSV
                        hotel.setHotelName(line[1]);
                        hotel.setHotelAddress(line[2]);
                        return hotel;
                    },
                    hotel -> new String[]{String.valueOf(hotel.getHotelId()), hotel.getHotelName(), hotel.getHotelAddress()},
                    new String[]{"Hotel ID", "Hotel Name", "Hotel Address"}
            );
            hotelController = new Controller<Hotel>(hotelStorage);
        }
        return hotelController;
    }

    public static Controller<User> getUserController() {
        if (userController == null) {
            userStorage = new Storage<>(
                    "src/Data/users.csv",
                    line -> {
                        User user = new User();
                        user.setId(Integer.parseInt(line[0].isEmpty() ? "0" : line[0])); // Установка числового ID из CSV
                        user.setUsername(line[1]);
                        user.setPassword(line[2]);
                        user.setRole(line[3]);
                        return user;
                    },
                    user -> new String[]{String.valueOf(user.getId()), user.getUsername(), user.getPassword(), user.getRole()},
                    new String[]{"ID", "Username", "Password", "Role"}
            );
            userController = new Controller<User>(userStorage);
        }
        return userController;
    }

    public static Controller<Booking> getBookingController() {
        if (bookingController == null) {
            bookingStorage = new Storage<>(
                    "src/Data/bookings.csv",
                    line -> {
                        Booking booking = new Booking();
                        booking.setBookingId(Integer.parseInt(line[0].isEmpty() ? "0" : line[0]));
                        booking.setBookingStatus(Booking.BookingStatus.valueOf(line[1]));
                        booking.setHotelId(Integer.parseInt(line[2]));
                        booking.setUserId(Integer.parseInt(line[3]));
                        booking.setRoomId(Integer.parseInt(line[4]));
                        booking.setStartDate(line[5]);
                        booking.setEndDate(line[6]);
                        return booking;
                    },
                    booking -> new String[]{
                            String.valueOf(booking.getBookingId()),
                            booking.getBookingStatus().toString(),
                            String.valueOf(booking.getHotelId()),
                            String.valueOf(booking.getUserId()),
                            String.valueOf(booking.getRoomId()),
                            booking.getStartDate(),
                            booking.getEndDate()
                    },
                    new String[]{"Booking ID", "Booking status", "Hotel ID", "User ID", "Room ID", "Start Date", "End Date"}
            );
            bookingController = new Controller<Booking>(bookingStorage);
        }
        return bookingController;
    }

    public static Controller<Room> getRoomController() {
        if (roomStorage == null) {
            roomStorage = new Storage<>(
                    "src/Data/room.csv",
                    line -> {
                        Room room = new Room();
                        room.setRoomId(Integer.parseInt(line[0].isEmpty() ? "0" : line[0]));
                        room.setHotelId(Integer.parseInt(line[1]));
                        room.setCleanerId(Integer.parseInt(line[2]));
                        room.setRoomType(Room.RoomType.valueOf(line[3]));
                        room.setStatus(Room.RoomStatus.valueOf(line[4]));
                        return room;
                    },
                    room -> new String[]{
                        String.valueOf(room.getRoomId()),
                        String.valueOf(room.getHotelId()),
                        String.valueOf(room.getCleanerId()),
                        String.valueOf(room.getRoomType()),
                        String.valueOf(room.getStatus())
                    },
                    new String[]{"Room ID", "Hotel ID", "Cleaner ID", "Room type", "Room status"}
            );
            roomController = new Controller<Room>(roomStorage);
        }
        return roomController;
    }
}
