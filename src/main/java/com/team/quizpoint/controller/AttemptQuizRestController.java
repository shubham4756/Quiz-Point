package com.team.quizpoint.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team.quizpoint.service.QuizService;
import com.team.quizpoint.service.UserAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AttemptQuizRestController {

    @Autowired
    UserAnswerService userAnswerService;
    @Autowired
    QuizService quizService;

    @PostMapping("/quiz/{quizId}/answer")
    public String addUserAnswer(@PathVariable String quizId, @RequestBody String json) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(json);

        String userId = "", questionId = "" , ans = "";

        userId = jsonNode.get("userId").textValue();
        questionId = jsonNode.get("questionId").textValue();
        ans = jsonNode.get("ans").textValue();

        userAnswerService.updateAnswer(questionId, userId, ans);

        return "\"message\" : \"Ok\"";
    }

    @PostMapping("/quiz/{quizId}/count")
    public String countPoint(@PathVariable String quizId) {
        System.out.println("counting is coming");
        quizService.countPoints(quizId);
        return "\"status\": \"OK\"";
    }
}
