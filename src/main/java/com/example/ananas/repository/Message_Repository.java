package com.example.ananas.repository;

import com.example.ananas.entity.Messages;
import io.jsonwebtoken.security.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Message_Repository extends JpaRepository<Messages, Integer> {
    List<Messages> findBySenderId(Integer senderId);
    List<Messages> findByReceiverId(Integer receiverId);
    List<Messages> findBySenderIdAndReceiverId(Integer senderId, Integer receiverId);
    List<Messages> findByReceiverIdAndSenderId(Integer receiverId, Integer senderId);


    @Query(value = """
        SELECT m.message_id, m.sender_id, m.receiver_id, m.message, m.created_at, u.username AS sender_name
                             FROM (
                                 SELECT message_id, sender_id, receiver_id, message, created_at,
                                        ROW_NUMBER() OVER (PARTITION BY sender_id ORDER BY created_at DESC) AS rn
                                 FROM ananas.messages
                                 WHERE receiver_id = 1
                             ) m
                             JOIN ananas.user u ON m.sender_id = u.user_id
                             WHERE m.rn = 1;
    """, nativeQuery = true)
    List<Object[]> findLatestMessagesBySender(@Param("receiverId") Integer receiverId);
}
