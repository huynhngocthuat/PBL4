package com.edu.bkdn.services;

import com.edu.bkdn.dtos.UserContact.GetContactDto;
import com.edu.bkdn.models.Contact;
import com.edu.bkdn.repositories.ContactRepository;
import com.edu.bkdn.utils.ObjectMapperUtils;
import com.edu.bkdn.utils.httpResponse.exceptions.EmptyListException;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ContactService {

    private final ContactRepository contactRepository;

    public List<GetContactDto> getContactsByUserId(long userId) {
        List<Contact> foundContacts = this.contactRepository.getContactsByUserId(userId);
        return ObjectMapperUtils.mapAll(foundContacts, GetContactDto.class);
    }
    
}
