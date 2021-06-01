package com.team.quizpoint.controller;

import com.team.quizpoint.model.AnalyticsQuiz;
import com.team.quizpoint.model.AttendedQuiz;
import com.team.quizpoint.model.Quiz;
import com.team.quizpoint.service.QuizService;
import com.team.quizpoint.service.UserAnswerService;
import com.team.quizpoint.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AttemptQuizController {

    @Autowired
    QuizService quizService;

    @Autowired
    UserService userService;

    @Autowired
    UserAnswerService userAnswerService;

    @PostMapping("quiz/{quizId}/register")
    public String registerQuiz(Model model, @PathVariable String quizId, Principal principal) {
        if(principal == null) {
            return "redirect:dashboard";
        }

        String userId = userService.getUserIdByEmail(principal.getName());
        AttendedQuiz attendedQuiz = new AttendedQuiz(quizId, "0");

        userService.addAttendedQuizInUser(userId, attendedQuiz);
//        String link = "/attempt";

        return "redirect:attempt";
    }

    @GetMapping("quiz/{quizId}/attempt")
    public String attemptQuiz(Model model, Principal principal, @PathVariable String quizId) {

        int status = quizService.quizStatus(quizId);

        System.out.println(status);

        if(status == 0) {
            long remainingTime = quizService.findSecondsToQuiz(quizId);

            model.addAttribute("remainingTime", remainingTime);

            return "quiz_wait";
        } else if (status == 2) {
            // create the page
            return "redirect:/user/attendedquizzes";
        }


        List<String> questionIds = quizService.findQuestionIdsByQuizId(quizId);
        model.addAttribute("questionIds", questionIds);
        model.addAttribute("quizId", quizId);

        Quiz qz = quizService.getQuizById(quizId);
        model.addAttribute("quizName",qz.getQuizName());


        // task is user has given the answer or not



        String userId = userService.getUserIdByEmail(principal.getName());

        System.out.println("USER id is" + userId);

        List<Boolean> userHasAnsweredOrNot = new ArrayList<>();

        for(String questionId : questionIds) {
            userHasAnsweredOrNot.add(!userAnswerService.getAnswer(questionId, userId).equals("-1"));
        };

        long remainingTime = quizService.findRemainingDuration(quizId);

        model.addAttribute("remainingTime", remainingTime);
        model.addAttribute("isMarked", userHasAnsweredOrNot);
        model.addAttribute("userId", userId);


        return "quiz_answer";
    }

    @GetMapping("quiz/{quizId}/review/{userId}")
    public String rewievQuiz(Model model, Principal principal, @PathVariable String quizId, @PathVariable String userId) {

        List<String> questionIds = quizService.findQuestionIdsByQuizId(quizId);
        model.addAttribute("questionIds", questionIds);
        model.addAttribute("quizId", quizId);

        // task is user has given the answer or not

        String puserId = userService.getUserIdByEmail(principal.getName());

        // allow organizor to view all end for other only thier's
//        if(!userId.equals(puserId)){
//            return "redirect:/";
//        }

        System.out.println("USER id is" + userId);

        List<Boolean> userHasAnsweredOrNot = new ArrayList<>();

        for(String questionId : questionIds) {
            userHasAnsweredOrNot.add(!userAnswerService.getAnswer(questionId, userId).equals("-1"));
        };

        model.addAttribute("isMarked", userHasAnsweredOrNot);
        model.addAttribute("userId", userId);


        return "quiz_review";
    }

    @GetMapping("/quiz/{quizId}/analytics")
    public String quizAnalytics(@PathVariable String quizId,Model model) {
        ArrayList<String> users = quizService.findAllUserRegistered(quizId);

        ArrayList<AnalyticsQuiz> analyticsQuizzes = new ArrayList<>();

        for(String userId : users) {
            String email = userService.getEmailByUserId(userId);
            String point = userService.getUserPoints(userId,quizId);
            analyticsQuizzes.add(new AnalyticsQuiz(userId,email,point));
        }
        model.addAttribute("analyticsQuizzes",analyticsQuizzes);
        model.addAttribute("quizId",quizId);
        return "analytics_quiz";
    }
}
