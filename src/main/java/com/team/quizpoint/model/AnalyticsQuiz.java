package com.team.quizpoint.model;

public class AnalyticsQuiz {
    private String userId;
    private String userEmail;
    private String point;

    AnalyticsQuiz() {
    }

    public AnalyticsQuiz(String userId, String userEmail, String point) {
        this.userId = userId;
        this.userEmail = userEmail;
        this.point = point;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    @Override
    public String toString() {
        return "AnalyticsQuiz{" +
                "userId='" + userId + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", point='" + point + '\'' +
                '}';
    }
}
