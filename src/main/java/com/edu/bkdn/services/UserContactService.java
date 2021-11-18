package com.edu.bkdn.services;

import com.edu.bkdn.dtos.UserContact.CreateUserContactDto;
import com.edu.bkdn.models.UserContact;
import com.edu.bkdn.repositories.UserContactRepository;
import com.edu.bkdn.utils.HelperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserContactService {

    @Autowired
    private UserContactRepository userContactRepository;

    public void createUserContact(CreateUserContactDto createUserContactDto){
        Optional<UserContact> foundUserContact =
                HelperUtil.correctSingleTimestamp(
                this.userContactRepository
                        .findUserContactByUserEqualsAndContactEquals(
                                createUserContactDto.getContactId(),
                                createUserContactDto.getUserId()
                        ));
        if(foundUserContact.isPresent()){

        }
    }
}
