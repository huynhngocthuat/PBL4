package com.edu.bkdn.services;

import com.edu.bkdn.dtos.Contact.GetContactDto;
import com.edu.bkdn.models.Contact;
import com.edu.bkdn.repositories.ContactRepository;
import com.edu.bkdn.utils.ObjectMapperUtils;
import com.edu.bkdn.utils.httpResponse.exceptions.EmptyListException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ContactService {

    private final ContactRepository contactRepository;

    public List<GetContactDto> getContactsByUserId(long userId) throws EmptyListException {
        // Check if user exist or not
        if(userId == -1L){
            throw new UsernameNotFoundException("User id not found!");
        }
        // Find contacts by user ID
        List<Contact> foundContacts = this.contactRepository.getContactsByUserId(userId);
        // If user have no conversation
        if(foundContacts.isEmpty()){
            throw new EmptyListException("User: " + userId + " has no conversation!!!");
        }
        return ObjectMapperUtils.mapAll(foundContacts, GetContactDto.class);
    }
    
}
