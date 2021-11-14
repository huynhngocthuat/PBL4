package com.edu.bkdn.controllers;

import com.edu.bkdn.dtos.Contact.GetContactDto;
import com.edu.bkdn.models.ApplicationUser;
import com.edu.bkdn.services.ContactService;
import com.edu.bkdn.utils.httpResponse.exceptions.EmptyListException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ContactController {

    @Autowired
    private ContactService contactService;

    @GetMapping("/contacts")
    @ResponseBody
    public List<GetContactDto> getAllContactsByUserId(Authentication authentication) throws EmptyListException {
        long userId = -1L;
        if(authentication.getPrincipal() instanceof UserDetails){
            userId = ((ApplicationUser)authentication.getPrincipal()).getUser().getId();
        }
        return contactService.getContactsByUserId(userId);
    }
}
