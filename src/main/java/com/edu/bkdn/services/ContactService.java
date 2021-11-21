package com.edu.bkdn.services;

import com.edu.bkdn.dtos.Contact.CreateContactDto;
import com.edu.bkdn.dtos.Contact.GetContactDto;
import com.edu.bkdn.models.Contact;
import com.edu.bkdn.models.User;
import com.edu.bkdn.repositories.ContactRepository;
import com.edu.bkdn.utils.HelperUtil;
import com.edu.bkdn.utils.ObjectMapperUtils;
import com.edu.bkdn.utils.httpResponse.exceptions.DuplicateException;
import com.edu.bkdn.utils.httpResponse.exceptions.EmptyListException;
import com.edu.bkdn.utils.httpResponse.exceptions.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ContactService {

    private final ContactRepository contactRepository;

    public List<GetContactDto> getContactsByUserId(long userId) throws EmptyListException
    {
        // Check if user exist or not
        if(userId == -1L){
            throw new UsernameNotFoundException("User id not found!");
        }
        // Find contacts by user ID
        List<Contact> foundContacts = this.contactRepository.getContactsByUserId(userId);
        // If user have no conversation
        if(foundContacts.isEmpty()){
            throw new EmptyListException("User: " + userId + " has no contact!!!");
        }
        return ObjectMapperUtils.mapAll(foundContacts, GetContactDto.class);
    }

    public void createContact(CreateContactDto createContactDto) throws DuplicateException
    {
        Optional<Contact> foundContact = this.contactRepository.findContactByPhone(createContactDto.getPhone());
        // Check for duplicate contact
        // Throw error if contact(phone number) already exist
        if(foundContact.isPresent()){
            throw new DuplicateException("Contact with phone number: " + createContactDto.getPhone() + " is existed");
        }

        Contact newContact = ObjectMapperUtils.map(createContactDto, Contact.class);
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        newContact.setCreatedAt(currentTime);
        newContact.setUpdatedAt(currentTime);
        newContact.setIsActive(false);
        this.contactRepository.save(newContact);
    }

    public void updateActive(Long id, boolean active) throws NotFoundException
    {
        Optional<Contact> foundContact = this.contactRepository.findById(id);
        foundContact.orElseThrow(() -> new NotFoundException("Not found contact Id"));

        foundContact.get().setIsActive(active);
        this.contactRepository.save(foundContact.get());
    }
}
