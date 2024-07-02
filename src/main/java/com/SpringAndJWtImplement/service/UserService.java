package com.SpringAndJWtImplement.service;

import com.SpringAndJWtImplement.entity.User;
import com.SpringAndJWtImplement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    public User createUser(User user){
        user.setUserId(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User save = userRepository.save(user);
        return save;
    }

    public List<User> findAllUsers(){
        List<User> all = userRepository.findAll();
        return all;
    }
}
