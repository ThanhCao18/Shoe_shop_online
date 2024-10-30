package com.example.ananas.service.IService;

import com.example.ananas.dto.request.VoucherResquest;
import com.example.ananas.dto.response.ResultPaginationDTO;
import com.example.ananas.dto.response.VoucherResponse;
import com.example.ananas.entity.voucher.Voucher;
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

     boolean checkVoucher(String code);

     BigDecimal applyVoucher(Voucher voucher, BigDecimal priceBefore);

}
