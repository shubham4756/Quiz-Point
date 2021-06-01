package com.team.quizpoint.model;

public class Answer {
    private String userId;
    private String ans;

    public Answer() {
    }

    public Answer(String userId, String ans) {
        this.userId = userId;
        this.ans = ans;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAns() {
        return ans;
    }

    public void setAns(String ans) {
        this.ans = ans;
    }

    @Override
    public String toString() {
        return "Answers{" +
                "userId='" + userId + '\'' +
                ", ans='" + ans + '\'' +
                '}';
    }
}
