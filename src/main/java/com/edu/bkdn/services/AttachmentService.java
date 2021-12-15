package com.edu.bkdn.services;

import com.edu.bkdn.dtos.Attachment.CreateAttachmentDto;
import com.edu.bkdn.dtos.Attachment.GetAttachmentDto;
import com.edu.bkdn.models.Attachment;
import com.edu.bkdn.models.Message;
import com.edu.bkdn.repositories.AttachmentRepository;
import javassist.NotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class AttachmentService {
    @Autowired
    private AttachmentRepository attachmentRepository;
    @Autowired
    private MessageService messageService;

    public List<Attachment> findAll(){
        return attachmentRepository.findAll();
    }

    public GetAttachmentDto getAttachmentDtoById(Long attachmentId) throws NotFoundException{
        Optional<Attachment> foundAttachment = attachmentRepository.findAttachmentById(attachmentId);
        if(!foundAttachment.isPresent()){
            throw new NotFoundException("Attachment with id : " + attachmentId + " does not existed!!!");
        }
        GetAttachmentDto getAttachmentDto = new GetAttachmentDto();
        BeanUtils.copyProperties(foundAttachment.get(), getAttachmentDto);
        return getAttachmentDto;
    }

    public Long createAttachment(CreateAttachmentDto createAttachmentDto) throws NotFoundException{
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        Attachment attachment = new Attachment();
        attachment.setCreatedAt(currentTime);
        attachment.setData(createAttachmentDto.getData());
        attachment.setFileName(createAttachmentDto.getFileName());
        attachment.setFileType(createAttachmentDto.getFileType());
        return attachmentRepository.save(attachment).getId();
    }

    public Optional<Attachment> findAttachmentById(Long attachmentId){
        return attachmentRepository.findAttachmentById(attachmentId);
    }
}