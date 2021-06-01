package com.team.quizpoint.controller;

import com.team.quizpoint.model.ChatMessage;
import com.team.quizpoint.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ChatRestController {

    @Autowired
    ChatService chatService;


    @PostMapping("/quiz/{quizId}/allMessages")
    public ResponseEntity<List<ChatMessage>> getAllMessages(@PathVariable String quizId) {

        List<ChatMessage> chatMessages = chatService.getAllMessages(quizId);

        return new ResponseEntity<>(chatMessages, HttpStatus.OK);
    }

}
