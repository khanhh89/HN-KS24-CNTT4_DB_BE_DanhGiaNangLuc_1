-- I3. Vé đã xác nhận của khách an.nguyen@example.com (xem đề). Viết MỘT câu SELECT bên dưới, kết thúc bằng dấu ;
SELECT b.id AS booking_id,
       m.title AS title,
       s.start_time AS start_time,
       b.quantity AS quantity,
       b.total_amount AS total_amount
FROM bookings b
JOIN customers c ON c.id = b.customer_id
JOIN showtimes s ON s.id = b.showtime_id
JOIN movies m ON m.id = s.movie_id
WHERE c.email = 'an.nguyen@example.com'
  AND b.status = 'CONFIRMED'
ORDER BY s.start_time ASC;