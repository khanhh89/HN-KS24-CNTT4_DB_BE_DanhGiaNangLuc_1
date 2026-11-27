# Bài làm CinemaBox – Database + Backend

Họ tên: Đào Xuân Khánh
Mã sinh viên:
Lớp:

## Pha 1 (không dùng AI)

Tự đánh dấu phần đã xong trước mốc 1 (chỉ để GV tham khảo, không thay cho kết quả chấm):

- [ ] I1  - [ ] I2  - [ ] I3  - [ ] Endpoint GET /api/showtimes/{id}
- [ ] F1  - [ ] F2  - [ ] F3  - [ ] API khách hàng (tạo, xem, danh sách, validate, lỗi)

Ghi chú (nếu có):

## Pha 2 (được dùng AI)

### Cách bạn chống bán vượt số ghế khi nhiều người đặt cùng lúc

Dùng khoá ghi pessimistic `PESSIMISTIC_WRITE` trên suất chiếu trong cùng transaction đặt vé.
Mỗi request phải lấy khoá trước khi kiểm tra ghế và trừ ghế, nên các request cùng suất được xử lý tuần tự.
Kiểm chứng bằng test nhiều luồng `datVeDongThoiKhongVuotGhe`.

### Cách bạn hoàn tác khi thanh toán lỗi

Toàn bộ đặt vé nằm trong `@Transactional`. Gọi thanh toán trước khi lưu vé và trừ ghế.
Nếu `PaymentFailedException` được ném, transaction rollback dữ liệu, ghế không đổi và không tạo vé.

### Test bạn đã viết (tên lớp, tên test, mỗi test kiểm tra điều gì)

- `BookingHardCasesTest.thanhToanThatBaiKhongGiamGheVaKhongTaoVe`: thanh toán lỗi không đổi ghế và không tăng vé.
- `BookingHardCasesTest.datVeDongThoiKhongVuotGhe`: nhiều luồng đặt cùng suất không bán vượt và ghế cuối khớp số vé thành công.

### Nhật ký dùng AI

Trong Pha 2: dùng AI để gợi ý cách khởi tạo API đặt vé, dùng khoá ghi pessimistic,
dùng @Transactional để hoàn tác khi thanh toán ném PaymentFailedException,
và viết test đồng thời. Phần đính kèm đã được đọc, chạy compile/test và chỉnh lại
cho phù hợp với cấu trúc dự án hiện tại.

| # | Bạn hỏi AI điều gì | AI gợi ý gì | Bạn giữ / sửa / bỏ, vì sao |
|---|---|---|---|
| 1 |  |  |  |
| 2 |  |  |  |
| 3 |  |  |  |
