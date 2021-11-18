package com.edu.bkdn.services;

import com.edu.bkdn.dtos.Message.GetLastMessageDto;
import com.edu.bkdn.dtos.Message.GetMessageDto;
import com.edu.bkdn.models.BaseEntity;
import com.edu.bkdn.models.Conversation;
import com.edu.bkdn.models.Message;
import com.edu.bkdn.repositories.MessageRepository;
import com.edu.bkdn.utils.ObjectMapperUtils;
import com.edu.bkdn.utils.httpResponse.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private ConversationService conversationService;

    public List<Message> findAll(){
        return messageRepository.findAll();
    }

    public <S extends Message> S save(S s) {
        return messageRepository.save(s);
    }

    public List<GetMessageDto> getAllMessageByConversationId(long conversationId) throws NotFoundException {
        Optional<Conversation> foundConversation = this.conversationService.findConversationById(conversationId);
        if(!foundConversation.isPresent()){
            throw new NotFoundException("Conversation with ID: " + conversationId + " does not existed");
        }
        return ObjectMapperUtils.mapAll(foundConversation.get().getMessages(), GetMessageDto.class);
    }

    public List<GetMessageDto> getAllMessageByUserAndConversationAndDeletedAtIsNull(Long conversationId, Long userId){
        return ObjectMapperUtils.mapAll(
                        this.messageRepository.findAllByConversationAndUserAndDeletedAtIsNull(conversationId, userId),
                        GetMessageDto.class);
    }

    public GetLastMessageDto getLastMessageDtoFromListMessage(List<Message> messages){
        Message lastMessage = Collections.max(messages, Comparator.comparing(Message::getCreatedAt));
        return ObjectMapperUtils.map(lastMessage, GetLastMessageDto.class);
    }
}
