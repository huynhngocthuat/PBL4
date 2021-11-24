package com.edu.bkdn.services;

import com.edu.bkdn.dtos.Contact.GetContactDto;
import com.edu.bkdn.dtos.Conversation.GetConversationDto;
import com.edu.bkdn.dtos.Conversation.UpdateConversationDto;
import com.edu.bkdn.dtos.Message.GetLastMessageDto;
import com.edu.bkdn.dtos.Message.GetMessageDto;
import com.edu.bkdn.dtos.Participant.CreateParticipantDto;
import com.edu.bkdn.models.*;
import com.edu.bkdn.repositories.ConversationRepository;
import com.edu.bkdn.utils.HelperUtil;
import com.edu.bkdn.utils.ObjectMapperUtils;
import com.edu.bkdn.utils.httpResponse.exceptions.DuplicateException;
import com.edu.bkdn.utils.httpResponse.exceptions.EmptyListException;
import com.edu.bkdn.utils.httpResponse.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ConversationService {
    @Autowired
    private ConversationRepository conversationRepository;
    @Autowired
    private ParticipantService participantService;
    @Autowired
    private ContactService contactService;
    @Autowired
    private UserService userService;
    @Autowired
    private MessageService messageService;

    public List<Conversation> findAll(){
        return this.conversationRepository.findAll();
    }

    public Optional<Conversation> findById(long conversationId){
        return this.conversationRepository.findById(conversationId);
    }

    public List<GetConversationDto> findAllUsersConversations(String userPhone) throws EmptyListException, NotFoundException {
        // Check if user exist or not
        User foundUser = this.checkUserExistenceByPhone(userPhone);
        // Find conversations by user ID
        List<Conversation> foundConversations = this.conversationRepository.findAllConversationByUserId(foundUser.getId())
                .stream()
                .filter(conversation -> conversation.getDeletedAt() == null)
                .collect(Collectors.toList());
        // If user have no conversation
        if(foundConversations.isEmpty()){
            throw new EmptyListException("User: " + foundUser.getId() + " has no conversation!!!");
        }
        List<GetConversationDto> getConversationDtos = ObjectMapperUtils.mapAll(foundConversations, GetConversationDto.class);
        for(int i = 0; i < foundConversations.size(); i++){
            // Check for number of user in conversation
            if(foundConversations.get(i).getParticipants().size() == 2){
                // If conversation between 2 people => get the name and urlAvatar of the other user
                Optional<Participant> otherParticipant = foundConversations.get(i).getParticipants()
                                .stream()
                                .filter(participant -> participant.getUser().getId() != foundUser.getId())
                                .findFirst();
                String title = otherParticipant.get().getUser().getFirstName() + " " + otherParticipant.get().getUser().getLastName();
                getConversationDtos.get(i).setTitle(title);
                getConversationDtos.get(i).setUrlAvatar(otherParticipant.get().getUser().getUrlAvatar());
                getConversationDtos.get(i).getParticipants().get(0).setUserUrlAvatar("");
                getConversationDtos.get(i).getParticipants().get(1).setUserUrlAvatar("");
            }
            else if(foundConversations.get(i).getParticipants().size() > 2){
                // If conversation of a group and title is empty => set title = names
                if(foundConversations.get(i).getTitle().equals("")){
                    StringBuilder title = new StringBuilder();
                    for(Participant participant : foundConversations.get(i).getParticipants()){
                        title.append(participant.getUser().getLastName()).append(",");
                    }
                    getConversationDtos.get(i).setTitle(title.substring(0, title.length() - 1));
                }
            }
            // Set the last message
            if(foundConversations.get(i).getMessages().size() > 0){
                GetLastMessageDto lassMessage = this.messageService.getLastMessageDtoFromListMessage(foundConversations.get(i).getMessages());
                getConversationDtos.get(i).setLastMessage(lassMessage);
                getConversationDtos.get(i).setUpdatedAt(lassMessage.getCreatedAt());
            }
            else{
                getConversationDtos.get(i).setLastMessage(null);
            }
        }
        Collections.reverse(getConversationDtos = this.sortConversation(getConversationDtos));
        return getConversationDtos;
    }

    public Optional<Conversation> findConversationById(Long id) {
        return conversationRepository.findById(id);
    }

    // Method to create the default conversation between 2 user when they are friended of each other
    public void createConversation(List<CreateParticipantDto> createParticipantDtos, Long creatorId) throws DuplicateException, NotFoundException {
        // Single conversation
        if(createParticipantDtos.size() == 2){
            long savedConversationId = newConversation("single-channel-", creatorId);
            // Create participants
            for(CreateParticipantDto createParticipantDto : createParticipantDtos){
                createParticipantDto.setConversationId(savedConversationId);
                createParticipantDto.setParticipantType(ParticipantType.SINGLE);
                this.participantService.createParticipant(createParticipantDto);
            }
        }
        // Group conversation
        else if(createParticipantDtos.size() > 2){
            long savedConversationId = newConversation("group-channel-", creatorId);
            // Create participants
            for(CreateParticipantDto createParticipantDto : createParticipantDtos){
                createParticipantDto.setConversationId(savedConversationId);
                createParticipantDto.setParticipantType(ParticipantType.GROUP);
                this.participantService.createParticipant(createParticipantDto);
            }
        }
    }

    public void updateConversation(Long conversationId, UpdateConversationDto updateConversationDto) throws NotFoundException {
        Optional<Conversation> foundConversation = this.conversationRepository.findById(conversationId);
        if(!foundConversation.isPresent() || foundConversation.get().getDeletedAt() != null){
            throw new NotFoundException("Conversation with ID: " + conversationId + " does not existed");
        }
        foundConversation.get().setTitle(updateConversationDto.getTitle());
        foundConversation.get().setCreatorId(updateConversationDto.getCreatorId());
        foundConversation.get().setChannelId(updateConversationDto.getChannelId());
        foundConversation.get().setUrlAvatar(updateConversationDto.getUrlAvatar());
        foundConversation.get().setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        this.conversationRepository.save(foundConversation.get());
    }

    public Long newConversation(String channelType, Long creatorId){
        Conversation newConversation = new Conversation();
        newConversation.setCreatorId(creatorId);
        newConversation.setChannelId("");
        newConversation.setTitle("");
        long savedConversationId = this.conversationRepository.save(newConversation).getId();
        Optional<Conversation> savedConversation = this.conversationRepository.findById(savedConversationId);
        savedConversation.get().setChannelId(channelType+savedConversationId);
        this.conversationRepository.save(savedConversation.get());

        return savedConversationId;
    }

    public List<GetContactDto> getAllConversationOutsider(Long conversationId, String userPhone) throws NotFoundException, EmptyListException {
        // Check exist or not
        User user = this.checkUserExistenceByPhone(userPhone);
        Conversation conversation = checkConversationExistenceById(conversationId);

        // Get list of user in conversation
        List<User> conversationParticipants = conversation.getParticipants()
                .stream()
                .map(Participant::getUser)
                .collect(Collectors.toList());
        // Get list of user ID from list of user
        List<Long> conversationParticipantIds = conversationParticipants.stream()
                .map(User::getId)
                .collect(Collectors.toList());
        // Get list of contact that not in current conversation
        List<GetContactDto> conversationOutsiders = this.contactService.getContactsByUserPhoneAndIsAccepted(user.getPhone())
                .stream()
                .filter(contact -> !conversationParticipantIds.contains(contact.getId()))
                .collect(Collectors.toList());

        return conversationOutsiders;
    }

    public void deleteConversation(Long conversationId) throws NotFoundException {
        Optional<Conversation> foundConversation = this.conversationRepository.findById(conversationId);
        if(!foundConversation.isPresent() || foundConversation.get().getDeletedAt() != null){
            throw new NotFoundException("Conversation with ID: " + conversationId + " does not existed or has been deleted");
        }

        try{
            if(foundConversation.get().getParticipants().isEmpty() && foundConversation.get().getMessages().isEmpty()){
                this.conversationRepository.delete(foundConversation.get());
            }
            else{
                Timestamp deleteTimestamp = new Timestamp(System.currentTimeMillis());
                if(foundConversation.get().getDeletedAt() == null){
                    foundConversation.get().setDeletedAt(deleteTimestamp);
                    foundConversation.get().setUpdatedAt(deleteTimestamp);
                    this.conversationRepository.save(foundConversation.get());
                }
            }
        }
        catch(Exception e){
            Timestamp deleteTimestamp = new Timestamp(System.currentTimeMillis());
            if(foundConversation.get().getDeletedAt() == null){
                foundConversation.get().setDeletedAt(deleteTimestamp);
                foundConversation.get().setUpdatedAt(deleteTimestamp);
                this.conversationRepository.save(foundConversation.get());
            }
        }
    }

    public void leaveConversation(Long conversationId, String userPhone) throws NotFoundException {
        // Check exist or not
        User user = this.checkUserExistenceByPhone(userPhone);
        Conversation conversation = checkConversationExistenceById(conversationId);

        Optional<Participant> foundParticipant = this.participantService.findParticipantByUserIdAndConversationIdAndDeletedAtIsNull(conversationId, user.getId());
        List<GetMessageDto> participantsMessages = this.messageService.getAllMessageByUserAndConversationAndDeletedAtIsNull(conversationId, user.getId());
        if(foundParticipant.isPresent() && foundParticipant.get().getParticipantType() == ParticipantType.GROUP){
            if(!participantsMessages.isEmpty()){
                Timestamp leaveTime = new Timestamp(System.currentTimeMillis());
                foundParticipant.get().setDeletedAt(leaveTime);
                this.participantService.save(foundParticipant.get());
                conversation.setUpdatedAt(leaveTime);
                this.conversationRepository.save(conversation);
            }
            else{
                this.participantService.deleteParticipant(foundParticipant.get());
            }
        }
    }

    public List<GetConversationDto> sortConversation(List<GetConversationDto> conversationDtos){
        return conversationDtos.stream()
                .sorted(
                        (e1, e2) -> {
                            if(e1.getLastMessage() == null && e2.getLastMessage() == null){
                                return HelperUtil.toTimestamp(e1.getUpdatedAt())
                                        .compareTo(HelperUtil.toTimestamp(e2.getUpdatedAt()));
                            }
                            else if (e1.getLastMessage() == null && e2.getLastMessage() != null){
                                return HelperUtil.toTimestamp(e1.getUpdatedAt())
                                        .compareTo(HelperUtil.toTimestamp(e2.getLastMessage().getCreatedAt()));
                            }
                            else if(e1.getLastMessage() != null && e2.getLastMessage() == null){
                                return HelperUtil.toTimestamp(e1.getLastMessage().getCreatedAt())
                                        .compareTo(HelperUtil.toTimestamp(e2.getUpdatedAt()));
                            }
                            return HelperUtil.toTimestamp(e1.getLastMessage().getCreatedAt())
                                    .compareTo(HelperUtil.toTimestamp(e2.getLastMessage().getCreatedAt()));
                        }
                )
                .collect(Collectors.toList());
    }

    public boolean checkFirstUserAndSecondUserHasSameConversation(String firstUserPhone, String secondUserPhone) throws NotFoundException, EmptyListException {
        User foundFirstUser = this.checkUserExistenceByPhone(firstUserPhone);
        User foundSecondUser = this.checkUserExistenceByPhone(secondUserPhone);

        for(Conversation conversation : this.conversationRepository.findAllConversationByUserId(foundFirstUser.getId())){
            if(conversation.getParticipants()
                    .stream()
                    .map(Participant::getUser)
                    .collect(Collectors.toList())
                    .stream()
                    .map(User::getPhone)
                    .collect(Collectors.toList())
                    .contains(secondUserPhone)){
                return true;
            }
        }
        return false;
    }

    private Conversation checkConversationExistenceById(long conversationId) throws NotFoundException {
        Optional<Conversation> foundConversation = this.conversationRepository.findById(conversationId);
        if(!foundConversation.isPresent() || foundConversation.get().getDeletedAt() != null){
            throw new NotFoundException("Conversation with ID: " + conversationId + " does not existed!");
        }
        return foundConversation.get();
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
