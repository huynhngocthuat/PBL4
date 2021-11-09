package com.edu.bkdn.controllers;

import com.edu.bkdn.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.userdetails.User;

import java.security.Principal;
import java.util.Optional;

@Controller
public class homeController {
    @Autowired
    private UserService userService;

    @GetMapping("")
    public String index(ModelMap modelMap, Principal principal){
        User loginedUser = (User) ((Authentication) principal).getPrincipal();
        Optional<com.edu.bkdn.models.User> user = userService.findUserByPhone(loginedUser.getUsername());
        modelMap.addAttribute("name", user.get().getSurname() + user.get().getFirstName());
        return "index.html";
    }
    @GetMapping("/hello")
    public String hello(){
        return "hello.html";
    }
}
