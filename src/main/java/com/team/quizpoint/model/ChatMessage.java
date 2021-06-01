package com.team.quizpoint.model;

import java.time.LocalDateTime;

public class ChatMessage {

    private String messageBody;
    private LocalDateTime messageTime;

    public ChatMessage() {
    }

    public ChatMessage(String messageBody, LocalDateTime messageTime) {
        this.messageBody = messageBody;
        this.messageTime = messageTime;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public LocalDateTime getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(LocalDateTime messageTime) {
        this.messageTime = messageTime;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "messageBody='" + messageBody + '\'' +
                ", messageTime=" + messageTime +
                '}';
    }
}
