package com.example.ananas.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ErrException {
    USER_EXISTED(101,"User existed"),
    VOUCHER_EXISTED(101,"Voucher existed"),
    VOUCHER_NOT_EXISTED(101,"Voucher not existed"),
    ORDER_NOT_EXISTED(101,"Order not existed"),
    ORDER_EXISTED(101,"Order existed"),
    ORDER_ERROR_STATUS(100, "Not change order with status DELIVERED or CANCELED"),
    ORDER_ERROR_FIND_PRODUCT(100, "Not find product"),
    VALIDATION_ERROR(102,"Validation Error"),
    USER_NOT_EXISTED(102,"User not existed"),
    INVALID_KEY(105,"Invalid key"),
    EMAIL_NOT_EXISTED(106,"Email not existed"),
    EMAIL_EXISTED(107,"Email existed"),
    NOT_FILE(108,"Not file"),
    DIRECTORY_CREATION_FAILED(109,"DIRECTORY_CREATION_FAILED"),
    FILE_STORAGE_FAILED(110, "File storage failed"),
    INVALID_DATE_FORMAT(112, "Invalid createdAt format"),
    ORDER_STATUS_INVALID(100, "status không hợp lệ");
    private int code;
    private String message;
}
