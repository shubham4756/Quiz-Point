package com.team.quizpoint.repository;

import com.team.quizpoint.model.Quiz;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuizRepository extends MongoRepository<Quiz, ObjectId> {
    Optional<Quiz> findQuizByQuizId(ObjectId quizId);
}
