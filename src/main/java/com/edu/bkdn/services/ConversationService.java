package com.edu.bkdn.services;

import com.edu.bkdn.dtos.Contact.GetContactDto;
import com.edu.bkdn.dtos.Conversation.GetConversationDto;
import com.edu.bkdn.dtos.Conversation.UpdateConversationDto;
import com.edu.bkdn.dtos.Message.GetLastMessageDto;
import com.edu.bkdn.dtos.Participant.CreateParticipantDto;
import com.edu.bkdn.dtos.Participant.GetParticipantDto;
import com.edu.bkdn.dtos.User.GetUserDto;
import com.edu.bkdn.models.Conversation;
import com.edu.bkdn.models.Participant;
import com.edu.bkdn.models.ParticipantType;
import com.edu.bkdn.models.User;
import com.edu.bkdn.repositories.ConversationRepository;
import com.edu.bkdn.utils.ObjectMapperUtils;
import com.edu.bkdn.utils.httpResponse.exceptions.DuplicateException;
import com.edu.bkdn.utils.httpResponse.exceptions.EmptyListException;
import com.edu.bkdn.utils.httpResponse.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
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

    public List<Conversation> findAll(){
        return this.conversationRepository.findAll();
    }

    public List<GetConversationDto> findAllUsersConversations(Long userId) throws EmptyListException {
        // Check if user exist or not
        if(userId == -1L){
            throw new UsernameNotFoundException("User id not found!");
        }
        // Find conversations by user ID
        List<Conversation> foundConversations = this.conversationRepository.findAllConversationByUserId(userId);
        // If user have no conversation
        if(foundConversations.isEmpty()){
            throw new EmptyListException("User: " + userId + " has no conversation!!!");
        }
        List<GetConversationDto> getConversationDtos = ObjectMapperUtils.mapAll(foundConversations, GetConversationDto.class);
        for(int i = 0; i < foundConversations.size(); i++){
            // Check conversation is deleted or not
            if(foundConversations.get(i).getDeletedAt() == null){
                // Check for number of user in conversation
                if(foundConversations.get(i).getParticipants().size() == 2){
                    // If conversation between 2 people => get the name and urlAvatar of the other user
                    Optional<Participant> otherParticipant = foundConversations.get(i).getParticipants()
                                    .stream()
                                    .filter(participant -> participant.getUser().getId() != userId)
                                    .findFirst();
                    String title = otherParticipant.get().getUser().getFirstName() + " " + otherParticipant.get().getUser().getSurname();
                    getConversationDtos.get(i).setTitle(title);
                    getConversationDtos.get(i).setUrlAvatar(otherParticipant.get().getUser().getUrlAvatar());
                }
                else if(foundConversations.get(i).getParticipants().size() > 2){
                    // If conversation of a group and title is empty => set title = names
                    if(foundConversations.get(i).getTitle().equals("")){
                        StringBuilder title = new StringBuilder();
                        for(Participant participant : foundConversations.get(i).getParticipants()){
                            title.append(participant.getUser().getSurname()).append(",");
                        }
                        getConversationDtos.get(i).setTitle(title.substring(0, title.length() - 1));
                    }
                }
                // Set the last message
                if(foundConversations.get(i).getMessages().size() > 1){
                    int lastMessageIndex = foundConversations.get(i).getMessages().size() - 1;
                    GetLastMessageDto getLastMessageDto
                            = ObjectMapperUtils.map(foundConversations.get(i).getMessages().get(lastMessageIndex), GetLastMessageDto.class);
                    getConversationDtos.get(i).setLastMessage(getLastMessageDto);
                }
                else{
                    getConversationDtos.get(i).setLastMessage(null);
                }
            }
        }
        Collections.sort(getConversationDtos, new Comparator<GetConversationDto>() {
            @Override
            public int compare(GetConversationDto o1, GetConversationDto o2) {
                return o2.getLastMessage().getCreatedAt().compareTo(o1.getLastMessage().getCreatedAt());
            }
        });
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
        if(!foundConversation.isPresent()){
            throw new NotFoundException("Conversation with ID: " + conversationId + " does not existed");
        }
        foundConversation.get().setTitle(updateConversationDto.getTitle());
        foundConversation.get().setCreatorId(updateConversationDto.getCreatorId());
        foundConversation.get().setChannelId(updateConversationDto.getChannelId());
        foundConversation.get().setUrlAvatar(updateConversationDto.getUrlAvatar());
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

    public List<GetContactDto> getAllConversationOutsider(Long conversationId, Long userId) throws NotFoundException, EmptyListException {
        Optional<Conversation> foundConversation = this.conversationRepository.findById(conversationId);
        if(!foundConversation.isPresent()){
            throw new NotFoundException("Conversation with ID: " + conversationId + " does not existed!");
        }
        // Get list of user in conversation
        List<User> conversationParticipants = foundConversation.get().getParticipants()
                .stream()
                .map(Participant::getUser)
                .collect(Collectors.toList());
        // Get list of user ID from list of user
        List<Long> conversationParticipantIds = conversationParticipants.stream()
                .map(User::getId)
                .collect(Collectors.toList());
        // Get list of contact that not in current conversation
        List<GetContactDto> conversationOutsiders = this.contactService.getContactsByUserId(userId)
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
                    this.conversationRepository.save(foundConversation.get());
                }
            }
        }
        catch(Exception e){
            Timestamp deleteTimestamp = new Timestamp(System.currentTimeMillis());
            if(foundConversation.get().getDeletedAt() == null){
                foundConversation.get().setDeletedAt(deleteTimestamp);
                this.conversationRepository.save(foundConversation.get());
            }
        }
    }

    public void leaveConversation(Long conversationId, Long userId) throws NotFoundException {
        Optional<Conversation> foundConversation = this.conversationRepository.findById(conversationId);
        if(!foundConversation.isPresent()){
            throw new NotFoundException("Conversation with ID: " + conversationId + " does not existed or has been deleted");
        }

        Optional<User> foundUser = this.userService.findUserById(userId);
        if(!foundUser.isPresent()){
            throw new NotFoundException("User with ID: " + userId + " does not existed or has been deleted");
        }

        Optional<Participant> foundParticipants = this.participantService.findParticipantByUserIdAndConversationIdAndDeletedAtIsNull(conversationId, userId);
        foundParticipants.ifPresent(participant -> this.participantService.deleteParticipant(participant));
    }
}
