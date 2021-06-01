package com.team.quizpoint.controller;

import com.team.quizpoint.model.User;
import com.team.quizpoint.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Collections;

@Controller
public class RegisterAndVerificationController {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserService userService;

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user,Model model) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Collections.singletonList("ROLE_USER"));
        user.setCreatedQuizzes(new ArrayList<>());
        user.setAttendedQuizzes(new ArrayList<>());
        user.setUserId(ObjectId.get().toString());
        System.out.println(user.getUserId());
        user = userService.addUser(user);
        System.out.println(user.toString());
        model.addAttribute("success", "Your Profile has been Registered !!");
        System.out.println(" in afadfasdfad ");
        return "login";
    }

    @GetMapping("/register/verify")
    public String verifyCustomer(@RequestParam(required = false) String token,Model model) {
        if (StringUtils.isEmpty(token)) {
            model.addAttribute("error", "Error - Verification Token Is Missing !!");
            return "login";
        }
        try {
            userService.emailVerificaton(token);
            model.addAttribute("success", "Your Email Verification Is Done !!");
        } catch (Exception e) {
            model.addAttribute("error", "Error - Verification Toke Is Invalid !!");
        }
        System.out.println("in email verification ");
        return "login";
    }
}