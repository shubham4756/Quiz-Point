package com.team.quizpoint.model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;

@Document(collection = "useranswers")
public class UserAnswers {

    private String questionId;
    private List<Answer> answers;

    public UserAnswers() {
    }

    public UserAnswers(String questionId, List<Answer> answers) {
        this.questionId = questionId;
        this.answers = answers;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    @Override
    public String toString() {
        return "UserAnswers{" +
                "questionId='" + questionId + '\'' +
                ", answers=" + answers +
                '}';
    }
}
