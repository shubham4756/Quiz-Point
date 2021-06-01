package com.team.quizpoint.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team.quizpoint.model.Question;
import com.team.quizpoint.service.QuestionService;
import com.team.quizpoint.service.UserAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class QuestionRestController {

    @Autowired
    QuestionService questionService;

    @Autowired
    UserAnswerService userAnswerService;

    @PostMapping("question/{questionId}")
    public ResponseEntity<Question> getQuestionById(@PathVariable String questionId, @RequestBody String json) throws JsonProcessingException {

        System.out.println(questionId);
        Question question = questionService.findQuestionById(questionId);
        System.out.println(question);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode node = objectMapper.readTree(json);

        int type = node.get("type").asInt();
        if (type == 1) {

            // validate that user has access to see the created quiz or not

        } else if (type == 2) {
            String userId = node.get("userId").textValue();
            question.setCorrectAnswer(userAnswerService.getAnswer(questionId, userId));
        } else if (type == 3) {
            String userId = node.get("userId").textValue();
            question.setCorrectAnswer(question.getCorrectAnswer() + "|" + userAnswerService.getAnswer(questionId, userId));
        }

        return new ResponseEntity<Question>(question, HttpStatus.OK);
    }

    @PostMapping("question/update")
    public void updateQuestion(@RequestBody String json) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(json);

        Question question = new Question(
                jsonNode.get("questionId").textValue(),
                String.valueOf(jsonNode.get("QuestionType").asInt()),
                jsonNode.get("Points").asInt(),
                jsonNode.get("QuestionBody").textValue(),
                jsonNode.get("AnswerBody").textValue(),
                objectMapper.convertValue(jsonNode.get("OptionsArray"), ArrayList.class)
        );

        questionService.updateQuestion(question);

    }
}
