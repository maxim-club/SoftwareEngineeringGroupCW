package com.studyspaces.spacefinder.repository;

import com.studyspaces.spacefinder.model.SimulatedData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SimulatedDataRepository extends MongoRepository<SimulatedData, String> {
}
