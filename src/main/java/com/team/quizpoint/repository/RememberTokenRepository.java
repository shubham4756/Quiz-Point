package com.team.quizpoint.repository;

import com.team.quizpoint.model.token.RememberToken;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RememberTokenRepository extends MongoRepository<RememberToken,String> {
    RememberToken findBySeries(String series);
    RememberToken findByUsername(String username);
}
