package com.example.ananas.mapper;

import com.example.ananas.dto.request.VoucherResquest;
import com.example.ananas.dto.response.VoucherResponse;
import com.example.ananas.entity.voucher.Voucher;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.lang.annotation.Target;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface IVoucherMapper {

    VoucherResponse voucherToVoucherResponse(Voucher voucher);

    void updateVoucher(@MappingTarget Voucher voucher, VoucherResquest voucherResquest);

    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(target = "discountType", ignore = true)
    Voucher voucherRequestToVoucher(VoucherResquest voucherResquest);
}
