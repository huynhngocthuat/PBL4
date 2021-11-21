package com.edu.bkdn.services;

import com.edu.bkdn.dtos.Contact.CreateContactDto;
import com.edu.bkdn.dtos.Contact.GetContactDto;
import com.edu.bkdn.models.Contact;
import com.edu.bkdn.models.User;
import com.edu.bkdn.models.UserContact;
import com.edu.bkdn.repositories.ContactRepository;
import com.edu.bkdn.utils.ObjectMapperUtils;
import com.edu.bkdn.utils.httpResponse.exceptions.DuplicateException;
import com.edu.bkdn.utils.httpResponse.exceptions.EmptyListException;
import com.edu.bkdn.utils.httpResponse.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private UserContactService userContactService;
    @Autowired
    private ConversationService conversationService;

    public Optional<Contact> findById(long contactId){
        return this.contactRepository.findById(contactId);
    }

    public Optional<Contact> findByPhone(String phoneNumber){
        return this.contactRepository.findContactByPhone(phoneNumber);
    }

    public List<GetContactDto> getContactsByUserPhoneAndIsAccepted(String userPhone) throws EmptyListException, NotFoundException {
        // Check if user exist or not
        User user = this.checkUserExistenceByPhone(userPhone);
        // Find contacts by user ID
        List<Contact> foundContacts = this.contactRepository.findAllByUserIdAndIsAcceptedAndDeletedAtIsNull(user.getId());
        // If user have no contact
        if(foundContacts.isEmpty()){
            throw new EmptyListException("User: " + user.getLastName() + " has no contact!!!");
        }
        List<GetContactDto> userContacts = ObjectMapperUtils.mapAll(foundContacts, GetContactDto.class);
        userContacts.forEach(contact -> {
            contact.setIsAccepted(true);
            long senderId = this.userContactService.findByUserIdAndContactId(user.getId(), contact.getId())
                    .get().getRequestSenderId();
            contact.setRequestSenderId(senderId);
        });
        // Return sorted contacts by lastname
        return userContacts.stream()
                .sorted(Comparator.comparing(GetContactDto::getLastName))
                .collect(Collectors.toList());
    }

    public List<GetContactDto> getAllPendingContactByUserPhone(String userPhone) throws NotFoundException {
        // Check if user exist or not
        User user = checkUserExistenceByPhone(userPhone);

        List<Contact> foundPendingContacts = this.contactRepository.findAllByUserIdAndAcceptedIsFalseAndDeletedAtIsNull(user.getId());
        List<GetContactDto> pendingContactDtos = ObjectMapperUtils.mapAll(foundPendingContacts, GetContactDto.class);
        pendingContactDtos.forEach(
                contact -> {
                    contact.setIsAccepted(false);
                    long senderId = this.userContactService.findByUserIdAndContactId(user.getId(), contact.getId())
                            .get().getRequestSenderId();
                    contact.setRequestSenderId(senderId);
                }
        );
        return pendingContactDtos;
    }

    public List<GetContactDto> getStrangerContactByUserPhone(String userPhone) throws NotFoundException {
        // Check if user exist or not
        User user = checkUserExistenceByPhone(userPhone);
        // List of unaccepted contact's phone (pending friend request)
        List<String> unAcceptedContactPhones = this.getAllPendingContactByUserPhone(user.getPhone())
                .stream()
                .map(GetContactDto::getPhone)
                .collect(Collectors.toList());
        // List of stranger contact (have not sent friend request)
        List<Contact> strangerContacts = this.contactRepository.findAllStrangerByUserIdAndIsNotAccepted(user.getId());
        // Dto map
        List<GetContactDto> getStrangerContactDtos = ObjectMapperUtils.mapAll(strangerContacts, GetContactDto.class);
        getStrangerContactDtos.forEach(
                contact -> {
                    // Set false for unaccepted contacts
                    if(unAcceptedContactPhones.contains(contact.getPhone())){
                        contact.setIsAccepted(false);
                    }
                    // Set request sender id for unaccepted and send(pending) contacts
                    if(this.contactRepository.findAllRelativeByUserId(user.getId())
                            .stream().map(Contact::getPhone)
                            .collect(Collectors.toList())
                            .contains(contact.getPhone())){
                        long senderId = this.userContactService.findByUserIdAndContactId(user.getId(), contact.getId())
                                .get().getRequestSenderId();
                        contact.setRequestSenderId(senderId);
                    }
                }
        );
        return getStrangerContactDtos;
    }

    public GetContactDto getSingleContactByUserPhone(String contactPhone, String userPhone) throws NotFoundException, EmptyListException {
        User user = checkUserExistenceByPhone(userPhone);
        Contact contact = checkContactExistenceByPhone(contactPhone);

        Optional<GetContactDto> getContactDto = this.getContactsByUserPhoneAndIsAccepted(user.getPhone()).stream()
                .filter(contactDto -> contactDto.getPhone().equals(contact.getPhone()))
                .findFirst();
        if(!getContactDto.isPresent()){
            throw new NotFoundException("User: " + user.getPhone() + " does not have contact with : " + contact.getPhone() + "!");
        }
        getContactDto.get().setIsAccepted(true);
        return getContactDto.get();
    }


    public void createContact(CreateContactDto createContactDto) throws DuplicateException {
        // Check for duplicate contact
        if(this.contactRepository.findContactByPhone(createContactDto.getPhone()).isPresent()){
            throw new DuplicateException("Contact with phone number: " + createContactDto.getPhone() + " is existed");
        }

        Contact newContact = ObjectMapperUtils.map(createContactDto, Contact.class);
        this.contactRepository.save(newContact);
    }

    public void createContactInvitation(String userPhone, String contactPhone) throws NotFoundException, DuplicateException {
        // Check exist
        User firstUser = checkUserExistenceByPhone(userPhone);
        Contact firstContact = checkContactExistenceByPhone(contactPhone);

        Optional<UserContact> firstUserContact = this.userContactService.findByUserIdAndContactId(firstUser.getId(), firstContact.getId());
        if(firstUserContact.isPresent() && firstUserContact.get().getDeletedAt() == null){
            throw new DuplicateException("User: " + firstUser.getPhone() + " already a friend with contact: " + firstContact.getPhone() + "!");
        }
        else if(firstUserContact.isPresent() && firstUserContact.get().getDeletedAt() != null){
            firstUserContact.get().setDeletedAt(null);
            firstUserContact.get().setRequestSenderId(firstUser.getId());
            this.userContactService.save(firstUserContact.get());
        }
        else if(!firstUserContact.isPresent()){
            UserContact newFirstUserContact = new UserContact(
                    firstUser,
                    firstContact,
                    firstUser.getId(),
                    false
            );
            this.userContactService.save(newFirstUserContact);
        }

        User secondUser = this.checkUserExistenceByPhone(firstContact.getPhone());
        Contact secondContact = this.checkContactExistenceByPhone(firstUser.getPhone());

        Optional<UserContact> secondUserContact
                = this.userContactService.findByUserIdAndContactId(secondUser.getId(), secondContact.getId());
        if(secondUserContact.isPresent() && secondUserContact.get().getDeletedAt() == null){
            throw new DuplicateException("User: " + secondUser.getPhone() + " already a friend with contact: " + secondContact.getPhone() + "!");
        }
        else if(secondUserContact.isPresent() && secondUserContact.get().getDeletedAt() != null){
            secondUserContact.get().setDeletedAt(null);
            secondUserContact.get().setRequestSenderId(firstUser.getId());
            this.userContactService.save(secondUserContact.get());
        }
        else if(!secondUserContact.isPresent()){
            UserContact newSecondUserContact = new UserContact(
                    secondUser,
                    secondContact,
                    firstUser.getId(),
                    false
            );
            this.userContactService.save(newSecondUserContact);
        }
    }

    public void acceptContactInvitation(String userPhone, String contactPhone) throws NotFoundException {
        // Check exist
        User firstUser = this.checkUserExistenceByPhone(userPhone);
        Contact firstContact = this.checkContactExistenceByPhone(contactPhone);

        Optional<UserContact> firstUserContact = this.userContactService.findByUserIdAndContactId(firstUser.getId(), firstContact.getId());
        if(!firstUserContact.isPresent()){
            throw new NotFoundException("User: " + firstUser.getPhone() +
                    " have not sent a friend request to user: " + firstContact.getPhone());
        }
        firstUserContact.get().setIsAccepted(true);
        this.userContactService.save(firstUserContact.get());

        // Check exist
        User secondUser = this.checkUserExistenceByPhone(firstContact.getPhone());
        Contact secondContact = this.checkContactExistenceByPhone(firstUser.getPhone());

        Optional<UserContact> secondUserContact
                = this.userContactService.findByUserIdAndContactId(secondUser.getId(), secondContact.getId());
        if(!secondUserContact.isPresent()){
            throw new NotFoundException("User: " + firstUser.getPhone() +
                    " have not sent a friend request to user: " + firstContact.getPhone());
        }
        secondUserContact.get().setIsAccepted(true);
        this.userContactService.save(secondUserContact.get());
    }

    public void declineContactInvitation(String userPhone, String contactPhone) throws NotFoundException, EmptyListException {
        // Check exist
        User firstUser = this.checkUserExistenceByPhone(userPhone);
        Contact firstContact = this.checkContactExistenceByPhone(contactPhone);

        Optional<UserContact> firstUserContact = this.userContactService.findByUserIdAndContactId(firstUser.getId(), firstContact.getId());
        if(!firstUserContact.isPresent()){
            throw new NotFoundException("User: " + firstUser.getPhone() +
                    " have not sent a friend request to user: " + firstContact.getPhone());
        }
        if(this.conversationService.checkFirstUserAndSecondUserHasSameConversation(firstUser.getPhone(), firstContact.getPhone())){
            firstUserContact.get().setDeletedAt(new Timestamp(System.currentTimeMillis()));
            this.userContactService.save(firstUserContact.get());
        }
        else{
            this.userContactService.delete(firstUserContact.get());
        }

        // Check exist
        User secondUser = this.checkUserExistenceByPhone(firstContact.getPhone());
        Contact secondContact = this.checkContactExistenceByPhone(firstUser.getPhone());

        Optional<UserContact> secondUserContact
                = this.userContactService.findByUserIdAndContactId(secondUser.getId(), secondContact.getId());
        if(!secondUserContact.isPresent()){
            throw new NotFoundException("User: " + firstUser.getPhone() +
                    " have not sent a friend request to user: " + firstContact.getPhone());
        }
        if(this.conversationService.checkFirstUserAndSecondUserHasSameConversation(secondUser.getPhone(), secondContact.getPhone())){
            secondUserContact.get().setDeletedAt(new Timestamp(System.currentTimeMillis()));
            this.userContactService.save(secondUserContact.get());
        }
        else{
            this.userContactService.delete(secondUserContact.get());
        }
    }

    public void deleteUserContact(String userPhone, String contactPhone) throws NotFoundException {
        // Check exist
        User firstUser = this.checkUserExistenceByPhone(userPhone);
        Contact firstContact = this.checkContactExistenceByPhone(contactPhone);

        Optional<UserContact> firstUserContact
                = this.userContactService.findByUserIdAndContactId(firstUser.getId(), firstContact.getId());
        if(!firstUserContact.isPresent() || firstUserContact.get().getDeletedAt() != null){
            throw new NotFoundException("User: " + firstUser.getPhone() + " no longer a friend with contact: " + firstContact.getPhone() + "!");
        }
        else if(firstUserContact.get().getDeletedAt() == null){
            firstUserContact.get().setIsAccepted(false);
            firstUserContact.get().setDeletedAt(new Timestamp(System.currentTimeMillis()));
            this.userContactService.save(firstUserContact.get());
        }

        // Check exist
        User secondUser = this.checkUserExistenceByPhone(firstContact.getPhone());
        Contact secondContact = this.checkContactExistenceByPhone(firstUser.getPhone());
        Optional<UserContact> secondUserContact
                = this.userContactService.findByUserIdAndContactId(secondUser.getId(), secondContact.getId());
        if(!secondUserContact.isPresent() || secondUserContact.get().getDeletedAt() != null){
            throw new NotFoundException("User: " + firstUser.getPhone() + " no longer a friend with contact: " + firstContact.getPhone() + "!");
        }
        else if(secondUserContact.get().getDeletedAt() == null){
            secondUserContact.get().setIsAccepted(false);
            secondUserContact.get().setDeletedAt(new Timestamp(System.currentTimeMillis()));
            this.userContactService.save(secondUserContact.get());
        }
    }

    private User checkUserExistenceByPhone(String userPhone) throws NotFoundException {
        Optional<User> user = this.userService.findUserByPhone(userPhone);
        if(userPhone.equals("") || !user.isPresent()){
            throw new NotFoundException("User with phone number: "+userPhone+" does not existed!");
        }
        return user.get();
    }

    private Contact checkContactExistenceByPhone(String contactPhone) throws NotFoundException {
        Optional<Contact> contact = this.contactRepository.findContactByPhone(contactPhone);
        if(contactPhone.equals("") || !contact.isPresent()){
            throw new NotFoundException("Contact with phone number: "+contactPhone+" does not existed!");
        }
        return contact.get();
    }
}
