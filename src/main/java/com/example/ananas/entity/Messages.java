package com.example.ananas.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.Date;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "messages")
public class Messages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    int messageId;

    @Column(name = "sender_id")
    int senderId;

    @Column(name = "receiver_id")
    int receiverId;

    @Column(name = "message")
    String message;

    @Column(name = "created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    Instant createdAt;

    @PrePersist
    public void handleBeforeCreate()
    {
        this.createdAt = Instant.now();
    }

    @ManyToOne
    @JoinColumn(name = "sender_id",insertable = false, updatable = false,nullable = false)
    @JsonBackReference(value = "sent-messages")
    User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id",insertable = false, updatable = false,nullable = false)
    @JsonBackReference(value = "received-messages")
    User receiver;
}