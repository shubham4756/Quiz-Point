package com.team.quizpoint.service;

import com.team.quizpoint.model.token.RememberToken;
import com.team.quizpoint.repository.RememberTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class RememberTokenService implements PersistentTokenRepository {

    @Autowired
    RememberTokenRepository rememberTokenRepository;

    @Autowired
    MongoTemplate template;

    @Override
    public void createNewToken(PersistentRememberMeToken persistentRememberMeToken) {
        rememberTokenRepository.save(new RememberToken(null,
                persistentRememberMeToken.getUsername(),
                persistentRememberMeToken.getSeries(),
                persistentRememberMeToken.getTokenValue(),
                persistentRememberMeToken.getDate()));
    }

    @Override
    public void updateToken(String series, String value, Date lastUsed) {
        RememberToken token = rememberTokenRepository.findBySeries(series);
        template.updateFirst(BasicQuery.query(Criteria.where("username").is(token.getUsername())),
                new Update().set("series",series).set("value",value).set("lastused",lastUsed), RememberToken.class,"rememberToken");
//        repository.save(new Token(token.getId(), token.getUsername(),series, value, lastUsed));
    }

    @Override
    public PersistentRememberMeToken getTokenForSeries(String s) {
        return rememberTokenRepository.findBySeries(s);
    }

    @Override
    public void removeUserTokens(String email) {
        RememberToken token = rememberTokenRepository.findByUsername(email);
        if (token != null) {
            rememberTokenRepository.delete(token);
        }
    }
}
