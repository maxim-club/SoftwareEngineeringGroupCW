package com.studyspaces.spacefinder;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

import com.studyspaces.spacefinder.model.*;

@Repository
public interface OccupancyRecordRepository
        extends MongoRepository<OccupancyRecord, String> {

    /**
     * Find a record document by its id.
     */
    Optional<OccupancyRecord> findById(String id);

    /**
     * Retrieve all occupancy entries for a given occupancy level.
     */
    @Query("{ 'records.occupancyLevel': ?0 }")
    List<OccupancyRecord> findByOccupancyLevel(OccupancyLevel occupancyLevel);

    /**
     * Retrieve all documents that contain records after a given timestamp.
     */
    @Query("{ 'records.timestamp': { $gt: ?0 } }")
    List<OccupancyRecord> findWithRecordsAfterTimestamp(int timestamp);

    /**
     * Retrieve a specific document and filter its records by timestamp.
     */
    @Query(value = "{ '_id': ?0 }",
           fields = "{ 'records': { $elemMatch: { 'timestamp': { $gt: ?1 } } } }")
    Optional<OccupancyRecord> findRecordsAfterTimestamp(String id, int timestamp);
}
