package com.edu.bkdn.service;

import com.edu.bkdn.domain.attachment;
import com.edu.bkdn.repository.attachmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class attachmentService {
    @Autowired
    private attachmentRepository attachmentRepository;

    public List<attachment> findAll(){
        return attachmentRepository.findAll();
    }
}
