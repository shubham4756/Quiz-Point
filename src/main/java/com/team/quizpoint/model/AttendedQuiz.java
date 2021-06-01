package com.team.quizpoint.model;

public class AttendedQuiz {
    private String quizId;
    private String totalPoints;

    public AttendedQuiz(){
    }

    public AttendedQuiz(String quizId, String totalPoints) {
        this.quizId = quizId;
        this.totalPoints = totalPoints;
    }

    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public String getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(String totalPoints) {
        this.totalPoints = totalPoints;
    }

    @Override
    public String toString() {
        return "AttendedQuiz{" +
                "quizId='" + quizId + '\'' +
                ", totalPoints='" + totalPoints + '\'' +
                '}';
    }
}
