package Model;

public class Room {
    public enum RoomStatus {
        CLEANING,
        FREE,
        OCCUPIED
    }
    private int roomId;
    private int hotelId;
    private String roomType;
    private RoomStatus status;

    // Конструкторы, геттеры и сеттеры
    public Room() {}

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getHotelId() {
        return hotelId;
    }

    public void setHotelId(int hotelId) {
        this.hotelId = hotelId;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public RoomStatus getStatus() {
        return status;
    }

    public void setStatus(RoomStatus status) {
        this.status = status;
    }
}
