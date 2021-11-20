package com.edu.bkdn.services;

import com.edu.bkdn.dtos.Contact.CreateContactDto;
import com.edu.bkdn.dtos.UserContact.CreateUserContactDto;
import com.edu.bkdn.models.Contact;
import com.edu.bkdn.models.User;
import com.edu.bkdn.models.UserContact;
import com.edu.bkdn.repositories.ContactRepository;
import com.edu.bkdn.repositories.UserContactRepository;
import com.edu.bkdn.repositories.UserRepository;
import com.edu.bkdn.utils.HelperUtil;
import com.edu.bkdn.utils.ObjectMapperUtils;
import com.edu.bkdn.utils.httpResponse.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;

@Service
public class UserContactService {

    @Autowired
    private UserContactRepository userContactRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ContactRepository contactRepository;

    public void createUserContact(CreateUserContactDto createUserContactDto){
        Optional<UserContact> foundUserContact =
                HelperUtil.correctSingleTimestamp(
                this.userContactRepository
                        .findUserContactByUserEqualsAndContactEquals(
                                createUserContactDto.getContactId(),
                                createUserContactDto.getUserId()
                        ));
        if(foundUserContact.isPresent()){

        }
    }

    public void createUserContactRegister(Long userId) throws NotFoundException
    {
        //Throw not found
        Optional<User> foundUser = this.userRepository.findById(userId);
        foundUser.orElseThrow(() -> new NotFoundException("Not found user Id"));
        Optional<Contact> foundContact = this.contactRepository.findById(userId);
        foundContact.orElseThrow(() -> new NotFoundException("Not found contact Id"));

        //Set data
        UserContact newUserContact = new UserContact();
        Contact contact = new Contact(); contact.setId(userId);
        User user = new User(); user.setId(userId);
        newUserContact.setContact(contact);
        newUserContact.setUser(user);
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        newUserContact.setCreatedAt(currentTime);
        newUserContact.setUpdatedAt(currentTime);

        //Save
        this.userContactRepository.save(newUserContact);
    }
}
