package com.edu.bkdn.services;

import com.edu.bkdn.models.Conversation;
import com.edu.bkdn.repositories.ConversationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConversationService {
    @Autowired
    private ConversationRepository conversationRepository;

    public List<Conversation> findAll(){
        return conversationRepository.findAll();
    }

    public Optional<Conversation> findConversationById(Long id) {
        return conversationRepository.findById(id);
    }
}
