package com.edu.bkdn.controllers;

import com.edu.bkdn.dtos.Conversation.GetConversationDto;
import com.edu.bkdn.dtos.Conversation.UpdateConversationDto;
import com.edu.bkdn.dtos.Message.GetMessageDto;
import com.edu.bkdn.dtos.Participant.CreateParticipantDto;
import com.edu.bkdn.models.ApplicationUser;
import com.edu.bkdn.services.ConversationService;
import com.edu.bkdn.services.MessageService;
import com.edu.bkdn.utils.httpResponse.NoContentResponse;
import com.google.gson.Gson;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("conversations")
public class ConversationController {

    @Autowired
    private ConversationService conversationService;
    @Autowired
    private MessageService messageService;

    @SneakyThrows
    @GetMapping("")
    @ResponseBody
    public List<GetConversationDto> getUsersConversationList(Authentication authentication){
        long userId = -1L;
        if(authentication.getPrincipal() instanceof UserDetails) {
            userId = ((ApplicationUser) authentication.getPrincipal()).getUser().getId();
        }
        return this.conversationService.findAllUsersConversations(userId);
    }

    @SneakyThrows
    @GetMapping("/{id}")
    @ResponseBody
    public List<GetMessageDto> getConversationsMessageList(@PathVariable("id") Long id,
                                                           Authentication authentication){
        return this.messageService.getAllMessageByConversationId(id);
    }

    @SneakyThrows
    @PostMapping("/default")
    @ResponseBody
    public String createDefaultConversation(@Valid @RequestBody List<CreateParticipantDto> createParticipantDtos,
                                            Authentication authentication){
        long userId = -1L;
        if(authentication.getPrincipal() instanceof UserDetails) {
            userId = ((ApplicationUser) authentication.getPrincipal()).getUser().getId();
        }
        CreateParticipantDto currentUser = new CreateParticipantDto(userId, null, null);
        createParticipantDtos.add(currentUser);
        this.conversationService.createConversation(createParticipantDtos, 0L);
        return new Gson().toJson(new NoContentResponse());
    }

    @SneakyThrows
    @PostMapping("")
    @ResponseBody
    public String createConversation(@Valid @RequestBody List<CreateParticipantDto> createParticipantDtos,
                                    Authentication authentication){
        long userId = -1L;
        if(authentication.getPrincipal() instanceof UserDetails) {
            userId = ((ApplicationUser) authentication.getPrincipal()).getUser().getId();
        }
        CreateParticipantDto currentUser = new CreateParticipantDto(userId, null, null);
        createParticipantDtos.add(currentUser);
        this.conversationService.createConversation(createParticipantDtos, userId);
        return new Gson().toJson(new NoContentResponse());
    }

    @SneakyThrows
    @PutMapping("/{id}")
    @ResponseBody
    public String updateConversation(@PathVariable("id") Long conversationId,
                                     @Valid @RequestBody UpdateConversationDto updateConversationDto){
        this.conversationService.updateConversation(conversationId, updateConversationDto);
        return new Gson().toJson(new NoContentResponse());
    }

    @SneakyThrows
    @PutMapping("/{id}/add")
    @ResponseBody
    public String addParticipantToConversation(@PathVariable("id") Long conversationId,
                                               @Valid @RequestBody List<CreateParticipantDto> createParticipantDtos){
        this.conversationService.addParticipantToConversation(conversationId, createParticipantDtos);
        return new Gson().toJson(new NoContentResponse());
    }

    @SneakyThrows
    @DeleteMapping("/{id}")
    @ResponseBody
    public String deleteConversation(@PathVariable("id") Long conversationId){
        this.conversationService.deleteConversation(conversationId);
        return new Gson().toJson(new NoContentResponse());
    }
}
