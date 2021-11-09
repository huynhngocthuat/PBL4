package com.edu.bkdn.config;

import com.edu.bkdn.dtos.User.CreateUserDto;
import com.edu.bkdn.services.UserService;
import com.edu.bkdn.utils.httpResponse.exceptions.DuplicateException;
import lombok.AllArgsConstructor;
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

    @Bean
    @PostConstruct
    public void createEntryUser(){
        CreateUserDto entryApplicationUser1 = new CreateUserDto(
                "nguyenvsleesin@gmail.com",
                "0934908500",
                "123456",
                "Huong Le",
                "Nguyen",
                false
        );
        CreateUserDto entryApplicationUser2 = new CreateUserDto(
                "huynhthuat852@gmail.com",
                "0869693721",
                "123456",
                "Huynh Ngoc",
                "Thuat",
                false
        );
        CreateUserDto entryApplicationUser3 = new CreateUserDto(
                "tanty@gmail.com",
                "0987654321",
                "123456",
                "Doan Tan",
                "Ty",
                false
        );

        try{
            log.info("Creating entry user");
            this.userService.createUser(entryApplicationUser1);
            this.userService.createUser(entryApplicationUser2);
            this.userService.createUser(entryApplicationUser3);
        }
        catch(Exception | DuplicateException dupError){
            log.error(dupError.getMessage());
        }
    }
}
