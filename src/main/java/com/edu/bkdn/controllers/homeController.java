package com.edu.bkdn.controllers;

import com.edu.bkdn.models.ApplicationUser;
import com.edu.bkdn.models.User;
import com.edu.bkdn.services.ContactService;
import com.edu.bkdn.services.UserService;
import com.edu.bkdn.utils.httpResponse.NoContentResponse;
import com.google.gson.Gson;
import lombok.SneakyThrows;
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
    @Autowired
    private ContactService contactService;

    @SneakyThrows
    @GetMapping({"/home", ""})
    public String hello(ModelMap modelMap, Authentication authentication){
        Long id =  ((ApplicationUser)authentication.getPrincipal()).getUser().getId();
//        Gson gson = new Gson() ;
//        String strmap = gson.toJson(id);

        userService.updateActive(id, true);
        modelMap.addAttribute("idUserLogin", id);
        return "home";
    }
}
