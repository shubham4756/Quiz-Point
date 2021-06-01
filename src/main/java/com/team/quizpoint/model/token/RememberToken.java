package com.team.quizpoint.model.token;


import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;

import java.util.Date;

@Document(collection = "rememberToken")
public class RememberToken extends PersistentRememberMeToken {

    @Id
    private final String id;

    @PersistenceConstructor
    public RememberToken(String id, String username, String series, String tokenValue, Date date) {
        super(username, series, tokenValue, date);
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
