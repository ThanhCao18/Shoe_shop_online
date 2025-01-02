package com.example.ananas.mapper;

import com.example.ananas.dto.MessageDTO;
import com.example.ananas.entity.Messages;
import com.example.ananas.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface IMessageMapper {
    MessageDTO toMessageDTO(Messages messages);
    Messages toMessages(MessageDTO messageDTO, User sender, User receiver);
    void updateMessageDTO(@MappingTarget Messages messages ,MessageDTO messageDTO);
}
