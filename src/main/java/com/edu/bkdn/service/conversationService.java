package com.edu.bkdn.service;

import com.edu.bkdn.domain.conversation;
import com.edu.bkdn.repository.conversationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class conversationService {
    @Autowired
    private conversationRepository conversationRepository;

    public List<conversation> findAll(){
        return conversationRepository.findAll();
    }
}
