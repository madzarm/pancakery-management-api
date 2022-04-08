package com.example.pancakes.service;

import com.example.pancakes.domain.User;
import com.example.pancakes.domain.repository.UserRepository;
import com.example.pancakes.service.request.RegisterUserRequest;
import com.example.pancakes.service.result.ActionResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ActionResult registerUser(RegisterUserRequest request) {
        Optional<User> userOptional = userRepository.findByUserName(request.getUsername());
        if(userOptional.isPresent())
            return new ActionResult(false,"User with that username already exists!");

        User user = User.builder()
                .userName(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .active(true).build();

        userRepository.save(user);
        return new ActionResult(true, "User successfully registered!");
    }
}
