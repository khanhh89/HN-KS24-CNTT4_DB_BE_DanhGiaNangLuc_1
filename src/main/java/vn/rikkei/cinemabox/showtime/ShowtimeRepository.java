package vn.rikkei.cinemabox.showtime;

import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from Showtime s where s.id = :id")
    Optional<Showtime> findByIdForUpdate(@Param("id") Long id);
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
