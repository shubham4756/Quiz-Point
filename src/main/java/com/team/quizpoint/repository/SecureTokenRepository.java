package com.team.quizpoint.repository;

import com.team.quizpoint.model.token.SecureToken;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SecureTokenRepository extends MongoRepository<SecureToken,String> {

    SecureToken findByToken(String token);

    // return collection unique object id
    String removeByToken(String token);
}
