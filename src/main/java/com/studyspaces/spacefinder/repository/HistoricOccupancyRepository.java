package com.studyspaces.spacefinder.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.Query;
import com.studyspaces.spacefinder.model.*;

import java.util.List;
import java.util.Optional;


import com.studyspaces.spacefinder.model.RoomOccupancyRecord;

@Repository
public interface HistoricOccupancyRepository extends MongoRepository<RoomOccupancyRecord, String> {

    /**
     * Find a record document by its id.
     */
    Optional<RoomOccupancyRecord> findById(String id);

}
