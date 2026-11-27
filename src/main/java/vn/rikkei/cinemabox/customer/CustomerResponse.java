package vn.rikkei.cinemabox.customer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record CustomerResponse(
        Long id,
        String fullName,
        String email,
        String phone,
        String memberLevel,
        String createdAt) {

    public static CustomerResponse from(Customer customer) {
        return new CustomerResponse(
                customer.getId(),
                customer.getFullName(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getMemberLevel(),
                customer.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
    }
}
