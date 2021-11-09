package com.edu.bkdn.services;

import com.edu.bkdn.dtos.Conversation.GetConversationDto;
import com.edu.bkdn.models.Conversation;
import com.edu.bkdn.repositories.ConversationRepository;
import com.edu.bkdn.utils.ObjectMapperUtils;
import com.edu.bkdn.utils.httpResponse.exceptions.EmptyListException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConversationService {
    @Autowired
    private ConversationRepository conversationRepository;

    public List<Conversation> findAll(){
        return this.conversationRepository.findAll();
    }

    public List<GetConversationDto> findAllUsersConversations(Long userId) throws EmptyListException {
        // Check if user exist or not
        if(userId == -1L){
            throw new UsernameNotFoundException("User id not found!");
        }
        // Find conversations by user ID
        List<Conversation> foundConversations = this.conversationRepository.findAllConversationByUserId(userId);
        // If user have no conversation
        if(foundConversations.isEmpty()){
            throw new EmptyListException("User: " + userId + " has no conversation!!!");
        }
        return ObjectMapperUtils.mapAll(foundConversations, GetConversationDto.class);
    }

    public Optional<Conversation> findConversationById(Long id) {
        return conversationRepository.findById(id);
    }
}
