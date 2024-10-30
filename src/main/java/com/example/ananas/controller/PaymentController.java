package com.example.ananas.controller;

import com.example.ananas.service.Service.VnpayService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

@RestController
@AllArgsConstructor
public class PaymentController {

    VnpayService vnpayService;

    @GetMapping("/createPayment")
    public String createPayment(@RequestParam String orderInfo, @RequestParam long amount)   {
        try {
            return vnpayService.createPaymentURL(orderInfo, amount);
        }
        catch (Exception e){
           return "xay ra loi: " + e.getMessage().toString();
        }
    }

    @GetMapping("/vnpay-return")
    public String vnpayReturn(@RequestParam Map<String, String> params) {
        String vnp_SecureHash = params.get("vnp_SecureHash");
        params.remove("vnp_SecureHash");

        // In ra các tham số nhận được
        System.out.println("Received params: " + params);
        StringBuilder hashData = new StringBuilder();
        params.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    try {
                        if (entry.getValue() != null) {
                            hashData.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), "UTF-8")).append("&");
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                });

        // Loại bỏ ký tự '&' cuối cùng
        if (hashData.length() > 0) {
            hashData.setLength(hashData.length() - 1);
        }

        // Tính toán mã hash
        String computedHash = vnpayService.HMACSHA512(vnpayService.getHashSecret(), hashData.toString());

        // In ra các thông tin cần thiết để kiểm tra
        System.out.println("Computed Hash: " + computedHash);
        System.out.println("Received Secure Hash: " + vnp_SecureHash);

        if (computedHash.equals(vnp_SecureHash)) {
            String vnp_ResponseCode = params.get("vnp_ResponseCode");
            if ("00".equals(vnp_ResponseCode)) {
                // thao tác lưu hóa đơn <<thêm sau :v
                //dùng một bảng phụ để lưu các thông tin liên quan đến đơn hàng gửi đi trước khi thanh toán.


                return "Giao dịch thành công";
            } else {
                return "Giao dịch thất bại";
            }
        } else {
            return "Chữ ký không hợp lệ";
        }
    }


}