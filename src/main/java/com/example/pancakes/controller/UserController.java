package com.example.pancakes.controller;

import com.example.pancakes.service.UserService;
import com.example.pancakes.service.request.RegisterUserRequest;
import com.example.pancakes.service.result.ActionResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<ActionResult> registerUser(@Valid RegisterUserRequest request){
        return userService.registerUser(request).intoResponseEntity();

    }
}
