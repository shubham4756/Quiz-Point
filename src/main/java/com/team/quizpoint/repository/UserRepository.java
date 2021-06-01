package com.team.quizpoint.repository;

import com.team.quizpoint.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    User findUserByUserId(String id);
    Optional<User> findUserByEmail(String email);
    boolean existsUserByEmail(String email);
}
