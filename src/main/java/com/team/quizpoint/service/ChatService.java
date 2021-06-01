package com.team.quizpoint.service;

import com.team.quizpoint.model.Chat;
import com.team.quizpoint.model.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ChatService {

    @Autowired
    MongoTemplate template;

    public void addQuiz(String quizId) {
        Chat chat = new Chat(quizId, new ArrayList<ChatMessage>());
        template.save(chat, "chats");
    }

    public void addMessage(String quizId,
                           String message) {

        Query query = new Query();
        query.addCriteria(Criteria.where("quizId").is(quizId));

        ChatMessage chatMessage = new ChatMessage(message, LocalDateTime.now());

        Update update = new Update();
        update.addToSet("messages", chatMessage);

        template.updateFirst(query, update, Chat.class,"chats");
    }

    public List<ChatMessage> getAllMessages(String quizId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("quizId").is(quizId));

        return template.findOne(query, Chat.class, "chats").getMessages();
    }


}
