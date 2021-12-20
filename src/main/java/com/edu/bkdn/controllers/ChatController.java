package com.edu.bkdn.controllers;


import com.edu.bkdn.dtos.Message.CreateMessageDto;
import com.edu.bkdn.models.ApplicationUser;
import com.edu.bkdn.services.MessageService;
import com.edu.bkdn.services.UserService;
import com.google.gson.Gson;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;

import java.sql.Timestamp;


@Controller
public class ChatController {
    @Autowired
    UserService userService;
    @Autowired
    MessageService messageService;
    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @SneakyThrows
    @MessageMapping("/send")
    public void sendMessage(@Payload CreateMessageDto createMessageDto, Authentication authentication) {

        long idSender = -1L;
        if(authentication.getPrincipal() instanceof UserDetails) {
            idSender = ((ApplicationUser) authentication.getPrincipal()).getUser().getId();
        }
        createMessageDto.setUserId(idSender);

        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        createMessageDto.setCreatedAt(currentTime);

        messageService.createMessage(createMessageDto);
        Gson gson = new Gson();

        simpMessagingTemplate.convertAndSend("/topic/public/"+ createMessageDto.getConversationId(), gson.toJson(createMessageDto));
        simpMessagingTemplate.convertAndSend("/topic/public/global", gson.toJson(createMessageDto));
    }
}
