package com.team.quizpoint.service;

import com.team.quizpoint.model.Answer;
import com.team.quizpoint.model.UserAnswers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserAnswerService {

    @Autowired
    MongoTemplate mongoTemplate;

    public void addQuestion(String questionId) {
        UserAnswers userAnswers = new UserAnswers(questionId, new ArrayList<>());
        mongoTemplate.save(userAnswers,"useranswers");
    }

    public void addAnswer(String questionId,
                          String userId,
                          String ans) {
        Answer answer = new Answer(userId, ans);

        Query query = new Query();
        query.addCriteria(Criteria.where("questionId").is(questionId));

        Update update = new Update();
        update.addToSet("answers", answer);

        mongoTemplate.updateFirst(query, update, "useranswers");
    }

    public void updateAnswer(String questionId,
                             String userId,
                             String ans) {
        Query query = new Query();
        query.addCriteria(Criteria.where("questionId").is(questionId));
        query.addCriteria(Criteria.where("answers.userId").is(userId));

        if(!checkUserAnsweredOrNot(questionId, userId)) {
            addAnswer(questionId, userId, ans);
            return;
        }

        Update update = new Update();
        update.set("answers.$.ans", ans);

        mongoTemplate.updateFirst(query, update, "useranswers");
    }

    public boolean checkUserAnsweredOrNot(String questionId,
                                          String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("questionId").is(questionId));
        query.addCriteria(Criteria.where("answers.userId").is(userId));

        return mongoTemplate.findOne(query, UserAnswers.class, "useranswers") != null;
    }

    public String getAnswer(String questionId,
                          String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("questionId").is(questionId));


        UserAnswers userAnswers = mongoTemplate.findOne(query, UserAnswers.class, "useranswers");

        if(userAnswers == null) {
            return "-1";
        }

        List<Answer> answerList = userAnswers.getAnswers();

        if(answerList.size() == 0) {
            return "-1";
        } else {
//            System.out.println(answerList);

            for(Answer ans : answerList) {
                if (ans.getUserId().equals(userId)) {
                    return ans.getAns();
                }
            }

            return "0";
        }

    }

}
