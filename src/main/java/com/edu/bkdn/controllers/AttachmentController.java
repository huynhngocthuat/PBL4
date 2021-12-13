package com.edu.bkdn.controllers;

import com.edu.bkdn.dtos.Attachment.CreateAttachmentDto;
import com.edu.bkdn.dtos.Attachment.GetAttachmentDto;
import com.edu.bkdn.dtos.Message.CreateAttachmentMessageDto;
import com.edu.bkdn.dtos.Message.CreateMessageDto;
import com.edu.bkdn.models.ApplicationUser;
import com.edu.bkdn.services.AttachmentService;
import com.edu.bkdn.services.MessageService;
import com.google.gson.Gson;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AttachmentController {
    @Autowired
    MessageService messageService;
    @Autowired
    AttachmentService attachmentService;
    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @SneakyThrows
    @PostMapping("/sendAttachment")
    @ResponseBody
    public void sendAttachment(@RequestParam("uploadFiles") MultipartFile[] uploadFiles,
                                @RequestParam("conversationId") Long conversationId,
                                Authentication authentication){

//        long conversationId = 1;
        long idSender = -1L;
        if(authentication.getPrincipal() instanceof UserDetails) {
            idSender = ((ApplicationUser) authentication.getPrincipal()).getUser().getId();
        }

        for (MultipartFile file: uploadFiles) {

            //Create new attachment
            CreateAttachmentDto createAttachmentDto = new CreateAttachmentDto();
            createAttachmentDto.setData(file.getBytes());
            createAttachmentDto.setFileName(file.getOriginalFilename());
            createAttachmentDto.setFileType(file.getContentType());
            Long attachmentId = attachmentService.createAttachment(createAttachmentDto);

            //Create new message
            CreateAttachmentMessageDto createAttachmentMessageDto = new CreateAttachmentMessageDto();
            createAttachmentMessageDto.setContent(file.getOriginalFilename());
            createAttachmentMessageDto.setConversationId(conversationId);
            createAttachmentMessageDto.setUserId(idSender);
            createAttachmentMessageDto.setAttachmentId(attachmentId);
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            createAttachmentMessageDto.setCreatedAt(currentTime);
            messageService.createAttachmentMessage(createAttachmentMessageDto);

            //Response websocket
            createAttachmentMessageDto.setFileType(file.getContentType());
            Gson gson = new Gson();
            simpMessagingTemplate.convertAndSend("/topic/public/"+ createAttachmentMessageDto.getConversationId(), gson.toJson(createAttachmentMessageDto));

        }

    }

    @SneakyThrows
    @GetMapping("/downloadAttachment/{id}")
    public ResponseEntity<ByteArrayResource> downloadAttachment(@PathVariable("id") Long attachmentId){
        GetAttachmentDto getAttachmentDto = attachmentService.getAttachmentDtoById(attachmentId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(getAttachmentDto.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment:filename=\""+getAttachmentDto.getFileName()+"\"")
                .body(new ByteArrayResource(getAttachmentDto.getData()));
    }

    @SneakyThrows
    @GetMapping("/getAttachment/{id}")
    public ResponseEntity<ByteArrayResource> getAttachment(@PathVariable("id") Long attachmentId){
        GetAttachmentDto getAttachmentDto = attachmentService.getAttachmentDtoById(attachmentId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(getAttachmentDto.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment:filename=\""+getAttachmentDto.getFileName()+"\"")
                .body(new ByteArrayResource(getAttachmentDto.getData()));
    }
}