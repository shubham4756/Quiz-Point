package com.team.quizpoint.service;

import com.team.quizpoint.model.Dashboard;
import com.team.quizpoint.model.Question;
import com.team.quizpoint.model.Quiz;
import com.team.quizpoint.model.User;
import com.team.quizpoint.repository.QuizRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.*;

@Service
public class QuizService {

    @Autowired
    MongoTemplate template;

    @Autowired
    QuizRepository quizRepository;

    @Autowired
    UserAnswerService userAnswerService;

    @Autowired
    UserService userService;

    public void insertQuiz(Quiz quiz) {

        System.out.println(template.insert(quiz,"quizzes"));
    }

    public Quiz findQuizById(ObjectId id) {
        return quizRepository.findQuizByQuizId(id).get();
    }

    public Quiz getQuizById(String quizId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(quizId));

        return template.findOne(query, Quiz.class, "quizzes");
    }

    public void addQuestionInQuiz(Question question, String quizId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(quizId));

        Update update = new Update();
        update.push("questions", question);

        System.out.println(template.updateFirst(query, update, "quizzes").getModifiedCount());
    }

    public ArrayList<String> findQuestionIdsByQuizId(String quizId) {
        ArrayList<String> list = new ArrayList<>();

        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(quizId));

        Quiz quiz = template.findOne(query, Quiz.class, "quizzes");

        assert quiz != null;
        for(Question question : quiz.getQuestions()) {
            list.add(question.getQuestionId());
        }

        return list;
    }

    public ArrayList<Boolean> findQuestionIsAnsweredOrNotByQuizId(String quizId) {
        ArrayList<Boolean> list = new ArrayList<>();

        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(quizId));

        Quiz quiz = template.findOne(query, Quiz.class, "quizzes");

        assert quiz != null;
        for(Question question : quiz.getQuestions()) {
//            list.add(question.getQuestionId());
            list.add(!question.getCorrectAnswer().equals("-1"));
        }

        return list;
    }


    public void findStartTimeOfQuiz(String quizId) {

        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(quizId));

        LocalDateTime dateTime = template.findOne(query, Quiz.class).getQuizStartTime();
        Date date = Date.from(dateTime.toInstant(ZoneOffset.UTC));

        Date date1 = Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC));

        System.out.println(dateTime);
        System.out.println(date);
        System.out.println(date1);

        long time_difference = date.getTime() - date1.getTime();

        System.out.println((time_difference / (1000*60)));
        System.out.println((time_difference / 1000)% 60);
    }

    public ArrayList<Dashboard> createdQuizzes(List<String> quiz) {
        ArrayList<Dashboard> createdQuizes = new ArrayList<>();
        for (String qId : quiz) {
            Quiz q = template.find(BasicQuery.query(Criteria.where("_id").is(qId)), Quiz.class).get(0);
            if (q != null) {
                Instant instant = q.getQuizCreatedTime().toInstant(ZoneOffset.UTC);
                Date date = Date.from(instant);
                String[] s = date.toString().split(" ");
                String dat = s[1] + " " + s[2] + " " + s[5];
                String time = s[0] + " " + s[3] + " " + s[4];
                int status = quizStatus(q.getQuizId().toString());
                createdQuizes.add(new Dashboard(qId, q.getQuizName(), q.getDescription(), dat, time,null,status));
            }
        }
        Collections.reverse(createdQuizes);
        return createdQuizes;
    }

    // return 0 is quiz is going to start
    //return 1 if quiz is started
    // return 2 if quiz is completed
    public int quizStatus(String quizId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(quizId));

        LocalDateTime dateTime = template.findOne(query, Quiz.class).getQuizStartTime();
        Date date = Date.from(dateTime.toInstant(ZoneOffset.UTC));

        Date date1 = Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC));
        long time_difference = date1.getTime() - date.getTime();

        int duration = findDurationByQuizId(quizId);

        System.out.println(duration);
        System.out.println(time_difference);
        System.out.println(time_difference - (long) duration*60*1000);

        if(time_difference < 0) {
             return 0;
        } else if(time_difference - (long) duration *60*1000 < 0) {
             return 1;
        } else {
             return 2;
        }
    }

    public int findDurationByQuizId(String quizId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(quizId));

        return template.findOne(query, Quiz.class).getDuration();
    }

    public long findSecondsToQuiz(String quizId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(quizId));

        LocalDateTime dateTime = template.findOne(query, Quiz.class).getQuizStartTime();
        Date date = Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());

        System.out.println(LocalDateTime.now());
        Date date1 = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        long time_difference = date.getTime() - date1.getTime();

        System.out.println(date);

        System.out.println(date1);
        System.out.println(time_difference/1000);

        return time_difference/1000;
    }

    public long findRemainingDuration(String quizId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(quizId));

        LocalDateTime dateTime = template.findOne(query, Quiz.class).getQuizStartTime();
        Date date = Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());

        Date date1 = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        long time_difference = date1.getTime() - date.getTime();

        long duration = findDurationByQuizId(quizId);

        return (duration*60 - time_difference/1000);
    }

    public ArrayList<String> findAllUserRegistered(String quizId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("attendedQuizzes.quizId").is(quizId));
        query.fields().include("_id");

        ArrayList<String> userList = new ArrayList<>();

        for(User u : template.find(query, User.class, "users")) {
            userList.add(u.getUserId());
        }
        System.out.println(userList);
        return userList;
    }

    public void countPoints(String quizId) {
        ArrayList<String> userIds = findAllUserRegistered(quizId);

        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(quizId));
        query.fields().include("questions._id", "questions.correctAnswer", "questions.points");

        Quiz quiz = template.find(query, Quiz.class, "quizzes").get(0);

        ArrayList<String> questionIds = new ArrayList<>();
        ArrayList<String> correctAns = new ArrayList<>();
        ArrayList<Integer> points = new ArrayList<>();
        for(Question id : quiz.getQuestions()) {
            questionIds.add(id.getQuestionId());
            correctAns.add(id.getCorrectAnswer());
            points.add(id.getPoints());
        }

//        System.out.println(questionIds);
//        System.out.println(correctAns);
//        System.out.println(points);

        for(String userId : userIds) {
            System.out.println(userId);
            int total = 0;
            // for particular user find its total

            for(int i = 0;i<questionIds.size();i++) {
//                System.out.println(userAnswerService.getAnswer(questionIds.get(i), userId) + " ans "  + correctAns.get(i));
                total += userAnswerService.getAnswer(questionIds.get(i), userId).equals(correctAns.get(i))? points.get(i) : 0;
            }

            System.out.println(total);
            // total counted bas jema 2 user chhe ema qeustion nakh ne run kar
            userService.updateTotalPoints(userId,quizId, total + "");

        }
    }

}
