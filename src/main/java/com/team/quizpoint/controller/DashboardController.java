package com.team.quizpoint.controller;

import com.team.quizpoint.model.AttendedQuiz;
import com.team.quizpoint.model.Dashboard;
import com.team.quizpoint.model.Quiz;
import com.team.quizpoint.model.User;
import com.team.quizpoint.repository.QuizRepository;
import com.team.quizpoint.service.QuizService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class DashboardController {

    @Autowired
    MongoTemplate template;

    @Autowired
    QuizRepository quizRepository;

    @Autowired
    QuizService quizService;

    @GetMapping("/dashboard")
    public String dashboardPage(Model model, Principal principal) {
        ArrayList<Dashboard> quizes = new ArrayList<>();
        Query query = new Query();
        query.with(Sort.by(Sort.Direction.ASC, "quizStartTime"));
        List<Quiz> qz = template.find(query, Quiz.class);

        System.out.println("dashboard");
        for (Quiz p : qz) {
            Date date = Date.from(p.getQuizStartTime().atZone(ZoneId.systemDefault()).toInstant());
            System.out.println(date);
            String[] s = date.toString().split(" ");
            String dat = s[1] + " " + s[2] + " " + s[5];
            String time = s[0] + " " + s[3] + " " + s[4];
            int status = quizService.quizStatus(p.getQuizId().toString());
            if(status!=2)
                quizes.add(new Dashboard(p.getQuizId().toString(),p.getQuizName(),p.getDescription(),dat,time));
        }
        model.addAttribute("quizes", quizes);
        model.addAttribute("isLogged", principal!=null);

        return "dashboard";
    }

    @GetMapping("/quiz/{id}/register")
    public String openQuiz(@PathVariable("id") ObjectId quizid,Model model,Principal principal) {
        if(principal==null) {
            return "redirect:login";
        }
        User user = template.findOne(BasicQuery.query(Criteria.where("email").is(principal.getName())), User.class,"users");


        for(AttendedQuiz quiz : user.getAttendedQuizzes()) {
            if (quiz.getQuizId().equals(quizid.toString())) {
                return "redirect:attempt";
            }
        }


        Quiz quiz = quizRepository.findQuizByQuizId(quizid).get();
        Date date = Date.from(quiz.getQuizStartTime().atZone(ZoneId.systemDefault()).toInstant());
        String[] s = date.toString().split(" ");
        String dat = s[1] + " " + s[2] + " " + s[5];
        String time = s[0] + " " + s[3] + " " + s[4];
        model.addAttribute("quiz",new Dashboard(quiz.getQuizId().toString(),quiz.getQuizName(),quiz.getDescription(),dat,time));
        model.addAttribute("isLogged",true);
        return "quiz_register";
    }

}
