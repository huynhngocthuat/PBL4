package com.edu.bkdn.services;

import com.edu.bkdn.dtos.User.CreateUserDto;
import com.edu.bkdn.models.ApplicationUser;
import com.edu.bkdn.models.User;
import com.edu.bkdn.repositories.UserRepository;
import com.edu.bkdn.utils.ObjectMapperUtils;
import com.edu.bkdn.utils.httpResponse.exceptions.DuplicateException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

@Service
public class UserService implements UserDetailsService{

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = this.userRepository.findUserByPhone(username);
        user.orElseThrow(() -> new UsernameNotFoundException("User '" + username + "' doesnt exist"));
        return new ApplicationUser(user.get());
    }

    public List<User> findAll(){
        return userRepository.findAll();
    }

    public Optional<User> findUserByPhone(String phoneNumber) {
        return userRepository.findUserByPhone(phoneNumber);
    }
    public void createUser(CreateUserDto createUserDto) throws DuplicateException {
        Optional<User> foundUser = this.userRepository.findUserByPhone(createUserDto.getPhone());
        // Check for duplicate user
        // Throw error if user(phone number) already exist
        if (foundUser.isPresent()) {
            throw new DuplicateException("User with phone number: " + createUserDto.getPhone() + " is existed");
        }

        User newUser = ObjectMapperUtils.map(createUserDto, User.class);
        // Encrypt user's password before saving to database
        newUser.setPassword(bCryptPasswordEncoder.encode(createUserDto.getPassword()));
        this.userRepository.save(newUser);
    }
}
