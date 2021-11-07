package com.edu.bkdn.controllers;

import com.edu.bkdn.dtos.UserContact.GetContactDto;
import com.edu.bkdn.services.ContactService;
import com.edu.bkdn.utils.httpResponse.exceptions.EmptyListException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@AllArgsConstructor
public class ContactController {

    private final ContactService contactService;

    @GetMapping("/contacts")
    @ResponseBody
    public List<GetContactDto> getAllContacsByUserId(long userId) {
        return contactService.getContactsByUserId(userId);
    }
}
