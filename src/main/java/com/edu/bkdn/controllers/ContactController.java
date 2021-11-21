package com.edu.bkdn.controllers;

import com.edu.bkdn.dtos.Contact.GetContactDto;
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
    public List<GetContactDto> getAllUserContactsBy(Authentication authentication) throws EmptyListException {
        String userPhone = "";
        if(authentication.getPrincipal() instanceof UserDetails){
            userPhone = ((ApplicationUser)authentication.getPrincipal()).getUser().getPhone();
        }
        return this.contactService.getContactsByUserPhoneAndIsAccepted(userPhone);
    }

    @SneakyThrows
    @GetMapping("/{contactPhone}")
    @ResponseBody
    public GetContactDto getUserSingleContact(@PathVariable("contactPhone") String contactPhone,
            Authentication authentication){
        String userPhone = "";
        if(authentication.getPrincipal() instanceof UserDetails){
            userPhone = ((ApplicationUser)authentication.getPrincipal()).getUser().getPhone();
        }
        return this.contactService.getSingleContactByUserPhone(contactPhone, userPhone);
    }

    @SneakyThrows
    @GetMapping("/strangers")
    @ResponseBody
    public List<GetContactDto> getUserStrangerContact(Authentication authentication){
        String userPhone = "";
        if(authentication.getPrincipal() instanceof UserDetails){
            userPhone = ((ApplicationUser)authentication.getPrincipal()).getUser().getPhone();
        }
        return this.contactService.getStrangerContactByUserPhone(userPhone);
    }

    @SneakyThrows
    @GetMapping("/pending")
    @ResponseBody
    public List<GetContactDto> getAllPendingContact(Authentication authentication){
        String userPhone = "";
        if(authentication.getPrincipal() instanceof UserDetails){
            userPhone = ((ApplicationUser)authentication.getPrincipal()).getUser().getPhone();
        }
        return this.contactService.getAllPendingContactByUserPhone(userPhone);
    }

    @SneakyThrows
    @PostMapping("/invitation/{contactPhone}")
    @ResponseBody
    public String sentContactInvitation(@PathVariable("contactPhone") String contactPhone,
                                        Authentication authentication){
        String userPhone = "";
        if(authentication.getPrincipal() instanceof UserDetails){
            userPhone = ((ApplicationUser)authentication.getPrincipal()).getUser().getPhone();
        }
        this.contactService.createContactInvitation(userPhone, contactPhone);
        return new Gson().toJson(new NoContentResponse());
    }

    @SneakyThrows
    @PostMapping("/accept/{contactPhone}")
    @ResponseBody
    public String acceptContactInvitation(@PathVariable("contactPhone") String contactPhone,
                                          Authentication authentication){
        String userPhone = "";
        if(authentication.getPrincipal() instanceof UserDetails){
            userPhone = ((ApplicationUser)authentication.getPrincipal()).getUser().getPhone();
        }
        this.contactService.acceptContactInvitation(userPhone, contactPhone);
        return new Gson().toJson(new NoContentResponse());
    }

    @SneakyThrows
    @DeleteMapping("/decline/{contactPhone}")
    @ResponseBody
    public String declineUserContact(@PathVariable("contactPhone") String contactPhone,
                                     Authentication authentication){
        String userPhone = "";
        if(authentication.getPrincipal() instanceof UserDetails){
            userPhone = ((ApplicationUser)authentication.getPrincipal()).getUser().getPhone();
        }
        this.contactService.declineContactInvitation(userPhone, contactPhone);
        return new Gson().toJson(new NoContentResponse());
    }

    @SneakyThrows
    @DeleteMapping("/delete/{contactPhone}")
    @ResponseBody
    public String deleteUserContact(@PathVariable("contactPhone") String contactPhone,
                                Authentication authentication){
        String userPhone = "";
        if(authentication.getPrincipal() instanceof UserDetails){
            userPhone = ((ApplicationUser)authentication.getPrincipal()).getUser().getPhone();
        }
        this.contactService.deleteUserContact(userPhone, contactPhone);
        return new Gson().toJson(new NoContentResponse());
    }
}
