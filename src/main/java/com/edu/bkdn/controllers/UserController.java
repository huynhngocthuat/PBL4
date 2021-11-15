package com.edu.bkdn.controllers;

import com.edu.bkdn.dtos.User.CreateUserDto;
import com.edu.bkdn.dtos.User.GetUserDto;
import com.edu.bkdn.dtos.User.UpdateUserDto;
import com.edu.bkdn.services.UserService;
import com.edu.bkdn.utils.httpResponse.NoContentResponse;
import com.google.gson.Gson;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("users")
public class UserController {

    @Autowired
    private UserService userService;


    @SneakyThrows
    @PostMapping("")
    @ResponseBody
    public String createNewUser(@Valid @RequestBody CreateUserDto createUserDto) {
        this.userService.createUser(createUserDto);
        return new Gson().toJson(new NoContentResponse());
    }

    @SneakyThrows
    @PutMapping("/{id}")
    @ResponseBody
    public String updateUser(@Valid @RequestBody UpdateUserDto updateUserDto,
                            @PathVariable("id") Long id) {
        this.userService.updateUser(id, updateUserDto);
        return new Gson().toJson(new NoContentResponse());
    }

    @GetMapping("")
    @ResponseBody
    public List<GetUserDto> getUser() {
        return userService.getAllUser();
    }
}
