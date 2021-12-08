package com.edu.bkdn.config;

import com.edu.bkdn.dtos.Contact.CreateContactDto;
import com.edu.bkdn.dtos.Participant.CreateParticipantDto;
import com.edu.bkdn.dtos.User.CreateUserDto;
import com.edu.bkdn.dtos.UserContact.CreateUserContactDto;
import com.edu.bkdn.services.ContactService;
import com.edu.bkdn.services.UserContactService;
import com.edu.bkdn.services.UserService;
import com.edu.bkdn.utils.httpResponse.exceptions.DuplicateException;
import com.edu.bkdn.utils.httpResponse.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Slf4j
// Class for creating users at the beginning of program
public class EntryUser {

    @Autowired
    private UserService userService;
    @Autowired
    private ContactService contactService;
    @Autowired
    private UserContactService userContactService;

    @Bean
    @PostConstruct
    public void createEntryUser(){
        CreateUserDto entryApplicationUser1 = new CreateUserDto(
                "nguyenvsleesin@gmail.com",
                "0934908500",
                "123456",
                "Huong Le",
                "Nguyen",
                false,
                "https://images.unsplash.com/photo-1592994202124-75c12df162cf?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=667&q=80"
        );
        CreateUserDto entryApplicationUser2 = new CreateUserDto(
                "huynhthuat852@gmail.com",
                "0869693721",
                "123456",
                "Huynh Ngoc",
                "Thuat",
                false,
                "https://images.unsplash.com/photo-1519058082700-08a0b56da9b4?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=687&q=80"
        );
        CreateUserDto entryApplicationUser3 = new CreateUserDto(
                "doantanty92@gmail.com",
                "0969643417",
                "123456",
                "Doan Tan",
                "Ty",
                false,
                "https://images.unsplash.com/photo-1499051284390-7dbd87e5fbdb?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1174&q=80"
        );
        CreateUserDto entryApplicationUser4 = new CreateUserDto(
                "hoangdat@gmail.com",
                "0987654322",
                "123456",
                "Dinh Hoang",
                "Dat",
                false,
                "https://images.unsplash.com/photo-1514794749374-fb67509dbb7f?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=764&q=80"
        );

        try{
            log.info("Creating entry user");
            this.userService.createUser(entryApplicationUser1);
            this.userService.createUser(entryApplicationUser2);
            this.userService.createUser(entryApplicationUser3);
            this.userService.createUser(entryApplicationUser4);
        }
        catch(Exception | DuplicateException dupError){
            log.error(dupError.getMessage());
        }

        CreateContactDto entryContact1 = new CreateContactDto(
                "nguyenvsleesin@gmail.com",
                "0934908500",
                "Huong Le",
                "Nguyen",
                false,
                "https://images.unsplash.com/photo-1592994202124-75c12df162cf?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=667&q=80"
        );
        CreateContactDto entryContact2 = new CreateContactDto(
                "huynhthuat852@gmail.com",
                "0869693721",
                "Huynh Ngoc",
                "Thuat",
                false,
                "https://images.unsplash.com/photo-1519058082700-08a0b56da9b4?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=687&q=80"

        );
        CreateContactDto entryContact3 = new CreateContactDto(
                "doantanty92@gmail.com",
                "0969643417",
                "Doan Tan",
                "Ty",
                false,
                "https://images.unsplash.com/photo-1499051284390-7dbd87e5fbdb?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1174&q=80"
        );
        CreateContactDto entryContact4 = new CreateContactDto(
                "hoangdat@gmail.com",
                "0987654322",
                "Dinh Hoang",
                "Dat",
                false,
                "https://images.unsplash.com/photo-1514794749374-fb67509dbb7f?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=764&q=80"
        );

        try{
            log.info("Creating entry contacts");
            this.contactService.createContact(entryContact1);
            this.contactService.createContact(entryContact2);
            this.contactService.createContact(entryContact3);
            this.contactService.createContact(entryContact4);
        }
        catch(Exception | DuplicateException dupError){
            log.error(dupError.getMessage());
        }

        CreateUserContactDto userContactDto1 = new CreateUserContactDto(
            "0934908500", "0934908500", true
        );
        CreateUserContactDto userContactDto2 = new CreateUserContactDto(
                "0869693721", "0869693721", true
        );
        CreateUserContactDto userContactDto3 = new CreateUserContactDto(
                "0969643417", "0969643417", true
        );
        CreateUserContactDto userContactDto4 = new CreateUserContactDto(
                "0987654322", "0987654322", true
        );

        try{
            log.info("Creating entry user_contacts");
            this.userContactService.createUserContact(userContactDto1);
            this.userContactService.createUserContact(userContactDto2);
            this.userContactService.createUserContact(userContactDto3);
            this.userContactService.createUserContact(userContactDto4);
        }
        catch(Exception |DuplicateException | NotFoundException e){
            log.error(e.getMessage());
        }
    }

}
