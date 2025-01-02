package com.example.ananas.service.IService;

import com.example.ananas.dto.request.VoucherArchive;
import com.example.ananas.dto.request.VoucherResquest;
import com.example.ananas.dto.response.ResultPaginationDTO;
import com.example.ananas.dto.response.VoucherResponse;
import com.example.ananas.entity.voucher.Voucher;
import com.example.ananas.entity.voucher.Voucher_User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.List;

public interface IVoucherService {

     ResultPaginationDTO getAllVouchersForAdmin(Specification<Voucher> specification, Pageable pageable);

     VoucherResponse getVouchersForUser(String code);

     Voucher createVoucher(VoucherResquest voucherResquest);

     Voucher updateVoucher(VoucherResquest voucherResquest);

     boolean deleteVoucher(String code);

     boolean checkVoucher(String code, BigDecimal priceBefore);

     BigDecimal applyVoucher(Voucher voucher, BigDecimal priceBefore);

     VoucherArchive archiveVoucherByUser(VoucherArchive voucherArchive);

     List<VoucherResponse> getVoucherOfUser(Integer userId);

     List<Voucher> getVoucherOk();

    BigDecimal getSumDiscount(String code, BigDecimal price);

    List<Integer> getVoucherUserByVoucherId(Integer voucherId);

    Boolean deleteVoucherUser(String code);
}
