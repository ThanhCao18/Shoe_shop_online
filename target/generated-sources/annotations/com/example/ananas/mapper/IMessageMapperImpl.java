package com.example.ananas.mapper;

import com.example.ananas.dto.MessageDTO;
import com.example.ananas.entity.Messages;
import com.example.ananas.entity.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 22.0.1 (Oracle Corporation)"
)
@Component
public class IMessageMapperImpl implements IMessageMapper {

    @Override
    public MessageDTO toMessageDTO(Messages messages) {
        if ( messages == null ) {
            return null;
        }

        MessageDTO.MessageDTOBuilder messageDTO = MessageDTO.builder();

        messageDTO.messageId( messages.getMessageId() );
        messageDTO.senderId( messages.getSenderId() );
        messageDTO.receiverId( messages.getReceiverId() );
        messageDTO.message( messages.getMessage() );
        messageDTO.createdAt( messages.getCreatedAt() );

        return messageDTO.build();
    }

    @Override
    public Messages toMessages(MessageDTO messageDTO, User sender, User receiver) {
        if ( messageDTO == null && sender == null && receiver == null ) {
            return null;
        }

        Messages.MessagesBuilder messages = Messages.builder();

        if ( messageDTO != null ) {
            messages.messageId( messageDTO.getMessageId() );
            messages.senderId( messageDTO.getSenderId() );
            messages.receiverId( messageDTO.getReceiverId() );
            messages.message( messageDTO.getMessage() );
            messages.createdAt( messageDTO.getCreatedAt() );
        }
        messages.sender( sender );
        messages.receiver( receiver );

        return messages.build();
    }

    @Override
    public void updateMessageDTO(Messages messages, MessageDTO messageDTO) {
        if ( messageDTO == null ) {
            return;
        }

        messages.setMessageId( messageDTO.getMessageId() );
        messages.setSenderId( messageDTO.getSenderId() );
        messages.setReceiverId( messageDTO.getReceiverId() );
        messages.setMessage( messageDTO.getMessage() );
        messages.setCreatedAt( messageDTO.getCreatedAt() );
    }
}
