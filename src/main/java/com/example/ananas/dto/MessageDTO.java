package com.example.ananas.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageDTO {
    int messageId;
    int senderId;
    int receiverId;
    String message;
    Instant createdAt;
    String senderName; // Thêm trường này

}
