package com.team.quizpoint.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team.quizpoint.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

@RestController
public class HomeRestController {

    @Autowired
    UserService userService;

    @PostMapping("/checkEmail")
    public String checkEmailExistOrNot(@RequestBody String emailJson) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(emailJson);

        System.out.println(emailJson);
        boolean exist = userService.existsUserByEmail(jsonNode.get("email").toString().substring(1, jsonNode.get("email").toString().length() - 1));
        System.out.println(exist);
        String str = "{\"message\" : \"" + exist +"\"}";
        return str;
    }
}

