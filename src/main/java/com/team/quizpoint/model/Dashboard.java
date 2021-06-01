package com.team.quizpoint.model;

import org.bson.types.ObjectId;

import java.time.LocalDateTime;

public class Dashboard {

    String id;
    String quizName,description,date,time,point;
    int status;

    public Dashboard(String id, String quizName, String description, String date, String time) {
        this.id = id;
        this.quizName = quizName;
        this.description = description;
        this.date = date;
        this.time = time;
    }

    public Dashboard(String id, String quizName, String description, String date, String time, String point,int status) {
        this.id = id;
        this.quizName = quizName;
        this.description = description;
        this.date = date;
        this.time = time;
        this.point = point;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuizName() {
        return quizName;
    }

    public void setQuizName(String quizName) {
        this.quizName = quizName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Dashboard{" +
                "id='" + id + '\'' +
                ", quizName='" + quizName + '\'' +
                ", description='" + description + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", point='" + point + '\'' +
                ", status=" + status +
                '}';
    }
}
