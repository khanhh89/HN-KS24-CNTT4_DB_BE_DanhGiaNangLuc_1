package vn.rikkei.cinemabox.booking;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.rikkei.cinemabox.common.InvalidStateException;
import vn.rikkei.cinemabox.common.NotEnoughSeatsException;
import vn.rikkei.cinemabox.common.ResourceNotFoundException;
import vn.rikkei.cinemabox.customer.Customer;
import vn.rikkei.cinemabox.customer.CustomerRepository;
import vn.rikkei.cinemabox.payment.PaymentGateway;
import vn.rikkei.cinemabox.showtime.Showtime;
import vn.rikkei.cinemabox.showtime.ShowtimeRepository;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final CustomerRepository customerRepository;
    private final ShowtimeRepository showtimeRepository;
    private final PaymentGateway paymentGateway;

    public BookingService(
            BookingRepository bookingRepository,
            CustomerRepository customerRepository,
            ShowtimeRepository showtimeRepository,
            PaymentGateway paymentGateway) {
        this.bookingRepository = bookingRepository;
        this.customerRepository = customerRepository;
        this.showtimeRepository = showtimeRepository;
        this.paymentGateway = paymentGateway;
    }

    @Transactional
    public BookingResponse create(CreateBookingRequest request) {
        Customer customer = customerRepository.findById(request.customerId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Không tìm thấy khách hàng id=" + request.customerId()));

        Showtime showtime = showtimeRepository.findByIdForUpdate(request.showtimeId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Không tìm thấy suất chiếu id=" + request.showtimeId()));

        if (showtime.getAvailableSeats() < request.quantity()) {
            throw new NotEnoughSeatsException(
                    "Suất chiếu không đủ " + request.quantity() + " ghế");
        }

        long grossAmount = showtime.getPrice() * request.quantity();
        long totalAmount = "GOLD".equals(customer.getMemberLevel())
                ? (grossAmount * 90L) / 100L
                : grossAmount;

        String paymentRef = paymentGateway.charge(customer.getId(), totalAmount);

        Booking booking = new Booking();
        booking.setCustomer(customer);
        booking.setShowtime(showtime);
        booking.setQuantity(request.quantity());
        booking.setTotalAmount(totalAmount);
        booking.setStatus("CONFIRMED");
        booking.setPaymentRef(paymentRef);
        bookingRepository.save(booking);

        showtime.setAvailableSeats(
                showtime.getAvailableSeats() - request.quantity());
        showtimeRepository.save(showtime);

        return BookingResponse.from(booking);
    }

    @Transactional
    public BookingResponse cancel(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Không tìm thấy vé id=" + id));

        if (!"CONFIRMED".equals(booking.getStatus())) {
            throw new InvalidStateException(
                    "Vé không ở trạng thái CONFIRMED");
        }

        Showtime showtime = showtimeRepository.findByIdForUpdate(
                        booking.getShowtime().getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Không tìm thấy suất chiếu"));

        booking.setStatus("CANCELLED");
        showtime.setAvailableSeats(
                showtime.getAvailableSeats() + booking.getQuantity());

        bookingRepository.save(booking);
        showtimeRepository.save(showtime);
        return BookingResponse.from(booking);
    }
}
