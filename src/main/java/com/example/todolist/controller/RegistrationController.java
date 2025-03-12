package com.example.todolist.controller;

import com.example.todolist.entity.User;
import com.example.todolist.service.RegistrationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class RegistrationController {
    private final RegistrationService registrationService;

    @Autowired
    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @GetMapping("/registration")
    public String openRegistrationPage(Model model) {
        model.addAttribute("userForm", new User());
        return "registration";
    }

    @PostMapping("/registration")
    public String signUp(@Valid @ModelAttribute("userForm") User userForm, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "registration";
        }
        if (!userForm.getPassword().equals(userForm.getPasswordConfirm())) {
            model.addAttribute("passwordMatch", "Passwords do not match");
            return "registration";
        }

        try {
            User user = User.builder()
                    .username(userForm.getUsername())
                    .password(userForm.getPassword())
                    .firstName(userForm.getFirstName())
                    .lastName(userForm.getLastName())
                    .build();
            registrationService.signUp(user);
            model.addAttribute("successMessage", "You successfully registered!");
        } catch (IllegalArgumentException exception) {
            model.addAttribute("userInUse", "User already exists!");
        }
        return "registration";
    }
}
