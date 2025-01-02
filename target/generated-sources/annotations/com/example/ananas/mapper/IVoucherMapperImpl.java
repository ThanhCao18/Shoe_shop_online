package com.example.ananas.mapper;

import com.example.ananas.dto.request.VoucherResquest;
import com.example.ananas.dto.response.VoucherResponse;
import com.example.ananas.entity.voucher.DiscountType;
import com.example.ananas.entity.voucher.Voucher;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 22.0.1 (Oracle Corporation)"
)
@Component
public class IVoucherMapperImpl implements IVoucherMapper {

    @Override
    public VoucherResponse voucherToVoucherResponse(Voucher voucher) {
        if ( voucher == null ) {
            return null;
        }

        VoucherResponse.VoucherResponseBuilder voucherResponse = VoucherResponse.builder();

        voucherResponse.code( voucher.getCode() );
        voucherResponse.discountType( voucher.getDiscountType() );
        voucherResponse.discountValue( voucher.getDiscountValue() );
        voucherResponse.description( voucher.getDescription() );
        voucherResponse.minOrderValue( voucher.getMinOrderValue() );
        voucherResponse.maxDiscount( voucher.getMaxDiscount() );
        voucherResponse.startDate( voucher.getStartDate() );
        voucherResponse.endDate( voucher.getEndDate() );

        return voucherResponse.build();
    }

    @Override
    public void updateVoucher(Voucher voucher, VoucherResquest voucherResquest) {
        if ( voucherResquest == null ) {
            return;
        }

        if ( voucherResquest.getCode() != null ) {
            voucher.setCode( voucherResquest.getCode() );
        }
        if ( voucherResquest.getDiscountType() != null ) {
            voucher.setDiscountType( Enum.valueOf( DiscountType.class, voucherResquest.getDiscountType() ) );
        }
        if ( voucherResquest.getDiscountValue() != null ) {
            voucher.setDiscountValue( voucherResquest.getDiscountValue() );
        }
        if ( voucherResquest.getDescription() != null ) {
            voucher.setDescription( voucherResquest.getDescription() );
        }
        if ( voucherResquest.getUsageLimit() != null ) {
            voucher.setUsageLimit( voucherResquest.getUsageLimit() );
        }
        if ( voucherResquest.getMinOrderValue() != null ) {
            voucher.setMinOrderValue( voucherResquest.getMinOrderValue() );
        }
        if ( voucherResquest.getMaxDiscount() != null ) {
            voucher.setMaxDiscount( voucherResquest.getMaxDiscount() );
        }
        if ( voucherResquest.getStartDate() != null ) {
            voucher.setStartDate( voucherResquest.getStartDate() );
        }
        if ( voucherResquest.getEndDate() != null ) {
            voucher.setEndDate( voucherResquest.getEndDate() );
        }
        if ( voucherResquest.getCreatedAt() != null ) {
            voucher.setCreatedAt( voucherResquest.getCreatedAt() );
        }
    }

    @Override
    public Voucher voucherRequestToVoucher(VoucherResquest voucherResquest) {
        if ( voucherResquest == null ) {
            return null;
        }

        Voucher voucher = new Voucher();

        voucher.setCreatedAt( voucherResquest.getCreatedAt() );
        voucher.setCode( voucherResquest.getCode() );
        voucher.setDiscountValue( voucherResquest.getDiscountValue() );
        voucher.setDescription( voucherResquest.getDescription() );
        voucher.setUsageLimit( voucherResquest.getUsageLimit() );
        voucher.setMinOrderValue( voucherResquest.getMinOrderValue() );
        voucher.setMaxDiscount( voucherResquest.getMaxDiscount() );
        voucher.setStartDate( voucherResquest.getStartDate() );
        voucher.setEndDate( voucherResquest.getEndDate() );

        return voucher;
    }
}
