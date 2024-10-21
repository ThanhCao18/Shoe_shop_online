package com.example.ananas.service.Service;

import com.example.ananas.dto.MessageDTO;
import com.example.ananas.entity.Messages;
import com.example.ananas.entity.User;
import com.example.ananas.mapper.IMessageMapper;
import com.example.ananas.repository.Message_Repository;
import com.example.ananas.repository.User_Repository;
import com.example.ananas.service.IService.IMessageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MessageService implements IMessageService {
    Message_Repository messageRepository;
    User_Repository userRepository;
    IMessageMapper messageMapper;

    @Override
    public List<MessageDTO> getAllMessages() {
        List<Messages> messages = messageRepository.findAll();
        return messages.stream()
                .map(messageMapper::toMessageDTO)
                .collect(Collectors.toList());
    }

    @Override
    public MessageDTO sendMessage(MessageDTO messageDto) {
        User sender = userRepository.findById(messageDto.getSenderId())
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        User receiver = userRepository.findById(messageDto.getReceiverId())
                .orElseThrow(() -> new RuntimeException("Receiver not found"));
        Messages newMessage = messageMapper.toMessages(messageDto,sender,receiver);
        return messageMapper.toMessageDTO(messageRepository.save(newMessage));
    }

    @Override
    public MessageDTO updateMessage(int id, MessageDTO newMessage) {
        Messages messages = messageRepository.findById(id).orElse(null);
        messageMapper.updateMessageDTO(messages, newMessage);
        return messageMapper.toMessageDTO(messageRepository.save(messages));
    }

    @Override
    public void deleteMessage(int id) {
        messageRepository.deleteById(id);
    }
}
