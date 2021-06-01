package com.team.quizpoint.model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;

@Document(collection = "chats")
public class Chat {

    private String quizId;
    private List<ChatMessage> messages;

    public Chat() {
    }

    public Chat(String quizId, List<ChatMessage> messages) {
        this.quizId = quizId;
        this.messages = messages;
    }

    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public List<ChatMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<ChatMessage> messages) {
        this.messages = messages;
    }

    @Override
    public String toString() {
        return "Chat{" +
                "quizId='" + quizId + '\'' +
                ", messages=" + messages +
                '}';
    }
}
