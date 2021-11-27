package com.edu.bkdn.controllers;

import com.edu.bkdn.dtos.Contact.CreateContactDto;
import com.edu.bkdn.dtos.User.CreateUserDto;
import com.edu.bkdn.dtos.User.GetUserDto;
import com.edu.bkdn.dtos.User.UpdateUserDto;
import com.edu.bkdn.dtos.UserContact.CreateUserContactDto;
import com.edu.bkdn.services.ContactService;
import com.edu.bkdn.services.UserContactService;
import com.edu.bkdn.services.UserService;
import com.edu.bkdn.utils.httpResponse.NoContentResponse;
import com.google.gson.Gson;
import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private ContactService contactService;
    @Autowired
    private UserContactService userContactService;

    @GetMapping("/signup")
    public String add(Model model) {
        model.addAttribute("user", new CreateUserDto());
        return "signup";
    }

    @SneakyThrows
    @PostMapping("/signup")
    public ModelAndView saveOrUpdate(ModelMap model, @Valid @ModelAttribute("user") CreateUserDto createUserDto, BindingResult result) {
        //Check validation
        if (result.hasErrors()) {
            return new ModelAndView("signup");
        }
        //Add new user
        userService.createUser(createUserDto);
        //Add new contact
        CreateContactDto createContactDto = new CreateContactDto();
        BeanUtils.copyProperties(createUserDto, createContactDto);
        contactService.createContact(createContactDto);
        //Add new user-contact
        GetUserDto getUserDto = userService.getUserDtoByPhoneNumber(createUserDto.getPhone());
        userContactService.createUserContactRegister(getUserDto.getId());

//        model.addAttribute("message", "User successfully created!");
        return new ModelAndView("login");
    }

    @SneakyThrows
    @PutMapping("/users/{id}")
    @ResponseBody
    public String updateUser(@Valid @RequestBody UpdateUserDto updateUserDto,
                            @PathVariable("id") Long id) {
        this.userService.updateUser(id, updateUserDto);
        return new Gson().toJson(new NoContentResponse());
    }

    @GetMapping("/users")
    @ResponseBody
    public List<GetUserDto> getUser() {
        return userService.getAllUser();
    }
}
