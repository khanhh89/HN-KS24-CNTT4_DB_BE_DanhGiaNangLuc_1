package vn.rikkei.cinemabox;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import vn.rikkei.cinemabox.booking.BookingRepository;
import vn.rikkei.cinemabox.booking.BookingResponse;
import vn.rikkei.cinemabox.booking.BookingService;
import vn.rikkei.cinemabox.booking.CreateBookingRequest;
import vn.rikkei.cinemabox.payment.PaymentFailedException;
import vn.rikkei.cinemabox.showtime.Showtime;
import vn.rikkei.cinemabox.showtime.ShowtimeRepository;

@SpringBootTest
class BookingHardCasesTest {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ShowtimeRepository showtimeRepository;

    @Test
    void thanhToanThatBaiKhongGiamGheVaKhongTaoVe() {
        Showtime showtimeBefore = showtimeRepository.findById(1L).orElseThrow();
        int seatsBefore = showtimeBefore.getAvailableSeats();
        long bookingCountBefore = bookingRepository.count();

        assertThrows(PaymentFailedException.class,
                () -> bookingService.create(new CreateBookingRequest(2L, 1L, 10)));

        Showtime showtimeAfter = showtimeRepository.findById(1L).orElseThrow();
        assertEquals(seatsBefore, showtimeAfter.getAvailableSeats());
        assertEquals(bookingCountBefore, bookingRepository.count());
    }

    @Test
    void datVeDongThoiKhongVuotGhe() throws Exception {
        Showtime showtimeBefore = showtimeRepository.findById(1L).orElseThrow();
        int seatsBefore = showtimeBefore.getAvailableSeats();
        int threadCount = 10;

        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch ready = new CountDownLatch(threadCount);
        CountDownLatch start = new CountDownLatch(1);
        List<Future<BookingResponse>> futures = new ArrayList<>();

        for (int i = 0; i < threadCount; i++) {
            futures.add(executor.submit(() -> {
                ready.countDown();
                start.await();
                return bookingService.create(new CreateBookingRequest(2L, 1L, 4));
            }));
        }

        ready.await();
        start.countDown();

        int successfulBookings = 0;
        for (Future<BookingResponse> future : futures) {
            try {
                future.get();
                successfulBookings++;
            } catch (ExecutionException ignored) {
            }
        }
        executor.shutdown();

        Showtime showtimeAfter = showtimeRepository.findById(1L).orElseThrow();
        assertEquals(seatsBefore - successfulBookings * 4,
                showtimeAfter.getAvailableSeats());
        assertTrue(showtimeAfter.getAvailableSeats() >= 0);
    }
}
