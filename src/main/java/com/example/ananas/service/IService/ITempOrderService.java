package com.example.ananas.service.IService;

import com.example.ananas.entity.TempOrder;

public interface ITempOrderService {

    void save(TempOrder tempOrder);

    TempOrder findByTxnRef(String vnpTxnRef);
}
