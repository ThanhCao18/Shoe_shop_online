package com.example.ananas.repository;

import com.example.ananas.entity.Messages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Message_Repository extends JpaRepository<Messages, Integer> {
    List<Messages> findBySenderId(Integer senderId);
    List<Messages> findByReceiverId(Integer receiverId);
    List<Messages> findBySenderIdAndReceiverId(Integer senderId, Integer receiverId);
}
