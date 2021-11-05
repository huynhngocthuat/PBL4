package com.edu.bkdn.services;

import com.edu.bkdn.models.Attachment;
import com.edu.bkdn.repositories.AttachmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttachmentService {
    @Autowired
    private AttachmentRepository attachmentRepository;

    public List<Attachment> findAll(){
        return attachmentRepository.findAll();
    }
}
