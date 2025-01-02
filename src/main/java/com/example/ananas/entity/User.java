package com.example.ananas.entity;

import com.example.ananas.entity.order.Order;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    int userId;

    @Column(name = "username")
    String username;

    @Column(name = "password")
    String password;

    @Column(name = "email")
    @Email(message = "")
    String email;

    @Column(name = "phone")
    String phone;

    @Column(name = "address")
    String address;

    @Column(name = "firstname")
    String firstname;

    @Column(name = "lastname")
    String lastname;

    @Column(name = "avatar")
    String avatar;

    @Column(name = "createAt")
    LocalDateTime createAt;

    @Column(name = "updateAt")
    LocalDateTime updateAt;

    @Column(name = "isActive")
    Boolean isActive;

    @ElementCollection
    Set<String>roles;

    @OneToMany(mappedBy = "sender")
    @JsonManagedReference(value = "sent-messages")
    List<Messages> sentMessages;

    @OneToMany(mappedBy = "receiver")
    @JsonManagedReference(value = "received-messages")
    List<Messages> receivedMessages;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference("user-cart")
    List<Cart> cart;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference("user-review")
    List<Review> review;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference("user-order")
    List<Order> orders;
}