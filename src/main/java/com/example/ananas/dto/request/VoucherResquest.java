package com.example.ananas.dto.request;

import com.example.ananas.entity.voucher.DiscountType;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class VoucherResquest {

    @NotBlank(message = "Voucher code không được để trống")
    @Size(min = 3, max = 20, message = "Voucher code phải có độ dài từ 3 đến 20 ký tự")
    String code;

    @NotNull(message = "Loại giảm giá không được để trống")
    DiscountType discountType;

    @NotNull(message = "Giá trị giảm giá không được để trống")
    @DecimalMin(value = "0.0", inclusive = false, message = "Giá trị giảm giá phải lớn hơn 0")
    BigDecimal discountValue;

    @Size(max = 255, message = "Mô tả không được vượt quá 255 ký tự")
    String description;

    @NotNull(message = "Giới hạn sử dụng không được để trống")
    @Min(value = 1, message = "Giới hạn sử dụng phải lớn hơn hoặc bằng 1")
    Integer usageLimit;

    @NotNull(message = "Giá trị đơn hàng tối thiểu không được để trống")
    @DecimalMin(value = "0.0", inclusive = false, message = "Giá trị đơn hàng tối thiểu phải lớn hơn 0")
    BigDecimal minOrderValue;

    @DecimalMin(value = "0.0", inclusive = false, message = "Giá trị giảm giá tối đa phải lớn hơn 0")
    @DecimalMax(value = "10000000.0", message = "Giá trị giảm giá tối đa không được vượt quá 10 triệu")
    BigDecimal maxDiscount;

    @NotNull(message = "Ngày bắt đầu không được để trống")
    Date startDate;

    @NotNull(message = "Ngày kết thúc không được để trống")
    @FutureOrPresent(message = "Ngày kết thúc phải là ngày hiện tại hoặc trong tương lai")
    Date endDate;

    @NotNull(message = "Thời gian tạo không được để trống")
    Timestamp createdAt;
}
