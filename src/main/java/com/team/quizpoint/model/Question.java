package com.team.quizpoint.model;

import org.bson.codecs.pojo.annotations.BsonId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;
import java.util.Objects;

public class Question {

    @MongoId(FieldType.OBJECT_ID)
    private String questionId;

    private String type;
    private int points;
    private String questionContent;
    private String correctAnswer;
    private List<String> options;

    public Question() {
    }

    public Question(String type, int points, String questionContent, String correctAnswer, List<String> options) {
        this.type = type;
        this.points = points;
        this.questionContent = questionContent;
        this.correctAnswer = correctAnswer;
        this.options = options;
    }

    public Question(String questionId, String type, int points, String questionContent, String correctAnswer, List<String> options) {
        this.questionId = questionId;
        this.type = type;
        this.points = points;
        this.questionContent = questionContent;
        this.correctAnswer = correctAnswer;
        this.options = options;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getQuestionContent() {
        return questionContent;
    }

    public void setQuestionContent(String questionContent) {
        this.questionContent = questionContent;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    @Override
    public String toString() {
        return "Question{" +
                "questionId='" + questionId + '\'' +
                ", type='" + type + '\'' +
                ", points=" + points +
                ", questionContent='" + questionContent + '\'' +
                ", correctAnswer='" + correctAnswer + '\'' +
                ", options=" + options +
                '}';
    }
}
