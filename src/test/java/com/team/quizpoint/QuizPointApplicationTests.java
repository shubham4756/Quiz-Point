package com.team.quizpoint;

import com.team.quizpoint.service.QuizService;
import com.team.quizpoint.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class QuizPointApplicationTests {

    @Autowired
    UserService userService;

    @Test
    void contextLoads() {
        userService.getUserPoints( "608b88d643d76e7a2e7ec5d6" , "608b9474b8f8e85e27221816");
    }

}
