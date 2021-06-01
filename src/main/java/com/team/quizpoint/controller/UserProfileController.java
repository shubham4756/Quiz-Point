package com.team.quizpoint.controller;

import com.team.quizpoint.model.AttendedQuiz;
import com.team.quizpoint.model.Dashboard;
import com.team.quizpoint.model.Quiz;
import com.team.quizpoint.model.User;
import com.team.quizpoint.repository.UserRepository;
import com.team.quizpoint.service.QuizService;
import com.team.quizpoint.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.*;

@Controller
@RequestMapping("/user")
public class UserProfileController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    QuizService quizService;

    @Autowired
    MongoTemplate template;

    @GetMapping("/profile")
    public String userProfile(Model model, Principal principal) {
        User user = userRepository.findUserByEmail(principal.getName()).get();
        model.addAttribute("isLogged",true);
        model.addAttribute(user);
        return "profile/profilepage";
    }

    @GetMapping("/editprofile")
    public String editProfile(Model model, Principal principal) {
        User user = userRepository.findUserByEmail(principal.getName()).get();
        model.addAttribute(user);
        return "profile/editprofile";
    }

    @PostMapping("/editprofile")
    public String editProfileNow(@ModelAttribute User newUser, Model model, RedirectAttributes redirectAttributes) {
        User oldUser = userRepository.findUserByEmail(newUser.getEmail()).get();
        boolean check = userService.updateProfile(newUser, oldUser);
        if (check) {
            System.out.println("profile has been updated !! ");
            redirectAttributes.addFlashAttribute("success", "Your Profile has been Updated !!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Error - Something Went Wrong !!");
        }
        return "redirect:profile";
    }

    @GetMapping("/changepassword")
    public String changePassword() {
        return "profile/changepassword";
    }

    @PostMapping("/changepassword")
    public String changePasswordNow(@RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword, Principal principal, Model model,RedirectAttributes redirectAttributes) {
        System.out.println("Old pass " + oldPassword);
        System.out.println("new pass " + newPassword);
        System.out.println(principal.getName());
        boolean check = userService.changePassword(oldPassword, newPassword, principal.getName());
        if (check)
            redirectAttributes.addFlashAttribute("success", "Your Password has been changed !!");
        else
            redirectAttributes.addFlashAttribute("error", "Error - Your Entered  old password is wrong !!");
        User user = userRepository.findUserByEmail(principal.getName()).get();
        model.addAttribute(user);
        return "redirect:profile";
    }

    @GetMapping("/createdquizzes")
    public String createdQuizzes(Model model, Principal principal) {
        List<String> quiz = userRepository.findUserByEmail(principal.getName()).get().getCreatedQuizzes();
        ArrayList<Dashboard> createdQuizes = quizService.createdQuizzes(quiz);
        model.addAttribute("quizes", createdQuizes);
        model.addAttribute("isLogged", true);
        return "createdquizzes";
    }

    @GetMapping("/attendedquizzes")
    public String attendedQuizzes(Model model, Principal principal) {
        List<AttendedQuiz> qui = userRepository.findUserByEmail(principal.getName()).get().getAttendedQuizzes();
        ArrayList<Dashboard> attendedQuizes = new ArrayList<>();
        for (AttendedQuiz qId : qui) {
            Quiz q = template.find(BasicQuery.query(Criteria.where("_id").is(qId.getQuizId())), Quiz.class).get(0);
            if (q != null) {
                Date date = Date.from(q.getQuizStartTime().atZone(ZoneId.systemDefault()).toInstant());
                String[] s = date.toString().split(" ");
                String dat = s[1] + " " + s[2] + " " + s[5];
                String time = s[0] + " " + s[3] + " " + s[4];
                int status = quizService.quizStatus(q.getQuizId().toString());
                                                                                                                                       // ended
                attendedQuizes.add(new Dashboard(qId.getQuizId(), q.getQuizName(), q.getDescription(), dat, time,qId.getTotalPoints(),status));
            }
        }
        Collections.reverse(attendedQuizes);
        String userId = userService.getUserIdByEmail(principal.getName());
        model.addAttribute("quizes", attendedQuizes);
        model.addAttribute("isLogged", true);
        model.addAttribute("userId", userId);
        return "attendedquizzes";
    }

}
