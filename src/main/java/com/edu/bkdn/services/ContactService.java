package com.edu.bkdn.services;

import com.edu.bkdn.dtos.Contact.CreateContactDto;
import com.edu.bkdn.dtos.Contact.GetContactDto;
import com.edu.bkdn.dtos.Contact.GetConversationContactDto;
import com.edu.bkdn.dtos.Contact.SearchContactDto;
import com.edu.bkdn.dtos.Contact.PendingContactDto;
import com.edu.bkdn.dtos.Conversation.CreateConversationDto;
import com.edu.bkdn.dtos.Conversation.GetConversationDto;
import com.edu.bkdn.dtos.Conversation.GetGroupConversationDto;
import com.edu.bkdn.dtos.Participant.CreateParticipantDto;
import com.edu.bkdn.models.*;
import com.edu.bkdn.repositories.ContactRepository;
import com.edu.bkdn.utils.ObjectMapperUtils;
import com.edu.bkdn.utils.httpResponse.exceptions.DuplicateException;
import com.edu.bkdn.utils.httpResponse.exceptions.EmptyListException;
import com.edu.bkdn.utils.httpResponse.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.sql.Timestamp;
import java.util.ArrayList;
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

    public SearchContactDto searchContactByPhone(String phoneNumber, long userId) throws NotFoundException {
        User foundUser = this.checkUserExistenceById(userId);
        Contact foundContact = this.checkContactExistenceByPhone(phoneNumber);

        SearchContactDto searchContactDto = ObjectMapperUtils.map(foundContact, SearchContactDto.class);
        Optional<UserContact> foundUserContact = this.userContactService
                .findByUserIdAndContactId(foundUser.getId(), foundContact.getId());
        searchContactDto.setIsFriend(
                foundUserContact.isPresent() && foundUserContact.get().getIsAccepted());
        return searchContactDto;
    }

    public List<GetContactDto> getContactsByUserIDAndIsAccepted(long userId) throws EmptyListException, NotFoundException {
        // Check if user exist or not
        User user = this.checkUserExistenceById(userId);
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

    public List<GetGroupConversationDto> getGroupConversations(long userId) throws NotFoundException, EmptyListException {
        User foundUser = this.checkUserExistenceById(userId);

        List<GetConversationDto> conversationDtos
                = this.conversationService.findAllUsersConversations(foundUser.getPhone())
                .stream()
                .filter(conversation -> conversation.getParticipants().get(0).getParticipantType() == ParticipantType.GROUP)
                .collect(Collectors.toList());
        List<GetGroupConversationDto> groupConversationDtos = new ArrayList<>();
        for(GetConversationDto conversation : conversationDtos){
            GetGroupConversationDto groupConversationDto = new GetGroupConversationDto(
                    conversation.getId(),
                    conversation.getTitle(),
                    conversation.getCreatorId(),
                    conversation.getChannelId(),
                    conversation.getUrlAvatar(),
                    this.conversationService.getAllConversationParticipants(conversation.getId(), userId)
            );
            groupConversationDtos.add(groupConversationDto);
        }
        return groupConversationDtos;
    }

    public List<PendingContactDto> getAllPendingContactByUserID(long userId) throws NotFoundException {
        // Check if user exist or not
        User user = this.checkUserExistenceById(userId);
        // Get all pending contacts
        List<Contact> foundPendingContacts = this.contactRepository.findAllByUserIdAndAcceptedIsFalseAndDeletedAtIsNull(user.getId());
        // Map
        List<PendingContactDto> pendingContactDtos = ObjectMapperUtils.mapAll(foundPendingContacts, PendingContactDto.class);
        // Set properties
        for(PendingContactDto pendingContactDto : pendingContactDtos){
            Optional<UserContact> sender = this.userContactService
                    .findByUserPhoneAndContactPhone(pendingContactDto.getPhone(), user.getPhone());
            if(!sender.isPresent()){
                throw new NotFoundException("User: " + user.getPhone() + " is not a friend with contact: " + pendingContactDto.getPhone() + "!");
            }
            pendingContactDto.setIsAccepted(false);
            pendingContactDto.setRequestSenderId(sender.get().getContact().getId());
            pendingContactDto.setInvitationMessage(sender.get().getInvitationMessage());
        }
        return pendingContactDtos;
    }

    public List<GetContactDto> getStrangerContactByUserID(long userId) throws NotFoundException {
        // Check if user exist or not
        User user = this.checkUserExistenceById(userId);
        // List of unaccepted contact's phone (pending friend request)
        List<String> unAcceptedContactPhones = this.getAllPendingContactByUserID(user.getId())
                .stream()
                .map(PendingContactDto::getPhone)
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

    public GetContactDto getSingleContactByUserID(long userId, long contactId) throws NotFoundException, EmptyListException {
        User user = this.checkUserExistenceById(userId);
        Contact contact = this.checkContactExistenceById(contactId);

        Optional<GetContactDto> getContactDto = this.getContactsByUserIDAndIsAccepted(user.getId()).stream()
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
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        newContact.setCreatedAt(currentTime);
        newContact.setUpdatedAt(currentTime);
        newContact.setIsActive(false);
        this.contactRepository.save(newContact);
    }

    public void updateActive(Long id, boolean active) throws NotFoundException {
        Optional<Contact> foundContact = this.contactRepository.findById(id);
        foundContact.orElseThrow(() -> new NotFoundException("Not found contact Id"));

        foundContact.get().setIsActive(active);
        this.contactRepository.save(foundContact.get());
    }
    public void createContactInvitation(long userId, long contactId) throws NotFoundException, DuplicateException {
        // Check exist
        User firstUser = this.checkUserExistenceById(userId);
        Contact firstContact = this.checkContactExistenceById(contactId);

        Optional<UserContact> firstUserContact = this.userContactService.findByUserIdAndContactId(firstUser.getId(), firstContact.getId());
        if(firstUserContact.isPresent()
                && firstUserContact.get().getDeletedAt() == null
                && firstUserContact.get().getIsAccepted()){
            throw new DuplicateException("User: " + firstUser.getPhone() + " already a friend with contact: " + firstContact.getPhone() + "!");
        }
        else if(firstUserContact.isPresent()
                && firstUserContact.get().getDeletedAt() == null
                && !firstUserContact.get().getIsAccepted()
                && firstUserContact.get().getRequestSenderId() != firstUser.getId()){
            throw new DuplicateException("Contact: " + firstContact.getPhone() + " already sent a friend request to you (" + firstUser.getPhone() + ")! Please accept or decline the request!");
        }
        else if(firstUserContact.isPresent()
                && firstUserContact.get().getDeletedAt() == null
                && !firstUserContact.get().getIsAccepted()
                && firstUserContact.get().getRequestSenderId() == firstUser.getId()){
            throw new DuplicateException("You( " + firstUser.getPhone() + " already sent a friend request to contact: " + firstContact.getPhone() + "!");
        }
        else if(firstUserContact.isPresent() && firstUserContact.get().getDeletedAt() != null){
            firstUserContact.get().setDeletedAt(null);
            firstUserContact.get().setUpdatedAt(new Timestamp(System.currentTimeMillis()));
            firstUserContact.get().setRequestSenderId(firstUser.getId());
            firstUserContact.get().setInvitationMessage("Hello I'm "+ firstUser.getLastName());
            this.userContactService.save(firstUserContact.get());
        }
        else if(!firstUserContact.isPresent()){
            UserContact newFirstUserContact = new UserContact(
                    firstUser,
                    firstContact,
                    firstUser.getId(),
                    false,
                    "Hello I'm " + firstUser.getLastName()
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
            secondUserContact.get().setUpdatedAt(new Timestamp(System.currentTimeMillis()));
            secondUserContact.get().setRequestSenderId(firstUser.getId());
            secondUserContact.get().setInvitationMessage("");
            this.userContactService.save(secondUserContact.get());
        }
        else if(!secondUserContact.isPresent()){
            UserContact newSecondUserContact = new UserContact(
                    secondUser,
                    secondContact,
                    firstUser.getId(),
                    false,
                    ""
            );
            this.userContactService.save(newSecondUserContact);
        }
    }

    public void acceptContactInvitation(long userId, long contactId) throws NotFoundException, DuplicateException {
        // Check exist
        User firstUser = this.checkUserExistenceById(userId);
        Contact firstContact = this.checkContactExistenceById(contactId);

        Optional<UserContact> firstUserContact = this.userContactService.findByUserIdAndContactId(firstUser.getId(), firstContact.getId());
        if(!firstUserContact.isPresent()){
            throw new NotFoundException("User: " + firstUser.getPhone() +
                    " did not sent any friend request to user: " + firstContact.getPhone());
        }
        firstUserContact.get().setIsAccepted(true);
        firstUserContact.get().setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        this.userContactService.save(firstUserContact.get());

        // Check exist
        User secondUser = this.checkUserExistenceByPhone(firstContact.getPhone());
        Contact secondContact = this.checkContactExistenceByPhone(firstUser.getPhone());

        Optional<UserContact> secondUserContact
                = this.userContactService.findByUserIdAndContactId(secondUser.getId(), secondContact.getId());
        if(!secondUserContact.isPresent()){
            throw new NotFoundException("User: " + firstUser.getPhone() +
                    " did not sent any friend request to user: " + firstContact.getPhone());
        }
        secondUserContact.get().setIsAccepted(true);
        secondUserContact.get().setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        this.userContactService.save(secondUserContact.get());

        // Check existed conversation before create
        Optional<Conversation> foundConversation = this.conversationService
                .findSingleConversationByUserIDs(firstUser.getId(), secondUser.getId());
        if(foundConversation.isPresent() && foundConversation.get().getDeletedAt() != null){
            foundConversation.get().setDeletedAt(null);
            this.conversationService.save(foundConversation.get());
        }
        else if(!foundConversation.isPresent()){
            // Create default conversation between 2 user
            List<CreateParticipantDto> participantDtos = new ArrayList<>();
            participantDtos.add(new CreateParticipantDto(
                    firstUser.getId(),
                    null,
                    ParticipantType.SINGLE
            ));
            participantDtos.add(new CreateParticipantDto(
                    secondUser.getId(),
                    null,
                    ParticipantType.SINGLE
            ));
            CreateConversationDto createConversationDto = new CreateConversationDto();
            createConversationDto.setCreatorId(secondUser.getId());
            this.conversationService.createConversation(createConversationDto, participantDtos);
        }

    }

    public void declineContactInvitation(long userId, long contactId) throws NotFoundException, EmptyListException {
        // Check exist
        User firstUser = this.checkUserExistenceById(userId);
        Contact firstContact = this.checkContactExistenceById(contactId);

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

    public void deleteUserContact(long userId, long contactId) throws NotFoundException {
        // Check exist
        User firstUser = this.checkUserExistenceById(userId);
        Contact firstContact = this.checkContactExistenceById(contactId);

        Optional<UserContact> firstUserContact
                = this.userContactService.findByUserIdAndContactId(firstUser.getId(), firstContact.getId());
        if(!firstUserContact.isPresent() || firstUserContact.get().getDeletedAt() != null){
            throw new NotFoundException("User: " + firstUser.getPhone() + " no longer a friend with contact: " + firstContact.getPhone() + "!");
        }
        else if(firstUserContact.get().getDeletedAt() == null){
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            firstUserContact.get().setIsAccepted(false);
            firstUserContact.get().setDeletedAt(currentTime);
            firstUserContact.get().setUpdatedAt(currentTime);
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
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            secondUserContact.get().setIsAccepted(false);
            secondUserContact.get().setDeletedAt(currentTime);
            secondUserContact.get().setUpdatedAt(currentTime);
            this.userContactService.save(secondUserContact.get());
        }
    }

    private User checkUserExistenceById(long userId) throws NotFoundException {
        Optional<User> user = this.userService.findUserById(userId);
        if(userId == -1L || !user.isPresent()){
            throw new NotFoundException("User with ID: "+userId+" does not existed!");
        }
        return user.get();
    }

    private Contact checkContactExistenceById(long contactId) throws NotFoundException {
        Optional<Contact> contact = this.contactRepository.findById(contactId);
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
        Optional<Contact> contact = this.contactRepository.findContactByPhone(contactPhone);
        if(contactPhone.equals("") || !contact.isPresent()){
            throw new NotFoundException("Contact with phone number: "+contactPhone+" does not existed!");
        }
        return contact.get();
    }
}
