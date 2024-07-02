package com.SpringAndJWtImplement.controller;

import com.SpringAndJWtImplement.entity.User;
import com.SpringAndJWtImplement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/home")
public class HomeController {
    @Autowired
    private UserService userService;

//    @RequestMapping("/welcome")
//    public String welcomePage(){
//        String text="Welcome to login api";
//        text+="this page is not private";
//        return text;
//    }
    @GetMapping("/find")
    public List<User> findUser(){
        List<User> allUsers = userService.findAllUsers();
        return allUsers;
    }

}
