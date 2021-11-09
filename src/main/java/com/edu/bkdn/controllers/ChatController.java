package com.edu.bkdn.controllers;


import com.edu.bkdn.dtos.ChatMessage;
import com.edu.bkdn.models.Conversation;
import com.edu.bkdn.models.Message;
import com.edu.bkdn.models.User;
import com.edu.bkdn.services.MessageService;
import com.edu.bkdn.services.UserService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.sql.Date;


@Controller
public class ChatController {
    @Autowired
    UserService userService;
    @Autowired
    MessageService messageService;
    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/send")
    public void sendMessage(@Payload ChatMessage chatMessage) {
        // Set Conversation and User
        Conversation conversation = new Conversation();
        conversation.setId(Long.parseLong(chatMessage.getIdConversation()));
        User user = new User();
        user = userService.findUserByPhone(chatMessage.getSender()).get();

        // Set realtime
        long miliSeconds = System.currentTimeMillis();
        Date date = new Date(miliSeconds);

        // Tạo và thêm giá trị vào entity
        Message entity = new Message();
        entity.setMessage(chatMessage.getContent());
        entity.setConversation(conversation);
        entity.setUser(user);
        entity.setCreatedAt(date);
        messageService.save(entity);

        Gson gson = new Gson();
        simpMessagingTemplate.convertAndSend("/topic/public/"+ chatMessage.getIdConversation(), gson.toJson(entity));

    }
}
