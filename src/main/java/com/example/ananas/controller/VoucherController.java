package com.example.ananas.controller;
import com.example.ananas.dto.request.VoucherResquest;
import com.example.ananas.dto.response.ApiResponse;
import com.example.ananas.dto.response.VoucherResponse;
import com.example.ananas.entity.voucher.Voucher;
import com.example.ananas.service.Service.VoucherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
@RequestMapping("/voucher")
public class VoucherController {

    @Autowired
    private VoucherService voucherService;

// Phương thức chung

    // Xem chi tiết voucher -  getVoucherDetail(String code)
    @GetMapping("/{code}")
    public ResponseEntity<VoucherResponse> getVouchersForUser(@PathVariable String code) {
        return ResponseEntity.ok(voucherService.getVouchersForUser(code));
    }

// Admin riêng

    @GetMapping("/admin/list")
    public ResponseEntity<List<Voucher>> getAllVouchersForAdmin() {
        return ResponseEntity.ok(voucherService.getAllVouchersForAdmin());
    }

    // Tạo voucher mới - VoucherResponse createVoucher(VoucherRequest voucherRequest)
    @PostMapping("/admin/create")
    public ResponseEntity<Voucher> createVoucher(@Valid @RequestBody VoucherResquest voucher) {
        Voucher voucher1 = voucherService.createVoucher(voucher);
        return ResponseEntity.ok(voucher1);
    }
    // Cập nhật voucher - void updateVoucher(VoucherUpdateRequest voucherUpdateRequest)
    @PutMapping("/admin/update")
    public  ResponseEntity<Voucher> updateVoucher(@Valid @RequestBody VoucherResquest voucher) {
        Voucher voucher1 = voucherService.updateVoucher(voucher);
        return ResponseEntity.ok(voucher1);
    }

    //  Xóa voucher - void deleteVoucher(Long voucherId)
    @DeleteMapping("/admin/delete/{code}")
    public ResponseEntity<String> deleteVoucher(@PathVariable String code) {
        return ResponseEntity.ok(voucherService.deleteVoucher(code)?"Deleted voucher!" : "Deleted voucher failed");
    }
}
