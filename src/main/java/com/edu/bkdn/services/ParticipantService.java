package com.edu.bkdn.services;

import com.edu.bkdn.models.Participant;
import com.edu.bkdn.repositories.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParticipantService {
    @Autowired
    private ParticipantRepository participantRepository;

    public List<Participant> findAll(){
        return participantRepository.findAll();
    }
}
