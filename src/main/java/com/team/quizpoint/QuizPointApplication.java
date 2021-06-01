package com.team.quizpoint;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;

@SpringBootApplication
public class QuizPointApplication {

    @Autowired
    private Environment environment;


    public static void main(String[] args) {
        SpringApplication.run(QuizPointApplication.class, args);
    }

    public @Bean MongoClient mongoClient() {
        return MongoClients.create(environment.getProperty("spring.data.mongodb.uri"));
    }

    public @Bean MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoClient(), environment.getProperty("spring.data.mongodb.database"));
    }
}
