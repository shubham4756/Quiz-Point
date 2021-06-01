package com.team.quizpoint.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team.quizpoint.model.Question;
import com.team.quizpoint.model.Quiz;
import com.team.quizpoint.service.ChatService;
import com.team.quizpoint.service.QuizService;
import com.team.quizpoint.service.UserAnswerService;
import com.team.quizpoint.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

@RestController
public class CreateQuizRestController {

    @Autowired
    UserService userService;

    @Autowired
    QuizService quizService;

    @Autowired
    UserAnswerService userAnswerService;

    @Autowired
    ChatService chatService;

    @PostMapping("/createquizform")
    public String createQuizForm(@RequestBody String json, Principal principal) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(json);

        String username = principal.getName();

        System.out.println("create quiz rest");
        System.out.println(LocalDateTime.parse(jsonNode.get("time").textValue()));
        System.out.println(LocalDateTime.parse(jsonNode.get("time").textValue()).toInstant(ZoneOffset.UTC));


        Quiz quiz = new Quiz(ObjectId.get(),
                jsonNode.get("name").textValue(),
                LocalDateTime.now(),
                LocalDateTime.parse(jsonNode.get("time").textValue()),
                false,
                jsonNode.get("description").textValue(),
                jsonNode.get("duration").asInt()
                , new ArrayList<>());



        boolean result = userService.addQuizInUser(username, quiz.getQuizId().toString());
        System.out.println(result);
        System.out.println(quiz);

        quizService.insertQuiz(quiz);

        chatService.addQuiz(quiz.getQuizId().toString());

        String quizLink = "http://localhost:8082/quiz/" + quiz.getQuizId() + "/edit";
        String response = "{\"link\" : \" "+ quizLink + "\"}";
        System.out.println(quizService.findQuizById(quiz.getQuizId()));
        return response;
    }

    @PostMapping("/quiz/{quizId}/edit")
    public ResponseEntity<String> editQuiz(@PathVariable String quizId, Principal principal, @RequestBody String json) throws JsonProcessingException {

        System.out.println(json);

        // save in the database

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(json);

        String questionId = ObjectId.get().toString();

        System.out.println(json);

        Question question = new Question(
                questionId,
                String.valueOf(jsonNode.get("QuestionType").asInt()),
                jsonNode.get("Points").asInt(),
                jsonNode.get("QuestionBody").textValue(),
                jsonNode.get("AnswerBody").textValue(),
                objectMapper.convertValue(jsonNode.get("OptionsArray"), ArrayList.class)
        );

        quizService.addQuestionInQuiz(question, quizId);


        System.out.println(question);

        userAnswerService.addQuestion(questionId);

        System.out.println(quizId);
        System.out.println(principal.getName());

        String response = "{\"questionId\" : \"" + questionId + "\"}";

        return new ResponseEntity<String>(response, HttpStatus.OK);
    }
}
