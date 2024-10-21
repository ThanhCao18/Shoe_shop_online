package com.example.ananas.entity.order;

public enum OrderStatus {
    PENDING, //  trạng thái chờ xử lý
    SHIPPED, //  trạng thái đã gửi đi
    DELIVERED, // giao thành công đến người nhận
    CANCELED;
}
