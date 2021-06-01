package com.team.quizpoint.model;

import org.bson.BsonArray;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.*;

@Document( collection = "quizzes")
public class Quiz {

    @BsonId
    @Field("_id")
    private ObjectId quizId;
    private String quizName;

    private LocalDateTime quizCreatedTime;

    private LocalDateTime quizStartTime;
    private boolean isEnded;
    private String description;

    // duration in minute
    private int duration;

    private List<Question> questions;


    public Quiz() {
    }

    public Quiz(String quizName, LocalDateTime quizCreatedTime, LocalDateTime quizStartTime, boolean isEnded, String description, List<Question> questions) {
        this.quizName = quizName;
        this.quizCreatedTime = quizCreatedTime;
        this.quizStartTime = quizStartTime;
        this.isEnded = isEnded;
        this.description = description;
        this.questions = questions;
    }

    public Quiz(ObjectId quizId, String quizName, LocalDateTime quizCreatedTime, LocalDateTime quizStartTime, boolean isEnded, String description, List<Question> questions) {
        this.quizId = quizId;
        this.quizName = quizName;
        this.quizCreatedTime = quizCreatedTime;
        this.quizStartTime = quizStartTime;
        this.isEnded = isEnded;
        this.description = description;
        this.questions = questions;
    }

    public Quiz(String quizName, LocalDateTime quizCreatedTime, LocalDateTime quizStartTime, boolean isEnded, String description, int duration, List<Question> questions) {
        this.quizName = quizName;
        this.quizCreatedTime = quizCreatedTime;
        this.quizStartTime = quizStartTime;
        this.isEnded = isEnded;
        this.description = description;
        this.duration = duration;
        this.questions = questions;
    }

    public Quiz(ObjectId quizId, String quizName, LocalDateTime quizCreatedTime, LocalDateTime quizStartTime, boolean isEnded, String description, int duration, List<Question> questions) {
        this.quizId = quizId;
        this.quizName = quizName;
        this.quizCreatedTime = quizCreatedTime;
        this.quizStartTime = quizStartTime;
        this.isEnded = isEnded;
        this.description = description;
        this.duration = duration;
        this.questions = questions;
    }

    public ObjectId getQuizId() {
        return quizId;
    }

    public void setQuizId(ObjectId quizId) {
        this.quizId = quizId;
    }

    public String getQuizName() {
        return quizName;
    }

    public void setQuizName(String quizName) {
        this.quizName = quizName;
    }

    public LocalDateTime getQuizCreatedTime() {
        return quizCreatedTime;
    }

    public void setQuizCreatedTime(LocalDateTime quizCreatedTime) {
        this.quizCreatedTime = quizCreatedTime;
    }

    public LocalDateTime getQuizStartTime() {
        return quizStartTime;
    }

    public void setQuizStartTime(LocalDateTime quizStartTime) {
        this.quizStartTime = quizStartTime;
    }

    public boolean isEnded() {
        return isEnded;
    }

    public void setEnded(boolean ended) {
        isEnded = ended;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Quiz{" +
                "quizId=" + quizId +
                ", quizName='" + quizName + '\'' +
                ", quizCreatedTime=" + quizCreatedTime +
                ", quizStartTime=" + quizStartTime +
                ", isEnded=" + isEnded +
                ", description='" + description + '\'' +
                ", duration=" + duration +
                ", questions=" + questions +
                '}';
    }
}
