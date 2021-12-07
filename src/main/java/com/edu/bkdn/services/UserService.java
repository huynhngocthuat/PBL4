package com.edu.bkdn.services;

import com.edu.bkdn.dtos.Message.GetMessageDto;
import com.edu.bkdn.dtos.User.CreateUserDto;
import com.edu.bkdn.dtos.User.GetUserDto;
import com.edu.bkdn.dtos.User.UpdateUserDto;
import com.edu.bkdn.models.ApplicationUser;
import com.edu.bkdn.models.Contact;
import com.edu.bkdn.models.Conversation;
import com.edu.bkdn.models.User;
import com.edu.bkdn.repositories.UserRepository;
import com.edu.bkdn.utils.HelperUtil;
import com.edu.bkdn.utils.ObjectMapperUtils;
import com.edu.bkdn.utils.httpResponse.exceptions.DuplicateException;
import com.edu.bkdn.utils.httpResponse.exceptions.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

@Service
public class UserService implements UserDetailsService{

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private ContactService contactService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = this.userRepository.findUserByPhone(username);
        user.orElseThrow(() -> new UsernameNotFoundException("User '" + username + "' doesnt exist"));
        return new ApplicationUser(user.get());
    }
    public Optional<User> findUserById(Long userId){
        return this.userRepository.findUserById(userId);
    }

    public List<User> findAll(){
        return userRepository.findAll();
    }

    public Optional<User> findUserByPhone(String phoneNumber) {
        return userRepository.findUserByPhone(phoneNumber);
    }

    public GetUserDto getUserDtoByPhoneNumber(String phoneNumber) throws NotFoundException{
        Optional<User> foundUser = userRepository.findUserByPhone(phoneNumber);
        if(!foundUser.isPresent()){
            throw new NotFoundException("User '" + phoneNumber + "' doesnt exist");
        }

        GetUserDto getUserDto = ObjectMapperUtils.map(foundUser.get(), GetUserDto.class);
        return getUserDto;
    }

    public void createUser(CreateUserDto createUserDto) throws DuplicateException
    {
        Optional<User> foundUser = this.userRepository.findUserByPhone(createUserDto.getPhone());
        // Check for duplicate user
        // Throw error if user(phone number) already exist
        if (foundUser.isPresent()) {
            throw new DuplicateException("User with phone number: " + createUserDto.getPhone() + " is existed");
        }

        User newUser = ObjectMapperUtils.map(createUserDto, User.class);
        // Encrypt user's password before saving to database
        newUser.setPassword(bCryptPasswordEncoder.encode(createUserDto.getPassword()));

        newUser.setIsActive(false);
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        newUser.setCreatedAt(currentTime);
        newUser.setUpdatedAt(currentTime);
        this.userRepository.save(newUser);
    }

    public void updateUser(Long id, UpdateUserDto updateUserDto) throws NotFoundException, DuplicateException
    {
        Optional<User> foundUser = this.userRepository.findById(id);
        foundUser.orElseThrow(() -> new NotFoundException("Not found user Id"));

        Optional<User> foundUserByPhone = findUserByPhone(updateUserDto.getPhone());
        if (foundUserByPhone.isPresent() && foundUserByPhone.get().getId() != foundUser.get().getId()) {
            throw new DuplicateException("Phone number is already existed");
        }

        foundUser.get().setEmail(updateUserDto.getEmail());
        foundUser.get().setFirstName(updateUserDto.getFirstName());
        foundUser.get().setLastName(updateUserDto.getLastName());
        foundUser.get().setUrlAvatar(updateUserDto.getUrlAvatar());
        foundUser.get().setPhone(updateUserDto.getPhone());

        this.userRepository.save(foundUser.get());
    }

    public List<GetUserDto> getAllUser()
    {
        return ObjectMapperUtils.mapAll(userRepository.findAll(), GetUserDto.class);
    }

    public void updateActive(Long id, boolean active) throws NotFoundException
    {
        Optional<User> foundUser = this.userRepository.findById(id);
        foundUser.orElseThrow(() -> new NotFoundException("User with ID: " + id + " does not exist!"));
        Optional<Contact> foundContact = this.contactService.findByPhone(foundUser.get().getPhone());
        foundContact.orElseThrow(() ->
                new NotFoundException("Contact with phone: " + foundUser.get().getPhone() + " does not exist!"));

        this.contactService.updateActive(foundContact.get().getId(), true);
        foundUser.get().setIsActive(active);
        this.userRepository.save(foundUser.get());
    }
}
