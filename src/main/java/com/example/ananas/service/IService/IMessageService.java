package com.example.ananas.service.IService;

import com.example.ananas.dto.MessageDTO;
import com.example.ananas.entity.Messages;

import java.util.List;
import java.util.Optional;

public interface IMessageService {
    List<MessageDTO> getMessBySender(int id);
    List<MessageDTO> getMessByReceiver(int id);
    MessageDTO sendMessage(MessageDTO MessageDTO);
    MessageDTO updateMessage(int id, MessageDTO MessageDTO);
    void deleteMessage(int id);
    List<MessageDTO> getMessList(int receiverId);
    List<MessageDTO> getMessBySenderAndReceiver(int senderId, int receiverId);
    List<MessageDTO> getMessByReceiverAndSender(int receiverId, int senderId);
}
