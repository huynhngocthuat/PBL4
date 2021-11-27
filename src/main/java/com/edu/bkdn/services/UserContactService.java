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
import com.edu.bkdn.utils.httpResponse.exceptions.DuplicateException;
import com.edu.bkdn.utils.httpResponse.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.OptionalInt;

@Service
public class UserContactService {

    @Autowired
    private UserContactRepository userContactRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ContactService contactService;

    public void save(UserContact userContact){
        this.userContactRepository.save(userContact);
    }

    public Optional<UserContact> findById(long userContactId){
        return this.userContactRepository.findById(userContactId);
    }

    public Optional<UserContact> findByUserIdAndContactId(long userId, long contactId){
        return this.userContactRepository.findUserContactByUserEqualsAndContactEquals(userId, contactId);
    }

    public Optional<UserContact> findByUserPhoneAndContactPhone(String userPhone, String contactPhone) throws NotFoundException {
        User foundUser = this.checkUserExistenceByPhone(userPhone);
        Contact foundCountact = this.checkContactExistenceByPhone(contactPhone);
        return this.findByUserIdAndContactId(foundUser.getId(), foundCountact.getId());
    }

    public void createUserContact(CreateUserContactDto createUserContactDto) throws DuplicateException, NotFoundException {
        User foundUser = this.checkUserExistenceByPhone(createUserContactDto.getUserPhone());
        Contact foundContact = this.checkContactExistenceByPhone(createUserContactDto.getContactPhone());

        Optional<UserContact> foundUserContact =
                this.findByUserIdAndContactId(
                                foundUser.getId(),
                                foundContact.getId()
                        );
        if(foundUserContact.isPresent()){
            throw new DuplicateException("Duplicate user contact, user phone: " + createUserContactDto.getUserPhone()
            + ", contact phone: " + createUserContactDto.getContactPhone());
        }

        this.userContactRepository.save(new UserContact(
                foundUser,
                foundContact,
                foundUser.getId(),
                createUserContactDto.isAccepted(),
                ""
                ));
    }

    public void delete(UserContact userContact){
        this.userContactRepository.delete(userContact);
    }

    public void createUserContactRegister(Long userId) throws NotFoundException
    {
        //Throw not found
        Optional<User> foundUser = this.userService.findUserById(userId);
        foundUser.orElseThrow(() -> new NotFoundException("Not found user Id"));
        Optional<Contact> foundContact = this.contactService.findById(userId);
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

    private User checkUserExistenceById(long userId) throws NotFoundException {
        Optional<User> user = this.userService.findUserById(userId);
        if(userId == -1L || !user.isPresent()){
            throw new NotFoundException("User with ID: "+userId+" does not existed!");
        }
        return user.get();
    }

    private Contact checkContactExistenceById(long contactId) throws NotFoundException {
        Optional<Contact> contact = this.contactService.findById(contactId);
        if(contactId == -1L || !contact.isPresent()){
            throw new NotFoundException("Contact with ID: "+contactId+" does not existed!");
        }
        return contact.get();
    }

    private User checkUserExistenceByPhone(String userPhone) throws NotFoundException {
        Optional<User> user = this.userService.findUserByPhone(userPhone);
        if(userPhone.equals("") || !user.isPresent()){
            throw new NotFoundException("User with phone number: "+userPhone+" does not existed!");
        }
        return user.get();
    }

    private Contact checkContactExistenceByPhone(String contactPhone) throws NotFoundException {
        Optional<Contact> contact = this.contactService.findByPhone(contactPhone);
        if(contactPhone.equals("") || !contact.isPresent()){
            throw new NotFoundException("Contact with phone number: "+contactPhone+" does not existed!");
        }
        return contact.get();
    }
}
