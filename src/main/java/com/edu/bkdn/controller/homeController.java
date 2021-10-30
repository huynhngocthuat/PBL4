package com.edu.bkdn.controller;

import com.edu.bkdn.domain.user;
import com.edu.bkdn.service.userService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class homeController {
    @Autowired
    private userService userService;

    @GetMapping("")
    public String index(ModelMap modelMap){
        List<user> list = userService.findAll();
        modelMap.addAttribute("users", list);
        return "index.html";
    }
}
