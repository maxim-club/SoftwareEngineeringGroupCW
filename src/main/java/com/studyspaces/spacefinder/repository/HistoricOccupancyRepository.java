package com.studyspaces.spacefinder.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.studyspaces.spacefinder.model.RoomOccupancyRecord;

@Repository
public interface HistoricOccupancyRepository extends MongoRepository<RoomOccupancyRecord, String> {
}
