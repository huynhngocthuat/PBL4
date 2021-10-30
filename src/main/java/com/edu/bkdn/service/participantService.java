package com.edu.bkdn.service;

import com.edu.bkdn.domain.participant;
import com.edu.bkdn.repository.participantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class participantService {
    @Autowired
    private participantRepository participantRepository;

    public List<participant> findAll(){
        return participantRepository.findAll();
    }
}
