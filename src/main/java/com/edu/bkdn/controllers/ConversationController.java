package com.edu.bkdn.controllers;

import com.edu.bkdn.dtos.Conversation.GetConversationDto;
import com.edu.bkdn.models.ApplicationUser;
import com.edu.bkdn.models.Conversation;
import com.edu.bkdn.models.User;
import com.edu.bkdn.services.ConversationService;
import com.edu.bkdn.utils.httpResponse.exceptions.EmptyListException;
import com.edu.bkdn.utils.httpResponse.exceptions.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ConversationController {

    @Autowired
    private ConversationService conversationService;

    @GetMapping("/conversations")
    @ResponseBody
    public List<GetConversationDto> getUsersConversationList(Authentication authentication) throws EmptyListException {
        long userId = -1L;
        if(authentication.getPrincipal() instanceof UserDetails) {
            userId = ((ApplicationUser) authentication.getPrincipal()).getUser().getId();
        }
        return this.conversationService.findAllUsersConversations(userId);
    }
}
