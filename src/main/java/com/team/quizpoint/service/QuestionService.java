package com.team.quizpoint.service;

import com.team.quizpoint.model.Question;
import com.team.quizpoint.model.Quiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
public class QuestionService {
    @Autowired
    MongoTemplate template;

    public Question findQuestionById(String id) {

        Question question;
        Query query = new Query();
        query.addCriteria(Criteria.where("questions.questionId").is(id));
        query.fields().include("questions.$");

        question = template.findOne(query, Quiz.class, "quizzes").getQuestions().get(0);

        return question;
    }

    public void updateQuestion(Question question) {
        Query query = new Query();


        Update update = new Update();
        update.set("questions.$.type", question.getType());
        update.set("questions.$.points", question.getPoints());
        update.set("questions.$.questionContent", question.getQuestionContent());
        update.set("questions.$.correctAnswer", question.getCorrectAnswer());

        update.set("questions.$.options", question.getOptions());

        System.out.println(question);

        System.out.println(template.findOne(query, Quiz.class));

        template.updateFirst(BasicQuery.query(Criteria.where("questions").elemMatch(Criteria.where("questionId").is(question.getQuestionId()))),
                            update,
                        Quiz.class);

    }
}
