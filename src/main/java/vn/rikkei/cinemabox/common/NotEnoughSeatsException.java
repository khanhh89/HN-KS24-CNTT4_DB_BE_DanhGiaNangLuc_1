package vn.rikkei.cinemabox.common;

public class NotEnoughSeatsException extends RuntimeException {
    public NotEnoughSeatsException(String message) {
        super(message);
    }
}
