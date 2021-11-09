package com.edu.bkdn.services;

import com.edu.bkdn.models.Message;
import com.edu.bkdn.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    public List<Message> findAll(){
        return messageRepository.findAll();
    }

    public <S extends Message> S save(S s) {
        return messageRepository.save(s);
    }
}
