package com.studyspaces.spacefinder.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.studyspaces.spacefinder.model.OccupancyRecord;

@Repository
public interface HistoricOccupancyRepository extends MongoRepository<OccupancyRecord, String> {
}
