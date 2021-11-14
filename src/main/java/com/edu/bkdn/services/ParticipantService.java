package com.edu.bkdn.services;

import com.edu.bkdn.dtos.Participant.CreateParticipantDto;
import com.edu.bkdn.models.Conversation;
import com.edu.bkdn.models.Participant;
import com.edu.bkdn.models.ParticipantType;
import com.edu.bkdn.models.User;
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
import java.util.stream.Collectors;

@Service
@Slf4j
public class ParticipantService {
    @Autowired
    private ParticipantRepository participantRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ConversationRepository conversationRepository;

    public List<Participant> findAll(){
        return participantRepository.findAll();
    }

    public List<Participant> findAllByConversationId(long conversationId){
        return this.participantRepository.findAllByConversationId(conversationId);
    }

    public void createParticipant(CreateParticipantDto createParticipantDto) throws DuplicateException, NotFoundException {
        // Check existed user
        Optional<User> foundUser = this.userRepository.findUserById(createParticipantDto.getUserId());
        if(!foundUser.isPresent()){
            throw new UsernameNotFoundException("User with ID: " + createParticipantDto.getUserId() + " does not existed!!!");
        }
        // Check existed conversation
        Optional<Conversation> foundConversation = this.conversationRepository.findById(createParticipantDto.getConversationId());
        if(!foundConversation.isPresent() || foundConversation.get().getDeletedAt() != null){
            throw new NotFoundException("Conversation with ID: " + createParticipantDto.getConversationId() + " does not existed!!!");
        }
        // Check user have already joined conversation or not
        Optional<Participant> foundParticipant =
                this.participantRepository
                        .findParticipantByUserIdAndConversationIdAndDeletedAtIsNull(
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

    public void addParticipantToConversation(long conversationId, List<CreateParticipantDto> createParticipantDtos) throws NotFoundException {
        Optional<Conversation> foundConversation = this.conversationRepository.findById(conversationId);
        if(!foundConversation.isPresent() || foundConversation.get().getDeletedAt() != null){
            throw new NotFoundException("Conversation with Id: " + conversationId + " does not existed");
        }

        List<Long> foundParticipantsByConvId = this.participantRepository.findAllByConversationId(conversationId)
                .stream()
                .map(Participant::getId)
                .collect(Collectors.toList());
//        // Change participant type if its a conversation between 2 user
//        if(foundParticipantsByConvId.size() < 3){
//            for(Participant participant : this.participantRepository.findAllByConversationId(conversationId)){
//                participant.setParticipantType(ParticipantType.GROUP);
//                this.participantRepository.save(participant);
//            }
//        }
        // List of new participant to add into conversation
        List<Participant> addParticipant = new ArrayList<Participant>();
        // Check if participant has already in conversation
        for(CreateParticipantDto createParticipantDto : createParticipantDtos){
            if(!foundParticipantsByConvId.contains(createParticipantDto.getUserId())){
                Optional<User> foundUser = this.userRepository.findUserById(createParticipantDto.getUserId());
                if(foundUser.isPresent() && foundUser.get().getDeletedAt() == null){
                    Participant newParticipant = new Participant();
                    newParticipant.setConversation(foundConversation.get());
                    newParticipant.setUser(foundUser.get());
                    newParticipant.setParticipantType(ParticipantType.GROUP);
                    this.participantRepository.save(newParticipant);
                }
            }
        }
    }
}
