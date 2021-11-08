package com.edu.bkdn.services;

import com.edu.bkdn.models.User;
import com.edu.bkdn.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public List<User> findAll(){
        return userRepository.findAll();
    }

    public Optional<User> findUserByPhone(String phoneNumber) {
        return userRepository.findUserByPhone(phoneNumber);
    }

}
