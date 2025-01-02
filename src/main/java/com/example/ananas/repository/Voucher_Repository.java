package com.example.ananas.repository;

import com.example.ananas.entity.voucher.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Voucher_Repository extends JpaRepository<Voucher, Integer>, JpaSpecificationExecutor<Voucher> {
    Voucher findVoucherByCode(String code);

    Voucher findVoucherByVoucherId(Integer id);
}
