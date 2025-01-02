package com.example.ananas.repository;

import com.example.ananas.entity.TempOrder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface Temp_Order_Repository extends JpaRepository<TempOrder,Integer>, JpaSpecificationExecutor<TempOrder> {
    TempOrder findByTxnRef(String txnref);
}
