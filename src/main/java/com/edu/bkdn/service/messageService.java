package com.edu.bkdn.service;

import com.edu.bkdn.domain.message;
import com.edu.bkdn.repository.messageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class messageService {
    @Autowired
    private messageRepository messageRepository;

    public List<message> findAll(){
        return messageRepository.findAll();
    }
}
