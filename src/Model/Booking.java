package Model;

import java.util.UUID;

public class Booking {
    private int bookingId;
    private Status bookingStatus;

    // Конструкторы, геттеры и сеттеры

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public Status getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(Status bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public enum Status {
        // Возможные статусы бронирования
        BOOKED,
        CONFIRMED,
        CANCELED
    }
}

