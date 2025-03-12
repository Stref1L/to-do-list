package com.example.todolist.service;

import com.example.todolist.entity.User;
import com.example.todolist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthentificationService {
    private final UserRepository userRepository;

    @Autowired
    public AuthentificationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> signIn(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, password);
    }
}
