package com.edu.bkdn.services;

import com.edu.bkdn.dtos.Message.GetMessageDto;
import com.edu.bkdn.models.Conversation;
import com.edu.bkdn.models.Message;
import com.edu.bkdn.repositories.MessageRepository;
import com.edu.bkdn.utils.ObjectMapperUtils;
import com.edu.bkdn.utils.httpResponse.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
}
