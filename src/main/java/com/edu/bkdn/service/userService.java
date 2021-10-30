package com.edu.bkdn.service;

import com.edu.bkdn.domain.user;
import com.edu.bkdn.repository.userRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class userService {
    @Autowired
    private userRepository userRepository;

    public List<user> findAll(){
        return userRepository.findAll();
    }
}
