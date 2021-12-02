package com.edu.bkdn.services;

import com.edu.bkdn.dtos.Participant.CreateParticipantDto;
import com.edu.bkdn.models.*;
import com.edu.bkdn.repositories.ConversationRepository;
import com.edu.bkdn.repositories.ParticipantRepository;
import com.edu.bkdn.repositories.UserRepository;
import com.edu.bkdn.utils.httpResponse.exceptions.DuplicateException;
import com.edu.bkdn.utils.httpResponse.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ParticipantService {
    @Autowired
    private ParticipantRepository participantRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ConversationService conversationService;
    @Autowired
    private ContactService contactService;
    @Autowired
    private MessageService messageService;

    public Participant save(Participant participant){
        return this.participantRepository.save(participant);
    }

    public List<Participant> findAll(){
        return participantRepository.findAll();
    }

    public List<Participant> findAllByConversationId(long conversationId){
        return this.participantRepository.findAllByConversationId(conversationId);
    }

    public Optional<Participant> findParticipantByUserIdAndConversationIdAndDeletedAtIsNull(Long conversationId, Long userId){
        return this.participantRepository.findParticipantByUserIdAndConversationIdAndDeletedAtIsNull(conversationId, userId);
    }

    public void createParticipant(CreateParticipantDto createParticipantDto) throws NotFoundException {
        // Check existed user
        Optional<User> foundUser = this.userService.findUserById(createParticipantDto.getUserId());
        if(!foundUser.isPresent()){
            throw new UsernameNotFoundException("User with ID: " + createParticipantDto.getUserId() + " does not existed!!!");
        }
        // Check existed conversation
        Optional<Conversation> foundConversation = this.conversationService.findById(createParticipantDto.getConversationId());
        if(!foundConversation.isPresent() || foundConversation.get().getDeletedAt() != null){
            throw new NotFoundException("Conversation with ID: " + createParticipantDto.getConversationId() + " does not existed!!!");
        }
        // Check user have already joined conversation or not
        Optional<Participant> foundParticipant =
                this.findParticipantByUserIdAndConversationIdAndDeletedAtIsNull(
                                createParticipantDto.getConversationId(),
                                createParticipantDto.getUserId());
        if(!foundParticipant.isPresent()){
            Participant newParticipant = new Participant();
            newParticipant.setConversation(foundConversation.get());
            newParticipant.setUser(foundUser.get());
            newParticipant.setParticipantType(createParticipantDto.getParticipantType());
            this.participantRepository.save(newParticipant);
        }
    }


    public void addParticipantToConversation(String adderPhone, Long conversationId, List<CreateParticipantDto> createParticipantDtos) throws NotFoundException, DuplicateException {
        if(!createParticipantDtos.isEmpty()){
            Optional<Conversation> foundConversation = this.conversationService.findById(conversationId);
            if(!foundConversation.isPresent() || foundConversation.get().getDeletedAt() != null){
                throw new NotFoundException("Conversation with ID: " + conversationId + " does not existed");
            }
            User foundUser = this.checkUserExistenceByPhone(adderPhone);

            for(CreateParticipantDto createParticipantDto : createParticipantDtos){
                createParticipantDto.setConversationId(conversationId);
                createParticipantDto.setParticipantType(ParticipantType.GROUP);
                this.createParticipant(createParticipantDto);
                Optional<User> participant = this.userService.findUserById(createParticipantDto.getUserId());
                Message newMessage = new Message(
                        foundUser.getLastName() + " just added " + participant.get().getLastName() + ".",
                        foundUser,
                        foundConversation.get()
                );
                this.messageService.save(newMessage);
            }
        }
    }

    public void deleteParticipant(Participant participant){
        if(participant != null){
            this.participantRepository.delete(participant);
        }
    }

    private Conversation checkConversationExistenceById(long conversationId) throws NotFoundException {
        Optional<Conversation> foundConversation = this.conversationService.findById(conversationId);
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
}
