package com.team.quizpoint.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team.quizpoint.configuration.WebSocketEventListener;
import com.team.quizpoint.service.ChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import static java.lang.String.format;

@Controller
public class ChatController {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @Autowired
    ChatService chatService;

    @MessageMapping("/chat/{roomId}/sendMessage")
    public void sendMessage(@DestinationVariable String roomId, @Payload String json) throws JsonProcessingException {

        // store message in the database
        System.out.println(json);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(json);

        String message = jsonNode.get("content").textValue();

        System.out.println(roomId);
        chatService.addMessage(roomId, message);

        messagingTemplate.convertAndSend(format("/channel/%s", roomId), json);
    }

    @GetMapping("/quiz/{quizId}/message")
    public String message(@PathVariable String quizId, Model model) {

        model.addAttribute("quizId", quizId);
        return "message_quiz";
    }

    @GetMapping("/quiz/{quizId}/read")
    public String readMessage(@PathVariable String quizId, Model model) {
        model.addAttribute("quizId", quizId);
        return "message_receiver";
    }
}
