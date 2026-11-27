-- I2. Suất chiếu ngày 05/12/2026 còn từ 20 ghế (xem đề). Viết MỘT câu SELECT bên dưới, kết thúc bằng dấu ;
SELECT m.title AS title,
       r.name AS room_name,
       s.start_time AS start_time,
       s.available_seats AS available_seats
FROM showtimes s
JOIN movies m ON m.id = s.movie_id
JOIN rooms r ON r.id = s.room_id
WHERE s.start_time >= '2026-12-05 00:00:00'
  AND s.start_time < '2026-12-06 00:00:00'
  AND s.available_seats >= 20
ORDER BY s.start_time ASC, r.name ASC;