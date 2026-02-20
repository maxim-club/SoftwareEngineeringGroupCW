package com.studyspaces.spacefinder.repository;

import com.studyspaces.spacefinder.model.UserRecord;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<UserRecord, String> {

    //CRUD
    @Query("{'username': ?0}")
    Optional<UserRecord> findUserRecordByUsername(String username);
}
