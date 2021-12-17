package com.edu.bkdn.controllers;

import com.edu.bkdn.dtos.Contact.GetContactDto;
import com.edu.bkdn.dtos.Contact.GetConversationContactDto;
import com.edu.bkdn.dtos.Conversation.CreateConversationDto;
import com.edu.bkdn.dtos.Conversation.GetConversationDto;
import com.edu.bkdn.dtos.Conversation.UpdateConversationDto;
import com.edu.bkdn.dtos.Message.GetMessageDto;
import com.edu.bkdn.dtos.Participant.CreateParticipantDto;
import com.edu.bkdn.dtos.User.GetUserDto;
import com.edu.bkdn.models.ApplicationUser;
import com.edu.bkdn.models.Conversation;
import com.edu.bkdn.models.ParticipantType;
import com.edu.bkdn.services.ConversationService;
import com.edu.bkdn.services.MessageService;
import com.edu.bkdn.services.ParticipantService;
import com.edu.bkdn.utils.httpResponse.NoContentResponse;
import com.google.gson.Gson;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("conversations")
public class ConversationController {

    @Autowired
    private ConversationService conversationService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private ParticipantService participantService;

    @SneakyThrows
    @GetMapping("")
    @ResponseBody
    public List<GetConversationDto> getUsersConversationList(Authentication authentication){
        String userPhone = "";
        if(authentication.getPrincipal() instanceof UserDetails) {
            userPhone = ((ApplicationUser) authentication.getPrincipal()).getUser().getPhone();
        }
        return this.conversationService.findAllUsersConversations(userPhone);
    }

    @SneakyThrows
    @GetMapping("/{id}")
    @ResponseBody
    public List<GetMessageDto> getConversationsMessageList(@PathVariable("id") long conversationId){
        return this.messageService.getAllMessageByConversationId(conversationId);
    }

    @SneakyThrows
    @GetMapping("/{id}/outsider")
    @ResponseBody
    public List<GetContactDto> getConversationOutsiders(@PathVariable("id") long conversationId,
                                                        Authentication authentication){
        String userPhone = "";
        if(authentication.getPrincipal() instanceof UserDetails) {
            userPhone = ((ApplicationUser) authentication.getPrincipal()).getUser().getPhone();
        }
        return this.conversationService.getAllConversationOutsider(conversationId, userPhone);
    }

    @SneakyThrows
    @GetMapping("/{id}/participants")
    @ResponseBody
    public List<GetConversationContactDto> getConversationParticipants(@PathVariable("id") long conversationId,
                                                                       Authentication authentication){
        long userId = -1L;
        if(authentication.getPrincipal() instanceof UserDetails) {
            userId = ((ApplicationUser) authentication.getPrincipal()).getUser().getId();
        }
        return this.conversationService.getAllConversationParticipants(conversationId, userId);
    }

    @SneakyThrows
    @PostMapping("/default")
    @ResponseBody
    public String createDefaultConversation(@RequestParam Long secondUserID,
                                            Authentication authentication){
        long userId = -1L;
        if(authentication.getPrincipal() instanceof UserDetails) {
            userId = ((ApplicationUser) authentication.getPrincipal()).getUser().getId();
        }
        List<Long> participantIDs = new ArrayList<>();
        participantIDs.add(userId);
        participantIDs.add(secondUserID);

        Conversation conversation = new Conversation();
        conversation.setTitle("");
        conversation.setCreatorId(secondUserID);
        conversation.setUrlAvatar("");
        this.conversationService.createConversation(conversation, participantIDs, ParticipantType.SINGLE);
        return new Gson().toJson(new NoContentResponse());
    }

    @SneakyThrows
    @PostMapping("")
    @ResponseBody
    public String createConversation(@RequestParam String conversationTitle,
                                    @RequestParam String conversationAvatar,
                                    @RequestParam List<Long> participantIDs,
                                    Authentication authentication){
        long userId = -1L;
        if(authentication.getPrincipal() instanceof UserDetails) {
            userId = ((ApplicationUser) authentication.getPrincipal()).getUser().getId();
        }

        Conversation conversation = new Conversation();
        if(conversationTitle == null){
            conversationTitle = "";
        }
        if(conversationAvatar == null){
            conversationAvatar = "";
        }
        conversation.setTitle(conversationTitle);
        conversation.setUrlAvatar(conversationAvatar);
        conversation.setCreatorId(userId);
        this.conversationService.createConversation(conversation, participantIDs, ParticipantType.GROUP);
        return new Gson().toJson(new NoContentResponse());
    }

    @SneakyThrows
    @PutMapping("/{id}")
    @ResponseBody
    public String updateConversation(@PathVariable("id") long conversationId,
                                     @Valid @RequestBody UpdateConversationDto updateConversationDto){
        this.conversationService.updateConversation(conversationId, updateConversationDto);
        return new Gson().toJson(new NoContentResponse());
    }

    @SneakyThrows
    @PutMapping("/{id}/add")
    @ResponseBody
    public String addParticipantToConversation(@PathVariable("id") long conversationId,
                                               @Valid @RequestBody List<Long> participantIDs,
                                               Authentication authentication){
        String userPhone = "";
        if(authentication.getPrincipal() instanceof UserDetails) {
            userPhone = ((ApplicationUser) authentication.getPrincipal()).getUser().getPhone();
        }
        this.participantService.addParticipantToConversation(userPhone, conversationId, participantIDs);
        return new Gson().toJson(new NoContentResponse());
    }

    @SneakyThrows
    @DeleteMapping("/{id}")
    @ResponseBody
    public String deleteConversation(@PathVariable("id") long conversationId){
        this.conversationService.deleteConversation(conversationId);
        return new Gson().toJson(new NoContentResponse());
    }

    @SneakyThrows
    @DeleteMapping("/{id}/leave")
    @ResponseBody
    public String leaveConversation(@PathVariable("id") long conversationId,
                                    Authentication authentication){
        String userPhone = "";
        if(authentication.getPrincipal() instanceof UserDetails) {
            userPhone = ((ApplicationUser) authentication.getPrincipal()).getUser().getPhone();
        }
        this.conversationService.leaveConversation(conversationId, userPhone);
        return new Gson().toJson(new NoContentResponse());
    }
}
