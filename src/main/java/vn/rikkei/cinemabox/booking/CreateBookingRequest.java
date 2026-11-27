package vn.rikkei.cinemabox.booking;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CreateBookingRequest(
        @NotNull(message = "customerId bắt buộc")
        Long customerId,
        @NotNull(message = "showtimeId bắt buộc")
        Long showtimeId,
        @NotNull(message = "quantity bắt buộc")
        @Min(value = 1, message = "quantity từ 1 đến 10")
        @Max(value = 10, message = "quantity từ 1 đến 10")
        Integer quantity) {
}
