package com.studyspaces.spacefinder.repository;

import com.studyspaces.spacefinder.model.RoomData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomDataRepository extends MongoRepository<RoomData, String> {
}
