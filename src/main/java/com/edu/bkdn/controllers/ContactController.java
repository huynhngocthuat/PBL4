package com.edu.bkdn.controllers;

import com.edu.bkdn.dtos.Contact.GetContactDto;
import com.edu.bkdn.dtos.Contact.SearchContactDto;
import com.edu.bkdn.dtos.Contact.PendingContactDto;
import com.edu.bkdn.dtos.Conversation.GetGroupConversationDto;
import com.edu.bkdn.models.ApplicationUser;
import com.edu.bkdn.services.ContactService;
import com.edu.bkdn.utils.httpResponse.NoContentResponse;
import com.edu.bkdn.utils.httpResponse.exceptions.EmptyListException;
import com.google.gson.Gson;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/contacts")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @SneakyThrows
    @GetMapping("")
    @ResponseBody
    public List<GetContactDto> getAllUserContacts(Authentication authentication) {
        long userId = -1L;
        if(authentication.getPrincipal() instanceof UserDetails) {
            userId = ((ApplicationUser) authentication.getPrincipal()).getUser().getId();
        }
        return this.contactService.getContactsByUserIDAndIsAccepted(userId);
    }

    @SneakyThrows
    @GetMapping("/{contactId}")
    @ResponseBody
    public GetContactDto getUserSingleContact(@PathVariable("contactId") long contactId,
            Authentication authentication){
        long userId = -1L;
        if(authentication.getPrincipal() instanceof UserDetails) {
            userId = ((ApplicationUser) authentication.getPrincipal()).getUser().getId();
        }
        return this.contactService.getSingleContactByUserID(userId, contactId);
    }

    @SneakyThrows
    @GetMapping("/strangers")
    @ResponseBody
    public List<GetContactDto> getUserStrangerContact(Authentication authentication){
        long userId = -1L;
        if(authentication.getPrincipal() instanceof UserDetails) {
            userId = ((ApplicationUser) authentication.getPrincipal()).getUser().getId();
        }
        return this.contactService.getStrangerContactByUserID(userId);
    }

    @SneakyThrows
    @GetMapping("/groups")
    @ResponseBody
    public List<GetGroupConversationDto> getGroupConversation(Authentication authentication){
        long userId = -1L;
        if(authentication.getPrincipal() instanceof UserDetails) {
            userId = ((ApplicationUser) authentication.getPrincipal()).getUser().getId();
        }
        return this.contactService.getGroupConversations(userId);
    }

    @SneakyThrows
    @GetMapping("/pending")
    @ResponseBody
    public List<PendingContactDto> getAllPendingContact(Authentication authentication){
        long userId = -1L;
        if(authentication.getPrincipal() instanceof UserDetails) {
            userId = ((ApplicationUser) authentication.getPrincipal()).getUser().getId();
        }
        return this.contactService.getAllPendingContactByUserID(userId);
    }

    @SneakyThrows
    @GetMapping("/search/{contactPhone}")
    @ResponseBody
    public GetContactDto searchContactByPhone(@PathVariable("contactPhone") String contactPhone,
                                                 Authentication authentication){
        long userId = -1L;
        if(authentication.getPrincipal() instanceof UserDetails) {
            userId = ((ApplicationUser) authentication.getPrincipal()).getUser().getId();
        }
        return this.contactService.searchContactByPhone(contactPhone, userId);
    }

    @SneakyThrows
    @PostMapping("/invitation/{contactId}")
    @ResponseBody
    public String sentContactInvitation(@PathVariable("contactId") long contactId,
                                        @RequestBody String invitationMessage,
                                        Authentication authentication){
        long userId = -1L;
        if(authentication.getPrincipal() instanceof UserDetails) {
            userId = ((ApplicationUser) authentication.getPrincipal()).getUser().getId();
        }
        this.contactService.createContactInvitation(userId, contactId, invitationMessage);
        return new Gson().toJson(new NoContentResponse());
    }

    @SneakyThrows
    @PostMapping("/accept/{contactId}")
    @ResponseBody
    public String acceptContactInvitation(@PathVariable("contactId") long contactId,
                                          Authentication authentication){
        long userId = -1L;
        if(authentication.getPrincipal() instanceof UserDetails) {
            userId = ((ApplicationUser) authentication.getPrincipal()).getUser().getId();
        }
        this.contactService.acceptContactInvitation(userId, contactId);
        return new Gson().toJson(new NoContentResponse());
    }

    @SneakyThrows
    @DeleteMapping("/decline/{contactId}")
    @ResponseBody
    public String declineContactInvitation(@PathVariable("contactId") long contactId,
                                     Authentication authentication){
        long userId = -1L;
        if(authentication.getPrincipal() instanceof UserDetails) {
            userId = ((ApplicationUser) authentication.getPrincipal()).getUser().getId();
        }
        this.contactService.declineContactInvitation(userId, contactId);
        return new Gson().toJson(new NoContentResponse());
    }

    @SneakyThrows
    @DeleteMapping("/delete/{contactId}")
    @ResponseBody
    public String deleteUserContact(@PathVariable("contactId") long contactId,
                                Authentication authentication){
        long userId = -1L;
        if(authentication.getPrincipal() instanceof UserDetails) {
            userId = ((ApplicationUser) authentication.getPrincipal()).getUser().getId();
        }
        this.contactService.deleteUserContact(userId, contactId);
        return new Gson().toJson(new NoContentResponse());
    }
}
