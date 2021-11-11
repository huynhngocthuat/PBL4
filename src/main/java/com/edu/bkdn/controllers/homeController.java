package com.edu.bkdn.controllers;

import com.edu.bkdn.models.ApplicationUser;
import com.edu.bkdn.models.User;
import com.edu.bkdn.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


@Controller
public class homeController {
    @Autowired
    private UserService userService;

    @GetMapping("")
    public String index(ModelMap modelMap){
        List<User> list = userService.findAll();
        modelMap.addAttribute("users", list);
        return "index";
    }

    @GetMapping("/home")
    public String hello(ModelMap modelMap, Authentication authentication){
        String phoneNumber = "";
        phoneNumber = ((ApplicationUser)authentication.getPrincipal()).getUser().getPhone();
        modelMap.addAttribute("homeNumber", phoneNumber);
        return "home";
    }
}
