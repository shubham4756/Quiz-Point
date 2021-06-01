package com.team.quizpoint.controller;

import com.team.quizpoint.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;

@Controller
public class CreateQuizController {

    @Autowired
    QuizService quizService;

    @GetMapping("/createquizform")
    public String createQuizForm() {
        return "create_quiz_form";
    }

    @GetMapping("/quiz/{quizId}/edit")
    public ModelAndView editQuiz(ModelAndView model, @PathVariable String quizId, Principal principal) {

        // TODO: do authentication

        ArrayList<String> list = quizService.findQuestionIdsByQuizId(quizId);
        ArrayList<Boolean> list2 = quizService.findQuestionIsAnsweredOrNotByQuizId(quizId);

        model.setViewName("create_quiz");
        model.addObject("quizId", quizId);
        model.addObject("questionIds", list);
        model.addObject("correctAnswers", list2);

        // get answered or not

        System.out.println(list2);

        System.out.println(quizId);
        System.out.println(principal.getName());

        return model;
    }
}
