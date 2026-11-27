package vn.rikkei.cinemabox.showtime;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {
}

package vn.rikkei.cinemabox.showtime;

import java.time.format.DateTimeFormatter;

public record ShowtimeResponse(
        Long id,
        String movieTitle,
        String roomName,
        String startTime,
        Long price,
        Integer availableSeats) {

    public static ShowtimeResponse from(Showtime showtime) {
        return new ShowtimeResponse(
                showtime.getId(),
                showtime.getMovie().getTitle(),
                showtime.getRoom().getName(),
                showtime.getStartTime()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")),
                showtime.getPrice(),
                showtime.getAvailableSeats()
        );
    }
}
