package Model;

public class Room {

    public enum RoomStatus {
        CLEANING,
        FREE,
        OCCUPIED
    }

    public enum RoomType {
        SINGLE,
        DOUBLE
    }
    private int roomId;
    private int hotelId;
    private int cleanerId;
    private RoomType roomType;
    private RoomStatus status;

    // Конструкторы, геттеры и сеттеры
    public Room() {}

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getCleanerId() {
        return cleanerId;
    }

    public void setCleanerId(int cleanerId) {
        this.cleanerId = cleanerId;
    }

    public int getHotelId() {
        return hotelId;
    }

    public void setHotelId(int hotelId) {
        this.hotelId = hotelId;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public RoomStatus getStatus() {
        return status;
    }

    public void setStatus(RoomStatus status) {
        this.status = status;
    }
}
